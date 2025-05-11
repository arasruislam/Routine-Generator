package utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import database.DatabaseHelper;

import java.io.FileOutputStream;
import java.sql.*;

public class PDFExporter {

   public static void exportRoutineToPDF(int batchId, String filename) {
      try (Connection conn = DatabaseHelper.getConnection()) {

         Document document = new Document();
         PdfWriter.getInstance(document, new FileOutputStream(filename));
         document.open();

         Font boldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
         document.add(new Paragraph("📅 Routine for Batch ID: " + batchId, boldFont));
         document.add(new Paragraph(" "));

         PdfPTable table = new PdfPTable(5);
         table.setWidthPercentage(100);
         table.addCell("Time Slot");
         table.addCell("Saturday");
         table.addCell("Sunday");
         table.addCell("Monday");
         table.addCell("Tuesday");

         // Load slots
         PreparedStatement slotStmt = conn.prepareStatement(
               "SELECT DISTINCT start_time, end_time FROM routine_slots ORDER BY start_time");
         ResultSet slotRs = slotStmt.executeQuery();

         while (slotRs.next()) {
            String start = slotRs.getString("start_time");
            String end = slotRs.getString("end_time");
            table.addCell(start + " - " + end);

            String[] days = { "Saturday", "Sunday", "Monday", "Tuesday" };
            for (String day : days) {
               String query = "SELECT c.code, r.room_number FROM class_assignments ca " +
                     "JOIN courses c ON ca.course_id = c.id " +
                     "JOIN rooms r ON ca.room_id = r.id " +
                     "JOIN routine_slots rs ON ca.slot_id = rs.id " +
                     "WHERE rs.start_time = ? AND rs.end_time = ? AND rs.day = ? AND ca.batch_id = ?";
               PreparedStatement stmt = conn.prepareStatement(query);
               stmt.setString(1, start);
               stmt.setString(2, end);
               stmt.setString(3, day);
               stmt.setInt(4, batchId);
               ResultSet rs2 = stmt.executeQuery();

               if (rs2.next()) {
                  String course = rs2.getString("code");
                  String room = rs2.getString("room_number");
                  table.addCell(course + " (" + room + ")");
               } else {
                  table.addCell("");
               }
            }
         }

         document.add(table);
         document.close();
         System.out.println("✅ Routine exported to PDF: " + filename);

      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public static void exportRoutineToPDFByVersion(int versionId, String filename) {
      try (Connection conn = DatabaseHelper.getConnection()) {

         Document doc = new Document();
         PdfWriter.getInstance(doc, new FileOutputStream(filename));
         doc.open();

         doc.add(new Paragraph("📄 Routine (Version ID: " + versionId + ")"));
         doc.add(new Paragraph(" "));

         PdfPTable table = new PdfPTable(5);
         table.setWidthPercentage(100);
         table.addCell("Day");
         table.addCell("Time Slot");
         table.addCell("Course");
         table.addCell("Room");
         table.addCell("Batch");

         String sql = "SELECT rs.day, rs.start_time, rs.end_time, c.code, r.room_number, b.batch_name " +
               "FROM class_assignments ca " +
               "JOIN routine_slots rs ON ca.slot_id = rs.id " +
               "JOIN courses c ON ca.course_id = c.id " +
               "JOIN rooms r ON ca.room_id = r.id " +
               "JOIN batches b ON ca.batch_id = b.id " +
               "WHERE ca.version_id = ? " +
               "ORDER BY rs.day, rs.start_time";

         PreparedStatement stmt = conn.prepareStatement(sql);
         stmt.setInt(1, versionId);
         ResultSet rs = stmt.executeQuery();

         while (rs.next()) {
            table.addCell(rs.getString("day"));
            table.addCell(rs.getString("start_time") + " - " + rs.getString("end_time"));
            table.addCell(rs.getString("code"));
            table.addCell(rs.getString("room_number"));
            table.addCell(rs.getString("batch_name"));
         }

         doc.add(table);
         doc.close();

      } catch (Exception e) {
         e.printStackTrace();
      }
   }
  
}
