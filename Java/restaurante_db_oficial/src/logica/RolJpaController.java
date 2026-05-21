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
import Clases.Usuario;
import java.util.ArrayList;
import java.util.Collection;
import Clases.Permiso;
import Clases.Rol;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import logica.exceptions.NonexistentEntityException;

/**
 *
 * @author ASUS
 */
public class RolJpaController implements Serializable {

    public RolJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Rol rol) {
        if (rol.getUsuarioCollection() == null) {
            rol.setUsuarioCollection(new ArrayList<Usuario>());
        }
        if (rol.getPermisoCollection() == null) {
            rol.setPermisoCollection(new ArrayList<Permiso>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Usuario> attachedUsuarioCollection = new ArrayList<Usuario>();
            for (Usuario usuarioCollectionUsuarioToAttach : rol.getUsuarioCollection()) {
                usuarioCollectionUsuarioToAttach = em.getReference(usuarioCollectionUsuarioToAttach.getClass(), usuarioCollectionUsuarioToAttach.getIdUsuario());
                attachedUsuarioCollection.add(usuarioCollectionUsuarioToAttach);
            }
            rol.setUsuarioCollection(attachedUsuarioCollection);
            Collection<Permiso> attachedPermisoCollection = new ArrayList<Permiso>();
            for (Permiso permisoCollectionPermisoToAttach : rol.getPermisoCollection()) {
                permisoCollectionPermisoToAttach = em.getReference(permisoCollectionPermisoToAttach.getClass(), permisoCollectionPermisoToAttach.getIdPermiso());
                attachedPermisoCollection.add(permisoCollectionPermisoToAttach);
            }
            rol.setPermisoCollection(attachedPermisoCollection);
            em.persist(rol);
            for (Usuario usuarioCollectionUsuario : rol.getUsuarioCollection()) {
                usuarioCollectionUsuario.getRolCollection().add(rol);
                usuarioCollectionUsuario = em.merge(usuarioCollectionUsuario);
            }
            for (Permiso permisoCollectionPermiso : rol.getPermisoCollection()) {
                permisoCollectionPermiso.getRolCollection().add(rol);
                permisoCollectionPermiso = em.merge(permisoCollectionPermiso);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    public Rol findRolByNombre(String nombre) {
    EntityManager em = getEntityManager();
    try {
        return em.createQuery(
                "SELECT r FROM Rol r WHERE r.nombre = :nombre", Rol.class)
                .setParameter("nombre", nombre)
                .getSingleResult();
    } finally {
        em.close();
    }
}
    
    
    public void edit(Rol rol) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Rol persistentRol = em.find(Rol.class, rol.getIdRol());
            Collection<Usuario> usuarioCollectionOld = persistentRol.getUsuarioCollection();
            Collection<Usuario> usuarioCollectionNew = rol.getUsuarioCollection();
            Collection<Permiso> permisoCollectionOld = persistentRol.getPermisoCollection();
            Collection<Permiso> permisoCollectionNew = rol.getPermisoCollection();
            Collection<Usuario> attachedUsuarioCollectionNew = new ArrayList<Usuario>();
            for (Usuario usuarioCollectionNewUsuarioToAttach : usuarioCollectionNew) {
                usuarioCollectionNewUsuarioToAttach = em.getReference(usuarioCollectionNewUsuarioToAttach.getClass(), usuarioCollectionNewUsuarioToAttach.getIdUsuario());
                attachedUsuarioCollectionNew.add(usuarioCollectionNewUsuarioToAttach);
            }
            usuarioCollectionNew = attachedUsuarioCollectionNew;
            rol.setUsuarioCollection(usuarioCollectionNew);
            Collection<Permiso> attachedPermisoCollectionNew = new ArrayList<Permiso>();
            for (Permiso permisoCollectionNewPermisoToAttach : permisoCollectionNew) {
                permisoCollectionNewPermisoToAttach = em.getReference(permisoCollectionNewPermisoToAttach.getClass(), permisoCollectionNewPermisoToAttach.getIdPermiso());
                attachedPermisoCollectionNew.add(permisoCollectionNewPermisoToAttach);
            }
            permisoCollectionNew = attachedPermisoCollectionNew;
            rol.setPermisoCollection(permisoCollectionNew);
            rol = em.merge(rol);
            for (Usuario usuarioCollectionOldUsuario : usuarioCollectionOld) {
                if (!usuarioCollectionNew.contains(usuarioCollectionOldUsuario)) {
                    usuarioCollectionOldUsuario.getRolCollection().remove(rol);
                    usuarioCollectionOldUsuario = em.merge(usuarioCollectionOldUsuario);
                }
            }
            for (Usuario usuarioCollectionNewUsuario : usuarioCollectionNew) {
                if (!usuarioCollectionOld.contains(usuarioCollectionNewUsuario)) {
                    usuarioCollectionNewUsuario.getRolCollection().add(rol);
                    usuarioCollectionNewUsuario = em.merge(usuarioCollectionNewUsuario);
                }
            }
            for (Permiso permisoCollectionOldPermiso : permisoCollectionOld) {
                if (!permisoCollectionNew.contains(permisoCollectionOldPermiso)) {
                    permisoCollectionOldPermiso.getRolCollection().remove(rol);
                    permisoCollectionOldPermiso = em.merge(permisoCollectionOldPermiso);
                }
            }
            for (Permiso permisoCollectionNewPermiso : permisoCollectionNew) {
                if (!permisoCollectionOld.contains(permisoCollectionNewPermiso)) {
                    permisoCollectionNewPermiso.getRolCollection().add(rol);
                    permisoCollectionNewPermiso = em.merge(permisoCollectionNewPermiso);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = rol.getIdRol();
                if (findRol(id) == null) {
                    throw new NonexistentEntityException("The rol with id " + id + " no longer exists.");
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
            Rol rol;
            try {
                rol = em.getReference(Rol.class, id);
                rol.getIdRol();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The rol with id " + id + " no longer exists.", enfe);
            }
            Collection<Usuario> usuarioCollection = rol.getUsuarioCollection();
            for (Usuario usuarioCollectionUsuario : usuarioCollection) {
                usuarioCollectionUsuario.getRolCollection().remove(rol);
                usuarioCollectionUsuario = em.merge(usuarioCollectionUsuario);
            }
            Collection<Permiso> permisoCollection = rol.getPermisoCollection();
            for (Permiso permisoCollectionPermiso : permisoCollection) {
                permisoCollectionPermiso.getRolCollection().remove(rol);
                permisoCollectionPermiso = em.merge(permisoCollectionPermiso);
            }
            em.remove(rol);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Rol> findRolEntities() {
        return findRolEntities(true, -1, -1);
    }

    public List<Rol> findRolEntities(int maxResults, int firstResult) {
        return findRolEntities(false, maxResults, firstResult);
    }

    private List<Rol> findRolEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Rol.class));
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

    public Rol findRol(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Rol.class, id);
        } finally {
            em.close();
        }
    }

    public int getRolCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Rol> rt = cq.from(Rol.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
