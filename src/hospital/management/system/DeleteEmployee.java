package hospital.management.system;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.util.Vector;

public class DeleteEmployee extends JPanel {

    private JComboBox<String> comboName;
    private JComboBox<String> comboDepartment;
    private JButton buttonDelete;

    public DeleteEmployee() {
        setPreferredSize(new Dimension(400, 350));
        setSize(700,700);
        setBackground(Color.WHITE);
        setLayout(null);

        int y = 20;

        addLabel("Employee Name:", 20, y);
        comboName = new JComboBox<>();
        comboName.setBounds(150, y, 200, 25);
        add(comboName);
        y += 40;

        addLabel("Department:", 20, y);
        comboDepartment = new JComboBox<>();
        comboDepartment.setBounds(150, y, 200, 25);
        add(comboDepartment);
        y += 50;

        buttonDelete = new JButton("Delete Employee");
        buttonDelete.setBounds(150, y, 160, 35);
        buttonDelete.setBackground(new Color(234, 88, 12));
        buttonDelete.setForeground(Color.WHITE);
        buttonDelete.setFocusPainted(false);
        add(buttonDelete);

        buttonDelete.addActionListener(this::handleDelete);

        loadEmployees();
    }

    private void addLabel(String text, int x, int y) {
        JLabel lbl = new JLabel(text);
        lbl.setBounds(x, y, 120, 25);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        add(lbl);
    }

    private void loadEmployees() {
        try {
            conn c = new conn();
            Connection connection = c.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT DISTINCT name, department FROM employee_info ORDER BY name");

            Vector<String> names = new Vector<>();
            Vector<String> departments = new Vector<>();

            while (rs.next()) {
                String name = rs.getString("name");
                String department = rs.getString("department");
                names.add(name);
                if (!departments.contains(department)) {
                    departments.add(department);
                }
            }

            comboName.setModel(new DefaultComboBoxModel<>(names));
            comboDepartment.setModel(new DefaultComboBoxModel<>(departments));

            rs.close();
            stmt.close();
            connection.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading employees: " + ex.getMessage());
        }
    }

    private void handleDelete(ActionEvent e) {
        String name = (String) comboName.getSelectedItem();
        String department = (String) comboDepartment.getSelectedItem();

        if (name == null || department == null) {
            JOptionPane.showMessageDialog(this, "Please select both employee name and department.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete " + name + " from " + department + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            conn c = new conn();
            Connection connection = c.getConnection();
            String query = "DELETE FROM employee_info WHERE name = ? AND department = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, name);
            ps.setString(2, department);

            int rows = ps.executeUpdate();

            ps.close();
            connection.close();

            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Employee deleted successfully.");
                loadEmployees();  // Refresh combo boxes
            } else {
                JOptionPane.showMessageDialog(this, "Employee not found or already deleted.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }

    public static void showDialog(Component parent) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(parent), "Delete Employee", true);
        DeleteEmployee panel = new DeleteEmployee();
        dialog.setContentPane(panel);
        dialog.setSize(420, 250);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }
}
