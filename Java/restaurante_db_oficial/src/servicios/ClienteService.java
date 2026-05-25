/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicios;

import Clases.Cliente;
import java.util.List;
import java.util.stream.Collectors;
import logica.ClienteJpaController;
import utilJpa.JPAUtil;

/**
 *
 * @author ASUS
 */
public class ClienteService {
    private final ClienteJpaController controller;

    public ClienteService() {

        this.controller = new ClienteJpaController(JPAUtil.getEMF());
    }

    public void crearCliente(
            String nombre,
            String apellido,
            String cedula,
            String celular,
            String correo) throws Exception {

        if (nombre.isBlank()
                || apellido.isBlank()
                || cedula.isBlank()) {

            throw new Exception(
                    "Complete los campos obligatorios"
            );
        }

        Cliente cliente = new Cliente();

        cliente.setNombre(nombre);
        cliente.setApellido(apellido);
        cliente.setCedula(cedula);
        cliente.setCelular(celular);
        cliente.setCorreo(correo);

        controller.create(cliente);
    }
    
    public List<Cliente> obtenerClientes() {
        return controller.findClienteEntities();
    }
    
    public List<Cliente> buscarClientes(String texto) {

        String t = texto.toLowerCase();

        return obtenerClientes().stream()
                .filter(c
                        -> String.valueOf(c.getIdCliente()).contains(t)
                || c.getNombre().toLowerCase().contains(t)
                || c.getApellido().toLowerCase().contains(t)
                || c.getCedula().contains(t)
                || c.getCelular().contains(t)
                )
                .collect(Collectors.toList());
    }

}