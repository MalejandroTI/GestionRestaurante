/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Clases.Cliente;
import Clases.Pedido;
import Clases.Usuario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import logica.exceptions.NonexistentEntityException;

/**
 *
 * @author ASUS
 */
public class PedidoJpaController implements Serializable {

    public PedidoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pedido pedido) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente idCliente = pedido.getIdCliente();
            if (idCliente != null) {
                idCliente = em.getReference(idCliente.getClass(), idCliente.getIdCliente());
                pedido.setIdCliente(idCliente);
            }
            Usuario idUsuario = pedido.getIdUsuario();
            if (idUsuario != null) {
                idUsuario = em.getReference(idUsuario.getClass(), idUsuario.getIdUsuario());
                pedido.setIdUsuario(idUsuario);
            }
            em.persist(pedido);
            if (idCliente != null) {
                idCliente.getPedidoCollection().add(pedido);
                idCliente = em.merge(idCliente);
            }
            if (idUsuario != null) {
                idUsuario.getPedidoCollection().add(pedido);
                idUsuario = em.merge(idUsuario);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pedido pedido) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pedido persistentPedido = em.find(Pedido.class, pedido.getIdPedido());
            Cliente idClienteOld = persistentPedido.getIdCliente();
            Cliente idClienteNew = pedido.getIdCliente();
            Usuario idUsuarioOld = persistentPedido.getIdUsuario();
            Usuario idUsuarioNew = pedido.getIdUsuario();
            if (idClienteNew != null) {
                idClienteNew = em.getReference(idClienteNew.getClass(), idClienteNew.getIdCliente());
                pedido.setIdCliente(idClienteNew);
            }
            if (idUsuarioNew != null) {
                idUsuarioNew = em.getReference(idUsuarioNew.getClass(), idUsuarioNew.getIdUsuario());
                pedido.setIdUsuario(idUsuarioNew);
            }
            pedido = em.merge(pedido);
            if (idClienteOld != null && !idClienteOld.equals(idClienteNew)) {
                idClienteOld.getPedidoCollection().remove(pedido);
                idClienteOld = em.merge(idClienteOld);
            }
            if (idClienteNew != null && !idClienteNew.equals(idClienteOld)) {
                idClienteNew.getPedidoCollection().add(pedido);
                idClienteNew = em.merge(idClienteNew);
            }
            if (idUsuarioOld != null && !idUsuarioOld.equals(idUsuarioNew)) {
                idUsuarioOld.getPedidoCollection().remove(pedido);
                idUsuarioOld = em.merge(idUsuarioOld);
            }
            if (idUsuarioNew != null && !idUsuarioNew.equals(idUsuarioOld)) {
                idUsuarioNew.getPedidoCollection().add(pedido);
                idUsuarioNew = em.merge(idUsuarioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pedido.getIdPedido();
                if (findPedido(id) == null) {
                    throw new NonexistentEntityException("The pedido with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pedido pedido;
            try {
                pedido = em.getReference(Pedido.class, id);
                pedido.getIdPedido();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pedido with id " + id + " no longer exists.", enfe);
            }
            Cliente idCliente = pedido.getIdCliente();
            if (idCliente != null) {
                idCliente.getPedidoCollection().remove(pedido);
                idCliente = em.merge(idCliente);
            }
            Usuario idUsuario = pedido.getIdUsuario();
            if (idUsuario != null) {
                idUsuario.getPedidoCollection().remove(pedido);
                idUsuario = em.merge(idUsuario);
            }
            em.remove(pedido);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pedido> findPedidoEntities() {
        return findPedidoEntities(true, -1, -1);
    }

    public List<Pedido> findPedidoEntities(int maxResults, int firstResult) {
        return findPedidoEntities(false, maxResults, firstResult);
    }

    private List<Pedido> findPedidoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pedido.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Pedido findPedido(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pedido.class, id);
        } finally {
            em.close();
        }
    }

    public int getPedidoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pedido> rt = cq.from(Pedido.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public Pedido findPedidoConDetalles(Integer id) {

        EntityManager em = getEntityManager();

        try {
            TypedQuery<Pedido> query = em.createQuery(
                    "SELECT p FROM Pedido p "
                    + "LEFT JOIN FETCH p.detallePedidoCollection dp "
                    + "LEFT JOIN FETCH dp.idProducto "
                    + "LEFT JOIN FETCH p.idCliente "
                    + "LEFT JOIN FETCH p.idUsuario "
                    + "WHERE p.idPedido = :id",
                    Pedido.class
            );

            query.setParameter("id", id);

            return query.getSingleResult();

        } finally {
            em.close();
        }
    }

}
