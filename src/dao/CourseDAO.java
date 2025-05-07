package dao;

import database.DatabaseHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class CourseDAO {
   public static boolean insertCourse(String name, String code, String dept) {
      String sql = "INSERT INTO courses (name, code, department) VALUES (?, ?, ?)";

      try (Connection conn = DatabaseHelper.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
         pstmt.setString(1, name);
         pstmt.setString(2, code);
         pstmt.setString(3, dept);
         pstmt.executeUpdate();
         return true;
      } catch (Exception e) {
         System.err.println("❌ Error inserting course: " + e.getMessage());
         return false;
      }
   }
}
