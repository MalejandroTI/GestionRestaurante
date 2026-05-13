/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import Clases.Permiso;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Clases.Rol;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import logica.exceptions.NonexistentEntityException;

/**
 *
 * @author ASUS
 */
public class PermisoJpaController implements Serializable {

    public PermisoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Permiso permiso) {
        if (permiso.getRolCollection() == null) {
            permiso.setRolCollection(new ArrayList<Rol>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Rol> attachedRolCollection = new ArrayList<Rol>();
            for (Rol rolCollectionRolToAttach : permiso.getRolCollection()) {
                rolCollectionRolToAttach = em.getReference(rolCollectionRolToAttach.getClass(), rolCollectionRolToAttach.getIdRol());
                attachedRolCollection.add(rolCollectionRolToAttach);
            }
            permiso.setRolCollection(attachedRolCollection);
            em.persist(permiso);
            for (Rol rolCollectionRol : permiso.getRolCollection()) {
                rolCollectionRol.getPermisoCollection().add(permiso);
                rolCollectionRol = em.merge(rolCollectionRol);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Permiso permiso) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Permiso persistentPermiso = em.find(Permiso.class, permiso.getIdPermiso());
            Collection<Rol> rolCollectionOld = persistentPermiso.getRolCollection();
            Collection<Rol> rolCollectionNew = permiso.getRolCollection();
            Collection<Rol> attachedRolCollectionNew = new ArrayList<Rol>();
            for (Rol rolCollectionNewRolToAttach : rolCollectionNew) {
                rolCollectionNewRolToAttach = em.getReference(rolCollectionNewRolToAttach.getClass(), rolCollectionNewRolToAttach.getIdRol());
                attachedRolCollectionNew.add(rolCollectionNewRolToAttach);
            }
            rolCollectionNew = attachedRolCollectionNew;
            permiso.setRolCollection(rolCollectionNew);
            permiso = em.merge(permiso);
            for (Rol rolCollectionOldRol : rolCollectionOld) {
                if (!rolCollectionNew.contains(rolCollectionOldRol)) {
                    rolCollectionOldRol.getPermisoCollection().remove(permiso);
                    rolCollectionOldRol = em.merge(rolCollectionOldRol);
                }
            }
            for (Rol rolCollectionNewRol : rolCollectionNew) {
                if (!rolCollectionOld.contains(rolCollectionNewRol)) {
                    rolCollectionNewRol.getPermisoCollection().add(permiso);
                    rolCollectionNewRol = em.merge(rolCollectionNewRol);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = permiso.getIdPermiso();
                if (findPermiso(id) == null) {
                    throw new NonexistentEntityException("The permiso with id " + id + " no longer exists.");
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
            Permiso permiso;
            try {
                permiso = em.getReference(Permiso.class, id);
                permiso.getIdPermiso();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The permiso with id " + id + " no longer exists.", enfe);
            }
            Collection<Rol> rolCollection = permiso.getRolCollection();
            for (Rol rolCollectionRol : rolCollection) {
                rolCollectionRol.getPermisoCollection().remove(permiso);
                rolCollectionRol = em.merge(rolCollectionRol);
            }
            em.remove(permiso);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Permiso> findPermisoEntities() {
        return findPermisoEntities(true, -1, -1);
    }

    public List<Permiso> findPermisoEntities(int maxResults, int firstResult) {
        return findPermisoEntities(false, maxResults, firstResult);
    }

    private List<Permiso> findPermisoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Permiso.class));
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

    public Permiso findPermiso(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Permiso.class, id);
        } finally {
            em.close();
        }
    }

    public int getPermisoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Permiso> rt = cq.from(Permiso.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
