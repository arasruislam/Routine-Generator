import javax.swing.*;

import database.SchemaCreator;
import gui.DashboardWindow;

public class Main {
   public static void main(String[] args) {
      // 🗃️ Step 1: Create database tables (if not exists)
      SchemaCreator.createTables();

      // 🖥️ Step 2: Launch the main dashboard
      SwingUtilities.invokeLater(() -> {
         DashboardWindow window = new DashboardWindow();
         window.setVisible(true);
      });
   }
}
