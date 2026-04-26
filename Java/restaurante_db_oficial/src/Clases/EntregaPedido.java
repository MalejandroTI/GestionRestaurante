/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author ASUS
 */
@Entity
@Table(name = "entrega_pedido")
@NamedQueries({
    @NamedQuery(name = "EntregaPedido.findAll", query = "SELECT e FROM EntregaPedido e"),
    @NamedQuery(name = "EntregaPedido.findByIdEntrega", query = "SELECT e FROM EntregaPedido e WHERE e.idEntrega = :idEntrega"),
    @NamedQuery(name = "EntregaPedido.findByDireccionEntrega", query = "SELECT e FROM EntregaPedido e WHERE e.direccionEntrega = :direccionEntrega"),
    @NamedQuery(name = "EntregaPedido.findByFechaHoraSalida", query = "SELECT e FROM EntregaPedido e WHERE e.fechaHoraSalida = :fechaHoraSalida"),
    @NamedQuery(name = "EntregaPedido.findByFechaHoraEntrega", query = "SELECT e FROM EntregaPedido e WHERE e.fechaHoraEntrega = :fechaHoraEntrega"),
    @NamedQuery(name = "EntregaPedido.findByNombreRecibe", query = "SELECT e FROM EntregaPedido e WHERE e.nombreRecibe = :nombreRecibe"),
    @NamedQuery(name = "EntregaPedido.findByCostoEnvio", query = "SELECT e FROM EntregaPedido e WHERE e.costoEnvio = :costoEnvio"),
    @NamedQuery(name = "EntregaPedido.findByDistanciaKm", query = "SELECT e FROM EntregaPedido e WHERE e.distanciaKm = :distanciaKm")})
public class EntregaPedido implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_entrega")
    private Integer idEntrega;
    @Basic(optional = false)
    @Column(name = "direccion_entrega")
    private String direccionEntrega;
    @Basic(optional = false)
    @Column(name = "fecha_hora_salida")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraSalida;
    @Column(name = "fecha_hora_entrega")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraEntrega;
    @Column(name = "nombre_recibe")
    private String nombreRecibe;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "costo_envio")
    private BigDecimal costoEnvio;
    @Basic(optional = false)
    @Column(name = "distancia_km")
    private BigDecimal distanciaKm;
    @Lob
    @Column(name = "observacion")
    private String observacion;
    @JoinColumn(name = "id_pedido", referencedColumnName = "id_pedido")
    @OneToOne(optional = false)
    private Pedido idPedido;
    @JoinColumn(name = "id_tarifa", referencedColumnName = "id_tarifa")
    @ManyToOne(optional = false)
    private Tarifa idTarifa;
    @JoinColumn(name = "id_usuario_repartidor", referencedColumnName = "id_usuario")
    @ManyToOne(optional = false)
    private Usuario idUsuarioRepartidor;

    public EntregaPedido() {
    }

    public EntregaPedido(Integer idEntrega) {
        this.idEntrega = idEntrega;
    }

    public EntregaPedido(Integer idEntrega, String direccionEntrega, Date fechaHoraSalida, BigDecimal costoEnvio, BigDecimal distanciaKm) {
        this.idEntrega = idEntrega;
        this.direccionEntrega = direccionEntrega;
        this.fechaHoraSalida = fechaHoraSalida;
        this.costoEnvio = costoEnvio;
        this.distanciaKm = distanciaKm;
    }

    public Integer getIdEntrega() {
        return idEntrega;
    }

    public void setIdEntrega(Integer idEntrega) {
        this.idEntrega = idEntrega;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    public Date getFechaHoraSalida() {
        return fechaHoraSalida;
    }

    public void setFechaHoraSalida(Date fechaHoraSalida) {
        this.fechaHoraSalida = fechaHoraSalida;
    }

    public Date getFechaHoraEntrega() {
        return fechaHoraEntrega;
    }

    public void setFechaHoraEntrega(Date fechaHoraEntrega) {
        this.fechaHoraEntrega = fechaHoraEntrega;
    }

    public String getNombreRecibe() {
        return nombreRecibe;
    }

    public void setNombreRecibe(String nombreRecibe) {
        this.nombreRecibe = nombreRecibe;
    }

    public BigDecimal getCostoEnvio() {
        return costoEnvio;
    }

    public void setCostoEnvio(BigDecimal costoEnvio) {
        this.costoEnvio = costoEnvio;
    }

    public BigDecimal getDistanciaKm() {
        return distanciaKm;
    }

    public void setDistanciaKm(BigDecimal distanciaKm) {
        this.distanciaKm = distanciaKm;
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

    public Tarifa getIdTarifa() {
        return idTarifa;
    }

    public void setIdTarifa(Tarifa idTarifa) {
        this.idTarifa = idTarifa;
    }

    public Usuario getIdUsuarioRepartidor() {
        return idUsuarioRepartidor;
    }

    public void setIdUsuarioRepartidor(Usuario idUsuarioRepartidor) {
        this.idUsuarioRepartidor = idUsuarioRepartidor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEntrega != null ? idEntrega.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EntregaPedido)) {
            return false;
        }
        EntregaPedido other = (EntregaPedido) object;
        if ((this.idEntrega == null && other.idEntrega != null) || (this.idEntrega != null && !this.idEntrega.equals(other.idEntrega))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Clases.EntregaPedido[ idEntrega=" + idEntrega + " ]";
    }
    
}
