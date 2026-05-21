/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presentacion;

import Clases.Factura;
import CreacionDocsPdf.PdfFacturaService;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import logica.FacturaJpaController;
import servicios.FacturaService;

public class TestFactura {
public static void main(String[] args) {

    EntityManagerFactory emf = Persistence
            .createEntityManagerFactory(
                    "restaurante_db_oficialPU"
            );

    EntityManager em = emf.createEntityManager();

    try {
        // buscar factura con ID 2
        Factura factura = em.find(Factura.class, 2);

        if (factura == null) {
            System.out.println("Factura no encontrada");
            return;
        }

        // forzar lazy
        factura.getDetalleFacturaCollection().size();
        factura.getIdPedido().getIdCliente().getNombre();
        factura.getIdUsuario().getNombre();

        // generar PDF
        PdfFacturaService pdfService = new PdfFacturaService();
        pdfService.generarPdf(factura);

    } finally {
        em.close();
        emf.close();
    }
}
}