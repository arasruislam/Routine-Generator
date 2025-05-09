package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import gui.forms.BatchForm;
import gui.forms.CourseForm;
import gui.forms.RoomForm;
import gui.forms.RoutineSlotForm;
import gui.forms.TeacherForm;
import logic.RoutineGenerator;

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
      // homeBtn.addActionListener(e -> loadContent(new JLabel("Welcome to Dashboard!
      // 🎓", SwingConstants.CENTER)));
      homeBtn.addActionListener(e -> {
         JPanel homePanel = new JPanel(new GridBagLayout());
         homePanel.setBackground(Color.WHITE);
         homePanel.setBorder(BorderFactory.createCompoundBorder(
               BorderFactory.createLineBorder(new Color(210, 210, 210)),
               BorderFactory.createEmptyBorder(30, 30, 40, 30)));

         // Title Label
         JLabel titleLabel = new JLabel(
               "<html><div style='text-align:center;'><b>📘 Welcome to University Routine Generator</b></div></html>");
         titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
         titleLabel.setForeground(new Color(50, 50, 80));

         // Subtitle Message
         JLabel messageLabel = new JLabel("<html><div style='text-align:center;'>"
               + "Manage teachers, courses, rooms, and generate university routines efficiently.<br>"
               + "Select an option from the sidebar to get started.</div></html>");
         messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
         messageLabel.setForeground(new Color(80, 80, 80));
         messageLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

         // Optional: Add an illustration or icon (if you have one)
         // ImageIcon icon = new ImageIcon("path_to_icon.png");
         // JLabel iconLabel = new JLabel(icon);

         // Layout constraints
         GridBagConstraints gbc = new GridBagConstraints();
         gbc.gridx = 0;
         gbc.gridy = 0;
         gbc.anchor = GridBagConstraints.CENTER;

         // Uncomment if using an icon
         // homePanel.add(iconLabel, gbc);
         // gbc.gridy++;

         homePanel.add(titleLabel, gbc);
         gbc.gridy++;
         homePanel.add(messageLabel, gbc);

         loadContent(homePanel);
      });

      JButton teacherBtn = createSidebarButton("👨 Add Teacher");
      teacherBtn.addActionListener(e -> loadContent(new TeacherForm()));

      JButton courseBtn = createSidebarButton("📚 Add Course");
      courseBtn.addActionListener(e -> loadContent(new CourseForm()));

      JButton roomBtn = createSidebarButton("🏫 Add Room");
      roomBtn.addActionListener(e -> loadContent(new RoomForm()));

      JButton batchBtn = createSidebarButton("👥 Add Batch");
      batchBtn.addActionListener(e -> loadContent(new BatchForm()));

      JButton slotBtn = createSidebarButton("⏱ Add Slot");
      slotBtn.addActionListener(e -> loadContent(new RoutineSlotForm()));
      sidebarPanel.add(slotBtn);

      JButton routineBtn = createSidebarButton("🧠 Generate Routine");
      routineBtn.addActionListener(e -> {
         loadContent(new RoutineTablePanel(1)); // use correct batch ID
      });
      sidebarPanel.add(routineBtn);

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

   // private void initTopbar() {
   // topbarPanel = new JPanel();
   // topbarPanel.setBackground(new Color(60, 60, 100));
   // topbarPanel.setPreferredSize(new Dimension(getWidth(), 50));
   // topbarPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // Centering the
   // title

   // JLabel title = new JLabel("📅 University Routine Generator");
   // title.setForeground(Color.WHITE);
   // title.setFont(new Font("Segoe UI", Font.BOLD, 18));

   // topbarPanel.add(title);
   // add(topbarPanel, BorderLayout.PAGE_START);
   // }
   private void initTopbar() {
      topbarPanel = new JPanel(new BorderLayout());
      topbarPanel.setBackground(new Color(45, 45, 90)); // Softer dark blue for depth
      topbarPanel.setPreferredSize(new Dimension(getWidth(), 70));

      // Add subtle border to separate from sidebar/content
      topbarPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(60, 60, 120)));

      // Title label
      JLabel titleLabel = new JLabel("📅 University Routine Generator");
      titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
      titleLabel.setForeground(Color.WHITE);
      titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

      // Right panel for user/profile or icons (optional)
      // JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
      // rightPanel.setOpaque(false);

      // Example: Profile Icon Button
      // JButton profileButton = new JButton("👤");
      // profileButton.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
      // profileButton.setToolTipText("Profile");
      // profileButton.setContentAreaFilled(false);
      // profileButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
      // profileButton.setFocusPainted(false);
      // profileButton.setForeground(Color.WHITE);
      // profileButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

      // You can add more icons like notifications here if needed
      // rightPanel.add(profileButton);

      // Assemble components
      topbarPanel.add(titleLabel, BorderLayout.CENTER);
      // topbarPanel.add(rightPanel, BorderLayout.EAST);

      add(topbarPanel, BorderLayout.NORTH);
   }

   private void initContentArea() {
      contentPanel = new JPanel(new BorderLayout());
      contentPanel.setBackground(new Color(245, 247, 250));
      contentPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

      // Card panel for welcome message
      JPanel cardPanel = new JPanel(new GridBagLayout());
      cardPanel.setBackground(Color.WHITE);
      cardPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)));
      ((JComponent) cardPanel).setBorder(BorderFactory.createMatteBorder(1, 1, 3, 3, new Color(200, 200, 200)));

      JLabel welcomeLabel = new JLabel(
            "<html><div style='text-align:center;'><h2 style='color:#333;'>📘 Welcome to the University Routine System</h2><p style='color:#666;'>Use the menu on the left to navigate between forms.</p></div></html>");
      welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));

      cardPanel.add(welcomeLabel);
      contentPanel.add(cardPanel, BorderLayout.CENTER);

      add(contentPanel, BorderLayout.CENTER);
   }

   // 🔁 Helper method to load forms dynamically
   private void loadContent(JComponent component) {
      contentPanel.removeAll();
      contentPanel.add(component, BorderLayout.CENTER);
      contentPanel.revalidate();
      contentPanel.repaint();
   }

   // Helper class for rounded borders
   private static class RoundedBorder implements Border {
      private int radius;
      private Color borderColor;

      RoundedBorder(int radius, Color borderColor) {
         this.radius = radius;
         this.borderColor = borderColor;
      }

      public Insets getBorderInsets(Component c) {
         return new Insets(radius + 1, radius + 1, radius + 2, radius + 2);
      }

      public boolean isBorderOpaque() {
         return true;
      }

      public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
         Graphics2D g2 = (Graphics2D) g.create();
         g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
         g2.setColor(borderColor);
         g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
         g2.dispose();
      }
   }

   // 🎨 Helper method for styled buttons with hover effect
   private JButton createSidebarButton(String text) {
      JButton button = new JButton(text);
      button.setAlignmentX(Component.CENTER_ALIGNMENT);
      button.setMaximumSize(new Dimension(180, 45));
      button.setFocusPainted(false);
      button.setBackground(new Color(60, 120, 200)); // Professional blue tone
      button.setForeground(Color.WHITE);
      button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
      button.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
      button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

      // Rounded corners
      button.setContentAreaFilled(false);
      button.setOpaque(false);

      // Custom painting for rounded border and background
      button.setBorder(new RoundedBorder(10, new Color(60, 120, 200)));

      // Hover effect
      button.addMouseListener(new MouseAdapter() {
         public void mouseEntered(MouseEvent e) {
            button.setBackground(new Color(70, 140, 220));
         }

         public void mouseExited(MouseEvent e) {
            button.setBackground(new Color(60, 120, 200));
         }
      });

      return button;
   }

   public static void main(String[] args) {
      new DashboardWindow();
   }
}
