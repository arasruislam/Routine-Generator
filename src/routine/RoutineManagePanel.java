package routine;

import java.awt.BorderLayout;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import database.DatabaseHelper;
import logic.EditAssignmentDialog;

public class RoutineManagePanel extends JPanel {

   private JComboBox<String> batchBox;
   private Map<String, Integer> batchMap = new HashMap<>();
   private JTable table;
   private DefaultTableModel model;

   public RoutineManagePanel() {
      setLayout(new BorderLayout());

      JLabel title = new JLabel("🛠 Manage Routine", JLabel.CENTER);
      title.setFont(new Font("Segoe UI", Font.BOLD, 20));
      add(title, BorderLayout.NORTH);

      // 🔍 Batch Filter
      JPanel filterPanel = new JPanel();
      filterPanel.add(new JLabel("📚 Select Batch:"));
      batchBox = new JComboBox<>();
      try (Connection conn = DatabaseHelper.getConnection()) {
         ResultSet rs = conn.createStatement().executeQuery("SELECT id, semester, batch_name FROM batches");
         while (rs.next()) {
            String label = rs.getString("semester") + " - " + rs.getString("batch_name");
            int id = rs.getInt("id");
            batchBox.addItem(label);
            batchMap.put(label, id);
         }
      } catch (Exception e) {
         e.printStackTrace();
      }

      JButton loadBtn = new JButton("🔄 Load Assignments");
      loadBtn.addActionListener(e -> {
         int batchId = batchMap.get((String) batchBox.getSelectedItem());
         loadAssignments(batchId);
      });

      filterPanel.add(batchBox);
      filterPanel.add(loadBtn);
      add(filterPanel, BorderLayout.NORTH);

      // 📋 Table
      model = new DefaultTableModel(new String[] { "ID", "Course", "Teacher", "Room", "Slot", "✏️ Edit", "🗑 Delete" },
            0);
      table = new JTable(model);
      add(new JScrollPane(table), BorderLayout.CENTER);
   }

   private void loadAssignments(int batchId) {
      model.setRowCount(0); // clear table

      try (Connection conn = DatabaseHelper.getConnection()) {
         String sql = "SELECT ca.id, c.code, t.name, r.room_number, rs.day || ' ' || rs.start_time || '-' || rs.end_time AS slot "
               +
               "FROM class_assignments ca " +
               "JOIN courses c ON ca.course_id = c.id " +
               "JOIN teachers t ON ca.teacher_id = t.id " +
               "JOIN rooms r ON ca.room_id = r.id " +
               "JOIN routine_slots rs ON ca.slot_id = rs.id " +
               "WHERE ca.batch_id = ?";
         PreparedStatement stmt = conn.prepareStatement(sql);
         stmt.setInt(1, batchId);
         ResultSet rs = stmt.executeQuery();

         while (rs.next()) {
            int assignmentId = rs.getInt("id");
            String course = rs.getString("code");
            String teacher = rs.getString("name");
            String room = rs.getString("room_number");
            String slot = rs.getString("slot");

            JButton editBtn = new JButton("✏️ Edit");
            JButton deleteBtn = new JButton("🗑 Delete");

            // final needed for lambda
            final int id = assignmentId;

            editBtn.addActionListener(e -> {
               EditAssignmentDialog dialog = new EditAssignmentDialog(id);
               dialog.setVisible(true);
            });

            deleteBtn.addActionListener(e -> {
               int confirm = JOptionPane.showConfirmDialog(this, "Are you sure?", "Delete", JOptionPane.YES_NO_OPTION);
               if (confirm == JOptionPane.YES_OPTION) {
                  try (PreparedStatement dstmt = conn.prepareStatement("DELETE FROM class_assignments WHERE id = ?")) {
                     dstmt.setInt(1, id);
                     dstmt.executeUpdate();
                     JOptionPane.showMessageDialog(this, "✅ Assignment deleted.");
                     loadAssignments(batchId);
                  } catch (Exception ex) {
                     ex.printStackTrace();
                  }
               }
            });

            model.addRow(new Object[] { assignmentId, course, teacher, room, slot, editBtn, deleteBtn });
         }

      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}
