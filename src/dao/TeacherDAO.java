package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

import database.DatabaseHelper;

public class TeacherDAO {
   public static boolean insertTeacher(String name, String department) {
      String sql = "INSERT INTO teachers(name, department) VALUES (?, ?)";

      try (Connection conn = DatabaseHelper.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

         pstmt.setString(1, name);
         pstmt.setString(2, department);
         pstmt.executeUpdate();
         return true;

      } catch (Exception e) {
         e.printStackTrace();
         return false;
      }
   }
}
