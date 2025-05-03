package gui;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

   public MainWindow() {
      setTitle("Smart Routine Generator");
      setSize(800, 600);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setLocationRelativeTo(null);

      JLabel label = new JLabel("Welcome to Smart Routine Generator!", JLabel.CENTER);
      label.setFont(new Font("Arial", Font.BOLD, 20));

      add(label, BorderLayout.CENTER);
   }
}
