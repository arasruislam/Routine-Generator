package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseHelper {
   private static final String URL = "jdbc:sqlite:routine.db";
   private static Connection connection = null;

   public static Connection getConnection() {
      if (connection == null) {
         try {
            connection = DriverManager.getConnection(URL);
            System.out.println("✅ Database connected successfully.");

            // 🆕 Automatically create all tables when DB connects
            SchemaCreator.createTables(); // <- this line added

         } catch (SQLException e) {
            System.out.println("❌ Failed to connect to database.");
            e.printStackTrace();
         }
      }
      return connection;
   }
}
