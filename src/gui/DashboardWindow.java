// src/gui/DashboardWindow.java
package gui;

import gui.forms.TeacherForm;
import gui.forms.CourseForm;

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

      initSidebar(); // ⬅️ Sidebar with button actions
      initTopbar(); // ⬅️ Topbar with title
      initContentArea();// ⬅️ Main area to display forms

      setVisible(true);
   }

   private void initSidebar() {
      sidebarPanel = new JPanel();
      sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
      sidebarPanel.setBackground(new Color(40, 40, 80));
      sidebarPanel.setPreferredSize(new Dimension(200, getHeight()));

      sidebarPanel.add(Box.createVerticalStrut(20));

      // 🎯 Updated with buttons that load forms
      JButton homeBtn = createSidebarButton("🏠 Home");
      homeBtn.addActionListener(e -> loadContent(new JLabel("Welcome to Dashboard! 🎓", SwingConstants.CENTER)));

      JButton teacherBtn = createSidebarButton("👨‍🏫 Add Teacher");
      teacherBtn.addActionListener(e -> loadContent(new TeacherForm()));

      JButton courseBtn = createSidebarButton("📚 Add Course");
      courseBtn.addActionListener(e -> loadContent(new CourseForm()));

      JButton roomBtn = createSidebarButton("🏫 Add Room");
      // You can later add form e.g., roomBtn.addActionListener(e -> loadContent(new
      // RoomForm()));

      JButton batchBtn = createSidebarButton("👥 Add Batch");
      // batchBtn.addActionListener(...);

      JButton routineBtn = createSidebarButton("🧠 Generate Routine");
      // routineBtn.addActionListener(...);

      sidebarPanel.add(homeBtn);
      sidebarPanel.add(Box.createVerticalStrut(15));
      sidebarPanel.add(teacherBtn);
      sidebarPanel.add(Box.createVerticalStrut(15));
      sidebarPanel.add(courseBtn);
      sidebarPanel.add(Box.createVerticalStrut(15));
      sidebarPanel.add(roomBtn);
      sidebarPanel.add(Box.createVerticalStrut(15));
      sidebarPanel.add(batchBtn);
      sidebarPanel.add(Box.createVerticalStrut(15));
      sidebarPanel.add(routineBtn);

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

      // 🟨 Default content view
      JLabel welcomeLabel = new JLabel("Welcome to the University Routine System", SwingConstants.CENTER);
      welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
      contentPanel.add(welcomeLabel, BorderLayout.CENTER);
   }

   // 🔁 Helper method to load forms dynamically
   private void loadContent(JComponent component) {
      contentPanel.removeAll();
      contentPanel.add(component, BorderLayout.CENTER);
      contentPanel.revalidate();
      contentPanel.repaint();
   }

   // 🎨 Helper method for styled buttons
   private JButton createSidebarButton(String text) {
      JButton button = new JButton(text);
      button.setAlignmentX(Component.CENTER_ALIGNMENT);
      button.setMaximumSize(new Dimension(180, 40));
      button.setFocusPainted(false);
      button.setBackground(new Color(60, 60, 100));
      button.setForeground(Color.WHITE);
      button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
      return button;
   }

   public static void main(String[] args) {
      new DashboardWindow();
   }
}
