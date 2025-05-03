package gui;

import database.DatabaseHelper;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class CourseForm extends JFrame {
   public CourseForm() {
      setTitle("Add Course");
      setSize(350, 300);
      setLocationRelativeTo(null);
      setLayout(new GridLayout(5, 2, 10, 10));

      JLabel nameLabel = new JLabel("Course Name:");
      JTextField nameField = new JTextField();

      JLabel codeLabel = new JLabel("Course Code:");
      JTextField codeField = new JTextField();

      JLabel deptLabel = new JLabel("Department:");
      JTextField deptField = new JTextField();

      JButton submitBtn = new JButton("Submit");

      submitBtn.addActionListener(e -> {
         try (Connection conn = DatabaseHelper.getConnection()) {
            String query = "INSERT INTO courses (name, code, department) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, nameField.getText());
            stmt.setString(2, codeField.getText());
            stmt.setString(3, deptField.getText());
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "✅ Course added successfully!");
            nameField.setText("");
            codeField.setText("");
            deptField.setText("");
         } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage());
         }
      });

      add(nameLabel);
      add(nameField);
      add(codeLabel);
      add(codeField);
      add(deptLabel);
      add(deptField);
      add(new JLabel());
      add(submitBtn);

      setVisible(true);
   }
}
