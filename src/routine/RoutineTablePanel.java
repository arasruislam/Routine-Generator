package routine;

import database.DatabaseHelper;
import utils.PDFExporter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class RoutineTablePanel extends JPanel {

   private JComboBox<String> batchBox;
   private Map<String, Integer> batchMap;
   private JTable routineTable;
   private DefaultTableModel model;

   public RoutineTablePanel() {
      setLayout(new BorderLayout());

      // Top Panel with Dropdown
      JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      JLabel title = new JLabel("📅 Routine Preview");
      title.setFont(new Font("Segoe UI", Font.BOLD, 18));

      batchBox = new JComboBox<>();
      batchMap = new HashMap<>();

      try (Connection conn = DatabaseHelper.getConnection()) {
         ResultSet rs = conn.createStatement().executeQuery("SELECT id, semester, batch_name FROM batches");
         while (rs.next()) {
            String name = rs.getString("semester") + " - " + rs.getString("batch_name");
            int id = rs.getInt("id");
            batchBox.addItem(name);
            batchMap.put(name, id);
         }
      } catch (Exception e) {
         e.printStackTrace();
      }

      batchBox.addActionListener(e -> {
         String selected = (String) batchBox.getSelectedItem();
         int batchId = batchMap.get(selected);
         loadRoutine(batchId);
      });

      topPanel.add(title);
      topPanel.add(new JLabel("🧪 Batch:"));
      topPanel.add(batchBox);
      add(topPanel, BorderLayout.NORTH);

      // Table Setup
      String[] columns = { "Time Slot", "Saturday", "Sunday", "Monday", "Tuesday" };
      model = new DefaultTableModel(columns, 0);
      routineTable = new JTable(model);
      add(new JScrollPane(routineTable), BorderLayout.CENTER);

      // Export Button
      JButton exportBtn = new JButton("📄 Export to PDF");
      exportBtn.addActionListener(e -> {
         String selectedBatch = (String) batchBox.getSelectedItem();
         int batchId = batchMap.get(selectedBatch);
         PDFExporter.exportRoutineToPDF(batchId, "routine_batch_" + batchId + ".pdf");
         JOptionPane.showMessageDialog(this, "✅ PDF exported!");
      });
      add(exportBtn, BorderLayout.SOUTH);

      // Load initial routine (first batch)
      if (batchBox.getItemCount() > 0) {
         batchBox.setSelectedIndex(0);
      }
   }

   private void loadRoutine(int batchId) {
      model.setRowCount(0); // clear previous

      try (Connection conn = DatabaseHelper.getConnection()) {
         PreparedStatement slotStmt = conn.prepareStatement(
               "SELECT DISTINCT start_time, end_time FROM routine_slots ORDER BY start_time");
         ResultSet slotRs = slotStmt.executeQuery();

         while (slotRs.next()) {
            String start = slotRs.getString("start_time");
            String end = slotRs.getString("end_time");
            String timeSlot = start + " - " + end;

            String[] row = new String[5];
            row[0] = timeSlot;

            String[] days = { "Saturday", "Sunday", "Monday", "Tuesday" };
            for (int i = 0; i < days.length; i++) {
               String query = "SELECT c.code, r.room_number FROM class_assignments ca " +
                     "JOIN courses c ON ca.course_id = c.id " +
                     "JOIN rooms r ON ca.room_id = r.id " +
                     "JOIN routine_slots rs ON ca.slot_id = rs.id " +
                     "WHERE rs.start_time = ? AND rs.end_time = ? AND rs.day = ? AND ca.batch_id = ?";
               PreparedStatement stmt = conn.prepareStatement(query);
               stmt.setString(1, start);
               stmt.setString(2, end);
               stmt.setString(3, days[i]);
               stmt.setInt(4, batchId);
               ResultSet rs = stmt.executeQuery();

               if (rs.next()) {
                  String code = rs.getString("code");
                  String room = rs.getString("room_number");
                  row[i + 1] = code + " (" + room + ")";
               } else {
                  row[i + 1] = "";
               }
            }

            model.addRow(row);
         }

      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}
