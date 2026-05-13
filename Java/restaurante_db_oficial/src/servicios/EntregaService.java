/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicios;

import Clases.Pedido;
import Clases.Usuario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import logica.PedidoJpaController;

/**
 *
 * @author ASUS
 */
public class EntregaService {

    private EntityManagerFactory emf;

    private PedidoJpaController pedidoController;

    public EntregaService(EntityManagerFactory emf) {
        this.emf = emf;
        this.pedidoController = new PedidoJpaController(emf);
    }

    public List<Pedido> obtenerPedidosAsignados(Usuario repartidor) {

        EntityManager em = emf.createEntityManager();

        try {
            TypedQuery<Pedido> query = em.createQuery(
                    "SELECT p FROM Pedido p "
                    + "JOIN p.entregaPedido e "
                    + "WHERE e.idUsuarioRepartidor = :repartidor",
                    Pedido.class
            );

            query.setParameter("repartidor", repartidor);

            return query.getResultList();

        } finally {
            em.close();
        }

    }

    public Pedido verPedido(int idPedido) {
        return pedidoController.findPedido(idPedido);
    }
}
