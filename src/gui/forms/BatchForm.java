package gui.forms;

import dao.BatchDAO;

import javax.swing.*;
import java.awt.*;

public class BatchForm extends JPanel {

   private JTextField semesterField, batchNameField;

   public BatchForm() {
      setLayout(new BorderLayout());
      JLabel title = new JLabel("👥 Add New Batch");
      title.setFont(new Font("Arial", Font.BOLD, 20));
      title.setHorizontalAlignment(JLabel.CENTER);
      add(title, BorderLayout.NORTH);

      JPanel formPanel = new JPanel(new GridBagLayout());
      formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(10, 10, 10, 10);
      gbc.fill = GridBagConstraints.HORIZONTAL;

      semesterField = new JTextField(20);
      batchNameField = new JTextField(20);

      gbc.gridx = 0;
      gbc.gridy = 0;
      formPanel.add(new JLabel("Semester:"), gbc);
      gbc.gridx = 1;
      formPanel.add(semesterField, gbc);

      gbc.gridx = 0;
      gbc.gridy = 1;
      formPanel.add(new JLabel("Batch Name:"), gbc);
      gbc.gridx = 1;
      formPanel.add(batchNameField, gbc);

      JButton submitBtn = new JButton("➕ Add Batch");
      gbc.gridx = 1;
      gbc.gridy = 2;
      formPanel.add(submitBtn, gbc);

      submitBtn.addActionListener(e -> {
         String sem = semesterField.getText().trim();
         String batch = batchNameField.getText().trim();

         if (sem.isEmpty() || batch.isEmpty()) {
            JOptionPane.showMessageDialog(this, "❗All fields are required!");
            return;
         }

         boolean success = BatchDAO.insertBatch(sem, batch);
         if (success) {
            JOptionPane.showMessageDialog(this, "✅ Batch added successfully!");
            semesterField.setText("");
            batchNameField.setText("");
         } else {
            JOptionPane.showMessageDialog(this, "❌ Failed to add batch.");
         }
      });

      add(formPanel, BorderLayout.CENTER);
   }
}
