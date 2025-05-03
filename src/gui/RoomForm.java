package gui;

import database.DatabaseHelper;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class RoomForm extends JFrame {
   public RoomForm() {
      setTitle("Add Room");
      setSize(350, 200);
      setLocationRelativeTo(null);
      setLayout(new GridLayout(3, 2, 10, 10));

      JLabel roomNoLabel = new JLabel("Room No:");
      JTextField roomField = new JTextField();

      JButton submitBtn = new JButton("Submit");

      submitBtn.addActionListener(e -> {
         try (Connection conn = DatabaseHelper.getConnection()) {
            String query = "INSERT INTO rooms (room_no) VALUES (?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, roomField.getText());
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "✅ Room added successfully!");
            roomField.setText("");
         } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage());
         }
      });

      add(roomNoLabel);
      add(roomField);
      add(new JLabel());
      add(submitBtn);

      setVisible(true);
   }
}
