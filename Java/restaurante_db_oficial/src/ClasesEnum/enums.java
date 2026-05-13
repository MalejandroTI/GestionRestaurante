/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ClasesEnum;

/**
 *
 * @author ASUS
 */
public class enums {

    public enum TipoPedido {
        LOCAL,
        LLEVAR,
        DELIVERY
    }

    public enum EstadoPedido {
        PENDIENTE,
        EN_PREPARACION,
        LISTO,
        EN_RUTA,
        ENTREGADO,
        CANCELADO
    }
}
