package utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import database.DatabaseHelper;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TeacherRoutinePDFExporter {

   public static void export(int teacherId, String teacherName, String filePath) {
      try (Connection conn = DatabaseHelper.getConnection()) {

         Document document = new Document();
         PdfWriter.getInstance(document, new FileOutputStream(filePath));
         document.open();

         Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
         Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);

         document.add(new Paragraph("👨‍🏫 Routine for " + teacherName, titleFont));
         document.add(new Paragraph(" ")); // blank line

         PdfPTable table = new PdfPTable(5);
         table.setWidthPercentage(100);

         // Header
         table.addCell(new PdfPCell(new Phrase("Day", headerFont)));
         table.addCell(new PdfPCell(new Phrase("Time Slot", headerFont)));
         table.addCell(new PdfPCell(new Phrase("Course Code", headerFont)));
         table.addCell(new PdfPCell(new Phrase("Room", headerFont)));
         table.addCell(new PdfPCell(new Phrase("Batch", headerFont)));

         String sql = "SELECT rs.day, rs.start_time, rs.end_time, c.code, r.room_number, b.batch_name " +
               "FROM class_assignments ca " +
               "JOIN routine_slots rs ON ca.slot_id = rs.id " +
               "JOIN courses c ON ca.course_id = c.id " +
               "JOIN rooms r ON ca.room_id = r.id " +
               "JOIN batches b ON ca.batch_id = b.id " +
               "WHERE ca.teacher_id = ? " +
               "ORDER BY rs.day, rs.start_time";

         try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, teacherId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
               table.addCell(rs.getString("day"));
               table.addCell(rs.getString("start_time") + " - " + rs.getString("end_time"));
               table.addCell(rs.getString("code"));
               table.addCell(rs.getString("room_number"));
               table.addCell(rs.getString("batch_name"));
            }
         }

         document.add(table);
         document.close();

         System.out.println("✅ Teacher routine PDF exported to: " + filePath);

      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}
