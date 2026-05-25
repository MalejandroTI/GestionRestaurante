/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicios;

import Clases.Categoria;
import Clases.Producto;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import logica.ProductoJpaController;
import utilJpa.JPAUtil;

/**
 *
 * @author ASUS
 */
public class ProductoService {

    private final ProductoJpaController productoController;

    public ProductoService() {

        this.productoController
                = new ProductoJpaController(JPAUtil.getEMF());
    }

    public void crearProducto(
            String nombre,
            String precioTexto,
            Categoria categoria) throws Exception {

        if (nombre.isBlank()) {
            throw new Exception(
                    "Ingrese el nombre del producto"
            );
        }

        if (precioTexto.isBlank()) {
            throw new Exception(
                    "Ingrese el precio"
            );
        }

        if (categoria == null) {

            throw new Exception(
                    "Seleccione una categoría"
            );
        }

        BigDecimal precio;

        try {

            precio = new BigDecimal(precioTexto);

        } catch (NumberFormatException e) {

            throw new Exception(
                    "El precio debe ser numérico"
            );
        }

        Producto producto = new Producto();

        producto.setNombre(nombre.trim());

        producto.setPrecio(precio);

        producto.setIdCategoria(categoria);

        productoController.create(producto);
    }

    public List<Producto> obtenerProductos() {

        return productoController.findProductoEntities();
    }

    public List<Producto> buscarProductos(
            String texto
    ) {

        String t = texto.toLowerCase();

        return obtenerProductos().stream()
                .filter(p
                        -> String.valueOf(
                        p.getIdProducto()
                ).contains(t)
                || p.getNombre()
                        .toLowerCase()
                        .contains(t)
                || p.getIdCategoria()
                        .getNombre()
                        .toLowerCase()
                        .contains(t)
                )
                .collect(Collectors.toList());
    }
}
