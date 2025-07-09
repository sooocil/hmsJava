package hospital.management.system;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class ViewRoom extends JPanel {
    private JTextField   searchField;
    private JTable       table;
    private JScrollPane  scrollPane;

    public ViewRoom() {
        setBackground(new Color(247, 247, 250));
        setLayout(null);

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int width  = screen.width  - 420;
        int height = screen.height - 100;

        JPanel card = new JPanel(null);
        card.setBackground(Color.WHITE);
        card.setBounds(0, 0, width, height);
        card.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        add(card);

        // title
        JLabel title = new JLabel("Room Information");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(17, 24, 39));
        title.setBounds(20, 10, 300, 40);
        card.add(title);

        // search
        searchField = new JTextField();
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        searchField.setBounds(20, 60, 300, 35);
        card.add(searchField);

        JButton searchBtn = new JButton("Search");
        searchBtn.setBounds(330, 60, 100, 35);
        searchBtn.setBackground(new Color(59, 130, 246));
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setFocusPainted(false);
        searchBtn.addActionListener(e -> updateTable(searchField.getText().trim()));
        card.add(searchBtn);

        JButton addRoomBtn = new JButton("Add Room");
        addRoomBtn.setBounds(450, 60, 120, 35);
        addRoomBtn.setBackground(new Color(34, 197, 94));
        addRoomBtn.setForeground(Color.WHITE);
        addRoomBtn.setFocusPainted(false);
        addRoomBtn.addActionListener(e -> openAddRoomDialog());
        card.add(addRoomBtn);

        JButton updateRoomBtn = new JButton("Update Room");
        updateRoomBtn.setBounds(580, 60, 130, 35);
        updateRoomBtn.setBackground(new Color(234, 88, 12));
        updateRoomBtn.setForeground(Color.WHITE);
        updateRoomBtn.setFocusPainted(false);
        updateRoomBtn.addActionListener(e -> openUpdateRoomDialog());
        card.add(updateRoomBtn);

        table = createRoomTable("");
        scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 110, width - 40, height - 130);
        card.add(scrollPane);
    }

    private JTable createRoomTable(String search) {
        Vector<String> columns = new Vector<>();
        Vector<Vector<Object>> data = new Vector<>();

        // Define the column headers
        columns.add("Room No");
        columns.add("Availability");
        columns.add("Price");
        columns.add("Room Type");
        columns.add("Patient Name");

        try {
            conn c = new conn();
            Statement st = c.getConnection().createStatement();

            // SQL: LEFT JOIN room with patient_info on room_no to get patient name
            String baseSql = "SELECT r.room_no, r.availability, r.price, r.room_type, p.name " +
                    "FROM room r LEFT JOIN patient_info p ON r.room_no = p.room_no";

            if (!search.isEmpty()) {
                baseSql += " WHERE r.room_no LIKE ? OR r.room_type LIKE ? OR p.name LIKE ?";
            }

            PreparedStatement ps = c.getConnection().prepareStatement(baseSql);

            if (!search.isEmpty()) {
                String pattern = "%" + search + "%";
                ps.setString(1, pattern);
                ps.setString(2, pattern);
                ps.setString(3, pattern);
            }

            ResultSet rs = ps.executeQuery();
// process rs


            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("room_no"));
                // Show availability as Yes/No string for readability
                row.add(rs.getBoolean("availability") ? "Available" : "Occupied");
                row.add(rs.getString("price"));
                row.add(rs.getString("room_type"));
                // patient name might be null if no patient assigned
                String patientName = rs.getString("name");
                row.add(patientName != null ? patientName : "None");
                data.add(row);
            }

            rs.close();
            st.close();
            c.getConnection().close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "DB Error: " + e.getMessage());
        }

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // all cells non-editable
            }
        };

        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        table.setRowHeight(28);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        return table;
    }


    private void updateTable(String search) {
        table = createRoomTable(search);
        scrollPane.setViewportView(table);
    }

    private void openAddRoomDialog() {
        JDialog d = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add Room", true);
        d.setContentPane(new AddRoomPanel());
        d.pack(); d.setLocationRelativeTo(this); d.setVisible(true);
        d.setResizable(false);

        updateTable(searchField.getText().trim());
    }

    private void openUpdateRoomDialog() {
        JDialog d = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Update Room", true);
        d.setContentPane(new UpdateRoomPanel());
        d.pack(); d.setLocationRelativeTo(this); d.setVisible(true);
        d.setResizable(false);

        updateTable(searchField.getText().trim());
    }
}