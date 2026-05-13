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

import ClasesEnum.enums.EstadoPedido;
import ClasesEnum.enums.TipoPedido;
import servicios.PedidoService;



public class TestPedidoEstado {

    public static void main(String[] args) {

        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("restaurante_db_oficialPU");

        PedidoService service = new PedidoService(emf);

        EntityManager em = emf.createEntityManager();

        try {
            // =========================
            // OBTENER ENTIDADES BASE
            // =========================
            Cliente cliente = em.find(Cliente.class, 1);
            Usuario usuario = em.find(Usuario.class, 1);

            if (cliente == null || usuario == null) {
                throw new RuntimeException("Cliente o usuario no existe");
            }

            // =========================
            // CREAR PEDIDO
            // =========================
            Pedido pedido = new Pedido();
            pedido.setTipoPedido(TipoPedido.LOCAL);
            pedido.setSubtotal(new BigDecimal("10.00"));
            pedido.setImpuesto(new BigDecimal("1.50"));
            pedido.setTotal(new BigDecimal("11.50"));

            Pedido creado = service.crearPedido(pedido, null, usuario, cliente);

            System.out.println("Pedido creado ID: " + creado.getIdPedido());
            System.out.println("Estado inicial: " + creado.getEstado());

            // =========================
            // CAMBIAR ESTADO 1
            // =========================
            Pedido p1 = service.cambiarEstado(
                    creado.getIdPedido(),
                    EstadoPedido.EN_PREPARACION,
                    usuario
            );

            System.out.println("Estado actualizado 1: " + p1.getEstado());

            // =========================
            // CAMBIAR ESTADO 2
            // =========================
            Pedido p2 = service.cambiarEstado(
                    creado.getIdPedido(),
                    EstadoPedido.LISTO,
                    usuario
            );

            System.out.println("Estado actualizado 2: " + p2.getEstado());

            // =========================
            // CAMBIAR ESTADO 3
            // =========================
            Pedido p3 = service.cambiarEstado(
                    creado.getIdPedido(),
                    EstadoPedido.CANCELADO,
                    usuario
            );

            System.out.println("Estado final: " + p3.getEstado());

        } finally {
            em.close();
            emf.close();
        }
    }
}