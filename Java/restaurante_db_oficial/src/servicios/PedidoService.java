/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicios;

import Clases.*;
import ClasesEnum.enums.EstadoPedido;
import ClasesEnum.enums.TipoPedido;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import logica.PedidoJpaController;

public class PedidoService {

    private final EntityManagerFactory emf;
    private final PedidoJpaController pedidoController;

    public PedidoService(EntityManagerFactory emf) {
        this.emf = emf;
        this.pedidoController = new PedidoJpaController(emf);
    }

    // =====================================================
    // CREAR PEDIDO
    // =====================================================
    public Pedido crearPedido(Pedido pedido,
                              EntregaPedido entrega,
                              Usuario usuario,
                              Cliente cliente) {

        if (pedido == null) {
            throw new IllegalArgumentException("Pedido requerido");
        }

        if (pedido.getTipoPedido() == null) {
            throw new IllegalArgumentException("Tipo de pedido obligatorio");
        }

        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            // =========================
            // VALIDACIONES BASE
            // =========================
            if (cliente == null) {
                throw new IllegalArgumentException("Cliente obligatorio");
            }

            if (usuario == null) {
                throw new IllegalArgumentException("Usuario obligatorio");
            }

            pedido.setIdCliente(cliente);
            pedido.setIdUsuario(usuario);

            // =========================
            // CÓDIGO
            // =========================
            if (pedido.getCodigo() == null || pedido.getCodigo().isBlank()) {
                pedido.setCodigo(generarCodigo(em, pedido.getTipoPedido().name()));
            }

            // =========================
            // ESTADO INICIAL
            // =========================
            if (pedido.getEstado() == null) {
                pedido.setEstado(EstadoPedido.PENDIENTE);
            }

            if (pedido.getFechaHora() == null) {
                pedido.setFechaHora(new Date());
            }

            // =========================
            // IMPORTES DEFAULT
            // =========================
            pedido.setSubtotal(
                    pedido.getSubtotal() != null ? pedido.getSubtotal() : BigDecimal.ZERO
            );

            pedido.setImpuesto(
                    pedido.getImpuesto() != null ? pedido.getImpuesto() : BigDecimal.ZERO
            );

            pedido.setTotal(
                    pedido.getTotal() != null ? pedido.getTotal() : BigDecimal.ZERO
            );

            // =========================
            // PERSIST PEDIDO
            // =========================
            em.persist(pedido);
            em.flush();

            // =========================
            // SOLO DELIVERY GENERA ENTREGA
            // =========================
            if (pedido.getTipoPedido() == TipoPedido.DELIVERY) {

                if (entrega == null) {
                    throw new IllegalArgumentException("Delivery requiere entrega");
                }

                validarEntrega(entrega);

                if (entrega.getCostoEnvio() == null && entrega.getIdTarifa() != null) {
                    entrega.setCostoEnvio(entrega.getIdTarifa().getPrecio());
                }

                entrega.setIdPedido(pedido);

                if (entrega.getFechaHoraSalida() == null) {
                    entrega.setFechaHoraSalida(new Date());
                }

                em.persist(entrega);
            }

            em.getTransaction().commit();
            return pedido;

        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Error creando pedido: " + e.getMessage(), e);

        } finally {
            em.close();
        }
    }

    // =====================================================
    // CONSULTA POR ID
    // =====================================================
    public Pedido verPedido(int idPedido) {
        return pedidoController.findPedido(idPedido);
    }

    // =====================================================
    // PEDIDOS POR USUARIO
    // =====================================================
    public java.util.List<Pedido> obtenerPedidosPorUsuario(Usuario usuario) {

        EntityManager em = emf.createEntityManager();

        try {
            TypedQuery<Pedido> q = em.createQuery(
                    "SELECT p FROM Pedido p WHERE p.idUsuario = :usuario",
                    Pedido.class
            );

            q.setParameter("usuario", usuario);
            return q.getResultList();

        } finally {
            em.close();
        }
    }

    // =====================================================
    // CAMBIO DE ESTADO
    // =====================================================
    public Pedido cambiarEstado(int idPedido,
                                EstadoPedido nuevoEstado,
                                Usuario usuario) {

        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            Pedido pedido = em.find(Pedido.class, idPedido);

            if (pedido == null) {
                throw new IllegalArgumentException("Pedido no existe");
            }

            EstadoPedido actual = pedido.getEstado();

            // =========================
            // BLOQUEO FINAL
            // =========================
            if (actual == EstadoPedido.ENTREGADO ||
                actual == EstadoPedido.CANCELADO) {

                throw new IllegalStateException("Pedido finalizado no modificable");
            }

            // =========================
            // VALIDACIÓN DE TRANSICIÓN
            // =========================
            if (!esTransicionValida(actual, nuevoEstado)) {
                throw new IllegalStateException(
                        "Transición inválida: " + actual + " → " + nuevoEstado
                );
            }

            pedido.setEstado(nuevoEstado);

            em.merge(pedido);

            em.getTransaction().commit();
            return pedido;

        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException(e);

        } finally {
            em.close();
        }
    }

    // =====================================================
    // REGLAS DE NEGOCIO DE ESTADOS
    // =====================================================
    private boolean esTransicionValida(EstadoPedido actual,
                                      EstadoPedido nuevo) {

        switch (actual) {

            case PENDIENTE:
                return nuevo == EstadoPedido.EN_PREPARACION
                        || nuevo == EstadoPedido.CANCELADO;

            case EN_PREPARACION:
                return nuevo == EstadoPedido.LISTO
                        || nuevo == EstadoPedido.CANCELADO;

            case LISTO:
                return nuevo == EstadoPedido.EN_RUTA
                        || nuevo == EstadoPedido.CANCELADO;

            case EN_RUTA:
                return nuevo == EstadoPedido.ENTREGADO
                        || nuevo == EstadoPedido.CANCELADO;

            default:
                return false;
        }
    }

    // =====================================================
    // VALIDAR ENTREGA
    // =====================================================
    private void validarEntrega(EntregaPedido e) {

        if (e.getDireccionEntrega() == null || e.getDireccionEntrega().isBlank()) {
            throw new IllegalArgumentException("Dirección requerida");
        }

        if (e.getIdUsuarioRepartidor() == null) {
            throw new IllegalArgumentException("Repartidor requerido");
        }

        if (e.getIdTarifa() == null) {
            throw new IllegalArgumentException("Tarifa requerida");
        }

        if (e.getDistanciaKm() == null) {
            throw new IllegalArgumentException("Distancia requerida");
        }

        if (e.getCostoEnvio() == null) {
            throw new IllegalArgumentException("Costo de envío requerido");
        }
    }

    // =====================================================
    // GENERAR CÓDIGO
    // =====================================================
    private String generarCodigo(EntityManager em, String tipo) {

        String prefijo = "P-" + tipo.toUpperCase();

        TypedQuery<String> q = em.createQuery(
                "SELECT p.codigo FROM Pedido p "
              + "WHERE p.codigo LIKE :pref "
              + "ORDER BY p.idPedido DESC",
                String.class
        );

        q.setParameter("pref", prefijo + "%");
        q.setMaxResults(1);

        String ultimo = q.getResultStream().findFirst().orElse(null);

        int num = 1;

        if (ultimo != null && ultimo.contains("-")) {
            String[] parts = ultimo.split("-");
            num = Integer.parseInt(parts[parts.length - 1]) + 1;
        }

        return String.format("%s-%03d", prefijo, num);
    }
}