package logic;

import database.DatabaseHelper;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class EditAssignmentDialog extends JDialog {

   private JComboBox<String> roomBox;
   private JComboBox<String> slotBox;
   private int assignmentId;

   public EditAssignmentDialog(int id) {
      this.assignmentId = id;
      setTitle("✏️ Edit Assignment #" + id);
      setSize(400, 250);
      setLayout(new GridLayout(4, 2, 10, 10));
      setLocationRelativeTo(null);

      roomBox = new JComboBox<>();
      slotBox = new JComboBox<>();

      try (Connection conn = DatabaseHelper.getConnection()) {
         ResultSet rs = conn.createStatement().executeQuery("SELECT id, room_number FROM rooms");
         while (rs.next()) {
            roomBox.addItem(rs.getString("room_number") + " - ID:" + rs.getInt("id"));
         }

         rs = conn.createStatement().executeQuery("SELECT id, day, start_time, end_time FROM routine_slots");
         while (rs.next()) {
            slotBox.addItem(rs.getString("day") + " " + rs.getString("start_time") + "-" + rs.getString("end_time")
                  + " - ID:" + rs.getInt("id"));
         }

      } catch (Exception e) {
         e.printStackTrace();
      }

      add(new JLabel("🏫 Room:"));
      add(roomBox);
      add(new JLabel("🕒 Slot:"));
      add(slotBox);

      JButton saveBtn = new JButton("💾 Save");
      saveBtn.addActionListener(e -> saveChanges());
      add(new JLabel(""));
      add(saveBtn);
   }

   private void saveChanges() {
      try (Connection conn = DatabaseHelper.getConnection()) {
         int roomId = Integer.parseInt(roomBox.getSelectedItem().toString().split("ID:")[1]);
         int slotId = Integer.parseInt(slotBox.getSelectedItem().toString().split("ID:")[1]);

         String sql = "UPDATE class_assignments SET room_id = ?, slot_id = ? WHERE id = ?";
         PreparedStatement stmt = conn.prepareStatement(sql);
         stmt.setInt(1, roomId);
         stmt.setInt(2, slotId);
         stmt.setInt(3, assignmentId);
         stmt.executeUpdate();

         JOptionPane.showMessageDialog(this, "✅ Assignment updated!");
         dispose();

      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}