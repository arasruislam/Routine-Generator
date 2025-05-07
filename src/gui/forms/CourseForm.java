package gui.forms;

import dao.CourseDAO;

import javax.swing.*;
import java.awt.*;

public class CourseForm extends JPanel {
   private JTextField nameField, codeField, deptField;

   public CourseForm() {
      setLayout(new BorderLayout());
      JLabel title = new JLabel("Add New Course");
      title.setFont(new Font("Arial", Font.BOLD, 20));
      title.setHorizontalAlignment(JLabel.CENTER);
      add(title, BorderLayout.NORTH);

      JPanel formPanel = new JPanel(new GridBagLayout());
      formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(10, 10, 10, 10);
      gbc.fill = GridBagConstraints.HORIZONTAL;

      nameField = new JTextField(20);
      codeField = new JTextField(20);
      deptField = new JTextField(20);

      gbc.gridx = 0;
      gbc.gridy = 0;
      formPanel.add(new JLabel("Course Name:"), gbc);
      gbc.gridx = 1;
      formPanel.add(nameField, gbc);

      gbc.gridx = 0;
      gbc.gridy = 1;
      formPanel.add(new JLabel("Course Code:"), gbc);
      gbc.gridx = 1;
      formPanel.add(codeField, gbc);

      gbc.gridx = 0;
      gbc.gridy = 2;
      formPanel.add(new JLabel("Department:"), gbc);
      gbc.gridx = 1;
      formPanel.add(deptField, gbc);

      JButton submitBtn = new JButton("➕ Add Course");
      gbc.gridx = 1;
      gbc.gridy = 3;
      formPanel.add(submitBtn, gbc);

      add(formPanel, BorderLayout.CENTER);

      submitBtn.addActionListener(e -> {
         String name = nameField.getText().trim();
         String code = codeField.getText().trim();
         String dept = deptField.getText().trim();

         if (name.isEmpty() || code.isEmpty() || dept.isEmpty()) {
            JOptionPane.showMessageDialog(this, "❗ All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
         }

         boolean success = CourseDAO.insertCourse(name, code, dept);
         if (success) {
            JOptionPane.showMessageDialog(this, "✅ Course added successfully!");
            nameField.setText("");
            codeField.setText("");
            deptField.setText("");
         } else {
            JOptionPane.showMessageDialog(this, "❌ Failed to add course.", "Error", JOptionPane.ERROR_MESSAGE);
         }
      });
   }
}
