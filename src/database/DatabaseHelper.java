package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseHelper {
   private static final String URL = "jdbc:sqlite:routine.db";
   private static Connection connection = null;

   public static Connection getConnection() {
      try {
         if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL);
            System.out.println("✅ Database connected successfully.");
         }
      } catch (SQLException e) {
         System.out.println("❌ Failed to connect to database.");
         e.printStackTrace();
      }
      return connection;
   }
}
