// src/gui/forms/CourseForm.java
package gui.forms;

import javax.swing.*;
import java.awt.*;

public class CourseForm extends JPanel {
   public CourseForm() {
      setBackground(new Color(245, 245, 245));
      setLayout(new FlowLayout());
      add(new JLabel("📚 Course Form"));
   }
}
