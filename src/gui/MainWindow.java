package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainWindow extends JFrame {

   public MainWindow() {
      setTitle("Varsity Routine Builder");
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setSize(500, 400);
      setLocationRelativeTo(null);
      setLayout(new GridLayout(5, 1, 10, 10));

      JButton teacherBtn = new JButton("➕ Add Teacher");
      JButton courseBtn = new JButton("➕ Add Course");
      JButton batchBtn = new JButton("➕ Add Batch");
      JButton roomBtn = new JButton("➕ Add Room");

      teacherBtn.addActionListener((ActionEvent e) -> new TeacherForm());
      courseBtn.addActionListener((ActionEvent e) -> new CourseForm());
      batchBtn.addActionListener((ActionEvent e) -> new BatchForm());
      roomBtn.addActionListener((ActionEvent e) -> new RoomForm());

      add(teacherBtn);
      add(courseBtn);
      add(batchBtn);
      add(roomBtn);
   }
}
