package com.emsi.utils;

import com.emsi.entities.Client;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.jfree.chart.ChartPanel;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PDFExporter {

    public void exportStatistics(Client client, String year, ChartPanel categoryChart, ChartPanel timeChart) {
        try {
            // Create file chooser
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Statistics Report");

            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!filePath.toLowerCase().endsWith(".pdf")) {
                    filePath += ".pdf";
                }

                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(filePath));
                document.open();

                // Add title
                Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
                Paragraph title = new Paragraph("Reservation Statistics Report", titleFont);
                title.setAlignment(Element.ALIGN_CENTER);
                document.add(title);
                document.add(new Paragraph("\n"));

                // Add client info
                Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
                document.add(new Paragraph("Client: " + client.getNom() + " " + client.getPrenom(), normalFont));
                document.add(new Paragraph("Year: " + year, normalFont));
                document.add(new Paragraph(
                        "Generated on: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), normalFont));
                document.add(new Paragraph("\n"));

                // Add charts
                if (categoryChart != null) {
                    BufferedImage categoryImage = new BufferedImage(
                            categoryChart.getWidth(),
                            categoryChart.getHeight(),
                            BufferedImage.TYPE_INT_RGB);
                    categoryChart.paint(categoryImage.createGraphics());
                    Image chartImage = Image.getInstance(categoryImage, null);
                    chartImage.scaleToFit(500, 300);
                    document.add(chartImage);
                    document.add(new Paragraph("\n"));
                }

                if (timeChart != null) {
                    BufferedImage timeImage = new BufferedImage(
                            timeChart.getWidth(),
                            timeChart.getHeight(),
                            BufferedImage.TYPE_INT_RGB);
                    timeChart.paint(timeImage.createGraphics());
                    Image chartImage = Image.getInstance(timeImage, null);
                    chartImage.scaleToFit(500, 300);
                    document.add(chartImage);
                }

                document.close();

                JOptionPane.showMessageDialog(null,
                        "Statistics report exported successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error exporting statistics: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}