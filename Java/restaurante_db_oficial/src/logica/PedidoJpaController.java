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
import Clases.Factura;
import Clases.Cliente;
import Clases.Usuario;
import Clases.HistorialPedido;
import java.util.ArrayList;
import java.util.Collection;
import Clases.DetallePedido;
import Clases.Pedido;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import logica.exceptions.IllegalOrphanException;
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
        if (pedido.getHistorialPedidoCollection() == null) {
            pedido.setHistorialPedidoCollection(new ArrayList<HistorialPedido>());
        }
        if (pedido.getDetallePedidoCollection() == null) {
            pedido.setDetallePedidoCollection(new ArrayList<DetallePedido>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EntregaPedido entregaPedido = pedido.getEntregaPedido();
            if (entregaPedido != null) {
                entregaPedido = em.getReference(entregaPedido.getClass(), entregaPedido.getIdEntrega());
                pedido.setEntregaPedido(entregaPedido);
            }
            Factura factura = pedido.getFactura();
            if (factura != null) {
                factura = em.getReference(factura.getClass(), factura.getIdFactura());
                pedido.setFactura(factura);
            }
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
            Collection<HistorialPedido> attachedHistorialPedidoCollection = new ArrayList<HistorialPedido>();
            for (HistorialPedido historialPedidoCollectionHistorialPedidoToAttach : pedido.getHistorialPedidoCollection()) {
                historialPedidoCollectionHistorialPedidoToAttach = em.getReference(historialPedidoCollectionHistorialPedidoToAttach.getClass(), historialPedidoCollectionHistorialPedidoToAttach.getIdHistorial());
                attachedHistorialPedidoCollection.add(historialPedidoCollectionHistorialPedidoToAttach);
            }
            pedido.setHistorialPedidoCollection(attachedHistorialPedidoCollection);
            Collection<DetallePedido> attachedDetallePedidoCollection = new ArrayList<DetallePedido>();
            for (DetallePedido detallePedidoCollectionDetallePedidoToAttach : pedido.getDetallePedidoCollection()) {
                detallePedidoCollectionDetallePedidoToAttach = em.getReference(detallePedidoCollectionDetallePedidoToAttach.getClass(), detallePedidoCollectionDetallePedidoToAttach.getIdDetalle());
                attachedDetallePedidoCollection.add(detallePedidoCollectionDetallePedidoToAttach);
            }
            pedido.setDetallePedidoCollection(attachedDetallePedidoCollection);
            em.persist(pedido);
            if (entregaPedido != null) {
                Pedido oldIdPedidoOfEntregaPedido = entregaPedido.getIdPedido();
                if (oldIdPedidoOfEntregaPedido != null) {
                    oldIdPedidoOfEntregaPedido.setEntregaPedido(null);
                    oldIdPedidoOfEntregaPedido = em.merge(oldIdPedidoOfEntregaPedido);
                }
                entregaPedido.setIdPedido(pedido);
                entregaPedido = em.merge(entregaPedido);
            }
            if (factura != null) {
                Pedido oldIdPedidoOfFactura = factura.getIdPedido();
                if (oldIdPedidoOfFactura != null) {
                    oldIdPedidoOfFactura.setFactura(null);
                    oldIdPedidoOfFactura = em.merge(oldIdPedidoOfFactura);
                }
                factura.setIdPedido(pedido);
                factura = em.merge(factura);
            }
            if (idCliente != null) {
                idCliente.getPedidoCollection().add(pedido);
                idCliente = em.merge(idCliente);
            }
            if (idUsuario != null) {
                idUsuario.getPedidoCollection().add(pedido);
                idUsuario = em.merge(idUsuario);
            }
            for (HistorialPedido historialPedidoCollectionHistorialPedido : pedido.getHistorialPedidoCollection()) {
                Pedido oldIdPedidoOfHistorialPedidoCollectionHistorialPedido = historialPedidoCollectionHistorialPedido.getIdPedido();
                historialPedidoCollectionHistorialPedido.setIdPedido(pedido);
                historialPedidoCollectionHistorialPedido = em.merge(historialPedidoCollectionHistorialPedido);
                if (oldIdPedidoOfHistorialPedidoCollectionHistorialPedido != null) {
                    oldIdPedidoOfHistorialPedidoCollectionHistorialPedido.getHistorialPedidoCollection().remove(historialPedidoCollectionHistorialPedido);
                    oldIdPedidoOfHistorialPedidoCollectionHistorialPedido = em.merge(oldIdPedidoOfHistorialPedidoCollectionHistorialPedido);
                }
            }
            for (DetallePedido detallePedidoCollectionDetallePedido : pedido.getDetallePedidoCollection()) {
                Pedido oldIdPedidoOfDetallePedidoCollectionDetallePedido = detallePedidoCollectionDetallePedido.getIdPedido();
                detallePedidoCollectionDetallePedido.setIdPedido(pedido);
                detallePedidoCollectionDetallePedido = em.merge(detallePedidoCollectionDetallePedido);
                if (oldIdPedidoOfDetallePedidoCollectionDetallePedido != null) {
                    oldIdPedidoOfDetallePedidoCollectionDetallePedido.getDetallePedidoCollection().remove(detallePedidoCollectionDetallePedido);
                    oldIdPedidoOfDetallePedidoCollectionDetallePedido = em.merge(oldIdPedidoOfDetallePedidoCollectionDetallePedido);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pedido pedido) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pedido persistentPedido = em.find(Pedido.class, pedido.getIdPedido());
            EntregaPedido entregaPedidoOld = persistentPedido.getEntregaPedido();
            EntregaPedido entregaPedidoNew = pedido.getEntregaPedido();
            Factura facturaOld = persistentPedido.getFactura();
            Factura facturaNew = pedido.getFactura();
            Cliente idClienteOld = persistentPedido.getIdCliente();
            Cliente idClienteNew = pedido.getIdCliente();
            Usuario idUsuarioOld = persistentPedido.getIdUsuario();
            Usuario idUsuarioNew = pedido.getIdUsuario();
            Collection<HistorialPedido> historialPedidoCollectionOld = persistentPedido.getHistorialPedidoCollection();
            Collection<HistorialPedido> historialPedidoCollectionNew = pedido.getHistorialPedidoCollection();
            Collection<DetallePedido> detallePedidoCollectionOld = persistentPedido.getDetallePedidoCollection();
            Collection<DetallePedido> detallePedidoCollectionNew = pedido.getDetallePedidoCollection();
            List<String> illegalOrphanMessages = null;
            if (entregaPedidoOld != null && !entregaPedidoOld.equals(entregaPedidoNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain EntregaPedido " + entregaPedidoOld + " since its idPedido field is not nullable.");
            }
            if (facturaOld != null && !facturaOld.equals(facturaNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Factura " + facturaOld + " since its idPedido field is not nullable.");
            }
            for (HistorialPedido historialPedidoCollectionOldHistorialPedido : historialPedidoCollectionOld) {
                if (!historialPedidoCollectionNew.contains(historialPedidoCollectionOldHistorialPedido)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain HistorialPedido " + historialPedidoCollectionOldHistorialPedido + " since its idPedido field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (entregaPedidoNew != null) {
                entregaPedidoNew = em.getReference(entregaPedidoNew.getClass(), entregaPedidoNew.getIdEntrega());
                pedido.setEntregaPedido(entregaPedidoNew);
            }
            if (facturaNew != null) {
                facturaNew = em.getReference(facturaNew.getClass(), facturaNew.getIdFactura());
                pedido.setFactura(facturaNew);
            }
            if (idClienteNew != null) {
                idClienteNew = em.getReference(idClienteNew.getClass(), idClienteNew.getIdCliente());
                pedido.setIdCliente(idClienteNew);
            }
            if (idUsuarioNew != null) {
                idUsuarioNew = em.getReference(idUsuarioNew.getClass(), idUsuarioNew.getIdUsuario());
                pedido.setIdUsuario(idUsuarioNew);
            }
            Collection<HistorialPedido> attachedHistorialPedidoCollectionNew = new ArrayList<HistorialPedido>();
            for (HistorialPedido historialPedidoCollectionNewHistorialPedidoToAttach : historialPedidoCollectionNew) {
                historialPedidoCollectionNewHistorialPedidoToAttach = em.getReference(historialPedidoCollectionNewHistorialPedidoToAttach.getClass(), historialPedidoCollectionNewHistorialPedidoToAttach.getIdHistorial());
                attachedHistorialPedidoCollectionNew.add(historialPedidoCollectionNewHistorialPedidoToAttach);
            }
            historialPedidoCollectionNew = attachedHistorialPedidoCollectionNew;
            pedido.setHistorialPedidoCollection(historialPedidoCollectionNew);
            Collection<DetallePedido> attachedDetallePedidoCollectionNew = new ArrayList<DetallePedido>();
            for (DetallePedido detallePedidoCollectionNewDetallePedidoToAttach : detallePedidoCollectionNew) {
                detallePedidoCollectionNewDetallePedidoToAttach = em.getReference(detallePedidoCollectionNewDetallePedidoToAttach.getClass(), detallePedidoCollectionNewDetallePedidoToAttach.getIdDetalle());
                attachedDetallePedidoCollectionNew.add(detallePedidoCollectionNewDetallePedidoToAttach);
            }
            detallePedidoCollectionNew = attachedDetallePedidoCollectionNew;
            pedido.setDetallePedidoCollection(detallePedidoCollectionNew);
            pedido = em.merge(pedido);
            if (entregaPedidoNew != null && !entregaPedidoNew.equals(entregaPedidoOld)) {
                Pedido oldIdPedidoOfEntregaPedido = entregaPedidoNew.getIdPedido();
                if (oldIdPedidoOfEntregaPedido != null) {
                    oldIdPedidoOfEntregaPedido.setEntregaPedido(null);
                    oldIdPedidoOfEntregaPedido = em.merge(oldIdPedidoOfEntregaPedido);
                }
                entregaPedidoNew.setIdPedido(pedido);
                entregaPedidoNew = em.merge(entregaPedidoNew);
            }
            if (facturaNew != null && !facturaNew.equals(facturaOld)) {
                Pedido oldIdPedidoOfFactura = facturaNew.getIdPedido();
                if (oldIdPedidoOfFactura != null) {
                    oldIdPedidoOfFactura.setFactura(null);
                    oldIdPedidoOfFactura = em.merge(oldIdPedidoOfFactura);
                }
                facturaNew.setIdPedido(pedido);
                facturaNew = em.merge(facturaNew);
            }
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
            for (HistorialPedido historialPedidoCollectionNewHistorialPedido : historialPedidoCollectionNew) {
                if (!historialPedidoCollectionOld.contains(historialPedidoCollectionNewHistorialPedido)) {
                    Pedido oldIdPedidoOfHistorialPedidoCollectionNewHistorialPedido = historialPedidoCollectionNewHistorialPedido.getIdPedido();
                    historialPedidoCollectionNewHistorialPedido.setIdPedido(pedido);
                    historialPedidoCollectionNewHistorialPedido = em.merge(historialPedidoCollectionNewHistorialPedido);
                    if (oldIdPedidoOfHistorialPedidoCollectionNewHistorialPedido != null && !oldIdPedidoOfHistorialPedidoCollectionNewHistorialPedido.equals(pedido)) {
                        oldIdPedidoOfHistorialPedidoCollectionNewHistorialPedido.getHistorialPedidoCollection().remove(historialPedidoCollectionNewHistorialPedido);
                        oldIdPedidoOfHistorialPedidoCollectionNewHistorialPedido = em.merge(oldIdPedidoOfHistorialPedidoCollectionNewHistorialPedido);
                    }
                }
            }
            for (DetallePedido detallePedidoCollectionOldDetallePedido : detallePedidoCollectionOld) {
                if (!detallePedidoCollectionNew.contains(detallePedidoCollectionOldDetallePedido)) {
                    detallePedidoCollectionOldDetallePedido.setIdPedido(null);
                    detallePedidoCollectionOldDetallePedido = em.merge(detallePedidoCollectionOldDetallePedido);
                }
            }
            for (DetallePedido detallePedidoCollectionNewDetallePedido : detallePedidoCollectionNew) {
                if (!detallePedidoCollectionOld.contains(detallePedidoCollectionNewDetallePedido)) {
                    Pedido oldIdPedidoOfDetallePedidoCollectionNewDetallePedido = detallePedidoCollectionNewDetallePedido.getIdPedido();
                    detallePedidoCollectionNewDetallePedido.setIdPedido(pedido);
                    detallePedidoCollectionNewDetallePedido = em.merge(detallePedidoCollectionNewDetallePedido);
                    if (oldIdPedidoOfDetallePedidoCollectionNewDetallePedido != null && !oldIdPedidoOfDetallePedidoCollectionNewDetallePedido.equals(pedido)) {
                        oldIdPedidoOfDetallePedidoCollectionNewDetallePedido.getDetallePedidoCollection().remove(detallePedidoCollectionNewDetallePedido);
                        oldIdPedidoOfDetallePedidoCollectionNewDetallePedido = em.merge(oldIdPedidoOfDetallePedidoCollectionNewDetallePedido);
                    }
                }
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

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
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
            List<String> illegalOrphanMessages = null;
            EntregaPedido entregaPedidoOrphanCheck = pedido.getEntregaPedido();
            if (entregaPedidoOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pedido (" + pedido + ") cannot be destroyed since the EntregaPedido " + entregaPedidoOrphanCheck + " in its entregaPedido field has a non-nullable idPedido field.");
            }
            Factura facturaOrphanCheck = pedido.getFactura();
            if (facturaOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pedido (" + pedido + ") cannot be destroyed since the Factura " + facturaOrphanCheck + " in its factura field has a non-nullable idPedido field.");
            }
            Collection<HistorialPedido> historialPedidoCollectionOrphanCheck = pedido.getHistorialPedidoCollection();
            for (HistorialPedido historialPedidoCollectionOrphanCheckHistorialPedido : historialPedidoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pedido (" + pedido + ") cannot be destroyed since the HistorialPedido " + historialPedidoCollectionOrphanCheckHistorialPedido + " in its historialPedidoCollection field has a non-nullable idPedido field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
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
            Collection<DetallePedido> detallePedidoCollection = pedido.getDetallePedidoCollection();
            for (DetallePedido detallePedidoCollectionDetallePedido : detallePedidoCollection) {
                detallePedidoCollectionDetallePedido.setIdPedido(null);
                detallePedidoCollectionDetallePedido = em.merge(detallePedidoCollectionDetallePedido);
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
    
}
