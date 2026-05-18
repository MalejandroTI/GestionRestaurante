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
import javax.persistence.EntityManagerFactory;
import logica.FacturaJpaController;
import logica.PedidoJpaController;

/**
 *
 * @author ASUS
 */
public class FacturaService {
    
    private final EntityManagerFactory emf;
    
    public FacturaService(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public Factura crearFactura(Integer idPedido) {
        
        PedidoJpaController pedidoController
                = new PedidoJpaController(emf);
        
        FacturaJpaController facturaController
                = new FacturaJpaController(emf);
        
        Pedido pedido = pedidoController.findPedido(idPedido);
        DetalleFacturaJpaController detalleFacturaController
                = new DetalleFacturaJpaController(emf);
        
        if (pedido == null) {
            throw new IllegalArgumentException(
                    "Pedido no encontrado"
            );
        }
        
        Factura factura = new Factura();
        
        factura.setIdUsuario(pedido.getIdUsuario());
        factura.setIdPedido(pedido);
        factura.setImpuesto(pedido.getImpuesto());
        factura.setNumero(pedido.getCodigo());
        factura.setSubtotal(pedido.getSubtotal());
        factura.setTotal(pedido.getTotal());
        
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
}
