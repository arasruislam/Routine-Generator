package dao;

import database.DatabaseHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class RoutineSlotDAO {

   public static boolean insertSlot(String day, String start, String end) {
      String sql = "INSERT INTO routine_slots (day, start_time, end_time) VALUES (?, ?, ?)";

      try (Connection conn = DatabaseHelper.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
         stmt.setString(1, day);
         stmt.setString(2, start);
         stmt.setString(3, end);
         stmt.executeUpdate();
         return true;
      } catch (Exception e) {
         System.err.println("❌ Slot insert error: " + e.getMessage());
         return false;
      }
   }

   // Optional: Insert 20 default slots (already provided earlier)
   public static void insertDefaultSlots() {
      // keep this if needed
   }
}
