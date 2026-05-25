/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicios;

/**
 *
 * @author ASUS
 */

import Clases.EntregaPedido;
import Clases.Usuario;
import ClasesEnum.enums.EstadoPedido;
import ClasesTemporales.ResumenDelivery;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import utilJpa.JPAUtil;
import logica.EntregaPedidoJpaController;

public class EntregaService {

    private final EntregaPedidoJpaController entregaController;
    private final javax.persistence.EntityManagerFactory emf;

    public EntregaService() {
        this.emf = JPAUtil.getEMF();
        this.entregaController = new EntregaPedidoJpaController(emf);
    }

    // ─────────────────────────────
    // CONSULTAS BASE
    // ─────────────────────────────
    public List<EntregaPedido> obtenerAsignados(Usuario repartidor) {
        return entregaController.findEntregaPedidoEntities()
                .stream()
                .filter(e -> e.getIdUsuarioRepartidor() != null
                        && e.getIdUsuarioRepartidor().equals(repartidor))
                .toList();
    }

    public List<EntregaPedido> obtenerEnRuta(Usuario repartidor) {
        return obtenerAsignados(repartidor)
                .stream()
                .filter(e -> e.getIdPedido().getEstado() == EstadoPedido.EN_RUTA)
                .toList();
    }

    public List<EntregaPedido> obtenerEntregados(Usuario repartidor) {
        return obtenerAsignados(repartidor)
                .stream()
                .filter(e -> e.getIdPedido().getEstado() == EstadoPedido.ENTREGADO)
                .toList();
    }

    // ─────────────────────────────
    // ACCIONES
    // ─────────────────────────────
    public void marcarEntregado(EntregaPedido entrega) {

        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            EntregaPedido managed = em.merge(entrega);
            managed.getIdPedido().setEstado(EstadoPedido.ENTREGADO);

            em.getTransaction().commit();

        } catch (Exception e) {

            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            throw e;

        } finally {
            em.close();
        }
    }

    public EntregaPedido ver(int id) {
        return entregaController.findEntregaPedido(id);
    }

    // ─────────────────────────────
    // FORMATEO (UI helpers, NO lógica de negocio)
    // ─────────────────────────────
    public String obtenerNombreCliente(EntregaPedido entrega) {

        if (entrega == null
                || entrega.getIdPedido() == null
                || entrega.getIdPedido().getIdCliente() == null
                || entrega.getIdPedido().getIdCliente().getNombre() == null) {
            return "—";
        }

        return entrega.getIdPedido().getIdCliente().getNombre();
    }

    public String obtenerDireccion(EntregaPedido entrega) {

        if (entrega == null
                || entrega.getDireccionEntrega() == null
                || entrega.getDireccionEntrega().isBlank()) {
            return "—";
        }

        return entrega.getDireccionEntrega();
    }

    public String obtenerDistanciaTexto(EntregaPedido entrega) {

        if (entrega == null || entrega.getDistanciaKm() == null) {
            return "—";
        }

        return entrega.getDistanciaKm().toPlainString();
    }

    public String obtenerTotalTexto(EntregaPedido entrega) {

        if (entrega == null
                || entrega.getIdPedido() == null
                || entrega.getIdPedido().getTotal() == null) {
            return "—";
        }

        return "$ " + entrega.getIdPedido().getTotal().toPlainString();
    }

    public BigDecimal calcularTotalEntregado(List<EntregaPedido> entregas) {

        BigDecimal total = BigDecimal.ZERO;

        if (entregas == null) return total;

        for (EntregaPedido e : entregas) {

            if (e == null
                    || e.getIdPedido() == null
                    || e.getIdPedido().getTotal() == null) {
                continue;
            }

            total = total.add(e.getIdPedido().getTotal());
        }

        return total;
    }

    // ─────────────────────────────
    // RESUMEN DELIVERY
    // ─────────────────────────────
    public ResumenDelivery obtenerResumenDelivery(
            Usuario repartidor,
            int pedidosPreviosEnRuta
    ) {

        int enRuta = obtenerEnRuta(repartidor).size();
        int entregados = obtenerEntregados(repartidor).size();
        boolean hayNuevos = enRuta > pedidosPreviosEnRuta;

        return new ResumenDelivery(enRuta, entregados, hayNuevos);
    }

    // ─────────────────────────────
    // UTILIDADES UI
    // ─────────────────────────────
    public String obtenerHoraActualizacion() {
        return new java.text.SimpleDateFormat("HH:mm:ss")
                .format(new java.util.Date());
    }

    public String obtenerTextoActualizacion() {
        return "Actualizado: " + obtenerHoraActualizacion();
    }

    public String obtenerTextoErrorActualizacion() {
        return "Error al actualizar";
    }
}