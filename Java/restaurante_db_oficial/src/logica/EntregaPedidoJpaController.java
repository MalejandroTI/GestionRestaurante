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
import Clases.Pedido;
import Clases.Tarifa;
import Clases.Usuario;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import logica.exceptions.IllegalOrphanException;
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

    public void create(EntregaPedido entregaPedido) throws IllegalOrphanException {
        List<String> illegalOrphanMessages = null;
        Pedido idPedidoOrphanCheck = entregaPedido.getIdPedido();
        if (idPedidoOrphanCheck != null) {
            EntregaPedido oldEntregaPedidoOfIdPedido = idPedidoOrphanCheck.getEntregaPedido();
            if (oldEntregaPedidoOfIdPedido != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Pedido " + idPedidoOrphanCheck + " already has an item of type EntregaPedido whose idPedido column cannot be null. Please make another selection for the idPedido field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pedido idPedido = entregaPedido.getIdPedido();
            if (idPedido != null) {
                idPedido = em.getReference(idPedido.getClass(), idPedido.getIdPedido());
                entregaPedido.setIdPedido(idPedido);
            }
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
            if (idPedido != null) {
                idPedido.setEntregaPedido(entregaPedido);
                idPedido = em.merge(idPedido);
            }
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

    public void edit(EntregaPedido entregaPedido) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EntregaPedido persistentEntregaPedido = em.find(EntregaPedido.class, entregaPedido.getIdEntrega());
            Pedido idPedidoOld = persistentEntregaPedido.getIdPedido();
            Pedido idPedidoNew = entregaPedido.getIdPedido();
            Tarifa idTarifaOld = persistentEntregaPedido.getIdTarifa();
            Tarifa idTarifaNew = entregaPedido.getIdTarifa();
            Usuario idUsuarioRepartidorOld = persistentEntregaPedido.getIdUsuarioRepartidor();
            Usuario idUsuarioRepartidorNew = entregaPedido.getIdUsuarioRepartidor();
            List<String> illegalOrphanMessages = null;
            if (idPedidoNew != null && !idPedidoNew.equals(idPedidoOld)) {
                EntregaPedido oldEntregaPedidoOfIdPedido = idPedidoNew.getEntregaPedido();
                if (oldEntregaPedidoOfIdPedido != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Pedido " + idPedidoNew + " already has an item of type EntregaPedido whose idPedido column cannot be null. Please make another selection for the idPedido field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idPedidoNew != null) {
                idPedidoNew = em.getReference(idPedidoNew.getClass(), idPedidoNew.getIdPedido());
                entregaPedido.setIdPedido(idPedidoNew);
            }
            if (idTarifaNew != null) {
                idTarifaNew = em.getReference(idTarifaNew.getClass(), idTarifaNew.getIdTarifa());
                entregaPedido.setIdTarifa(idTarifaNew);
            }
            if (idUsuarioRepartidorNew != null) {
                idUsuarioRepartidorNew = em.getReference(idUsuarioRepartidorNew.getClass(), idUsuarioRepartidorNew.getIdUsuario());
                entregaPedido.setIdUsuarioRepartidor(idUsuarioRepartidorNew);
            }
            entregaPedido = em.merge(entregaPedido);
            if (idPedidoOld != null && !idPedidoOld.equals(idPedidoNew)) {
                idPedidoOld.setEntregaPedido(null);
                idPedidoOld = em.merge(idPedidoOld);
            }
            if (idPedidoNew != null && !idPedidoNew.equals(idPedidoOld)) {
                idPedidoNew.setEntregaPedido(entregaPedido);
                idPedidoNew = em.merge(idPedidoNew);
            }
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
            Pedido idPedido = entregaPedido.getIdPedido();
            if (idPedido != null) {
                idPedido.setEntregaPedido(null);
                idPedido = em.merge(idPedido);
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
    
}
