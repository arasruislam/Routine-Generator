package routine;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import database.DatabaseHelper;

public class RoutineManualAddPanel extends JPanel {

   private JComboBox<String> batchBox, courseBox, teacherBox, roomBox, slotBox;
   private Map<String, Integer> batchMap = new HashMap<>();
   private Map<String, Integer> courseMap = new HashMap<>();
   private Map<String, Integer> teacherMap = new HashMap<>();
   private Map<String, Integer> roomMap = new HashMap<>();
   private Map<String, Integer> slotMap = new HashMap<>();

   public RoutineManualAddPanel() {
      setLayout(new BorderLayout());

      JLabel title = new JLabel("✍️ Manual Routine Add", JLabel.CENTER);
      title.setFont(new Font("Segoe UI", Font.BOLD, 20));
      add(title, BorderLayout.NORTH);

      JPanel form = new JPanel(new GridLayout(6, 2, 10, 10));
      form.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

      batchBox = new JComboBox<>();
      courseBox = new JComboBox<>();
      teacherBox = new JComboBox<>();
      roomBox = new JComboBox<>();
      slotBox = new JComboBox<>();

      try (Connection conn = DatabaseHelper.getConnection()) {
         ResultSet rs1 = conn.createStatement().executeQuery("SELECT id, semester, batch_name FROM batches");
         while (rs1.next()) {
            String name = rs1.getString("semester") + " - " + rs1.getString("batch_name");
            int id = rs1.getInt("id");
            batchBox.addItem(name);
            batchMap.put(name, id);
         }

         ResultSet rs2 = conn.createStatement().executeQuery("SELECT id, name, code, department FROM courses");
         while (rs2.next()) {
            String name = rs2.getString("name") + " (" + rs2.getString("code") + ")";
            int id = rs2.getInt("id");
            courseBox.addItem(name);
            courseMap.put(name, id);
         }

         ResultSet rs3 = conn.createStatement().executeQuery("SELECT id, room_number FROM rooms");
         while (rs3.next()) {
            String name = rs3.getString("room_number");
            int id = rs3.getInt("id");
            roomBox.addItem(name);
            roomMap.put(name, id);
         }

         ResultSet rs4 = conn.createStatement().executeQuery("SELECT id, day, start_time, end_time FROM routine_slots");
         while (rs4.next()) {
            String name = rs4.getString("day") + " " + rs4.getString("start_time") + "-" + rs4.getString("end_time");
            int id = rs4.getInt("id");
            slotBox.addItem(name);
            slotMap.put(name, id);
         }
      } catch (Exception e) {
         e.printStackTrace();
      }

      // Auto-filter teachers by course dept
      courseBox.addActionListener(e -> {
         teacherBox.removeAllItems();
         teacherMap.clear();

         String selectedCourse = (String) courseBox.getSelectedItem();
         if (selectedCourse == null)
            return;

         int courseId = courseMap.get(selectedCourse);
         try (Connection conn = DatabaseHelper.getConnection()) {
            String deptQuery = "SELECT department FROM courses WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(deptQuery);
            stmt.setInt(1, courseId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
               String dept = rs.getString("department");

               String teacherSql = "SELECT id, name FROM teachers WHERE department = ?";
               PreparedStatement tstmt = conn.prepareStatement(teacherSql);
               tstmt.setString(1, dept);
               ResultSet trs = tstmt.executeQuery();

               while (trs.next()) {
                  String name = trs.getString("name");
                  int id = trs.getInt("id");
                  teacherBox.addItem(name);
                  teacherMap.put(name, id);
               }
            }
         } catch (Exception ex) {
            ex.printStackTrace();
         }
      });

      // Trigger initial teacher load
      if (courseBox.getItemCount() > 0) {
         courseBox.setSelectedIndex(0);
      }

      form.add(new JLabel("📚 Batch:"));
      form.add(batchBox);
      form.add(new JLabel("📖 Course:"));
      form.add(courseBox);
      form.add(new JLabel("👨‍🏫 Teacher:"));
      form.add(teacherBox);
      form.add(new JLabel("🏫 Room:"));
      form.add(roomBox);
      form.add(new JLabel("🕒 Slot:"));
      form.add(slotBox);

      JButton saveBtn = new JButton("➕ Add Assignment");
      saveBtn.addActionListener(e -> saveManualEntry());
      form.add(new JLabel(""));
      form.add(saveBtn);

      add(form, BorderLayout.CENTER);
   }

   private void saveManualEntry() {
      try (Connection conn = DatabaseHelper.getConnection()) {
         String sql = "INSERT INTO class_assignments (batch_id, course_id, teacher_id, room_id, slot_id) " +
               "VALUES (?, ?, ?, ?, ?)";
         PreparedStatement stmt = conn.prepareStatement(sql);

         int batchId = batchMap.get((String) batchBox.getSelectedItem());
         int courseId = courseMap.get((String) courseBox.getSelectedItem());
         int teacherId = teacherMap.get((String) teacherBox.getSelectedItem());
         int roomId = roomMap.get((String) roomBox.getSelectedItem());
         int slotId = slotMap.get((String) slotBox.getSelectedItem());

         stmt.setInt(1, batchId);
         stmt.setInt(2, courseId);
         stmt.setInt(3, teacherId);
         stmt.setInt(4, roomId);
         stmt.setInt(5, slotId);

         stmt.executeUpdate();
         JOptionPane.showMessageDialog(this, "✅ Assignment added manually.");

      } catch (Exception e) {
         e.printStackTrace();
         JOptionPane.showMessageDialog(this, "❌ Failed to add.");
      }
   }
}