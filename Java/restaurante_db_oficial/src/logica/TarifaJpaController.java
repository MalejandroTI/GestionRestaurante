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
import Clases.EntregaPedido;
import Clases.Tarifa;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import logica.exceptions.IllegalOrphanException;
import logica.exceptions.NonexistentEntityException;

/**
 *
 * @author ASUS
 */
public class TarifaJpaController implements Serializable {

    public TarifaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tarifa tarifa) {
        if (tarifa.getEntregaPedidoCollection() == null) {
            tarifa.setEntregaPedidoCollection(new ArrayList<EntregaPedido>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<EntregaPedido> attachedEntregaPedidoCollection = new ArrayList<EntregaPedido>();
            for (EntregaPedido entregaPedidoCollectionEntregaPedidoToAttach : tarifa.getEntregaPedidoCollection()) {
                entregaPedidoCollectionEntregaPedidoToAttach = em.getReference(entregaPedidoCollectionEntregaPedidoToAttach.getClass(), entregaPedidoCollectionEntregaPedidoToAttach.getIdEntrega());
                attachedEntregaPedidoCollection.add(entregaPedidoCollectionEntregaPedidoToAttach);
            }
            tarifa.setEntregaPedidoCollection(attachedEntregaPedidoCollection);
            em.persist(tarifa);
            for (EntregaPedido entregaPedidoCollectionEntregaPedido : tarifa.getEntregaPedidoCollection()) {
                Tarifa oldIdTarifaOfEntregaPedidoCollectionEntregaPedido = entregaPedidoCollectionEntregaPedido.getIdTarifa();
                entregaPedidoCollectionEntregaPedido.setIdTarifa(tarifa);
                entregaPedidoCollectionEntregaPedido = em.merge(entregaPedidoCollectionEntregaPedido);
                if (oldIdTarifaOfEntregaPedidoCollectionEntregaPedido != null) {
                    oldIdTarifaOfEntregaPedidoCollectionEntregaPedido.getEntregaPedidoCollection().remove(entregaPedidoCollectionEntregaPedido);
                    oldIdTarifaOfEntregaPedidoCollectionEntregaPedido = em.merge(oldIdTarifaOfEntregaPedidoCollectionEntregaPedido);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tarifa tarifa) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tarifa persistentTarifa = em.find(Tarifa.class, tarifa.getIdTarifa());
            Collection<EntregaPedido> entregaPedidoCollectionOld = persistentTarifa.getEntregaPedidoCollection();
            Collection<EntregaPedido> entregaPedidoCollectionNew = tarifa.getEntregaPedidoCollection();
            List<String> illegalOrphanMessages = null;
            for (EntregaPedido entregaPedidoCollectionOldEntregaPedido : entregaPedidoCollectionOld) {
                if (!entregaPedidoCollectionNew.contains(entregaPedidoCollectionOldEntregaPedido)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain EntregaPedido " + entregaPedidoCollectionOldEntregaPedido + " since its idTarifa field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<EntregaPedido> attachedEntregaPedidoCollectionNew = new ArrayList<EntregaPedido>();
            for (EntregaPedido entregaPedidoCollectionNewEntregaPedidoToAttach : entregaPedidoCollectionNew) {
                entregaPedidoCollectionNewEntregaPedidoToAttach = em.getReference(entregaPedidoCollectionNewEntregaPedidoToAttach.getClass(), entregaPedidoCollectionNewEntregaPedidoToAttach.getIdEntrega());
                attachedEntregaPedidoCollectionNew.add(entregaPedidoCollectionNewEntregaPedidoToAttach);
            }
            entregaPedidoCollectionNew = attachedEntregaPedidoCollectionNew;
            tarifa.setEntregaPedidoCollection(entregaPedidoCollectionNew);
            tarifa = em.merge(tarifa);
            for (EntregaPedido entregaPedidoCollectionNewEntregaPedido : entregaPedidoCollectionNew) {
                if (!entregaPedidoCollectionOld.contains(entregaPedidoCollectionNewEntregaPedido)) {
                    Tarifa oldIdTarifaOfEntregaPedidoCollectionNewEntregaPedido = entregaPedidoCollectionNewEntregaPedido.getIdTarifa();
                    entregaPedidoCollectionNewEntregaPedido.setIdTarifa(tarifa);
                    entregaPedidoCollectionNewEntregaPedido = em.merge(entregaPedidoCollectionNewEntregaPedido);
                    if (oldIdTarifaOfEntregaPedidoCollectionNewEntregaPedido != null && !oldIdTarifaOfEntregaPedidoCollectionNewEntregaPedido.equals(tarifa)) {
                        oldIdTarifaOfEntregaPedidoCollectionNewEntregaPedido.getEntregaPedidoCollection().remove(entregaPedidoCollectionNewEntregaPedido);
                        oldIdTarifaOfEntregaPedidoCollectionNewEntregaPedido = em.merge(oldIdTarifaOfEntregaPedidoCollectionNewEntregaPedido);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tarifa.getIdTarifa();
                if (findTarifa(id) == null) {
                    throw new NonexistentEntityException("The tarifa with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tarifa tarifa;
            try {
                tarifa = em.getReference(Tarifa.class, id);
                tarifa.getIdTarifa();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tarifa with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<EntregaPedido> entregaPedidoCollectionOrphanCheck = tarifa.getEntregaPedidoCollection();
            for (EntregaPedido entregaPedidoCollectionOrphanCheckEntregaPedido : entregaPedidoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tarifa (" + tarifa + ") cannot be destroyed since the EntregaPedido " + entregaPedidoCollectionOrphanCheckEntregaPedido + " in its entregaPedidoCollection field has a non-nullable idTarifa field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tarifa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tarifa> findTarifaEntities() {
        return findTarifaEntities(true, -1, -1);
    }

    public List<Tarifa> findTarifaEntities(int maxResults, int firstResult) {
        return findTarifaEntities(false, maxResults, firstResult);
    }

    private List<Tarifa> findTarifaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tarifa.class));
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

    public Tarifa findTarifa(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tarifa.class, id);
        } finally {
            em.close();
        }
    }

    public int getTarifaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tarifa> rt = cq.from(Tarifa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
