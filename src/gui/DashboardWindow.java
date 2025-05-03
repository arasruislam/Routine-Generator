
// src/gui/DashboardWindow.java
package gui;

import javax.swing.*;
import java.awt.*;

public class DashboardWindow extends JFrame {
   private JPanel sidebarPanel;
   private JPanel topbarPanel;
   private JPanel contentPanel;

   public DashboardWindow() {
      setTitle("University Routine Dashboard");
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setSize(1000, 600);
      setLocationRelativeTo(null);
      setLayout(new BorderLayout());

      initSidebar();
      initTopbar();
      initContentArea();

      setVisible(true);
   }

   private void initSidebar() {
      sidebarPanel = new JPanel();
      sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
      sidebarPanel.setBackground(new Color(40, 40, 80));
      sidebarPanel.setPreferredSize(new Dimension(200, getHeight()));

      String[] buttons = { "Home", "Add Teacher", "Add Course", "Add Room", "Add Batch", "Generate Routine" };
      for (String btnText : buttons) {
         JButton button = new JButton(btnText);
         button.setAlignmentX(Component.CENTER_ALIGNMENT);
         button.setMaximumSize(new Dimension(180, 40));
         sidebarPanel.add(Box.createVerticalStrut(20));
         sidebarPanel.add(button);
      }

      add(sidebarPanel, BorderLayout.LINE_START);
   }

   private void initTopbar() {
      topbarPanel = new JPanel();
      topbarPanel.setBackground(new Color(60, 60, 100));
      topbarPanel.setPreferredSize(new Dimension(getWidth(), 50));
      topbarPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

      JLabel title = new JLabel("📅 University Routine Generator");
      title.setForeground(Color.WHITE);
      title.setFont(new Font("Segoe UI", Font.BOLD, 18));

      topbarPanel.add(title);
      add(topbarPanel, BorderLayout.PAGE_START);
   }

   private void initContentArea() {
      contentPanel = new JPanel();
      contentPanel.setLayout(new BorderLayout());
      contentPanel.setBackground(Color.WHITE);
      add(contentPanel, BorderLayout.CENTER);
   }

   public static void main(String[] args) {
      new DashboardWindow();
   }
}
