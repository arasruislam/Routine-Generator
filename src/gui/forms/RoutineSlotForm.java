package gui.forms;

import dao.RoutineSlotDAO;

import javax.swing.*;
import java.awt.*;

public class RoutineSlotForm extends JPanel {

   private JComboBox<String> dayBox;
   private JTextField startTimeField, endTimeField;

   public RoutineSlotForm() {
      setLayout(new BorderLayout());
      JLabel title = new JLabel("⏱ Add Routine Slot", JLabel.CENTER);
      title.setFont(new Font("Segoe UI", Font.BOLD, 20));
      add(title, BorderLayout.NORTH);

      JPanel formPanel = new JPanel(new GridBagLayout());
      formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(10, 10, 10, 10);
      gbc.fill = GridBagConstraints.HORIZONTAL;

      dayBox = new JComboBox<>(new String[] { "Saturday", "Sunday", "Monday", "Tuesday" });
      startTimeField = new JTextField(20);
      endTimeField = new JTextField(20);

      gbc.gridx = 0;
      gbc.gridy = 0;
      formPanel.add(new JLabel("Day:"), gbc);
      gbc.gridx = 1;
      formPanel.add(dayBox, gbc);

      gbc.gridx = 0;
      gbc.gridy = 1;
      formPanel.add(new JLabel("Start Time (HH:MM):"), gbc);
      gbc.gridx = 1;
      formPanel.add(startTimeField, gbc);

      gbc.gridx = 0;
      gbc.gridy = 2;
      formPanel.add(new JLabel("End Time (HH:MM):"), gbc);
      gbc.gridx = 1;
      formPanel.add(endTimeField, gbc);

      JButton saveBtn = new JButton("➕ Add Slot");
      gbc.gridx = 1;
      gbc.gridy = 3;
      formPanel.add(saveBtn, gbc);

      saveBtn.addActionListener(e -> {
         String day = (String) dayBox.getSelectedItem();
         String start = startTimeField.getText().trim();
         String end = endTimeField.getText().trim();

         if (start.isEmpty() || end.isEmpty()) {
            JOptionPane.showMessageDialog(this, "❗ Time fields cannot be empty.");
            return;
         }

         boolean success = RoutineSlotDAO.insertSlot(day, start, end);
         if (success) {
            JOptionPane.showMessageDialog(this, "✅ Slot added successfully!");
            startTimeField.setText("");
            endTimeField.setText("");
         } else {
            JOptionPane.showMessageDialog(this, "❌ Failed to add slot.");
         }
      });

      add(formPanel, BorderLayout.CENTER);
   }
}
