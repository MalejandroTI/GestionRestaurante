/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicios;

import ClasesTemporales.ResumenPedido;
import Clases.*;
import ClasesEnum.enums.EstadoPedido;
import ClasesEnum.enums.TipoPedido;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import logica.ConfiguracionJpaController;
import logica.PedidoJpaController;
import logica.TarifaJpaController;

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
    // =====================================================
// CREAR PEDIDO
// =====================================================
    public Pedido crearPedido(
            Pedido pedido,
            Collection<DetallePedido> detalles,
            EntregaPedido entrega,
            Usuario usuario,
            Cliente cliente) {

        // =========================
        // VALIDACIONES
        // =========================
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido requerido");
        }

        if (pedido.getTipoPedido() == null) {
            throw new IllegalArgumentException("Tipo de pedido obligatorio");
        }

        if (detalles == null || detalles.isEmpty()) {
            throw new IllegalArgumentException("El pedido debe tener productos");
        }

        if (cliente == null) {
            throw new IllegalArgumentException("Cliente obligatorio");
        }

        if (usuario == null) {
            throw new IllegalArgumentException("Usuario obligatorio");
        }

        EntityManager em = emf.createEntityManager();

        try {

            em.getTransaction().begin();

            // =========================
            // RELACIONES
            // =========================
            pedido.setIdCliente(cliente);
            pedido.setIdUsuario(usuario);

            // =========================
            // GENERAR CÓDIGO
            // =========================
            if (pedido.getCodigo() == null
                    || pedido.getCodigo().isBlank()) {

                pedido.setCodigo(
                        generarCodigo(
                                em,
                                pedido.getTipoPedido().name()
                        )
                );
            }

            // =========================
            // ESTADO INICIAL
            // =========================
            if (pedido.getEstado() == null) {
                pedido.setEstado(EstadoPedido.PENDIENTE);
            }

            // =========================
            // FECHA
            // =========================
            if (pedido.getFechaHora() == null) {
                pedido.setFechaHora(new Date());
            }

            // =========================
            // IMPORTES INICIALES
            // =========================
            pedido.setSubtotal(BigDecimal.ZERO);
            pedido.setImpuesto(BigDecimal.ZERO);
            pedido.setTotal(BigDecimal.ZERO);

            // =========================
            // GUARDAR PEDIDO
            // =========================
            em.persist(pedido);

            // importante para obtener ID
            em.flush();

            // =========================
            // CREAR DETALLES
            // =========================
            BigDecimal subtotal = BigDecimal.ZERO;

            for (DetallePedido d : detalles) {

                if (d.getIdProducto() == null) {
                    throw new IllegalArgumentException(
                            "Producto requerido en detalle"
                    );
                }

                if (d.getCantidad() == null
                        || d.getCantidad() <= 0) {

                    throw new IllegalArgumentException(
                            "Cantidad inválida"
                    );
                }

                // relacionar pedido
                d.setIdPedido(pedido);

                // precio actual producto
                BigDecimal precio
                        = d.getIdProducto().getPrecio();

                d.setPrecioUnitario(precio);

                // subtotal detalle
                BigDecimal sub
                        = precio.multiply(
                                BigDecimal.valueOf(
                                        d.getCantidad()
                                )
                        );

                d.setSubtotal(sub);

                // acumular subtotal general
                subtotal = subtotal.add(sub);

                // guardar detalle
                em.persist(d);
            }

            // =========================
            // CALCULAR TOTALES
            // =========================
            pedido.setSubtotal(subtotal);
            ConfiguracionJpaController configuracionController
                    = new ConfiguracionJpaController(emf);

            Configuracion config
                    = configuracionController.findConfiguracion(1);

            BigDecimal ivaDecimal
                    = config.getValor().divide(
                            new BigDecimal("100")
                    );

            BigDecimal impuesto
                    = subtotal.multiply(ivaDecimal);

            pedido.setImpuesto(impuesto);

            pedido.setTotal(
                    subtotal.add(impuesto)
            );

            // actualizar pedido
            em.merge(pedido);

            // =========================
            // SOLO DELIVERY
            // =========================
            if (pedido.getTipoPedido() == TipoPedido.DELIVERY) {

                if (entrega == null) {
                    throw new IllegalArgumentException("Delivery requiere entrega");
                }

                // 1. calcular tarifa primero (dato de negocio externo)
                TarifaJpaController tarifaController = new TarifaJpaController(emf);

                Tarifa tarifa = tarifaController.obtenerTarifaPorDistancia(
                        entrega.getDistanciaKm()
                );

                if (tarifa == null) {
                    throw new IllegalArgumentException("No existe tarifa para la distancia");
                }

                entrega.setIdTarifa(tarifa);
                entrega.setCostoEnvio(tarifa.getPrecio());

                // 2. ahora sí completar datos necesarios para validación
                entrega.setIdPedido(pedido);

                if (entrega.getFechaHoraSalida() == null) {
                    entrega.setFechaHoraSalida(new Date());
                }

                // 3. ahora validar (ya está completo)
                validarEntrega(entrega);

                // 4. persistir entrega
                em.persist(entrega);

                em.merge(pedido);
            }

            // =========================
            // COMMIT
            // =========================
            em.getTransaction().commit();

            return pedido;

        } catch (Exception e) {

            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            throw new RuntimeException(
                    "Error creando pedido",
                    e
            );

        } finally {
            em.close();
        }
    }

    //metodo para calcular el total + envio 
    public BigDecimal calcularTotalCobrar(
            Pedido pedido,
            EntregaPedido entrega
    ) {

        BigDecimal total = pedido.getTotal();

        if (entrega != null) {
            total = total.add(entrega.getCostoEnvio());
        }

        return total;
    }

    public Pedido buscarPedidoConDetalles(Integer id) {
        return pedidoController.findPedidoConDetalles(id);
    }

    // =====================================================
    // CONSULTA POR ID
    // =====================================================
    public Pedido verPedido(int idPedido) {

        EntityManager em = emf.createEntityManager();

        try {
            return em.find(Pedido.class, idPedido);

        } finally {
            em.close();
        }
    }

    // =====================================================
    // PEDIDOS POR USUARIO
    // =====================================================
    public Collection<EntregaPedido> obtenerEntregasPorRepartidor(Usuario repartidor) {

        return repartidor.getEntregaPedidoCollection();
    }

    public List<Pedido> listaPedidos() {

        return pedidoController.findPedidoEntities();
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

            if (actual == EstadoPedido.ENTREGADO || actual == EstadoPedido.CANCELADO) {
                throw new IllegalStateException("Pedido finalizado");
            }

            if (!esTransicionValida(actual, nuevoEstado)) {
                throw new IllegalStateException("Transición inválida");
            }

            // 1. actualizar pedido
            pedido.setEstado(nuevoEstado);
            em.merge(pedido);

            // 2. crear historial (AQUÍ ES DONDE SE LLENA LA TABLA)
            HistorialPedido h = new HistorialPedido();

            h.setIdPedido(pedido);
            h.setIdUsuario(usuario);
            h.setEstado(nuevoEstado);   // SOLO el estado actual
            h.setFechaHora(new Date());

            h.setObservacion(
                    "Cambio de estado de " + actual + " a " + nuevoEstado
            );

            em.persist(h);

            em.getTransaction().commit();
            return pedido;

        } catch (Exception e) {

            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

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
    //HACERLO CON JPQL es mucho mejor, ya que hacerlo solamente con jpa, seria 
    //cargar innesesariamente la memoria al traer todos los pedidos existentes
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

    public ResumenPedido calcularResumen(
            List<DetallePedido> detalles) {

        BigDecimal subtotal = BigDecimal.ZERO;

        for (DetallePedido d : detalles) {

            subtotal = subtotal.add(
                    d.getSubtotal()
            );
        }

        ConfiguracionJpaController configuracionController
                = new ConfiguracionJpaController(emf);

        Configuracion config
                = configuracionController.findConfiguracion(1);

        BigDecimal ivaDecimal
                = config.getValor().divide(
                        new BigDecimal("100")
                );

        BigDecimal impuesto
                = subtotal.multiply(ivaDecimal);

        BigDecimal total
                = subtotal.add(impuesto);

        ResumenPedido resumen
                = new ResumenPedido();

        resumen.setSubtotal(subtotal);

        resumen.setIva(impuesto);

        resumen.setTotal(total);

        return resumen;
    }
}
