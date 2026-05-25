/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ClasesTemporales;

/**
 *
 * @author ASUS
 */
public class ResumenDelivery {

    private final int pedidosEnRuta;
    private final int pedidosEntregados;
    private final boolean hayNuevosPedidos;

    public ResumenDelivery(
            int pedidosEnRuta,
            int pedidosEntregados,
            boolean hayNuevosPedidos
    ) {
        this.pedidosEnRuta = pedidosEnRuta;
        this.pedidosEntregados = pedidosEntregados;
        this.hayNuevosPedidos = hayNuevosPedidos;
    }

    public int getPedidosEnRuta() {
        return pedidosEnRuta;
    }

    public int getPedidosEntregados() {
        return pedidosEntregados;
    }

    public boolean isHayNuevosPedidos() {
        return hayNuevosPedidos;
    }  
}