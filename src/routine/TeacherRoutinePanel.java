package routine;

import database.DatabaseHelper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class TeacherRoutinePanel extends JPanel {

   private JComboBox<String> teacherBox;
   private Map<String, Integer> teacherMap;
   private DefaultTableModel model;

   public TeacherRoutinePanel() {
      setLayout(new BorderLayout());

      // 🔼 Top Panel: Dropdown
      JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      JLabel title = new JLabel("👨‍🏫 Teacher Routine View");
      title.setFont(new Font("Segoe UI", Font.BOLD, 18));

      teacherBox = new JComboBox<>();
      teacherMap = new HashMap<>();

      try (Connection conn = DatabaseHelper.getConnection()) {
         ResultSet rs = conn.createStatement().executeQuery("SELECT id, name FROM teachers");
         while (rs.next()) {
            String name = rs.getString("name");
            int id = rs.getInt("id");
            teacherBox.addItem(name);
            teacherMap.put(name, id);
         }
      } catch (Exception e) {
         e.printStackTrace();
      }

      teacherBox.addActionListener(e -> {
         String selected = (String) teacherBox.getSelectedItem();
         int teacherId = teacherMap.get(selected);
         loadRoutineForTeacher(teacherId);
      });

      topPanel.add(title);
      topPanel.add(new JLabel("👨‍🏫 Select Teacher:"));
      topPanel.add(teacherBox);
      add(topPanel, BorderLayout.NORTH);

      // 🧾 Table
      String[] cols = { "Day", "Time Slot", "Course", "Room", "Batch" };
      model = new DefaultTableModel(cols, 0);
      JTable table = new JTable(model);
      add(new JScrollPane(table), BorderLayout.CENTER);

      // Initial load
      if (teacherBox.getItemCount() > 0) {
         teacherBox.setSelectedIndex(0);
      }

      JButton exportBtn = new JButton("📄 Export to PDF");

      exportBtn.addActionListener(e -> {
         String selected = (String) teacherBox.getSelectedItem();
         int teacherId = teacherMap.get(selected);
         String fileName = "routine_teacher_" + teacherId + ".pdf";

         utils.TeacherRoutinePDFExporter.export(teacherId, selected, fileName);
         JOptionPane.showMessageDialog(this, "✅ PDF exported to: " + fileName);
      });

      add(exportBtn, BorderLayout.SOUTH);

   }

   private void loadRoutineForTeacher(int teacherId) {
      model.setRowCount(0);

      String query = "SELECT rs.day, rs.start_time, rs.end_time, c.code, r.room_number, b.batch_name " +
            "FROM class_assignments ca " +
            "JOIN routine_slots rs ON ca.slot_id = rs.id " +
            "JOIN courses c ON ca.course_id = c.id " +
            "JOIN rooms r ON ca.room_id = r.id " +
            "JOIN batches b ON ca.batch_id = b.id " +
            "WHERE ca.teacher_id = ? " +
            "ORDER BY rs.day, rs.start_time";

      try (Connection conn = DatabaseHelper.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
         stmt.setInt(1, teacherId);
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
