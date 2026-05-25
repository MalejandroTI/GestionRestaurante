/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicios;

import Clases.Categoria;
import java.util.List;
import logica.CategoriaJpaController;
import utilJpa.JPAUtil;

/**
 *
 * @author ASUS
 */
public class CategoriaService {

    private final CategoriaJpaController categoriaController;

    public CategoriaService() {

        this.categoriaController =
                new CategoriaJpaController(JPAUtil.getEMF());
    }

    public void crearCategoria(
            String nombre,
            String descripcion) throws Exception {

        if (nombre == null || nombre.isBlank()) {
            throw new Exception(
                    "Ingrese el nombre de la categoría"
            );
        }

        Categoria categoria = new Categoria();

        categoria.setNombre(nombre.trim());
        categoria.setDescripcion(descripcion.trim());

        categoriaController.create(categoria);
    }
    
    public List<Categoria> obtenerCategorias() {

        return categoriaController.findCategoriaEntities();
    }
}