package hospital.management.system;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class AllPatientInfo extends JPanel {
    private JTextField searchField;
    private JTable table;
    private JScrollPane scrollPane;
    private JPanel card;

    public AllPatientInfo() {
        setBackground(new Color(247, 247, 250));
        setLayout(null);

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screen.width - 420;
        int height = screen.height - 100;

        card = new JPanel(null);
        card.setBackground(Color.WHITE);
        card.setBounds(0, 0, width, height);
        card.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        JLabel title = new JLabel("All Patient Information");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(17, 24, 39));
        title.setBounds(20, 10, 400, 40);
        card.add(title);

        searchField = new JTextField();
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        searchField.setBounds(20, 60, 300, 35);
        card.add(searchField);

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { refresh(); }
            public void removeUpdate(DocumentEvent e) { refresh(); }
            public void changedUpdate(DocumentEvent e) { refresh(); }
            private void refresh() { updateTable(searchField.getText().trim()); }
        });

        JButton dischargeBtn = new JButton("Discharge");
        dischargeBtn.setBounds(340, 60, 110, 35);
        dischargeBtn.setBackground(new Color(234, 88, 12));
        dischargeBtn.setForeground(Color.WHITE);
        dischargeBtn.setFocusPainted(false);
        dischargeBtn.addActionListener(e -> openDischargePanel());
        card.add(dischargeBtn);

        JButton updateBtn = new JButton("Update");
        updateBtn.setBounds(460, 60, 110, 35);
        updateBtn.setBackground(new Color(59, 130, 246));
        updateBtn.setForeground(Color.WHITE);
        updateBtn.setFocusPainted(false);
        updateBtn.addActionListener(e -> openUpdatePanel());
        card.add(updateBtn);

        table = createPatientTable("");
        scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 110, width - 40, height - 130);
        card.add(scrollPane);

        add(card);
    }

    private JTable createPatientTable(String searchText) {
        try {
            conn connObj = new conn();
            Statement stmt = connObj.getConnection().createStatement();
            String query = searchText.isEmpty() ? "SELECT * FROM patient_info" :
                    "SELECT * FROM patient_info WHERE name LIKE '%" + searchText + "%'";
            ResultSet rs = stmt.executeQuery(query);

            Vector<String> columnNames = new Vector<>();
            ResultSetMetaData meta = rs.getMetaData();
            int cols = meta.getColumnCount();
            for (int i = 1; i <= cols; i++) {
                columnNames.add(meta.getColumnName(i));
            }

            Vector<Vector<Object>> data = new Vector<>();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                for (int i = 1; i <= cols; i++) {
                    row.add(rs.getObject(i));
                }
                data.add(row);
            }
            rs.close();
            stmt.close();
            connObj.getConnection().close();

            JTable jt = new JTable(data, columnNames) {
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            jt.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            jt.setRowHeight(30);
            jt.setGridColor(new Color(229, 231, 235));
            jt.setShowGrid(true);
            jt.setBackground(Color.WHITE);
            jt.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
            jt.getTableHeader().setBackground(new Color(243, 244, 246));

            return jt;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            return new JTable();
        }
    }

    private void updateTable(String searchText) {
        JTable newTable = createPatientTable(searchText);
        scrollPane.setViewportView(newTable);
        table = newTable;
    }

    private void openDischargePanel() {
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Discharge Patient", true);
        dialog.setContentPane(new DischargePatientPanel());
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void openUpdatePanel() {
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Update Patient", true);
        dialog.setContentPane(new UpdatePatientDetails());
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}