import javax.swing.*;

import database.SchemaCreator;
import gui.MainWindow;

public class Main {
   public static void main(String[] args) {
      // Create database tables
      SchemaCreator.createTables();


      SwingUtilities.invokeLater(() -> {
         MainWindow window = new MainWindow();
         window.setVisible(true);
      });
   }
}
