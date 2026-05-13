/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presentacion;

import Clases.*;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import logica.*;
import logica.exceptions.IllegalOrphanException;

public class PruebaSistemaCompleto {

    public static void main(String[] args) throws IllegalOrphanException {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("restaurante_db_oficialPU");

        PedidoJpaController pedidoController = new PedidoJpaController(emf);
        UsuarioJpaController usuarioController = new UsuarioJpaController(emf);
        ClienteJpaController clienteController = new ClienteJpaController(emf);
        EntregaPedidoJpaController entregaController = new EntregaPedidoJpaController(emf);

// =========================
// Buscar entidades existentes
// =========================
        Usuario usuario = usuarioController.findUsuario(1);
        Usuario delivery = usuarioController.findUsuario(14);
        Cliente cliente = clienteController.findCliente(1);

// =========================
// Crear pedido DELIVERY
// =========================
        Pedido pedido = new Pedido();
        pedido.setCodigo("776");
        pedido.setTipoPedido("delivery");
        pedido.setEstado("PENDIENTE");
        pedido.setFechaHora(new java.util.Date());

        pedido.setSubtotal(new java.math.BigDecimal("10.00"));
        pedido.setImpuesto(new java.math.BigDecimal("1.20"));
        pedido.setTotal(new java.math.BigDecimal("11.20"));

        pedido.setIdCliente(cliente);
        pedido.setIdUsuario(usuario);

// persistir pedido
        pedidoController.create(pedido);

// =========================
// Crear entrega (aquí va el delivery real)
// =========================
        EntregaPedido entrega = new EntregaPedido();
        entrega.setIdPedido(pedido);
        entrega.setIdUsuarioRepartidor(delivery);
        entrega.setDireccionEntrega("Loja centro - Calle Bolívar");



// persistir entrega
        entregaController.create(entrega);

        System.out.println("Pedido DELIVERY creado correctamente con entrega asignada");
    }
}
