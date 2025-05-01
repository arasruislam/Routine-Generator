# 🧠 Smart Routine Generator System

A Java-based desktop application to **automatically generate class routines** for universities based on specific **batch, semester, and class data**, with proper **teacher and room assignment**, **break management**, and **PDF export** functionality. The app ensures **no teacher or room clashes** and provides a fully interactive GUI, backed by **SQLite database**.

---

## 📌 Features

- 📅 Automatic Routine Generation by batch, semester, and class
- 👨‍🏫 Teacher Assignment based on department
- 🏫 Room Assignment ensuring no conflicts
- 🚫 Clash Prevention for teachers and rooms
- ☕ Break Management after every class (5 min) and every 3 classes (20 min)
- 📄 PDF Export of generated routines using iText
- 🗃️ SQLite Database Integration for all data
- 🖥️ GUI with Java Swing
- 🛠️ Windows Executable (.exe) output using Launch4j/JPackage

---

## 🏗️ Tech Stack

| Category         | Technologies Used          |
|------------------|-----------------------------|
| Programming      | Java                        |
| GUI              | Java Swing                  |
| Database         | SQLite                      |
| PDF Generator    | iText (Java PDF library)    |
| Build Tool       | Launch4j / JPackage         |
| IDE              | VS Code                     |
| Version Control  | Git & GitHub                |

---

## 🔄 Project Architecture

- **Models**: `Course`, `Teacher`, `Batch`, `Room`, `RoutineSlot`
- **Database Layer**: SQLite connection and queries
- **GUI Layer**: Forms, input fields, routine display panel
- **Routine Generator**: Clash-free slot selection and break logic
- **PDF Export**: Save routine in printable professional format
- **Executable Builder**: Converts application to `.exe` for Windows
- **Collaboration**: Git + GitHub for version control and teamwork

---

## 🔁 Workflow

1. **Read data** (batch, semester, teacher, room) from SQLite DB  
2. **Generate routine**:
   - Auto-select teachers by department
   - Ensure clash-free slot allocation
   - Insert 5-min break after each class
   - Insert 20-min break after every 3 classes
3. **Export routine as PDF**
4. (Optional) Save generated routine to DB to prevent duplication

---

## 🚫 Constraints

- No teacher assigned to two classes at the same time
- No room used for more than one class at a time
- Department-wise teacher filtering
- Breaks must strictly follow set logic
- No hardcoded data; all data comes from DB

---

## ✅ Expected Output

- Automatically generated, clash-free class routines
- Professional-quality routine in PDF format
- Windows executable (.exe) for easy installation and use

---

## 🚀 Future Enhancements

- Migrate GUI to JavaFX with animation
- Add/edit custom teachers and courses
- Support multiple routine history and backups
- Email routines directly from the app
- Expand to mobile app development

---

## 👥 Team Members

| Name                  | ID                    | Section |
|-----------------------|------------------------|---------|
| Ashraful              | 0272320005101225       | 63(D)   |
| Sumaiya Akter Bonna   | 0272320005101252       | 63(D)   |
| Tanjina Akter Mohema  | 0272320005101279       | 63(D)   |
| Sagor Kumar Das       | 0272320005101264       | 63(D)   |

---

## 🎯 Learning Outcomes

- Proficient in **Java** and **OOP principles**
- Experience with **SQLite** and SQL queries
- Building **interactive GUIs** using Swing
- Developing **logic-driven systems** for real-world problems
- Exporting structured documents with **iText PDF**
- Creating Windows applications using **Launch4j/JPackage**
- Strengthened teamwork and project management skills
