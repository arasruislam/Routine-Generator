package gui.forms;

import database.TeacherDAO;

import javax.swing.*;
import java.awt.*;

public class TeacherForm extends JPanel {

   private JTextField nameField;
   private JTextField departmentField;

   public TeacherForm() {
      setLayout(new BorderLayout());
      setBackground(Color.WHITE);

      JLabel titleLabel = new JLabel("👨‍🏫 Add Teacher", JLabel.CENTER);
      titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
      titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
      add(titleLabel, BorderLayout.NORTH);

      JPanel formPanel = new JPanel(new GridBagLayout());
      formPanel.setBackground(Color.WHITE);
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(10, 20, 10, 20);
      gbc.fill = GridBagConstraints.HORIZONTAL;

      JLabel nameLabel = new JLabel("Name:");
      nameField = new JTextField(20);
      gbc.gridx = 0;
      gbc.gridy = 0;
      formPanel.add(nameLabel, gbc);
      gbc.gridx = 1;
      formPanel.add(nameField, gbc);

      JLabel deptLabel = new JLabel("Department:");
      departmentField = new JTextField(20);
      gbc.gridx = 0;
      gbc.gridy = 1;
      formPanel.add(deptLabel, gbc);
      gbc.gridx = 1;
      formPanel.add(departmentField, gbc);

      JButton saveButton = new JButton("Save Teacher");
      saveButton.setBackground(new Color(0, 153, 76));
      saveButton.setForeground(Color.WHITE);
      saveButton.setFocusPainted(false);
      saveButton.setPreferredSize(new Dimension(140, 35));
      gbc.gridx = 1;
      gbc.gridy = 2;
      formPanel.add(saveButton, gbc);

      saveButton.addActionListener(e -> saveTeacher());

      add(formPanel, BorderLayout.CENTER);
   }

   private void saveTeacher() {
      String name = nameField.getText().trim();
      String dept = departmentField.getText().trim();

      if (name.isEmpty() || dept.isEmpty()) {
         JOptionPane.showMessageDialog(this, "❗Please fill in all fields.");
         return;
      }

      boolean success = TeacherDAO.insertTeacher(name, dept);
      if (success) {
         JOptionPane.showMessageDialog(this, "✅ Teacher saved successfully!");
         nameField.setText("");
         departmentField.setText("");
      } else {
         JOptionPane.showMessageDialog(this, "❌ Failed to save teacher.");
      }
   }
}
