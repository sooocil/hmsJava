package hospital.management.system;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.sql.*;
import java.util.Vector;


public class DischargePatientPanel extends JPanel {
    private JComboBox<String> patientCombo;

    public DischargePatientPanel() {
        setBackground(new Color(247, 247, 250));
        setLayout(new GridBagLayout());

        setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // title
        JLabel title = new JLabel("Discharge Patient");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(17, 24, 39));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(title, gbc);

        // selector label
        JLabel selLbl = new JLabel("Select Patient:");
        selLbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridy = 1; gbc.gridwidth = 1;
        add(selLbl, gbc);

        // combo box with patients
        patientCombo = new JComboBox<>(loadPatients());
        patientCombo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 1;
        add(patientCombo, gbc);

        // discharge button
        JButton dischargeBtn = new JButton("Discharge");
        dischargeBtn.setBackground(new Color(234, 88, 12));
        dischargeBtn.setForeground(Color.WHITE);
        dischargeBtn.setFocusPainted(false);
        dischargeBtn.addActionListener(e -> dischargePatient());

        // print bill button
        JButton printBtn = new JButton("Print Bill");
        printBtn.setBackground(new Color(59, 130, 246));
        printBtn.setForeground(Color.WHITE);
        printBtn.setFocusPainted(false);
        printBtn.addActionListener(e -> printBill());

        gbc.gridx = 0; gbc.gridy = 2;
        add(dischargeBtn, gbc);
        gbc.gridx = 1;
        add(printBtn, gbc);
    }

    /**
     * Queries DB and returns array of "<patient_id> - <name>" strings.
     */
    private String[] loadPatients() {
        Vector<String> list = new Vector<>();
        try {
            conn c = new conn();
            Statement st = c.getConnection().createStatement();
            ResultSet rs = st.executeQuery("SELECT patient_id, name FROM patient_info ORDER BY name");
            while (rs.next()) {
                list.add(rs.getInt("patient_id") + " - " + rs.getString("name"));
            }
            rs.close(); st.close(); c.getConnection().close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading patients: " + ex.getMessage());
        }
        return list.toArray(new String[0]);
    }

    /**
     * Deletes selected patient from DB.
     */
    private void dischargePatient() {
        if (patientCombo.getSelectedIndex() == -1) return;
        String sel = (String) patientCombo.getSelectedItem();
        int id = Integer.parseInt(sel.split(" - ")[0]);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Discharge patient ID " + id + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            conn c = new conn();
            PreparedStatement ps = c.getConnection().prepareStatement(
                    "DELETE FROM patient_info WHERE patient_id = ?");
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            ps.close(); c.getConnection().close();

            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Patient discharged.");
                // refresh list
                patientCombo.setModel(new DefaultComboBoxModel<>(loadPatients()));
            } else {
                JOptionPane.showMessageDialog(this, "Patient not found.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error discharging: " + ex.getMessage());
        }
    }

    /**
     * Simple stub for bill printout.
     */
    private void printBill() {
        if (patientCombo.getSelectedIndex() == -1) return;
        String sel = (String) patientCombo.getSelectedItem();
        JOptionPane.showMessageDialog(this, "Printing bill for \n" + sel + "\n(placeholder)");
    }
}
