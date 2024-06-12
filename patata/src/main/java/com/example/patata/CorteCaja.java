package com.example.patata;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CorteCaja {
    private SalesDAO salesDAO;

    public void generarReportePDF() {
        this.salesDAO = new SalesDAO();
        String userHome = System.getProperty("user.home");
        String desktopPath = userHome + File.separator + "Desktop";
        String filePath = desktopPath + File.separator;
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.beginText();
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 18);
                contentStream.newLineAtOffset(100, 750);
                contentStream.showText("Reporte de Ventas del Día");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
                contentStream.newLineAtOffset(100, 730);
                contentStream.showText("Fecha y hora de generación: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                contentStream.endText();

                contentStream.setLineWidth(0.5f);
                contentStream.moveTo(50, 720);
                contentStream.lineTo(550, 720);
                contentStream.stroke();

                List<Sale> sales = salesDAO.getAllSalesOfDay();

                float y = 700;

                for (Sale sale : sales) {
                    if (sale.getProductName().equals("Total de Ventas")) {
                        y -= 20;
                        contentStream.beginText();
                        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 14);
                        contentStream.newLineAtOffset(100, y);
                        contentStream.showText(String.format("Total de Ventas: $%.2f", sale.getPrice()));
                        contentStream.endText();
                    } else {
                        y -= 40;
                        contentStream.beginText();
                        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
                        contentStream.newLineAtOffset(100, y);
                        String timestamp = sale.getSaleTime() != null ? sale.getSaleTime().toString() : "N/A";
                        contentStream.showText(String.format("Mesa: %d, Producto: %s, Precio: $%.2f, Cantidad: %d, Hora: %s",
                                sale.getTableId(), sale.getProductName(), sale.getPrice(), sale.getQuantity(), timestamp));
                        contentStream.endText();
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
            String formattedDateTime = now.format(formatter);

            document.save(new File(filePath+"ReporteVentasDia"+formattedDateTime+".pdf"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
