/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import Clases.EntregaPedido;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Clases.Tarifa;
import Clases.Usuario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import logica.exceptions.NonexistentEntityException;

/**
 *
 * @author ASUS
 */
public class EntregaPedidoJpaController implements Serializable {

    public EntregaPedidoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(EntregaPedido entregaPedido) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tarifa idTarifa = entregaPedido.getIdTarifa();
            if (idTarifa != null) {
                idTarifa = em.getReference(idTarifa.getClass(), idTarifa.getIdTarifa());
                entregaPedido.setIdTarifa(idTarifa);
            }
            Usuario idUsuarioRepartidor = entregaPedido.getIdUsuarioRepartidor();
            if (idUsuarioRepartidor != null) {
                idUsuarioRepartidor = em.getReference(idUsuarioRepartidor.getClass(), idUsuarioRepartidor.getIdUsuario());
                entregaPedido.setIdUsuarioRepartidor(idUsuarioRepartidor);
            }
            em.persist(entregaPedido);
            if (idTarifa != null) {
                idTarifa.getEntregaPedidoCollection().add(entregaPedido);
                idTarifa = em.merge(idTarifa);
            }
            if (idUsuarioRepartidor != null) {
                idUsuarioRepartidor.getEntregaPedidoCollection().add(entregaPedido);
                idUsuarioRepartidor = em.merge(idUsuarioRepartidor);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(EntregaPedido entregaPedido) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EntregaPedido persistentEntregaPedido = em.find(EntregaPedido.class, entregaPedido.getIdEntrega());
            Tarifa idTarifaOld = persistentEntregaPedido.getIdTarifa();
            Tarifa idTarifaNew = entregaPedido.getIdTarifa();
            Usuario idUsuarioRepartidorOld = persistentEntregaPedido.getIdUsuarioRepartidor();
            Usuario idUsuarioRepartidorNew = entregaPedido.getIdUsuarioRepartidor();
            if (idTarifaNew != null) {
                idTarifaNew = em.getReference(idTarifaNew.getClass(), idTarifaNew.getIdTarifa());
                entregaPedido.setIdTarifa(idTarifaNew);
            }
            if (idUsuarioRepartidorNew != null) {
                idUsuarioRepartidorNew = em.getReference(idUsuarioRepartidorNew.getClass(), idUsuarioRepartidorNew.getIdUsuario());
                entregaPedido.setIdUsuarioRepartidor(idUsuarioRepartidorNew);
            }
            entregaPedido = em.merge(entregaPedido);
            if (idTarifaOld != null && !idTarifaOld.equals(idTarifaNew)) {
                idTarifaOld.getEntregaPedidoCollection().remove(entregaPedido);
                idTarifaOld = em.merge(idTarifaOld);
            }
            if (idTarifaNew != null && !idTarifaNew.equals(idTarifaOld)) {
                idTarifaNew.getEntregaPedidoCollection().add(entregaPedido);
                idTarifaNew = em.merge(idTarifaNew);
            }
            if (idUsuarioRepartidorOld != null && !idUsuarioRepartidorOld.equals(idUsuarioRepartidorNew)) {
                idUsuarioRepartidorOld.getEntregaPedidoCollection().remove(entregaPedido);
                idUsuarioRepartidorOld = em.merge(idUsuarioRepartidorOld);
            }
            if (idUsuarioRepartidorNew != null && !idUsuarioRepartidorNew.equals(idUsuarioRepartidorOld)) {
                idUsuarioRepartidorNew.getEntregaPedidoCollection().add(entregaPedido);
                idUsuarioRepartidorNew = em.merge(idUsuarioRepartidorNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = entregaPedido.getIdEntrega();
                if (findEntregaPedido(id) == null) {
                    throw new NonexistentEntityException("The entregaPedido with id " + id + " no longer exists.");
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
            EntregaPedido entregaPedido;
            try {
                entregaPedido = em.getReference(EntregaPedido.class, id);
                entregaPedido.getIdEntrega();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The entregaPedido with id " + id + " no longer exists.", enfe);
            }
            Tarifa idTarifa = entregaPedido.getIdTarifa();
            if (idTarifa != null) {
                idTarifa.getEntregaPedidoCollection().remove(entregaPedido);
                idTarifa = em.merge(idTarifa);
            }
            Usuario idUsuarioRepartidor = entregaPedido.getIdUsuarioRepartidor();
            if (idUsuarioRepartidor != null) {
                idUsuarioRepartidor.getEntregaPedidoCollection().remove(entregaPedido);
                idUsuarioRepartidor = em.merge(idUsuarioRepartidor);
            }
            em.remove(entregaPedido);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<EntregaPedido> findEntregaPedidoEntities() {
        return findEntregaPedidoEntities(true, -1, -1);
    }

    public List<EntregaPedido> findEntregaPedidoEntities(int maxResults, int firstResult) {
        return findEntregaPedidoEntities(false, maxResults, firstResult);
    }

    private List<EntregaPedido> findEntregaPedidoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EntregaPedido.class));
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

    public EntregaPedido findEntregaPedido(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EntregaPedido.class, id);
        } finally {
            em.close();
        }
    }

    public int getEntregaPedidoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EntregaPedido> rt = cq.from(EntregaPedido.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<EntregaPedido> findByRepartidorEstado(Usuario rep, String estado) {

        EntityManager em = getEntityManager();

        try {
            return em.createQuery(
                    "SELECT e FROM EntregaPedido e "
                    + "WHERE e.idUsuarioRepartidor = :rep "
                    + "AND e.idPedido.estado = :estado",
                    EntregaPedido.class
            )
                    .setParameter("rep", rep)
                    .setParameter("estado", estado)
                    .getResultList();

        } finally {
            em.close();
        }
    }

}
