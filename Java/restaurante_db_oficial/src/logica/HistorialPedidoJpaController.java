/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import Clases.HistorialPedido;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Clases.Pedido;
import Clases.Usuario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import logica.exceptions.NonexistentEntityException;

/**
 *
 * @author ASUS
 */
public class HistorialPedidoJpaController implements Serializable {

    public HistorialPedidoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(HistorialPedido historialPedido) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pedido idPedido = historialPedido.getIdPedido();
            if (idPedido != null) {
                idPedido = em.getReference(idPedido.getClass(), idPedido.getIdPedido());
                historialPedido.setIdPedido(idPedido);
            }
            Usuario idUsuario = historialPedido.getIdUsuario();
            if (idUsuario != null) {
                idUsuario = em.getReference(idUsuario.getClass(), idUsuario.getIdUsuario());
                historialPedido.setIdUsuario(idUsuario);
            }
            em.persist(historialPedido);
            if (idPedido != null) {
                idPedido.getHistorialPedidoCollection().add(historialPedido);
                idPedido = em.merge(idPedido);
            }
            if (idUsuario != null) {
                idUsuario.getHistorialPedidoCollection().add(historialPedido);
                idUsuario = em.merge(idUsuario);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(HistorialPedido historialPedido) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            HistorialPedido persistentHistorialPedido = em.find(HistorialPedido.class, historialPedido.getIdHistorial());
            Pedido idPedidoOld = persistentHistorialPedido.getIdPedido();
            Pedido idPedidoNew = historialPedido.getIdPedido();
            Usuario idUsuarioOld = persistentHistorialPedido.getIdUsuario();
            Usuario idUsuarioNew = historialPedido.getIdUsuario();
            if (idPedidoNew != null) {
                idPedidoNew = em.getReference(idPedidoNew.getClass(), idPedidoNew.getIdPedido());
                historialPedido.setIdPedido(idPedidoNew);
            }
            if (idUsuarioNew != null) {
                idUsuarioNew = em.getReference(idUsuarioNew.getClass(), idUsuarioNew.getIdUsuario());
                historialPedido.setIdUsuario(idUsuarioNew);
            }
            historialPedido = em.merge(historialPedido);
            if (idPedidoOld != null && !idPedidoOld.equals(idPedidoNew)) {
                idPedidoOld.getHistorialPedidoCollection().remove(historialPedido);
                idPedidoOld = em.merge(idPedidoOld);
            }
            if (idPedidoNew != null && !idPedidoNew.equals(idPedidoOld)) {
                idPedidoNew.getHistorialPedidoCollection().add(historialPedido);
                idPedidoNew = em.merge(idPedidoNew);
            }
            if (idUsuarioOld != null && !idUsuarioOld.equals(idUsuarioNew)) {
                idUsuarioOld.getHistorialPedidoCollection().remove(historialPedido);
                idUsuarioOld = em.merge(idUsuarioOld);
            }
            if (idUsuarioNew != null && !idUsuarioNew.equals(idUsuarioOld)) {
                idUsuarioNew.getHistorialPedidoCollection().add(historialPedido);
                idUsuarioNew = em.merge(idUsuarioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = historialPedido.getIdHistorial();
                if (findHistorialPedido(id) == null) {
                    throw new NonexistentEntityException("The historialPedido with id " + id + " no longer exists.");
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
            HistorialPedido historialPedido;
            try {
                historialPedido = em.getReference(HistorialPedido.class, id);
                historialPedido.getIdHistorial();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The historialPedido with id " + id + " no longer exists.", enfe);
            }
            Pedido idPedido = historialPedido.getIdPedido();
            if (idPedido != null) {
                idPedido.getHistorialPedidoCollection().remove(historialPedido);
                idPedido = em.merge(idPedido);
            }
            Usuario idUsuario = historialPedido.getIdUsuario();
            if (idUsuario != null) {
                idUsuario.getHistorialPedidoCollection().remove(historialPedido);
                idUsuario = em.merge(idUsuario);
            }
            em.remove(historialPedido);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<HistorialPedido> findHistorialPedidoEntities() {
        return findHistorialPedidoEntities(true, -1, -1);
    }

    public List<HistorialPedido> findHistorialPedidoEntities(int maxResults, int firstResult) {
        return findHistorialPedidoEntities(false, maxResults, firstResult);
    }

    private List<HistorialPedido> findHistorialPedidoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(HistorialPedido.class));
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

    public HistorialPedido findHistorialPedido(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(HistorialPedido.class, id);
        } finally {
            em.close();
        }
    }

    public int getHistorialPedidoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<HistorialPedido> rt = cq.from(HistorialPedido.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
