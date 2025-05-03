package gui;

import database.DatabaseHelper;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class TeacherForm extends JFrame {
   public TeacherForm() {
      setTitle("Add Teacher");
      setSize(350, 250);
      setLocationRelativeTo(null);
      setLayout(new GridLayout(4, 2, 10, 10));

      JLabel nameLabel = new JLabel("Name:");
      JTextField nameField = new JTextField();
      JLabel deptLabel = new JLabel("Department:");
      JTextField deptField = new JTextField();

      JButton submitBtn = new JButton("Submit");

      submitBtn.addActionListener(e -> {
         String name = nameField.getText();
         String dept = deptField.getText();

         try (Connection conn = DatabaseHelper.getConnection()) {
            String query = "INSERT INTO teachers (name, department) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setString(2, dept);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "✅ Teacher added successfully!");
            nameField.setText("");
            deptField.setText("");
         } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Failed to add teacher: " + ex.getMessage());
         }
      });

      add(nameLabel);
      add(nameField);
      add(deptLabel);
      add(deptField);
      add(new JLabel()); // empty
      add(submitBtn);

      setVisible(true);
   }
}
