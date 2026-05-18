/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presentacion;

import Clases.Factura;
import CreacionDocsPdf.PdfFacturaService;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import logica.FacturaJpaController;
import servicios.FacturaService;

public class TestFactura {

    public static void main(String[] args) {

        EntityManagerFactory emf
                = Persistence.createEntityManagerFactory(
                        "restaurante_db_oficialPU"
                );

        /*FacturaService facturaService
                = new FacturaService(emf);

        Factura factura
                = facturaService.crearFactura(16);

        System.out.println(
                "Factura creada correctamente. ID: "
                + factura.getIdFactura()
        );*/
        PdfFacturaService pdfFacturaService
                = new PdfFacturaService();

        FacturaJpaController facturaController
                = new FacturaJpaController(emf);

        Factura factura
                = facturaController.findFactura(1);
        try {
            pdfFacturaService.generarPdf(factura);
        } catch (Exception e) {

        }
    }
}
