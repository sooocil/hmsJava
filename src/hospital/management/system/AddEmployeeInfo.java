package hospital.management.system;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class AddEmployeeInfo extends JPanel {
    private JTextField textName, textPosition, textContact, textSalary;
    private JComboBox<String> comboDepartment;
    private JButton buttonAdd;

    public AddEmployeeInfo() {
        setPreferredSize(new Dimension(400, 350));
        setBackground(Color.WHITE);
        setLayout(null);

        int y = 20;

        addLabel("Name:", 20, y);
        textName = addTextField(150, y);
        y += 40;

        addLabel("Position:", 20, y);
        textPosition = addTextField(150, y);
        y += 40;

        addLabel("Department:", 20, y);
        comboDepartment = new JComboBox<>(new String[]{"IT", "HR", "Finance", "Surgery", "Nursing", "Laboratory", "Front Desk"});
        comboDepartment.setBounds(150, y, 200, 25);
        add(comboDepartment);
        y += 40;

        addLabel("Contact:", 20, y);
        textContact = addTextField(150, y);
        y += 40;

        addLabel("Salary:", 20, y);
        textSalary = addTextField(150, y);
        y += 50;

        buttonAdd = new JButton("Add Employee");
        buttonAdd.setBounds(150, y, 150, 35);
        buttonAdd.setBackground(new Color(59, 130, 246));
        buttonAdd.setForeground(Color.WHITE);
        buttonAdd.setFocusPainted(false);
        add(buttonAdd);

        buttonAdd.addActionListener(this::handleAdd);
    }

    private void addLabel(String text, int x, int y) {
        JLabel lbl = new JLabel(text);
        lbl.setBounds(x, y, 120, 25);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        add(lbl);
    }

    private JTextField addTextField(int x, int y) {
        JTextField tf = new JTextField();
        tf.setBounds(x, y, 200, 25);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        add(tf);
        return tf;
    }

    private void handleAdd(ActionEvent e) {
        String name = textName.getText().trim();
        String position = textPosition.getText().trim();
        String department = (String) comboDepartment.getSelectedItem();
        String contact = textContact.getText().trim();
        String salaryStr = textSalary.getText().trim();

        if (name.isEmpty() || position.isEmpty() || department == null || contact.isEmpty() || salaryStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        double salary;
        try {
            salary = Double.parseDouble(salaryStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Salary must be a valid number.");
            return;
        }

        try {
            conn c = new conn();
            Connection connection = c.getConnection();
            String query = "INSERT INTO employee_info (name, position, department, contact, salary) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, name);
            ps.setString(2, position);
            ps.setString(3, department);
            ps.setString(4, contact);
            ps.setDouble(5, salary);

            int rows = ps.executeUpdate();
            ps.close();
            connection.close();

            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Employee added successfully!");
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add employee.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }

    private void clearFields() {
        textName.setText("");
        textPosition.setText("");
        comboDepartment.setSelectedIndex(0);
        textContact.setText("");
        textSalary.setText("");
    }

    public static void showDialog(Component parent) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(parent), "Add Employee", true);
        AddEmployeeInfo panel = new AddEmployeeInfo();
        dialog.setContentPane(panel);
        dialog.setSize(400, 350);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }
}
