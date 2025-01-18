package com.emsi.utils;

import com.emsi.entities.Reservation;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.jfree.chart.JFreeChart;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.FileOutputStream;
import java.util.List;

public class PDFExporter {

    public void exportStatistics(String filePath, int year, int clientId,
            List<Reservation> reservations, StatisticsUtil statisticsUtil) {
        try {
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Add title
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("Statistics Report - Year " + year, titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("\n"));

            // Add charts
            JFreeChart roomsChart = statisticsUtil.createRoomsByCategoryChart(year, clientId);
            JFreeChart timeChart = statisticsUtil.createReservationsOverTimeChart(year, clientId);

            // Convert charts to images and add to PDF
            float width = document.getPageSize().getWidth() - 40;
            float height = 300;

            PdfContentByte contentByte = writer.getDirectContent();
            PdfTemplate template = contentByte.createTemplate(width, height);
            Graphics2D graphics = template.createGraphics(width, height);

            Rectangle2D rect = new Rectangle2D.Double(0, 0, width, height);

            roomsChart.draw(graphics, rect);
            graphics.dispose();
            Image chartImage = Image.getInstance(template);
            document.add(chartImage);
            document.add(new Paragraph("\n"));

            template = contentByte.createTemplate(width, height);
            graphics = template.createGraphics(width, height);
            timeChart.draw(graphics, rect);
            graphics.dispose();
            chartImage = Image.getInstance(template);
            document.add(chartImage);

            // Add reservations table
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Add table headers
            table.addCell("Room");
            table.addCell("Check-in");
            table.addCell("Check-out");
            table.addCell("Status");

            // Add reservation data
            for (Reservation res : reservations) {
                if (res.getClientId() == clientId) {
                    table.addCell(String.valueOf(res.getChamberId()));
                    table.addCell(res.getDateDebut().toString());
                    table.addCell(res.getDateFin().toString());
                    table.addCell(res.getStatus());
                }
            }

            document.add(table);
            document.close();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to export PDF: " + e.getMessage());
        }
    }
}