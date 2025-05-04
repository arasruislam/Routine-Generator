package gui.forms;

import database.TeacherDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TeacherForm extends JPanel {

   private JTextField nameField;
   private JTextField departmentField;

   public TeacherForm() {
      setLayout(new BorderLayout());
      setBackground(new Color(245, 247, 250)); // Light background for modern feel

      // Title label
      JLabel titleLabel = new JLabel("👨‍🏫 Add Teacher", JLabel.CENTER);
      titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
      titleLabel.setForeground(new Color(40, 40, 60)); // Darker title color
      titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
      add(titleLabel, BorderLayout.NORTH);

      // Form panel with subtle shadow and rounded corners
      JPanel formPanel = new JPanel(new GridBagLayout());
      formPanel.setBackground(Color.WHITE);
      formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)));
      formPanel.setPreferredSize(new Dimension(500, 300));

      // Adding drop shadow effect
      ((JComponent) formPanel).setBorder(BorderFactory.createMatteBorder(1, 1, 3, 3, new Color(200, 200, 200)));

      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(15, 20, 15, 20); // Padding between components
      gbc.fill = GridBagConstraints.HORIZONTAL;

      // Name field
      JLabel nameLabel = new JLabel("Name:");
      nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
      nameLabel.setForeground(new Color(60, 60, 60));

      nameField = new JTextField(20);
      nameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
      nameField.setBackground(new Color(250, 250, 250));
      nameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
      nameField.setPreferredSize(new Dimension(200, 40));

      gbc.gridx = 0;
      gbc.gridy = 0;
      formPanel.add(nameLabel, gbc);
      gbc.gridx = 1;
      formPanel.add(nameField, gbc);

      // Department field
      JLabel deptLabel = new JLabel("Department:");
      deptLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
      deptLabel.setForeground(new Color(60, 60, 60));

      departmentField = new JTextField(20);
      departmentField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
      departmentField.setBackground(new Color(250, 250, 250));
      departmentField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
      departmentField.setPreferredSize(new Dimension(200, 40));

      gbc.gridx = 0;
      gbc.gridy = 1;
      formPanel.add(deptLabel, gbc);
      gbc.gridx = 1;
      formPanel.add(departmentField, gbc);

      // Save button
      JButton saveButton = new JButton("Save Teacher");
      saveButton.setBackground(new Color(0, 153, 76)); // Green background
      saveButton.setForeground(Color.WHITE);
      saveButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
      saveButton.setFocusPainted(false);
      saveButton.setPreferredSize(new Dimension(150, 40));
      saveButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
      saveButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
      saveButton.setContentAreaFilled(true);
      saveButton.setOpaque(true);

      // Button hover effect
      saveButton.addMouseListener(new MouseAdapter() {
         public void mouseEntered(MouseEvent e) {
            saveButton.setBackground(new Color(0, 120, 60)); // Darker green on hover
         }

         public void mouseExited(MouseEvent e) {
            saveButton.setBackground(new Color(0, 153, 76)); // Reset to original color
         }
      });

      // Add button to form
      gbc.gridx = 1;
      gbc.gridy = 2;
      gbc.anchor = GridBagConstraints.EAST;
      formPanel.add(saveButton, gbc);

      saveButton.addActionListener(e -> saveTeacher());

      // Center panel wrapper to center form in window
      JPanel centerWrapper = new JPanel(new GridBagLayout());
      centerWrapper.setBackground(new Color(245, 247, 250));
      GridBagConstraints c = new GridBagConstraints();
      c.weighty = 1.0;
      centerWrapper.add(formPanel, c);

      add(centerWrapper, BorderLayout.CENTER);
   }

   // Save teacher method
   private void saveTeacher() {
      String name = nameField.getText().trim();
      String dept = departmentField.getText().trim();

      if (name.isEmpty() || dept.isEmpty()) {
         JOptionPane.showMessageDialog(this, "❗ Please fill in all fields.", "Input Error",
               JOptionPane.WARNING_MESSAGE);
         return;
      }

      boolean success = TeacherDAO.insertTeacher(name, dept);
      if (success) {
         JOptionPane.showMessageDialog(this, "✅ Teacher saved successfully!", "Success",
               JOptionPane.INFORMATION_MESSAGE);
         nameField.setText("");
         departmentField.setText("");
      } else {
         JOptionPane.showMessageDialog(this, "❌ Failed to save teacher.", "Error",
               JOptionPane.ERROR_MESSAGE);
      }
   }
}