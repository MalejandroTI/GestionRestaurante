/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicios;

import Clases.Factura;
import Clases.Pedido;
import Clases.DetalleFactura;
import Clases.DetallePedido;
import logica.DetalleFacturaJpaController;
import logica.FacturaJpaController;
import logica.PedidoJpaController;
import javax.persistence.EntityManagerFactory;

public class FacturaService {

    private final EntityManagerFactory emf;
    private final FacturaJpaController facturaController;

    public FacturaService(EntityManagerFactory emf) {
        this.emf = emf;
        this.facturaController = new FacturaJpaController(emf);
    }

    public Factura crearFactura(Pedido pedidoInput) {

        PedidoJpaController pedidoController =
                new PedidoJpaController(emf);

        // ✔ IMPORTANTE: traer pedido CON DETALLES
        Pedido pedido =
                pedidoController.findPedidoConDetalles(
                        pedidoInput.getIdPedido()
                );

        if (pedido == null) {
            throw new IllegalArgumentException("Pedido no encontrado");
        }

        Factura factura = new Factura();

        factura.setIdUsuario(pedido.getIdUsuario());
        factura.setIdPedido(pedido);
        factura.setImpuesto(pedido.getImpuesto());
        factura.setNumero(pedido.getCodigo());
        factura.setSubtotal(pedido.getSubtotal());
        factura.setTotal(pedido.getTotal());

        facturaController.create(factura);

        DetalleFacturaJpaController detalleFacturaController =
                new DetalleFacturaJpaController(emf);

        for (DetallePedido detallePedido : pedido.getDetallePedidoCollection()) {

            DetalleFactura detalleFactura = new DetalleFactura();

            detalleFactura.setIdFactura(factura);
            detalleFactura.setIdProducto(detallePedido.getIdProducto());
            detalleFactura.setNombreProducto(detallePedido.getIdProducto().getNombre());
            detalleFactura.setCantidad(detallePedido.getCantidad());
            detalleFactura.setPrecioUnitario(detallePedido.getPrecioUnitario());
            detalleFactura.setSubtotal(detallePedido.getSubtotal());

            detalleFacturaController.create(detalleFactura);
        }

        return factura;
    }

    // ✔ NOMBRE CORRECTO
    public Factura buscarFacturaConDetalles(Integer id) {
        return facturaController.findFacturaConDetalles(id);
    }
}