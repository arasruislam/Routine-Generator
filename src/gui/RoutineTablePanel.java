package gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import database.DatabaseHelper;

public class RoutineTablePanel extends JPanel {

   public RoutineTablePanel(int batchId) {
      setLayout(new BorderLayout());
      JLabel title = new JLabel("📅 Routine Preview", JLabel.CENTER);
      title.setFont(new Font("Segoe UI", Font.BOLD, 18));
      add(title, BorderLayout.NORTH);

      String[] columns = {
            "Time Slot", "Saturday", "Sunday", "Monday", "Tuesday"
      };

      DefaultTableModel model = new DefaultTableModel(columns, 0);
      JTable table = new JTable(model);
      JScrollPane scrollPane = new JScrollPane(table);

      try (Connection conn = DatabaseHelper.getConnection()) {
         // Load slots
         PreparedStatement slotStmt = conn.prepareStatement(
               "SELECT DISTINCT start_time, end_time FROM routine_slots ORDER BY start_time");
         ResultSet slotRs = slotStmt.executeQuery();

         while (slotRs.next()) {
            String slotTime = slotRs.getString("start_time") + " - " + slotRs.getString("end_time");
            String[] row = new String[5];
            row[0] = slotTime;

            // For each day
            String[] days = { "Saturday", "Sunday", "Monday", "Tuesday" };
            for (int i = 0; i < days.length; i++) {
               String sql = "SELECT c.code, r.room_number FROM class_assignments ca " +
                     "JOIN courses c ON ca.course_id = c.id " +
                     "JOIN rooms r ON ca.room_id = r.id " +
                     "JOIN routine_slots rs ON ca.slot_id = rs.id " +
                     "WHERE rs.start_time = ? AND rs.end_time = ? AND rs.day = ? AND ca.batch_id = ?";
               PreparedStatement stmt = conn.prepareStatement(sql);
               stmt.setString(1, slotRs.getString("start_time"));
               stmt.setString(2, slotRs.getString("end_time"));
               stmt.setString(3, days[i]);
               stmt.setInt(4, batchId);
               ResultSet rs = stmt.executeQuery();

               if (rs.next()) {
                  row[i + 1] = rs.getString("code") + " (" + rs.getString("room_number") + ")";
               } else {
                  row[i + 1] = "";
               }
            }

            model.addRow(row);
         }

      } catch (Exception e) {
         e.printStackTrace();
      }

      add(scrollPane, BorderLayout.CENTER);
   }
}
