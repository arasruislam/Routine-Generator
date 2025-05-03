package database;

import java.sql.Connection;
import java.sql.Statement;

public class SchemaCreator {
   public static void createTables() {
      try (Connection conn = DatabaseHelper.getConnection(); Statement stmt = conn.createStatement()) {

         String createTeachers = "CREATE TABLE IF NOT EXISTS teachers (" +
               "id INTEGER PRIMARY KEY AUTOINCREMENT," +
               "name TEXT NOT NULL," +
               "department TEXT NOT NULL" +
               ");";

         String createCourses = "CREATE TABLE IF NOT EXISTS courses (" +
               "id INTEGER PRIMARY KEY AUTOINCREMENT," +
               "name TEXT NOT NULL," +
               "code TEXT NOT NULL UNIQUE," +
               "department TEXT NOT NULL" +
               ");";

         String createBatches = "CREATE TABLE IF NOT EXISTS batches (" +
               "id INTEGER PRIMARY KEY AUTOINCREMENT," +
               "semester TEXT NOT NULL," +
               "batch_name TEXT NOT NULL" +
               ");";

         String createRooms = "CREATE TABLE IF NOT EXISTS rooms (" +
               "id INTEGER PRIMARY KEY AUTOINCREMENT," +
               "room_number TEXT NOT NULL UNIQUE" +
               ");";

         String createRoutineSlots = "CREATE TABLE IF NOT EXISTS routine_slots (" +
               "id INTEGER PRIMARY KEY AUTOINCREMENT," +
               "day TEXT NOT NULL," +
               "start_time TEXT NOT NULL," +
               "end_time TEXT NOT NULL" +
               ");";

         String createAssignments = "CREATE TABLE IF NOT EXISTS class_assignments (" +
               "id INTEGER PRIMARY KEY AUTOINCREMENT," +
               "batch_id INTEGER," +
               "course_id INTEGER," +
               "teacher_id INTEGER," +
               "room_id INTEGER," +
               "slot_id INTEGER," +
               "FOREIGN KEY (batch_id) REFERENCES batches(id)," +
               "FOREIGN KEY (course_id) REFERENCES courses(id)," +
               "FOREIGN KEY (teacher_id) REFERENCES teachers(id)," +
               "FOREIGN KEY (room_id) REFERENCES rooms(id)," +
               "FOREIGN KEY (slot_id) REFERENCES routine_slots(id)" +
               ");";

         stmt.execute(createTeachers);
         stmt.execute(createCourses);
         stmt.execute(createBatches);
         stmt.execute(createRooms);
         stmt.execute(createRoutineSlots);
         stmt.execute(createAssignments);

         System.out.println("✅ Tables created successfully!");

      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}
