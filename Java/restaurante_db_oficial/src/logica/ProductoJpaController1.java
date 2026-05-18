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
import Clases.Categoria;
import Clases.DetallePedido;
import java.util.ArrayList;
import java.util.Collection;
import Clases.DetalleFactura;
import Clases.Producto;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import logica.exceptions.NonexistentEntityException;

/**
 *
 * @author ASUS
 */
public class ProductoJpaController1 implements Serializable {

    public ProductoJpaController1(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Producto producto) {
        if (producto.getDetallePedidoCollection() == null) {
            producto.setDetallePedidoCollection(new ArrayList<DetallePedido>());
        }
        if (producto.getDetalleFacturaCollection() == null) {
            producto.setDetalleFacturaCollection(new ArrayList<DetalleFactura>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Categoria idCategoria = producto.getIdCategoria();
            if (idCategoria != null) {
                idCategoria = em.getReference(idCategoria.getClass(), idCategoria.getIdCategoria());
                producto.setIdCategoria(idCategoria);
            }
            Collection<DetallePedido> attachedDetallePedidoCollection = new ArrayList<DetallePedido>();
            for (DetallePedido detallePedidoCollectionDetallePedidoToAttach : producto.getDetallePedidoCollection()) {
                detallePedidoCollectionDetallePedidoToAttach = em.getReference(detallePedidoCollectionDetallePedidoToAttach.getClass(), detallePedidoCollectionDetallePedidoToAttach.getIdDetalle());
                attachedDetallePedidoCollection.add(detallePedidoCollectionDetallePedidoToAttach);
            }
            producto.setDetallePedidoCollection(attachedDetallePedidoCollection);
            Collection<DetalleFactura> attachedDetalleFacturaCollection = new ArrayList<DetalleFactura>();
            for (DetalleFactura detalleFacturaCollectionDetalleFacturaToAttach : producto.getDetalleFacturaCollection()) {
                detalleFacturaCollectionDetalleFacturaToAttach = em.getReference(detalleFacturaCollectionDetalleFacturaToAttach.getClass(), detalleFacturaCollectionDetalleFacturaToAttach.getIdDetalleFactura());
                attachedDetalleFacturaCollection.add(detalleFacturaCollectionDetalleFacturaToAttach);
            }
            producto.setDetalleFacturaCollection(attachedDetalleFacturaCollection);
            em.persist(producto);
            if (idCategoria != null) {
                idCategoria.getProductoCollection().add(producto);
                idCategoria = em.merge(idCategoria);
            }
            for (DetallePedido detallePedidoCollectionDetallePedido : producto.getDetallePedidoCollection()) {
                Producto oldIdProductoOfDetallePedidoCollectionDetallePedido = detallePedidoCollectionDetallePedido.getIdProducto();
                detallePedidoCollectionDetallePedido.setIdProducto(producto);
                detallePedidoCollectionDetallePedido = em.merge(detallePedidoCollectionDetallePedido);
                if (oldIdProductoOfDetallePedidoCollectionDetallePedido != null) {
                    oldIdProductoOfDetallePedidoCollectionDetallePedido.getDetallePedidoCollection().remove(detallePedidoCollectionDetallePedido);
                    oldIdProductoOfDetallePedidoCollectionDetallePedido = em.merge(oldIdProductoOfDetallePedidoCollectionDetallePedido);
                }
            }
            for (DetalleFactura detalleFacturaCollectionDetalleFactura : producto.getDetalleFacturaCollection()) {
                Producto oldIdProductoOfDetalleFacturaCollectionDetalleFactura = detalleFacturaCollectionDetalleFactura.getIdProducto();
                detalleFacturaCollectionDetalleFactura.setIdProducto(producto);
                detalleFacturaCollectionDetalleFactura = em.merge(detalleFacturaCollectionDetalleFactura);
                if (oldIdProductoOfDetalleFacturaCollectionDetalleFactura != null) {
                    oldIdProductoOfDetalleFacturaCollectionDetalleFactura.getDetalleFacturaCollection().remove(detalleFacturaCollectionDetalleFactura);
                    oldIdProductoOfDetalleFacturaCollectionDetalleFactura = em.merge(oldIdProductoOfDetalleFacturaCollectionDetalleFactura);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Producto producto) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Producto persistentProducto = em.find(Producto.class, producto.getIdProducto());
            Categoria idCategoriaOld = persistentProducto.getIdCategoria();
            Categoria idCategoriaNew = producto.getIdCategoria();
            Collection<DetallePedido> detallePedidoCollectionOld = persistentProducto.getDetallePedidoCollection();
            Collection<DetallePedido> detallePedidoCollectionNew = producto.getDetallePedidoCollection();
            Collection<DetalleFactura> detalleFacturaCollectionOld = persistentProducto.getDetalleFacturaCollection();
            Collection<DetalleFactura> detalleFacturaCollectionNew = producto.getDetalleFacturaCollection();
            if (idCategoriaNew != null) {
                idCategoriaNew = em.getReference(idCategoriaNew.getClass(), idCategoriaNew.getIdCategoria());
                producto.setIdCategoria(idCategoriaNew);
            }
            Collection<DetallePedido> attachedDetallePedidoCollectionNew = new ArrayList<DetallePedido>();
            for (DetallePedido detallePedidoCollectionNewDetallePedidoToAttach : detallePedidoCollectionNew) {
                detallePedidoCollectionNewDetallePedidoToAttach = em.getReference(detallePedidoCollectionNewDetallePedidoToAttach.getClass(), detallePedidoCollectionNewDetallePedidoToAttach.getIdDetalle());
                attachedDetallePedidoCollectionNew.add(detallePedidoCollectionNewDetallePedidoToAttach);
            }
            detallePedidoCollectionNew = attachedDetallePedidoCollectionNew;
            producto.setDetallePedidoCollection(detallePedidoCollectionNew);
            Collection<DetalleFactura> attachedDetalleFacturaCollectionNew = new ArrayList<DetalleFactura>();
            for (DetalleFactura detalleFacturaCollectionNewDetalleFacturaToAttach : detalleFacturaCollectionNew) {
                detalleFacturaCollectionNewDetalleFacturaToAttach = em.getReference(detalleFacturaCollectionNewDetalleFacturaToAttach.getClass(), detalleFacturaCollectionNewDetalleFacturaToAttach.getIdDetalleFactura());
                attachedDetalleFacturaCollectionNew.add(detalleFacturaCollectionNewDetalleFacturaToAttach);
            }
            detalleFacturaCollectionNew = attachedDetalleFacturaCollectionNew;
            producto.setDetalleFacturaCollection(detalleFacturaCollectionNew);
            producto = em.merge(producto);
            if (idCategoriaOld != null && !idCategoriaOld.equals(idCategoriaNew)) {
                idCategoriaOld.getProductoCollection().remove(producto);
                idCategoriaOld = em.merge(idCategoriaOld);
            }
            if (idCategoriaNew != null && !idCategoriaNew.equals(idCategoriaOld)) {
                idCategoriaNew.getProductoCollection().add(producto);
                idCategoriaNew = em.merge(idCategoriaNew);
            }
            for (DetallePedido detallePedidoCollectionOldDetallePedido : detallePedidoCollectionOld) {
                if (!detallePedidoCollectionNew.contains(detallePedidoCollectionOldDetallePedido)) {
                    detallePedidoCollectionOldDetallePedido.setIdProducto(null);
                    detallePedidoCollectionOldDetallePedido = em.merge(detallePedidoCollectionOldDetallePedido);
                }
            }
            for (DetallePedido detallePedidoCollectionNewDetallePedido : detallePedidoCollectionNew) {
                if (!detallePedidoCollectionOld.contains(detallePedidoCollectionNewDetallePedido)) {
                    Producto oldIdProductoOfDetallePedidoCollectionNewDetallePedido = detallePedidoCollectionNewDetallePedido.getIdProducto();
                    detallePedidoCollectionNewDetallePedido.setIdProducto(producto);
                    detallePedidoCollectionNewDetallePedido = em.merge(detallePedidoCollectionNewDetallePedido);
                    if (oldIdProductoOfDetallePedidoCollectionNewDetallePedido != null && !oldIdProductoOfDetallePedidoCollectionNewDetallePedido.equals(producto)) {
                        oldIdProductoOfDetallePedidoCollectionNewDetallePedido.getDetallePedidoCollection().remove(detallePedidoCollectionNewDetallePedido);
                        oldIdProductoOfDetallePedidoCollectionNewDetallePedido = em.merge(oldIdProductoOfDetallePedidoCollectionNewDetallePedido);
                    }
                }
            }
            for (DetalleFactura detalleFacturaCollectionOldDetalleFactura : detalleFacturaCollectionOld) {
                if (!detalleFacturaCollectionNew.contains(detalleFacturaCollectionOldDetalleFactura)) {
                    detalleFacturaCollectionOldDetalleFactura.setIdProducto(null);
                    detalleFacturaCollectionOldDetalleFactura = em.merge(detalleFacturaCollectionOldDetalleFactura);
                }
            }
            for (DetalleFactura detalleFacturaCollectionNewDetalleFactura : detalleFacturaCollectionNew) {
                if (!detalleFacturaCollectionOld.contains(detalleFacturaCollectionNewDetalleFactura)) {
                    Producto oldIdProductoOfDetalleFacturaCollectionNewDetalleFactura = detalleFacturaCollectionNewDetalleFactura.getIdProducto();
                    detalleFacturaCollectionNewDetalleFactura.setIdProducto(producto);
                    detalleFacturaCollectionNewDetalleFactura = em.merge(detalleFacturaCollectionNewDetalleFactura);
                    if (oldIdProductoOfDetalleFacturaCollectionNewDetalleFactura != null && !oldIdProductoOfDetalleFacturaCollectionNewDetalleFactura.equals(producto)) {
                        oldIdProductoOfDetalleFacturaCollectionNewDetalleFactura.getDetalleFacturaCollection().remove(detalleFacturaCollectionNewDetalleFactura);
                        oldIdProductoOfDetalleFacturaCollectionNewDetalleFactura = em.merge(oldIdProductoOfDetalleFacturaCollectionNewDetalleFactura);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = producto.getIdProducto();
                if (findProducto(id) == null) {
                    throw new NonexistentEntityException("The producto with id " + id + " no longer exists.");
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
            Producto producto;
            try {
                producto = em.getReference(Producto.class, id);
                producto.getIdProducto();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The producto with id " + id + " no longer exists.", enfe);
            }
            Categoria idCategoria = producto.getIdCategoria();
            if (idCategoria != null) {
                idCategoria.getProductoCollection().remove(producto);
                idCategoria = em.merge(idCategoria);
            }
            Collection<DetallePedido> detallePedidoCollection = producto.getDetallePedidoCollection();
            for (DetallePedido detallePedidoCollectionDetallePedido : detallePedidoCollection) {
                detallePedidoCollectionDetallePedido.setIdProducto(null);
                detallePedidoCollectionDetallePedido = em.merge(detallePedidoCollectionDetallePedido);
            }
            Collection<DetalleFactura> detalleFacturaCollection = producto.getDetalleFacturaCollection();
            for (DetalleFactura detalleFacturaCollectionDetalleFactura : detalleFacturaCollection) {
                detalleFacturaCollectionDetalleFactura.setIdProducto(null);
                detalleFacturaCollectionDetalleFactura = em.merge(detalleFacturaCollectionDetalleFactura);
            }
            em.remove(producto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Producto> findProductoEntities() {
        return findProductoEntities(true, -1, -1);
    }

    public List<Producto> findProductoEntities(int maxResults, int firstResult) {
        return findProductoEntities(false, maxResults, firstResult);
    }

    private List<Producto> findProductoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Producto.class));
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

    public Producto findProducto(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Producto.class, id);
        } finally {
            em.close();
        }
    }

    public int getProductoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Producto> rt = cq.from(Producto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
