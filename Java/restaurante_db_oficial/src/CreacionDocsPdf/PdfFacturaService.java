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
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;

import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;

public class PdfFacturaService {

    public void generarPdf(Factura factura) {

        try {

            File carpeta = new File(
                    "C:\\Users\\ASUS\\Desktop\\INGENIERIA\\Programacion IV\\ProyectoB1\\GestionRestaurante\\FacturasRestaurante"
            );

            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }

            String ruta = carpeta.getAbsolutePath()
                    + "\\factura_" + factura.getNumero() + ".pdf";

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(ruta));
            document.open();

            Font titulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            Font bold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11);
            Font normal = FontFactory.getFont(FontFactory.HELVETICA, 11);

            // =========================
            // TÍTULO
            // =========================
            Paragraph head = new Paragraph("RESTAURANTE UTPL - FACTURA", titulo);
            head.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(head);

            document.add(new Paragraph("========================================"));

            // =========================
            // DATOS FACTURA
            // =========================
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            document.add(new Paragraph("FACTURA N°: " + factura.getNumero(), bold));
            document.add(new Paragraph("Fecha: " + sdf.format(factura.getFecha()), normal));
            document.add(new Paragraph(" "));

            // =========================
            // CLIENTE
            // =========================
            Cliente cliente = factura.getIdPedido().getIdCliente();

            document.add(new Paragraph("CLIENTE", bold));
            document.add(new Paragraph(
                    cliente.getNombre() + " " + cliente.getApellido(),
                    normal
            ));
            document.add(new Paragraph("Cédula: " + cliente.getCedula(), normal));
            document.add(new Paragraph("Teléfono: " + cliente.getCelular(), normal));
            document.add(new Paragraph("Correo: " + cliente.getCorreo(), normal));
            document.add(new Paragraph(" "));

            // =========================
            // USUARIO
            // =========================
            Usuario usuario = factura.getIdUsuario();

            document.add(new Paragraph("USUARIO RESPONSABLE", bold));
            document.add(new Paragraph(
                    usuario.getNombre() + " " + usuario.getApellido(),
                    normal
            ));

            document.add(new Paragraph(" "));

            // =========================
            // DETALLE
            // =========================
            document.add(new Paragraph("DETALLE FACTURA", bold));
            document.add(new Paragraph(" "));

            PdfPTable tabla = new PdfPTable(4);
            tabla.setWidthPercentage(100);

            tabla.addCell("Producto");
            tabla.addCell("Cantidad");
            tabla.addCell("P. Unitario");
            tabla.addCell("Subtotal");

            BigDecimal subtotal = BigDecimal.ZERO;

            for (DetalleFactura d : factura.getDetalleFacturaCollection()) {

                tabla.addCell(d.getNombreProducto());
                tabla.addCell(String.valueOf(d.getCantidad()));
                tabla.addCell(d.getPrecioUnitario().toString());
                tabla.addCell(d.getSubtotal().toString());

                subtotal = subtotal.add(d.getSubtotal());
            }

            document.add(tabla);
            document.add(new Paragraph(" "));

            // =========================
            // TOTALES
            // =========================
            document.add(new Paragraph("RESUMEN", bold));

            document.add(new Paragraph("Subtotal: $" + subtotal, normal));

            BigDecimal iva = factura.getImpuesto() != null
                    ? factura.getImpuesto()
                    : BigDecimal.ZERO;

            BigDecimal total = factura.getTotal() != null
                    ? factura.getTotal()
                    : subtotal.add(iva);

            document.add(new Paragraph("IVA: $" + iva, normal));
            document.add(new Paragraph("TOTAL FACTURA: $" + total, bold));

            document.add(new Paragraph("========================================"));

            document.close();

            Desktop.getDesktop().open(new File(ruta));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

