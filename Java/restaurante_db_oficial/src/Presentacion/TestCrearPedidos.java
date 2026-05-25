/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presentacion;

import Clases.Cliente;
import Clases.DetallePedido;
import Clases.EntregaPedido;
import Clases.Pedido;
import Clases.Producto;
import Clases.Usuario;
import ClasesEnum.enums.TipoPedido;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import servicios.PedidoService;

public class TestCrearPedidos {

    public static void main(String[] args) {

        EntityManagerFactory emf
                = Persistence.createEntityManagerFactory(
                        "restaurante_db_oficialPU"
                );

        EntityManager em = emf.createEntityManager();

        try {

            PedidoService pedidoService
                    = new PedidoService();

            // =====================================================
            // DATOS BASE
            // =====================================================

            Cliente cliente
                    = em.find(Cliente.class,3);

            Usuario cajero
                    = em.find(Usuario.class, 1);

            Usuario repartidor
                    = em.find(Usuario.class, 14);

            Producto producto1
                    = em.find(Producto.class, 1);

            Producto producto2
                    = em.find(Producto.class, 2);

            // =====================================================
            // PEDIDO LOCAL
            // =====================================================

            Pedido pedidoLocal = new Pedido();

            pedidoLocal.setTipoPedido(
                    TipoPedido.LOCAL
            );

            Collection<DetallePedido> detallesLocal
                    = new ArrayList<>();

            DetallePedido d1 = new DetallePedido();

            d1.setIdProducto(producto1);
            d1.setCantidad(2);

            detallesLocal.add(d1);

            DetallePedido d2 = new DetallePedido();

            d2.setIdProducto(producto2);
            d2.setCantidad(1);

            detallesLocal.add(d2);

            Pedido creadoLocal
                    = pedidoService.crearPedido(
                            pedidoLocal,
                            detallesLocal,
                            null,
                            cajero,
                            cliente
                    );

            System.out.println("========== PEDIDO LOCAL ==========");
            System.out.println("ID: " + creadoLocal.getIdPedido());
            System.out.println("Código: " + creadoLocal.getCodigo());
            System.out.println("Subtotal: " + creadoLocal.getSubtotal());
            System.out.println("IVA: " + creadoLocal.getImpuesto());
            System.out.println("Total: " + creadoLocal.getTotal());

            // =====================================================
            // PEDIDO DELIVERY
            // =====================================================

            Pedido pedidoDelivery = new Pedido();

            pedidoDelivery.setTipoPedido(
                    TipoPedido.DELIVERY
            );

            Collection<DetallePedido> detallesDelivery
                    = new ArrayList<>();

            DetallePedido d3 = new DetallePedido();

            d3.setIdProducto(producto1);
            d3.setCantidad(3);

            detallesDelivery.add(d3);

            // =====================================================
            // ENTREGA
            // =====================================================

            EntregaPedido entrega
                    = new EntregaPedido();

            entrega.setDireccionEntrega(
                    "Loja - Centro"
            );

            entrega.setDistanciaKm(
                    new BigDecimal("2.5")
            );

            entrega.setIdUsuarioRepartidor(repartidor);

            // =====================================================
            // CREAR DELIVERY
            // =====================================================

            Pedido creadoDelivery
                    = pedidoService.crearPedido(
                            pedidoDelivery,
                            detallesDelivery,
                            entrega,
                            cajero,
                            cliente
                    );

            System.out.println("\n========== PEDIDO DELIVERY ==========");
            System.out.println("ID: " + creadoDelivery.getIdPedido());
            System.out.println("Código: " + creadoDelivery.getCodigo());
            System.out.println("Subtotal: " + creadoDelivery.getSubtotal());
            System.out.println("IVA: " + creadoDelivery.getImpuesto());
            System.out.println("Total: " + creadoDelivery.getTotal());

            System.out.println("\n========== ENTREGA ==========");
            System.out.println("Dirección: "
                    + entrega.getDireccionEntrega());

            System.out.println("Distancia: "
                    + entrega.getDistanciaKm());

            System.out.println("Costo envío: "
                    + entrega.getCostoEnvio());

            System.out.println("Tarifa aplicada ID: "
                    + entrega.getIdTarifa().getIdTarifa());

            System.out.println("Repartidor: "
                    + entrega.getIdUsuarioRepartidor().getNombre());

        } finally {

            em.close();
            emf.close();
        }
    }
}