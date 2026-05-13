/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import ClasesEnum.enums.EstadoPedido;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author ASUS
 */
@Entity
@Table(name = "historial_pedido")
@NamedQueries({
    @NamedQuery(name = "HistorialPedido.findAll", query = "SELECT h FROM HistorialPedido h"),
    @NamedQuery(name = "HistorialPedido.findByIdHistorial", query = "SELECT h FROM HistorialPedido h WHERE h.idHistorial = :idHistorial"),
    @NamedQuery(name = "HistorialPedido.findByEstado", query = "SELECT h FROM HistorialPedido h WHERE h.estado = :estado"),
    @NamedQuery(name = "HistorialPedido.findByFechaHora", query = "SELECT h FROM HistorialPedido h WHERE h.fechaHora = :fechaHora")})
public class HistorialPedido implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_historial")
    private Integer idHistorial;
    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoPedido estado;
    @Basic(optional = false)
    @Column(name = "fecha_hora")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHora;
    @Lob
    @Column(name = "observacion")
    private String observacion;
    @JoinColumn(name = "id_pedido", referencedColumnName = "id_pedido")
    @ManyToOne(optional = false)
    private Pedido idPedido;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    @ManyToOne(optional = false)
    private Usuario idUsuario;

    public HistorialPedido() {
    }

    public HistorialPedido(Integer idHistorial) {
        this.idHistorial = idHistorial;
    }

    public HistorialPedido(Integer idHistorial, EstadoPedido  estado, Date fechaHora) {
        this.idHistorial = idHistorial;
        this.estado = estado;
        this.fechaHora = fechaHora;
    }

    public Integer getIdHistorial() {
        return idHistorial;
    }

    public void setIdHistorial(Integer idHistorial) {
        this.idHistorial = idHistorial;
    }

    public EstadoPedido  getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido  estado) {
        this.estado = estado;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Pedido getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Pedido idPedido) {
        this.idPedido = idPedido;
    }

    public Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuario idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idHistorial != null ? idHistorial.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HistorialPedido)) {
            return false;
        }
        HistorialPedido other = (HistorialPedido) object;
        if ((this.idHistorial == null && other.idHistorial != null) || (this.idHistorial != null && !this.idHistorial.equals(other.idHistorial))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Clases.HistorialPedido[ idHistorial=" + idHistorial + " ]";
    }

}
