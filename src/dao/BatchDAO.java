package dao;

import database.DatabaseHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class BatchDAO {
   public static boolean insertBatch(String semester, String batchName) {
      String sql = "INSERT INTO batches (semester, batch_name) VALUES (?, ?)";

      try (Connection conn = DatabaseHelper.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
         pstmt.setString(1, semester);
         pstmt.setString(2, batchName);
         pstmt.executeUpdate();
         return true;
      } catch (Exception e) {
         System.err.println("❌ Error inserting batch: " + e.getMessage());
         return false;
      }
   }
}
