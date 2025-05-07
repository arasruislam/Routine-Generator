package gui.forms;

import dao.RoomDAO;

import javax.swing.*;
import java.awt.*;

public class RoomForm extends JPanel {

   private JTextField roomField;

   public RoomForm() {
      setLayout(new BorderLayout());
      JLabel title = new JLabel("🏫 Add New Room");
      title.setFont(new Font("Arial", Font.BOLD, 20));
      title.setHorizontalAlignment(JLabel.CENTER);
      add(title, BorderLayout.NORTH);

      JPanel formPanel = new JPanel(new GridBagLayout());
      formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(10, 10, 10, 10);
      gbc.fill = GridBagConstraints.HORIZONTAL;

      roomField = new JTextField(20);

      gbc.gridx = 0;
      gbc.gridy = 0;
      formPanel.add(new JLabel("Room Number:"), gbc);
      gbc.gridx = 1;
      formPanel.add(roomField, gbc);

      JButton submitBtn = new JButton("➕ Add Room");
      gbc.gridx = 1;
      gbc.gridy = 1;
      formPanel.add(submitBtn, gbc);

      submitBtn.addActionListener(e -> {
         String roomNo = roomField.getText().trim();

         if (roomNo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "❗Room number is required!");
            return;
         }

         boolean success = RoomDAO.insertRoom(roomNo);
         if (success) {
            JOptionPane.showMessageDialog(this, "✅ Room added successfully!");
            roomField.setText("");
         } else {
            JOptionPane.showMessageDialog(this, "❌ Failed to add room.");
         }
      });

      add(formPanel, BorderLayout.CENTER);
   }
}
