/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Seguridad;

import Clases.Usuario;
import java.util.Set;

/**
 *
 * @author ASUS
 */
public class SeguridadService {

    public Set<String> obtenerPermisos(Usuario usuario) {

        return usuario.getRolCollection().stream()
                .flatMap(r -> r.getPermisoCollection().stream())
                .map(p -> p.getNombre())
                .collect(java.util.stream.Collectors.toSet());
    }

    public boolean tienePermiso(Usuario usuario, String permiso) {

        return obtenerPermisos(usuario).contains(permiso);
    }

    public Set<String> cachePermisos(Usuario usuario) {
        return obtenerPermisos(usuario);
    }
}