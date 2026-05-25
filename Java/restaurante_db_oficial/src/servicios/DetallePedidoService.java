/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicios;

/**
 *
 * @author ASUS
 */
import Clases.DetallePedido;
import Clases.Pedido;
import Clases.Producto;
import java.math.BigDecimal;
import java.util.Collection;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import logica.DetallePedidoJpaController;

public class DetallePedidoService {

    private final DetallePedidoJpaController detalleController;

    public DetallePedidoService(EntityManagerFactory emf) {

        this.detalleController
                = new DetallePedidoJpaController(emf);
    }

    // =========================================
    // AGREGAR DETALLE
    // =========================================
    public DetallePedido agregarDetalle(
            Pedido pedido,
            Producto producto,
            Integer cantidad) {

        if (pedido == null) {
            throw new IllegalArgumentException();
        }

        if (producto == null) {
            throw new IllegalArgumentException();
        }

        if (cantidad == null || cantidad <= 0) {
            throw new IllegalArgumentException();
        }

        DetallePedido detalle = new DetallePedido();

        detalle.setIdPedido(pedido);

        detalle.setIdProducto(producto);

        detalle.setCantidad(cantidad);

        detalle.setPrecioUnitario(producto.getPrecio());

        BigDecimal subtotal
                = producto.getPrecio().multiply(
                        BigDecimal.valueOf(cantidad)
                );

        detalle.setSubtotal(subtotal);

        detalleController.create(detalle);

        return detalle;
    }

    // =========================================
    // OBTENER DETALLES
    // =========================================
    public Collection<DetallePedido> obtenerDetallesPorPedido(Pedido pedido) {

        if (pedido == null) {
            throw new IllegalArgumentException();
        }

        return pedido.getDetallePedidoCollection();
    }

    // =========================================
    // CALCULAR SUBTOTAL
    // =========================================
    public BigDecimal calcularSubtotalPedido(Pedido pedido) {

        BigDecimal subtotal = BigDecimal.ZERO;

        for (DetallePedido d
                : pedido.getDetallePedidoCollection()) {

            subtotal = subtotal.add(d.getSubtotal());
        }

        return subtotal;
    }

    // =========================================
    // ELIMINAR DETALLE
    // =========================================
    public void eliminarDetalle(Integer idDetalle)
            throws Exception {

        detalleController.destroy(idDetalle);
    }

    private BigDecimal procesarDetalles(EntityManager em,
            Pedido pedido,
            Collection<DetallePedido> detalles) {

        BigDecimal subtotal = BigDecimal.ZERO;

        for (DetallePedido d : detalles) {

            d.setIdPedido(pedido);
            d.setPrecioUnitario(d.getIdProducto().getPrecio());

            BigDecimal sub = d.getPrecioUnitario()
                    .multiply(BigDecimal.valueOf(d.getCantidad()));

            d.setSubtotal(sub);

            subtotal = subtotal.add(sub);

            em.persist(d);
        }

        return subtotal;
    }
}
