package routine;

import database.DatabaseHelper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class RoutineViewByVersionPanel extends JPanel {

   public RoutineViewByVersionPanel(int versionId) {
      setLayout(new BorderLayout());

      String[] cols = { "Day", "Time", "Course", "Room", "Batch" };
      DefaultTableModel model = new DefaultTableModel(cols, 0);
      JTable table = new JTable(model);
      add(new JScrollPane(table), BorderLayout.CENTER);

      try (Connection conn = DatabaseHelper.getConnection()) {
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
            String day = rs.getString("day");
            String time = rs.getString("start_time") + " - " + rs.getString("end_time");
            String course = rs.getString("code");
            String room = rs.getString("room_number");
            String batch = rs.getString("batch_name");

            model.addRow(new Object[] { day, time, course, room, batch });
         }

      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}
