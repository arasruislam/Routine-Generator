package dao;

import database.DatabaseHelper;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class RoomDAO {
   public static boolean insertRoom(String roomNo) {
      String sql = "INSERT INTO rooms (room_number) VALUES (?)";
      try (Connection conn = DatabaseHelper.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
         pstmt.setString(1, roomNo);
         pstmt.executeUpdate();
         return true;
      } catch (Exception e) {
         System.err.println("❌ Error inserting room: " + e.getMessage());
         return false;
      }
   }
}
