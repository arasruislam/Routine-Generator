package routine;

import database.DatabaseHelper;
import utils.PDFExporter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class RoutineHistoryPanel extends JPanel {

   public RoutineHistoryPanel() {
      setLayout(new BorderLayout());
      JLabel title = new JLabel("🕘 Routine History", JLabel.CENTER);
      title.setFont(new Font("Segoe UI", Font.BOLD, 20));
      add(title, BorderLayout.NORTH);

      String[] cols = { "Version", "Batch", "Created At", "Actions" };
      DefaultTableModel model = new DefaultTableModel(cols, 0);
      JTable table = new JTable(model);
      add(new JScrollPane(table), BorderLayout.CENTER);

      try (Connection conn = DatabaseHelper.getConnection()) {
         String sql = "SELECT rv.id, rv.version_name, rv.created_at, b.batch_name " +
               "FROM routine_versions rv " +
               "JOIN batches b ON rv.batch_id = b.id " +
               "ORDER BY rv.created_at DESC";
         ResultSet rs = conn.createStatement().executeQuery(sql);

         while (rs.next()) {
            int versionId = rs.getInt("id");
            String versionName = rs.getString("version_name");
            String createdAt = rs.getString("created_at");
            String batchName = rs.getString("batch_name");

            JButton viewBtn = new JButton("👁 View");
            JButton exportBtn = new JButton("📄 Export");

            // Needed to be inside final wrapper for lambda
            final int vId = versionId;

            viewBtn.addActionListener(e -> {
               JFrame frame = new JFrame("Routine View - " + versionName);
               frame.setSize(800, 500);
               frame.setLocationRelativeTo(null);
               frame.add(new RoutineViewByVersionPanel(vId)); // will create below
               frame.setVisible(true);
            });

            exportBtn.addActionListener(e -> {
               PDFExporter.exportRoutineToPDFByVersion(vId, "routine_v" + vId + ".pdf");
               JOptionPane.showMessageDialog(this, "✅ Exported to routine_v" + vId + ".pdf");
            });

            model.addRow(new Object[] { versionName, batchName, createdAt, new JPanel(new FlowLayout()) {
               {
                  add(viewBtn);
                  add(exportBtn);
               }
            } });
         }

      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}
