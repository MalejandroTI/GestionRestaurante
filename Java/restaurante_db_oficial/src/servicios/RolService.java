/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicios;

/**
 *
 * @author ASUS
 */
import Clases.Rol;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import logica.RolJpaController;
import utilJpa.JPAUtil;

public class RolService {

    private final RolJpaController rolController;

    public RolService() {

        rolController = new RolJpaController(JPAUtil.getEMF());
    }

    public List<Rol> obtenerRoles() {

        return rolController.findRolEntities();
    }
}