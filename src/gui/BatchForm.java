package gui;

import database.DatabaseHelper;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class BatchForm extends JFrame {
   public BatchForm() {
      setTitle("Add Batch");
      setSize(350, 250);
      setLocationRelativeTo(null);
      setLayout(new GridLayout(4, 2, 10, 10));

      JLabel semesterLabel = new JLabel("Semester:");
      JTextField semesterField = new JTextField();

      JLabel batchLabel = new JLabel("Batch:");
      JTextField batchField = new JTextField();

      JButton submitBtn = new JButton("Submit");

      submitBtn.addActionListener(e -> {
         try (Connection conn = DatabaseHelper.getConnection()) {
            String query = "INSERT INTO batches (semester, batch_name) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, semesterField.getText());
            stmt.setString(2, batchField.getText());
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "✅ Batch added successfully!");
            semesterField.setText("");
            batchField.setText("");
         } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage());
         }
      });

      add(semesterLabel);
      add(semesterField);
      add(batchLabel);
      add(batchField);
      add(new JLabel());
      add(submitBtn);

      setVisible(true);
   }
}
