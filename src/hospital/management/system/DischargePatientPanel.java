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
        gbc.insets  = new Insets(10, 10, 10, 10);
        gbc.anchor  = GridBagConstraints.WEST;

        JLabel title = new JLabel("Discharge Patient");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(17, 24, 39));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(title, gbc);

        JLabel selLbl = new JLabel("Select Patient:");
        selLbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridy = 1; gbc.gridwidth = 1;
        add(selLbl, gbc);

        patientCombo = new JComboBox<>(loadPatients());
        patientCombo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 1;
        add(patientCombo, gbc);

        JButton dischargeBtn = new JButton("Discharge");
        dischargeBtn.setBackground(new Color(234, 88, 12));
        dischargeBtn.setForeground(Color.WHITE);
        dischargeBtn.setFocusPainted(false);
        dischargeBtn.addActionListener(e -> dischargePatient());

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

    /* === fetch patients === */
    private String[] loadPatients() {
        Vector<String> list = new Vector<>();
        try (conn c = new conn();
             Statement st = c.getConnection().createStatement();
             ResultSet rs = st.executeQuery("SELECT patient_id, name FROM patient_info ORDER BY name")) {

            while (rs.next()) {
                list.add(rs.getInt("patient_id") + " - " + rs.getString("name"));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading patients: " + ex.getMessage());
        }
        return list.toArray(new String[0]);
    }

    private void dischargePatient() {
        if (patientCombo.getSelectedIndex() == -1) return;

        String sel = (String) patientCombo.getSelectedItem();
        int id = Integer.parseInt(sel.split(" - ")[0]);

        int confirm = JOptionPane.showConfirmDialog(
                this, "Discharge patient ID " + id + "?",
                "Confirm", JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        try (conn c = new conn()) {
            Connection con = c.getConnection();
            con.setAutoCommit(false);      // --- begin tx ---

            try (PreparedStatement psIns = con.prepareStatement(
                    "INSERT INTO discharge_info (patient_id, discharge_date, payment_done) VALUES (?, NOW(), false)")) {
                psIns.setInt(1, id);
                psIns.executeUpdate();
            }

            try (PreparedStatement psDel = con.prepareStatement(
                    "DELETE FROM patient_info WHERE patient_id = ?")) {
                psDel.setInt(1, id);
                int rows = psDel.executeUpdate();

                if (rows == 0) {
                    con.rollback();
                    JOptionPane.showMessageDialog(this, "Patient not found.");
                    return;
                }
            }

            con.commit();
            JOptionPane.showMessageDialog(this, "Patient discharged.");
            patientCombo.setModel(new DefaultComboBoxModel<>(loadPatients()));

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error discharging: " + ex.getMessage());
        }
    }

    private void printBill() {
        if (patientCombo.getSelectedIndex() == -1) return;
        String sel = (String) patientCombo.getSelectedItem();
        JOptionPane.showMessageDialog(this,
                "Printing bill for\n" + sel + "\n(placeholder)");
    }
}
