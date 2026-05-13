/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presentacion;

import Clases.*;
import java.math.BigDecimal;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import servicios.PedidoService;

public class TestPedido {

    public static void main(String[] args) {

        EntityManagerFactory emf
                = Persistence.createEntityManagerFactory("restaurante_db_oficialPU");
        EntityManager em = emf.createEntityManager();

        PedidoService service = new PedidoService(emf);

        try {

            Cliente cliente = em.find(Cliente.class, 1);
            Usuario usuario = em.find(Usuario.class, 1);
            Usuario repartidor = em.find(Usuario.class, 14);
            Tarifa tarifa = em.find(Tarifa.class, 1);

            Pedido pedido = new Pedido();
            pedido.setTipoPedido("LOCAL");

            pedido.setSubtotal(new BigDecimal("10.00"));
            pedido.setImpuesto(new BigDecimal("1.50"));
            pedido.setTotal(new BigDecimal("11.50"));

            EntregaPedido entrega = new EntregaPedido();
            entrega.setDireccionEntrega("Loja centro");
            entrega.setDistanciaKm(new BigDecimal("2.5"));
            entrega.setCostoEnvio(new BigDecimal("1.50"));
            entrega.setIdUsuarioRepartidor(repartidor);
            entrega.setIdTarifa(tarifa);
            entrega.setNombreRecibe("Juan Perez");

            Pedido result = service.crearPedido(
                    pedido,
                    entrega,
                    usuario,
                    cliente
            );

            System.out.println("Pedido creado ID: " + result.getIdPedido());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            emf.close();
        }
    }
}
