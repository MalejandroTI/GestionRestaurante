/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author ASUS
 */
@Entity
@Table(name = "tarifa")
@NamedQueries({
    @NamedQuery(name = "Tarifa.findAll", query = "SELECT t FROM Tarifa t"),
    @NamedQuery(name = "Tarifa.findByIdTarifa", query = "SELECT t FROM Tarifa t WHERE t.idTarifa = :idTarifa"),
    @NamedQuery(name = "Tarifa.findByKmMin", query = "SELECT t FROM Tarifa t WHERE t.kmMin = :kmMin"),
    @NamedQuery(name = "Tarifa.findByKmMax", query = "SELECT t FROM Tarifa t WHERE t.kmMax = :kmMax"),
    @NamedQuery(name = "Tarifa.findByPrecio", query = "SELECT t FROM Tarifa t WHERE t.precio = :precio"),
    @NamedQuery(name = "Tarifa.findByDescripcion", query = "SELECT t FROM Tarifa t WHERE t.descripcion = :descripcion")})
public class Tarifa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_tarifa")
    private Integer idTarifa;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "km_min")
    private BigDecimal kmMin;
    @Basic(optional = false)
    @Column(name = "km_max")
    private BigDecimal kmMax;
    @Basic(optional = false)
    @Column(name = "precio")
    private BigDecimal precio;
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTarifa")
    private Collection<EntregaPedido> entregaPedidoCollection;

    public Tarifa() {
    }

    public Tarifa(Integer idTarifa) {
        this.idTarifa = idTarifa;
    }

    public Tarifa(Integer idTarifa, BigDecimal kmMin, BigDecimal kmMax, BigDecimal precio) {
        this.idTarifa = idTarifa;
        this.kmMin = kmMin;
        this.kmMax = kmMax;
        this.precio = precio;
    }

    public Integer getIdTarifa() {
        return idTarifa;
    }

    public void setIdTarifa(Integer idTarifa) {
        this.idTarifa = idTarifa;
    }

    public BigDecimal getKmMin() {
        return kmMin;
    }

    public void setKmMin(BigDecimal kmMin) {
        this.kmMin = kmMin;
    }

    public BigDecimal getKmMax() {
        return kmMax;
    }

    public void setKmMax(BigDecimal kmMax) {
        this.kmMax = kmMax;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Collection<EntregaPedido> getEntregaPedidoCollection() {
        return entregaPedidoCollection;
    }

    public void setEntregaPedidoCollection(Collection<EntregaPedido> entregaPedidoCollection) {
        this.entregaPedidoCollection = entregaPedidoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTarifa != null ? idTarifa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tarifa)) {
            return false;
        }
        Tarifa other = (Tarifa) object;
        if ((this.idTarifa == null && other.idTarifa != null) || (this.idTarifa != null && !this.idTarifa.equals(other.idTarifa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Clases.Tarifa[ idTarifa=" + idTarifa + " ]";
    }
    
}
