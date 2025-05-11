// package logic;

// import database.DatabaseHelper;

// import java.sql.*;
// import java.util.*;

// public class RoutineGenerator {

//    // Stores assigned slots: Map<day+slot_time, List of teacher_ids or room_ids>
//    private static final Set<String> usedTeacherSlots = new HashSet<>();
//    private static final Set<String> usedRoomSlots = new HashSet<>();

//    public static void generateRoutineForBatch(int batchId) {
//       try (Connection conn = DatabaseHelper.getConnection()) {

//          // 1️⃣ Load all courses for the batch
//          List<Course> courses = getCoursesForBatch(conn, batchId);

//          // 2️⃣ Load all routine time slots
//          List<RoutineSlot> slots = getAllSlots(conn);

//          // 3️⃣ For each course, assign to a free slot
//          for (Course course : courses) {
//             RoutineSlot freeSlot = findFreeSlot(conn, slots, course.department);
//             if (freeSlot != null) {
//                assignCourseToSlot(conn, batchId, course, freeSlot);
//             } else {
//                System.out.println("❌ No slot available for: " + course.name);
//             }
//          }
//          System.out.println("Courses found for batch " + batchId + ":");
//          // for (Course c : courses) {
//          //    System.out.println("➡️ " + c.name + " (" + c.department + ")");
//          // }

//          System.out.println("✅ Routine generated successfully for batch ID: " + batchId);

//       } catch (Exception e) {
//          e.printStackTrace();
//       }
//    }

//    private static RoutineSlot findFreeSlot(Connection conn, List<RoutineSlot> slots, String department)
//          throws SQLException {
//       for (RoutineSlot slot : slots) {
//          String key = slot.day + "_" + slot.startTime;

//          int teacherId = getAvailableTeacher(conn, department, key);
//          int roomId = getAvailableRoom(conn, key);

//          if (teacherId != -1 && roomId != -1) {
//             slot.teacherId = teacherId;
//             slot.roomId = roomId;
//             usedTeacherSlots.add(key + "_T" + teacherId);
//             usedRoomSlots.add(key + "_R" + roomId);
//             return slot;
//          }
//       }
//       return null;
//    }

//    private static void assignCourseToSlot(Connection conn, int batchId, Course course, RoutineSlot slot)
//          throws SQLException {
//       String sql = "INSERT INTO class_assignments (batch_id, course_id, teacher_id, room_id, slot_id) VALUES (?, ?, ?, ?, ?)";
//       try (PreparedStatement stmt = conn.prepareStatement(sql)) {
//          stmt.setInt(1, batchId);
//          stmt.setInt(2, course.id);
//          stmt.setInt(3, slot.teacherId);
//          stmt.setInt(4, slot.roomId);
//          stmt.setInt(5, slot.id);
//          stmt.executeUpdate();
//       }
//    }

//    private static int getAvailableTeacher(Connection conn, String dept, String key) throws SQLException {
//       String sql = "SELECT id FROM teachers WHERE department = ?";
//       try (PreparedStatement stmt = conn.prepareStatement(sql)) {
//          stmt.setString(1, dept);
//          ResultSet rs = stmt.executeQuery();
//          while (rs.next()) {
//             int id = rs.getInt("id");
//             if (!usedTeacherSlots.contains(key + "_T" + id))
//                return id;
//          }
//       }
//       return -1;
//    }

//    private static int getAvailableRoom(Connection conn, String key) throws SQLException {
//       String sql = "SELECT id FROM rooms";
//       try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
//          while (rs.next()) {
//             int id = rs.getInt("id");
//             if (!usedRoomSlots.contains(key + "_R" + id))
//                return id;
//          }
//       }
//       return -1;
//    }

//    private static List<Course> getCoursesForBatch(Connection conn, int batchId) throws SQLException {
//       List<Course> list = new ArrayList<>();
//       String sql = "SELECT * FROM courses"; // Assuming all courses are related to batch
//       ResultSet rs = conn.createStatement().executeQuery(sql);
//       while (rs.next()) {
//          list.add(new Course(
//                rs.getInt("id"),
//                rs.getString("name"),
//                rs.getString("code"),
//                rs.getString("department")));
//       }

//       return list;
//    }

//    private static List<RoutineSlot> getAllSlots(Connection conn) throws SQLException {
//       List<RoutineSlot> slots = new ArrayList<>();
//       ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM routine_slots");
//       while (rs.next()) {
//          slots.add(new RoutineSlot(
//                rs.getInt("id"),
//                rs.getString("day"),
//                rs.getString("start_time"),
//                rs.getString("end_time")));
//       }
//       return slots;
//    }

//    // Helper classes
//    static class Course {
//       int id;
//       String name, code, department;

//       public Course(int id, String name, String code, String department) {
//          this.id = id;
//          this.name = name;
//          this.code = code;
//          this.department = department;
//       }
//    }

//    static class RoutineSlot {
//       int id;
//       String day, startTime, endTime;
//       int teacherId, roomId;

//       public RoutineSlot(int id, String day, String startTime, String endTime) {
//          this.id = id;
//          this.day = day;
//          this.startTime = startTime;
//          this.endTime = endTime;
//       }
//    }
// }

package logic;

import database.DatabaseHelper;

import java.sql.*;
import java.util.*;

public class RoutineGenerator {

   private static final Set<String> usedTeacherSlots = new HashSet<>();
   private static final Set<String> usedRoomSlots = new HashSet<>();

   public static void generateRoutineForBatch(int batchId) {
      try (Connection conn = DatabaseHelper.getConnection()) {

         // 🆕 Create routine version
         int versionId = -1;
         String versionName = "Version " + System.currentTimeMillis();
         String insertVersion = "INSERT INTO routine_versions (batch_id, version_name) VALUES (?, ?)";

         try (PreparedStatement vstmt = conn.prepareStatement(insertVersion, Statement.RETURN_GENERATED_KEYS)) {
            vstmt.setInt(1, batchId);
            vstmt.setString(2, versionName);
            vstmt.executeUpdate();

            ResultSet vrs = vstmt.getGeneratedKeys();
            if (vrs.next()) {
               versionId = vrs.getInt(1);
               System.out.println("🆕 Routine version created with ID: " + versionId);
            }
         }

         // Load courses & slots
         List<Course> courses = getCoursesForBatch(conn, batchId);
         List<RoutineSlot> slots = getAllSlots(conn);

         // Assign each course to free slot
         for (Course course : courses) {
            RoutineSlot freeSlot = findFreeSlot(conn, slots, course.department);
            if (freeSlot != null) {
               assignCourseToSlot(conn, batchId, course, freeSlot, versionId);
            } else {
               System.out.println("❌ No slot available for: " + course.name);
            }
         }

         System.out.println("✅ Routine generated successfully for batch ID: " + batchId);

      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   private static RoutineSlot findFreeSlot(Connection conn, List<RoutineSlot> slots, String department)
         throws SQLException {
      for (RoutineSlot slot : slots) {
         String key = slot.day + "_" + slot.startTime;

         int teacherId = getAvailableTeacher(conn, department, key);
         int roomId = getAvailableRoom(conn, key);

         if (teacherId != -1 && roomId != -1) {
            slot.teacherId = teacherId;
            slot.roomId = roomId;
            usedTeacherSlots.add(key + "_T" + teacherId);
            usedRoomSlots.add(key + "_R" + roomId);
            return slot;
         }
      }
      return null;
   }

   private static void assignCourseToSlot(Connection conn, int batchId, Course course, RoutineSlot slot, int versionId)
         throws SQLException {
      String sql = "INSERT INTO class_assignments (batch_id, course_id, teacher_id, room_id, slot_id, version_id) VALUES (?, ?, ?, ?, ?, ?)";
      try (PreparedStatement stmt = conn.prepareStatement(sql)) {
         stmt.setInt(1, batchId);
         stmt.setInt(2, course.id);
         stmt.setInt(3, slot.teacherId);
         stmt.setInt(4, slot.roomId);
         stmt.setInt(5, slot.id);
         stmt.setInt(6, versionId);
         stmt.executeUpdate();
      }
   }

   private static int getAvailableTeacher(Connection conn, String dept, String key) throws SQLException {
      String sql = "SELECT id FROM teachers WHERE department = ?";
      try (PreparedStatement stmt = conn.prepareStatement(sql)) {
         stmt.setString(1, dept);
         ResultSet rs = stmt.executeQuery();
         while (rs.next()) {
            int id = rs.getInt("id");
            if (!usedTeacherSlots.contains(key + "_T" + id))
               return id;
         }
      }
      return -1;
   }

   private static int getAvailableRoom(Connection conn, String key) throws SQLException {
      String sql = "SELECT id FROM rooms";
      try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
         while (rs.next()) {
            int id = rs.getInt("id");
            if (!usedRoomSlots.contains(key + "_R" + id))
               return id;
         }
      }
      return -1;
   }

   private static List<Course> getCoursesForBatch(Connection conn, int batchId) throws SQLException {
      List<Course> list = new ArrayList<>();
      String sql = "SELECT * FROM courses"; // later link to batch if needed
      ResultSet rs = conn.createStatement().executeQuery(sql);
      while (rs.next()) {
         list.add(new Course(
               rs.getInt("id"),
               rs.getString("name"),
               rs.getString("code"),
               rs.getString("department")));
      }
      return list;
   }

   private static List<RoutineSlot> getAllSlots(Connection conn) throws SQLException {
      List<RoutineSlot> slots = new ArrayList<>();
      ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM routine_slots");
      while (rs.next()) {
         slots.add(new RoutineSlot(
               rs.getInt("id"),
               rs.getString("day"),
               rs.getString("start_time"),
               rs.getString("end_time")));
      }
      return slots;
   }

   // Inner classes
   static class Course {
      int id;
      String name, code, department;

      public Course(int id, String name, String code, String department) {
         this.id = id;
         this.name = name;
         this.code = code;
         this.department = department;
      }
   }

   static class RoutineSlot {
      int id;
      String day, startTime, endTime;
      int teacherId, roomId;

      public RoutineSlot(int id, String day, String startTime, String endTime) {
         this.id = id;
         this.day = day;
         this.startTime = startTime;
         this.endTime = endTime;
      }
   }
}
