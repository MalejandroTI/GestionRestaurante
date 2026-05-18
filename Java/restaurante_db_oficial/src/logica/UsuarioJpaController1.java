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
import Clases.Rol;
import java.util.ArrayList;
import java.util.Collection;
import Clases.EntregaPedido;
import Clases.HistorialPedido;
import Clases.Factura;
import Clases.Pedido;
import Clases.Usuario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import logica.exceptions.IllegalOrphanException;
import logica.exceptions.NonexistentEntityException;

/**
 *
 * @author ASUS
 */
public class UsuarioJpaController1 implements Serializable {

    public UsuarioJpaController1(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) {
        if (usuario.getRolCollection() == null) {
            usuario.setRolCollection(new ArrayList<Rol>());
        }
        if (usuario.getEntregaPedidoCollection() == null) {
            usuario.setEntregaPedidoCollection(new ArrayList<EntregaPedido>());
        }
        if (usuario.getHistorialPedidoCollection() == null) {
            usuario.setHistorialPedidoCollection(new ArrayList<HistorialPedido>());
        }
        if (usuario.getFacturaCollection() == null) {
            usuario.setFacturaCollection(new ArrayList<Factura>());
        }
        if (usuario.getPedidoCollection() == null) {
            usuario.setPedidoCollection(new ArrayList<Pedido>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Rol> attachedRolCollection = new ArrayList<Rol>();
            for (Rol rolCollectionRolToAttach : usuario.getRolCollection()) {
                rolCollectionRolToAttach = em.getReference(rolCollectionRolToAttach.getClass(), rolCollectionRolToAttach.getIdRol());
                attachedRolCollection.add(rolCollectionRolToAttach);
            }
            usuario.setRolCollection(attachedRolCollection);
            Collection<EntregaPedido> attachedEntregaPedidoCollection = new ArrayList<EntregaPedido>();
            for (EntregaPedido entregaPedidoCollectionEntregaPedidoToAttach : usuario.getEntregaPedidoCollection()) {
                entregaPedidoCollectionEntregaPedidoToAttach = em.getReference(entregaPedidoCollectionEntregaPedidoToAttach.getClass(), entregaPedidoCollectionEntregaPedidoToAttach.getIdEntrega());
                attachedEntregaPedidoCollection.add(entregaPedidoCollectionEntregaPedidoToAttach);
            }
            usuario.setEntregaPedidoCollection(attachedEntregaPedidoCollection);
            Collection<HistorialPedido> attachedHistorialPedidoCollection = new ArrayList<HistorialPedido>();
            for (HistorialPedido historialPedidoCollectionHistorialPedidoToAttach : usuario.getHistorialPedidoCollection()) {
                historialPedidoCollectionHistorialPedidoToAttach = em.getReference(historialPedidoCollectionHistorialPedidoToAttach.getClass(), historialPedidoCollectionHistorialPedidoToAttach.getIdHistorial());
                attachedHistorialPedidoCollection.add(historialPedidoCollectionHistorialPedidoToAttach);
            }
            usuario.setHistorialPedidoCollection(attachedHistorialPedidoCollection);
            Collection<Factura> attachedFacturaCollection = new ArrayList<Factura>();
            for (Factura facturaCollectionFacturaToAttach : usuario.getFacturaCollection()) {
                facturaCollectionFacturaToAttach = em.getReference(facturaCollectionFacturaToAttach.getClass(), facturaCollectionFacturaToAttach.getIdFactura());
                attachedFacturaCollection.add(facturaCollectionFacturaToAttach);
            }
            usuario.setFacturaCollection(attachedFacturaCollection);
            Collection<Pedido> attachedPedidoCollection = new ArrayList<Pedido>();
            for (Pedido pedidoCollectionPedidoToAttach : usuario.getPedidoCollection()) {
                pedidoCollectionPedidoToAttach = em.getReference(pedidoCollectionPedidoToAttach.getClass(), pedidoCollectionPedidoToAttach.getIdPedido());
                attachedPedidoCollection.add(pedidoCollectionPedidoToAttach);
            }
            usuario.setPedidoCollection(attachedPedidoCollection);
            em.persist(usuario);
            for (Rol rolCollectionRol : usuario.getRolCollection()) {
                rolCollectionRol.getUsuarioCollection().add(usuario);
                rolCollectionRol = em.merge(rolCollectionRol);
            }
            for (EntregaPedido entregaPedidoCollectionEntregaPedido : usuario.getEntregaPedidoCollection()) {
                Usuario oldIdUsuarioRepartidorOfEntregaPedidoCollectionEntregaPedido = entregaPedidoCollectionEntregaPedido.getIdUsuarioRepartidor();
                entregaPedidoCollectionEntregaPedido.setIdUsuarioRepartidor(usuario);
                entregaPedidoCollectionEntregaPedido = em.merge(entregaPedidoCollectionEntregaPedido);
                if (oldIdUsuarioRepartidorOfEntregaPedidoCollectionEntregaPedido != null) {
                    oldIdUsuarioRepartidorOfEntregaPedidoCollectionEntregaPedido.getEntregaPedidoCollection().remove(entregaPedidoCollectionEntregaPedido);
                    oldIdUsuarioRepartidorOfEntregaPedidoCollectionEntregaPedido = em.merge(oldIdUsuarioRepartidorOfEntregaPedidoCollectionEntregaPedido);
                }
            }
            for (HistorialPedido historialPedidoCollectionHistorialPedido : usuario.getHistorialPedidoCollection()) {
                Usuario oldIdUsuarioOfHistorialPedidoCollectionHistorialPedido = historialPedidoCollectionHistorialPedido.getIdUsuario();
                historialPedidoCollectionHistorialPedido.setIdUsuario(usuario);
                historialPedidoCollectionHistorialPedido = em.merge(historialPedidoCollectionHistorialPedido);
                if (oldIdUsuarioOfHistorialPedidoCollectionHistorialPedido != null) {
                    oldIdUsuarioOfHistorialPedidoCollectionHistorialPedido.getHistorialPedidoCollection().remove(historialPedidoCollectionHistorialPedido);
                    oldIdUsuarioOfHistorialPedidoCollectionHistorialPedido = em.merge(oldIdUsuarioOfHistorialPedidoCollectionHistorialPedido);
                }
            }
            for (Factura facturaCollectionFactura : usuario.getFacturaCollection()) {
                Usuario oldIdUsuarioOfFacturaCollectionFactura = facturaCollectionFactura.getIdUsuario();
                facturaCollectionFactura.setIdUsuario(usuario);
                facturaCollectionFactura = em.merge(facturaCollectionFactura);
                if (oldIdUsuarioOfFacturaCollectionFactura != null) {
                    oldIdUsuarioOfFacturaCollectionFactura.getFacturaCollection().remove(facturaCollectionFactura);
                    oldIdUsuarioOfFacturaCollectionFactura = em.merge(oldIdUsuarioOfFacturaCollectionFactura);
                }
            }
            for (Pedido pedidoCollectionPedido : usuario.getPedidoCollection()) {
                Usuario oldIdUsuarioOfPedidoCollectionPedido = pedidoCollectionPedido.getIdUsuario();
                pedidoCollectionPedido.setIdUsuario(usuario);
                pedidoCollectionPedido = em.merge(pedidoCollectionPedido);
                if (oldIdUsuarioOfPedidoCollectionPedido != null) {
                    oldIdUsuarioOfPedidoCollectionPedido.getPedidoCollection().remove(pedidoCollectionPedido);
                    oldIdUsuarioOfPedidoCollectionPedido = em.merge(oldIdUsuarioOfPedidoCollectionPedido);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getIdUsuario());
            Collection<Rol> rolCollectionOld = persistentUsuario.getRolCollection();
            Collection<Rol> rolCollectionNew = usuario.getRolCollection();
            Collection<EntregaPedido> entregaPedidoCollectionOld = persistentUsuario.getEntregaPedidoCollection();
            Collection<EntregaPedido> entregaPedidoCollectionNew = usuario.getEntregaPedidoCollection();
            Collection<HistorialPedido> historialPedidoCollectionOld = persistentUsuario.getHistorialPedidoCollection();
            Collection<HistorialPedido> historialPedidoCollectionNew = usuario.getHistorialPedidoCollection();
            Collection<Factura> facturaCollectionOld = persistentUsuario.getFacturaCollection();
            Collection<Factura> facturaCollectionNew = usuario.getFacturaCollection();
            Collection<Pedido> pedidoCollectionOld = persistentUsuario.getPedidoCollection();
            Collection<Pedido> pedidoCollectionNew = usuario.getPedidoCollection();
            List<String> illegalOrphanMessages = null;
            for (EntregaPedido entregaPedidoCollectionOldEntregaPedido : entregaPedidoCollectionOld) {
                if (!entregaPedidoCollectionNew.contains(entregaPedidoCollectionOldEntregaPedido)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain EntregaPedido " + entregaPedidoCollectionOldEntregaPedido + " since its idUsuarioRepartidor field is not nullable.");
                }
            }
            for (HistorialPedido historialPedidoCollectionOldHistorialPedido : historialPedidoCollectionOld) {
                if (!historialPedidoCollectionNew.contains(historialPedidoCollectionOldHistorialPedido)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain HistorialPedido " + historialPedidoCollectionOldHistorialPedido + " since its idUsuario field is not nullable.");
                }
            }
            for (Factura facturaCollectionOldFactura : facturaCollectionOld) {
                if (!facturaCollectionNew.contains(facturaCollectionOldFactura)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Factura " + facturaCollectionOldFactura + " since its idUsuario field is not nullable.");
                }
            }
            for (Pedido pedidoCollectionOldPedido : pedidoCollectionOld) {
                if (!pedidoCollectionNew.contains(pedidoCollectionOldPedido)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Pedido " + pedidoCollectionOldPedido + " since its idUsuario field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Rol> attachedRolCollectionNew = new ArrayList<Rol>();
            for (Rol rolCollectionNewRolToAttach : rolCollectionNew) {
                rolCollectionNewRolToAttach = em.getReference(rolCollectionNewRolToAttach.getClass(), rolCollectionNewRolToAttach.getIdRol());
                attachedRolCollectionNew.add(rolCollectionNewRolToAttach);
            }
            rolCollectionNew = attachedRolCollectionNew;
            usuario.setRolCollection(rolCollectionNew);
            Collection<EntregaPedido> attachedEntregaPedidoCollectionNew = new ArrayList<EntregaPedido>();
            for (EntregaPedido entregaPedidoCollectionNewEntregaPedidoToAttach : entregaPedidoCollectionNew) {
                entregaPedidoCollectionNewEntregaPedidoToAttach = em.getReference(entregaPedidoCollectionNewEntregaPedidoToAttach.getClass(), entregaPedidoCollectionNewEntregaPedidoToAttach.getIdEntrega());
                attachedEntregaPedidoCollectionNew.add(entregaPedidoCollectionNewEntregaPedidoToAttach);
            }
            entregaPedidoCollectionNew = attachedEntregaPedidoCollectionNew;
            usuario.setEntregaPedidoCollection(entregaPedidoCollectionNew);
            Collection<HistorialPedido> attachedHistorialPedidoCollectionNew = new ArrayList<HistorialPedido>();
            for (HistorialPedido historialPedidoCollectionNewHistorialPedidoToAttach : historialPedidoCollectionNew) {
                historialPedidoCollectionNewHistorialPedidoToAttach = em.getReference(historialPedidoCollectionNewHistorialPedidoToAttach.getClass(), historialPedidoCollectionNewHistorialPedidoToAttach.getIdHistorial());
                attachedHistorialPedidoCollectionNew.add(historialPedidoCollectionNewHistorialPedidoToAttach);
            }
            historialPedidoCollectionNew = attachedHistorialPedidoCollectionNew;
            usuario.setHistorialPedidoCollection(historialPedidoCollectionNew);
            Collection<Factura> attachedFacturaCollectionNew = new ArrayList<Factura>();
            for (Factura facturaCollectionNewFacturaToAttach : facturaCollectionNew) {
                facturaCollectionNewFacturaToAttach = em.getReference(facturaCollectionNewFacturaToAttach.getClass(), facturaCollectionNewFacturaToAttach.getIdFactura());
                attachedFacturaCollectionNew.add(facturaCollectionNewFacturaToAttach);
            }
            facturaCollectionNew = attachedFacturaCollectionNew;
            usuario.setFacturaCollection(facturaCollectionNew);
            Collection<Pedido> attachedPedidoCollectionNew = new ArrayList<Pedido>();
            for (Pedido pedidoCollectionNewPedidoToAttach : pedidoCollectionNew) {
                pedidoCollectionNewPedidoToAttach = em.getReference(pedidoCollectionNewPedidoToAttach.getClass(), pedidoCollectionNewPedidoToAttach.getIdPedido());
                attachedPedidoCollectionNew.add(pedidoCollectionNewPedidoToAttach);
            }
            pedidoCollectionNew = attachedPedidoCollectionNew;
            usuario.setPedidoCollection(pedidoCollectionNew);
            usuario = em.merge(usuario);
            for (Rol rolCollectionOldRol : rolCollectionOld) {
                if (!rolCollectionNew.contains(rolCollectionOldRol)) {
                    rolCollectionOldRol.getUsuarioCollection().remove(usuario);
                    rolCollectionOldRol = em.merge(rolCollectionOldRol);
                }
            }
            for (Rol rolCollectionNewRol : rolCollectionNew) {
                if (!rolCollectionOld.contains(rolCollectionNewRol)) {
                    rolCollectionNewRol.getUsuarioCollection().add(usuario);
                    rolCollectionNewRol = em.merge(rolCollectionNewRol);
                }
            }
            for (EntregaPedido entregaPedidoCollectionNewEntregaPedido : entregaPedidoCollectionNew) {
                if (!entregaPedidoCollectionOld.contains(entregaPedidoCollectionNewEntregaPedido)) {
                    Usuario oldIdUsuarioRepartidorOfEntregaPedidoCollectionNewEntregaPedido = entregaPedidoCollectionNewEntregaPedido.getIdUsuarioRepartidor();
                    entregaPedidoCollectionNewEntregaPedido.setIdUsuarioRepartidor(usuario);
                    entregaPedidoCollectionNewEntregaPedido = em.merge(entregaPedidoCollectionNewEntregaPedido);
                    if (oldIdUsuarioRepartidorOfEntregaPedidoCollectionNewEntregaPedido != null && !oldIdUsuarioRepartidorOfEntregaPedidoCollectionNewEntregaPedido.equals(usuario)) {
                        oldIdUsuarioRepartidorOfEntregaPedidoCollectionNewEntregaPedido.getEntregaPedidoCollection().remove(entregaPedidoCollectionNewEntregaPedido);
                        oldIdUsuarioRepartidorOfEntregaPedidoCollectionNewEntregaPedido = em.merge(oldIdUsuarioRepartidorOfEntregaPedidoCollectionNewEntregaPedido);
                    }
                }
            }
            for (HistorialPedido historialPedidoCollectionNewHistorialPedido : historialPedidoCollectionNew) {
                if (!historialPedidoCollectionOld.contains(historialPedidoCollectionNewHistorialPedido)) {
                    Usuario oldIdUsuarioOfHistorialPedidoCollectionNewHistorialPedido = historialPedidoCollectionNewHistorialPedido.getIdUsuario();
                    historialPedidoCollectionNewHistorialPedido.setIdUsuario(usuario);
                    historialPedidoCollectionNewHistorialPedido = em.merge(historialPedidoCollectionNewHistorialPedido);
                    if (oldIdUsuarioOfHistorialPedidoCollectionNewHistorialPedido != null && !oldIdUsuarioOfHistorialPedidoCollectionNewHistorialPedido.equals(usuario)) {
                        oldIdUsuarioOfHistorialPedidoCollectionNewHistorialPedido.getHistorialPedidoCollection().remove(historialPedidoCollectionNewHistorialPedido);
                        oldIdUsuarioOfHistorialPedidoCollectionNewHistorialPedido = em.merge(oldIdUsuarioOfHistorialPedidoCollectionNewHistorialPedido);
                    }
                }
            }
            for (Factura facturaCollectionNewFactura : facturaCollectionNew) {
                if (!facturaCollectionOld.contains(facturaCollectionNewFactura)) {
                    Usuario oldIdUsuarioOfFacturaCollectionNewFactura = facturaCollectionNewFactura.getIdUsuario();
                    facturaCollectionNewFactura.setIdUsuario(usuario);
                    facturaCollectionNewFactura = em.merge(facturaCollectionNewFactura);
                    if (oldIdUsuarioOfFacturaCollectionNewFactura != null && !oldIdUsuarioOfFacturaCollectionNewFactura.equals(usuario)) {
                        oldIdUsuarioOfFacturaCollectionNewFactura.getFacturaCollection().remove(facturaCollectionNewFactura);
                        oldIdUsuarioOfFacturaCollectionNewFactura = em.merge(oldIdUsuarioOfFacturaCollectionNewFactura);
                    }
                }
            }
            for (Pedido pedidoCollectionNewPedido : pedidoCollectionNew) {
                if (!pedidoCollectionOld.contains(pedidoCollectionNewPedido)) {
                    Usuario oldIdUsuarioOfPedidoCollectionNewPedido = pedidoCollectionNewPedido.getIdUsuario();
                    pedidoCollectionNewPedido.setIdUsuario(usuario);
                    pedidoCollectionNewPedido = em.merge(pedidoCollectionNewPedido);
                    if (oldIdUsuarioOfPedidoCollectionNewPedido != null && !oldIdUsuarioOfPedidoCollectionNewPedido.equals(usuario)) {
                        oldIdUsuarioOfPedidoCollectionNewPedido.getPedidoCollection().remove(pedidoCollectionNewPedido);
                        oldIdUsuarioOfPedidoCollectionNewPedido = em.merge(oldIdUsuarioOfPedidoCollectionNewPedido);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usuario.getIdUsuario();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
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
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getIdUsuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<EntregaPedido> entregaPedidoCollectionOrphanCheck = usuario.getEntregaPedidoCollection();
            for (EntregaPedido entregaPedidoCollectionOrphanCheckEntregaPedido : entregaPedidoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the EntregaPedido " + entregaPedidoCollectionOrphanCheckEntregaPedido + " in its entregaPedidoCollection field has a non-nullable idUsuarioRepartidor field.");
            }
            Collection<HistorialPedido> historialPedidoCollectionOrphanCheck = usuario.getHistorialPedidoCollection();
            for (HistorialPedido historialPedidoCollectionOrphanCheckHistorialPedido : historialPedidoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the HistorialPedido " + historialPedidoCollectionOrphanCheckHistorialPedido + " in its historialPedidoCollection field has a non-nullable idUsuario field.");
            }
            Collection<Factura> facturaCollectionOrphanCheck = usuario.getFacturaCollection();
            for (Factura facturaCollectionOrphanCheckFactura : facturaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Factura " + facturaCollectionOrphanCheckFactura + " in its facturaCollection field has a non-nullable idUsuario field.");
            }
            Collection<Pedido> pedidoCollectionOrphanCheck = usuario.getPedidoCollection();
            for (Pedido pedidoCollectionOrphanCheckPedido : pedidoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Pedido " + pedidoCollectionOrphanCheckPedido + " in its pedidoCollection field has a non-nullable idUsuario field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Rol> rolCollection = usuario.getRolCollection();
            for (Rol rolCollectionRol : rolCollection) {
                rolCollectionRol.getUsuarioCollection().remove(usuario);
                rolCollectionRol = em.merge(rolCollectionRol);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
