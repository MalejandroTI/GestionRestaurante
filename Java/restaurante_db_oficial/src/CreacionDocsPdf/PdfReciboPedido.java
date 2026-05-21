/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CreacionDocsPdf;

/**
 *
 * @author ASUS
 */
import Clases.Cliente;
import Clases.Configuracion;
import Clases.DetalleFactura;
import Clases.DetallePedido;
import Clases.EntregaPedido;
import Clases.Factura;
import Clases.Pedido;
import Clases.Usuario;
import ClasesEnum.enums.TipoPedido;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.Persistence;
import logica.ConfiguracionJpaController;

public class PdfReciboPedido {

    public void generarRecibo(Pedido pedido) {

        try {

            File carpeta = new File(
                    "C:\\Users\\ASUS\\Desktop\\INGENIERIA\\Programacion IV\\ProyectoB1\\GestionRestaurante\\RecibosRestaurante"
            );

            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }

            String ruta = carpeta.getAbsolutePath()
                    + "\\recibo_" + pedido.getCodigo() + ".pdf";

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(ruta));
            document.open();

            Font tituloFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            Font bold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11);
            Font normal = FontFactory.getFont(FontFactory.HELVETICA, 11);

            // =========================
            // TÍTULO
            // =========================
            Paragraph titulo = new Paragraph(
                    "RESTAURANTE UTPL - RECIBO DE PEDIDO",
                    tituloFont
            );
            titulo.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(titulo);

            document.add(new Paragraph("========================================"));

            // =========================
            // DATOS PEDIDO
            // =========================
            document.add(new Paragraph("PEDIDO N°: " + pedido.getCodigo(), bold));
            document.add(new Paragraph("Tipo de Pedido: " + pedido.getTipoPedido(), normal));
            document.add(new Paragraph("Fecha: " + pedido.getFechaHora(), normal));
            document.add(new Paragraph(" "));

            // =========================
            // CLIENTE
            // =========================
            Cliente cliente = pedido.getIdCliente();

            document.add(new Paragraph("CLIENTE", bold));
            document.add(new Paragraph(cliente.getNombre() + " " + cliente.getApellido(), normal));
            document.add(new Paragraph("Cédula: " + cliente.getCedula(), normal));
            document.add(new Paragraph("Celular: " + cliente.getCelular(), normal));
            document.add(new Paragraph(" "));

            // =========================
            // DETALLE
            // =========================
            document.add(new Paragraph("DETALLE DEL PEDIDO", bold));
            document.add(new Paragraph(" "));

            PdfPTable tabla = new PdfPTable(4);
            tabla.setWidthPercentage(100);

            tabla.addCell("Producto");
            tabla.addCell("Cantidad");
            tabla.addCell("P. Unitario");
            tabla.addCell("Subtotal");

            BigDecimal subtotal = BigDecimal.ZERO;

            for (DetallePedido d : pedido.getDetallePedidoCollection()) {

                tabla.addCell(d.getIdProducto().getNombre());
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

            BigDecimal envio = BigDecimal.ZERO;

            if (pedido.getTipoPedido() == TipoPedido.DELIVERY
                    && pedido.getEntregaPedido() != null) {

                envio = pedido.getEntregaPedido().getCostoEnvio();

                if (envio == null) {
                    envio = BigDecimal.ZERO;
                }

                document.add(new Paragraph("Envío: $" + envio, normal));
            }

            // IVA desde configuración (RECOMENDADO)
            BigDecimal ivaPorcentaje = new BigDecimal("0.15"); // o cargar de ConfiguracionJpaController

            BigDecimal iva = subtotal.add(envio).multiply(ivaPorcentaje);

            BigDecimal total = subtotal.add(envio).add(iva);

            document.add(new Paragraph("IVA: $" + iva, normal));
            document.add(new Paragraph("TOTAL: $" + total, bold));

            // =========================
            // DELIVERY EXTRA
            // =========================
            if (pedido.getTipoPedido() == TipoPedido.DELIVERY
                    && pedido.getEntregaPedido() != null) {

                EntregaPedido e = pedido.getEntregaPedido();

                document.add(new Paragraph(" "));
                document.add(new Paragraph("DATOS DELIVERY", bold));

                document.add(new Paragraph("Dirección: " + e.getDireccionEntrega(), normal));
                document.add(new Paragraph("Km: " + e.getDistanciaKm(), normal));

                Usuario rep = e.getIdUsuarioRepartidor();

                document.add(new Paragraph(
                        "Repartidor: " + rep.getNombre() + " " + rep.getApellido(),
                        normal
                ));
            }

            document.add(new Paragraph("========================================"));

            document.close();

            Desktop.getDesktop().open(new File(ruta));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
