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
import utilJpa.JPAUtil;

public class FacturaService {

    private final FacturaJpaController facturaController;
    private final PedidoJpaController pedidoController;
    private final DetalleFacturaJpaController detalleFacturaController;

    public FacturaService() {
        this.facturaController = new FacturaJpaController(JPAUtil.getEMF());
        this.pedidoController = new PedidoJpaController(JPAUtil.getEMF());
        this.detalleFacturaController = new DetalleFacturaJpaController(JPAUtil.getEMF());

    }

    public Factura crearFactura(Pedido pedidoInput) {
        Pedido pedido
                = pedidoController.findPedidoConDetalles(
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

    public Factura buscarFacturaConDetalles(Integer id) {
        return facturaController.findFacturaConDetalles(id);
    }

    public Factura obtenerOCrearFactura(Pedido pedido) {
        Factura factura = buscarFacturaConDetalles(pedido.getIdPedido());
        if (factura != null) {
            return factura;
        }
        return crearFactura(pedido);
    }

}
