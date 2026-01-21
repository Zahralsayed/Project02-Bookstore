package com.Bookstore.service;

import com.Bookstore.model.OrderItem;
import com.Bookstore.model.Orders;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
public class OrderPdfService {

    public byte[] generateOrderPdf(Orders order) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, out);

            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("Order Invoice", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph(" "));

            document.add(new Paragraph("Order ID: " + order.getId()));
            document.add(new Paragraph("Order Date: " +
                    order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
            document.add(new Paragraph("Customer: " + order.getUser().getUsername()));
            document.add(new Paragraph("Status: " + order.getStatus()));

            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{4, 2, 2, 2});

            table.addCell("Book");
            table.addCell("Unit Price");
            table.addCell("Quantity");
            table.addCell("Subtotal");

            for (OrderItem item : order.getOrderItems()) {
                table.addCell(item.getBook().getName());
                table.addCell(item.getUnitPrice().toString());
                table.addCell(item.getQuantity().toString());
                table.addCell(item.getSubtotal().toString());
            }

            document.add(table);

            document.add(new Paragraph(" "));
            document.add(new Paragraph(
                    "Total: " + order.getTotalPrice(),
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD)
            ));

            document.close();
            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate order PDF", e);
        }
    }

}
