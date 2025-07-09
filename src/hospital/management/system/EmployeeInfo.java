package hospital.management.system;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class EmployeeInfo extends JPanel {
    private JTextField searchField;
    private JTable table;
    private JScrollPane scrollPane;
    private JButton searchBtn, addBtn, deleteBtn;

    public EmployeeInfo() {
        setLayout(null);
        setBackground(new Color(247, 247, 250));

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screen.width - 420;
        int height = screen.height - 100;

        JPanel card = new JPanel(null);
        card.setBackground(Color.WHITE);
        card.setBounds(0, 0, width, height);
        card.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        add(card);

        JLabel title = new JLabel("Employee Information");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setBounds(20, 10, 400, 40);
        card.add(title);

        searchField = new JTextField();
        searchField.setFont(new Font("Segoe UI", Font.BOLD, 16));
        searchField.setBounds(20, 60, 300, 35);
        card.add(searchField);

        searchBtn = new JButton("Search");
        searchBtn.setBounds(330, 60, 100, 35);
        searchBtn.setBackground(new Color(59, 130, 246));
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setFocusPainted(false);
        searchBtn.addActionListener(e -> updateTable(searchField.getText().trim()));
        card.add(searchBtn);

        // Add Button
        addBtn = new JButton("Add Employee");
        addBtn.setBounds(440, 60, 100, 35);
        addBtn.setBackground(new Color(34, 197, 94));
        addBtn.setForeground(Color.WHITE);
        addBtn.setFocusPainted(false);
        addBtn.addActionListener(e -> openAddEmployeePanel());
        card.add(addBtn);

        // Delete Button
        deleteBtn = new JButton("Delete Employee");
        deleteBtn.setBounds(550, 60, 100, 35);
        deleteBtn.setBackground(new Color(220, 38, 38)); // reddish
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setFocusPainted(false);
        deleteBtn.addActionListener(e -> openDeleteEmployeePanel());
        card.add(deleteBtn);

        table = createEmployeeTable("");
        scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 110, width - 40, height - 130);
        card.add(scrollPane);
    }

    private JTable createEmployeeTable(String search) {
        Vector<String> columns = new Vector<>();
        Vector<Vector<Object>> data = new Vector<>();

        columns.add("ID");
        columns.add("Name");
        columns.add("Position");
        columns.add("Department");
        columns.add("Contact");
        columns.add("Salary");

        try {
            conn c = new conn();
            String sql = "SELECT * FROM employee_info";
            if (!search.isEmpty()) {
                sql += " WHERE name LIKE ? OR position LIKE ? OR department LIKE ?";
            }

            PreparedStatement ps = c.getConnection().prepareStatement(sql);

            if (!search.isEmpty()) {
                String pattern = "%" + search + "%";
                ps.setString(1, pattern);
                ps.setString(2, pattern);
                ps.setString(3, pattern);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();

                row.add(rs.getInt("employee_id"));
                row.add(rs.getString("name"));
                row.add(rs.getString("position"));
                row.add(rs.getString("department"));
                row.add(rs.getString("contact"));
                row.add(rs.getDouble("salary"));
                data.add(row);
            }

            rs.close();
            ps.close();
            c.getConnection().close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "DB Error: " + e.getMessage());
        }

        JTable table = new JTable(new javax.swing.table.DefaultTableModel(data, columns) {
            public boolean isCellEditable(int row, int column) { return false; }
        });

        table.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        table.setRowHeight(28);  // increase row height for better spacing

        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 18));

        return table;
    }


    private void updateTable(String search) {
        table = createEmployeeTable(search);
        scrollPane.setViewportView(table);
    }

    private void openAddEmployeePanel() {
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Add Employee", true);
        dialog.setContentPane(new AddEmployeeInfo());
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);
        dialog.setVisible(true);
        updateTable(searchField.getText().trim());
    }

    private void openDeleteEmployeePanel() {
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Delete Employee", true);
        dialog.setContentPane(new DeleteEmployee());
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);
        dialog.setVisible(true);
        // Refresh table after adding
        updateTable(searchField.getText().trim());
    }
}
