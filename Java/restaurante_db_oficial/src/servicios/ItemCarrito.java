/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicios;

/**
 *
 * @author ASUS
 */

import Clases.Producto;
import java.math.BigDecimal;
public class ItemCarrito {

    private Producto producto;
    private int cantidad;
    private BigDecimal subtotal;

    public ItemCarrito() {
    }

    public ItemCarrito(Producto producto, int cantidad) {

        this.producto = producto;
        this.cantidad = cantidad;

        calcularSubtotal();
    }

    public void calcularSubtotal() {

        subtotal = producto.getPrecio()
                .multiply(BigDecimal.valueOf(cantidad));
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {

        this.cantidad = cantidad;

        calcularSubtotal();
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }
}