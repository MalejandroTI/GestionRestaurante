/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CreacionDocsPdf;

import Clases.Cliente;
import Clases.DetalleFactura;
import Clases.Factura;
import Clases.Usuario;
import java.text.SimpleDateFormat;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;

import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;

public class PdfFacturaService {

    public void generarPdf(Factura factura) {

        try {

            // =====================================
            // CREAR CARPETA SI NO EXISTE
            // =====================================
            File carpeta = new File(
                    "C:\\Users\\ASUS\\Desktop\\INGENIERIA\\Programacion IV\\ProyectoB1\\GestionRestaurante\\FacturasRestaurante"
            );

            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }

            // =====================================
            // RUTA PDF
            // =====================================
            String ruta
                    = "C:\\Users\\ASUS\\Desktop\\INGENIERIA\\Programacion IV\\ProyectoB1\\GestionRestaurante\\FacturasRestaurante\\factura_"
                    + factura.getNumero()
                    + ".pdf";

            // =====================================
            // CREAR DOCUMENTO
            // =====================================
            Document document = new Document();

            PdfWriter.getInstance(
                    document,
                    new FileOutputStream(ruta)
            );

            document.open();

            // =====================================
            // TITULO RESTAURANTE
            // =====================================
            Paragraph titulo = new Paragraph(
                    "RESTAURANTE UTPL"
            );

            titulo.setAlignment(Paragraph.ALIGN_CENTER);

            document.add(titulo);

            document.add(
                    new Paragraph(
                            "========================================"
                    )
            );

            // =====================================
            // DATOS FACTURA
            // =====================================
            document.add(
                    new Paragraph(
                            "Factura: "
                            + factura.getNumero()
                    )
            );
            
            //Crear un mejor formato para la fecha//
            SimpleDateFormat formatofecha = new SimpleDateFormat ("dd/MM/yyyy HH:mm:ss");
            String fechaFormateada = formatofecha.format((factura.getFecha()));
            document.add(
                    new Paragraph(
                            "Fecha: "
                            + fechaFormateada   
                    )
            );

            document.add(
                    new Paragraph(" ")
            );

            // =====================================
            // DATOS CLIENTE
            // =====================================
            Cliente cliente
                    = factura.getIdPedido().getIdCliente();

            document.add(
                    new Paragraph(
                            "DATOS CLIENTE"
                    )
            );

            document.add(
                    new Paragraph(
                            "Cliente: "
                            + cliente.getNombre()
                    )
            );

            document.add(
                    new Paragraph(
                            "Cedula: "
                            + cliente.getCedula()
                    )
            );

            document.add(
                    new Paragraph(
                            "Telefono: "
                            + cliente.getCelular()
                    )
            );

            document.add(
                    new Paragraph(
                            "Correo: "
                            + cliente.getCorreo()
                    )
            );

            document.add(
                    new Paragraph(" ")
            );

            // =====================================
            // USUARIO RESPONSABLE
            // =====================================
            Usuario usuario
                    = factura.getIdUsuario();

            document.add(
                    new Paragraph(
                            "USUARIO RESPONSABLE"
                    )
            );

            document.add(
                    new Paragraph(
                            "Usuario: " + usuario.getNombre() + " " + usuario.getApellido()
                    )
            );

            document.add(
                    new Paragraph(" ")
            );

            // =====================================
            // DETALLE FACTURA
            // =====================================
            document.add(
                    new Paragraph(
                            "DETALLE FACTURA"
                    )
            );
            document.add(
                    new Paragraph(" ")
            );
            PdfPTable tabla = new PdfPTable(4);

            tabla.setWidthPercentage(100);

            // ENCABEZADOS
            tabla.addCell("Producto");
            tabla.addCell("Cantidad");
            tabla.addCell("Precio Unitario");
            tabla.addCell("Subtotal");

            // =====================================
            // RECORRER DETALLES
            // =====================================
            for (DetalleFactura detalle
                    : factura.getDetalleFacturaCollection()) {

                tabla.addCell(
                        detalle.getNombreProducto()
                );

                tabla.addCell(
                        String.valueOf(
                                detalle.getCantidad()
                        )
                );

                tabla.addCell(
                        detalle.getPrecioUnitario().toString()
                );

                tabla.addCell(
                        detalle.getSubtotal().toString()
                );
            }

            document.add(tabla);

            document.add(
                    new Paragraph(" ")
            );

            // =====================================
            // TOTALES
            // =====================================
            document.add(
                    new Paragraph(
                            "Subtotal: $"
                            + factura.getSubtotal()
                    )
            );

            document.add(
                    new Paragraph(
                            "IVA: $"
                            + factura.getImpuesto()
                    )
            );

            document.add(
                    new Paragraph(
                            "TOTAL FACTURA: $"
                            + factura.getTotal()
                    )
            );

            document.add(
                    new Paragraph(
                            "========================================"
                    )
            );

            // =====================================
            // CERRAR DOCUMENTO
            // =====================================
            document.close();

            System.out.println(
                    "PDF generado correctamente"
            );

            System.out.println(
                    "PDF guardado en: "
                    + ruta
            );

            // =====================================
            // ABRIR PDF AUTOMATICAMENTE
            // =====================================
            Desktop.getDesktop().open(
                    new File(ruta)
            );

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
