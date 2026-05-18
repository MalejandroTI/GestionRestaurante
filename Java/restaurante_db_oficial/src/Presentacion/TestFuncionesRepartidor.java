/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presentacion;



import Clases.EntregaPedido;
import Clases.Pedido;
import Clases.Usuario;
import java.util.Collection;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class TestFuncionesRepartidor {

    public static void main(String[] args) {

        EntityManagerFactory emf
                = Persistence.createEntityManagerFactory("restaurante_db_oficialPU");

        EntityManager em = emf.createEntityManager();

        try {

            // buscar repartidor ID 14
            Usuario repartidor = em.find(Usuario.class, 14);

            if (repartidor != null) {

                Collection<EntregaPedido> entregas
                        = repartidor.getEntregaPedidoCollection();

                System.out.println("Cantidad entregas: "
                        + entregas.size());

                for (EntregaPedido e : entregas) {

                    Pedido p = e.getIdPedido();

                    System.out.println("----------------");
                    System.out.println("Entrega ID: " + e.getIdEntrega());

                    System.out.println("Pedido ID: "
                            + p.getIdPedido());

                    System.out.println("Código: "
                            + p.getCodigo());

                    System.out.println("Estado: "
                            + p.getEstado());

                    System.out.println("Tipo: "
                            + p.getTipoPedido());

                    System.out.println("Dirección: "
                            + e.getDireccionEntrega());
                }

            } else {

                System.out.println("Repartidor no encontrado");
            }

        } finally {

            em.close();
            emf.close();
        }
    }
}