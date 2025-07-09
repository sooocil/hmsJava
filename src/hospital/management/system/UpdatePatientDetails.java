package hospital.management.system;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Vector;

public class UpdatePatientDetails extends JPanel {
    private static final DateTimeFormatter DT_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private JComboBox<Integer> comboPatientId;
    private JTextField textName, textAge, textContact, textCitizen, textAddress, textCondition, textDeposit;
    private JComboBox<String> comboGender;
    private JComboBox<Integer> comboRoom;
    private JLabel labelDate;
    private JButton buttonUpdate, buttonRefreshDate;

    public UpdatePatientDetails() {
        setLayout(null);
        setBackground(new Color(247, 247, 250));

        // Card layout with fixed size
        JPanel card = new JPanel(null);
        card.setBounds(0, 0, 620, 640);
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        add(card);

        // Title
        JLabel title = new JLabel("Update Patient Details");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(17, 24, 39));
        title.setBounds(20, 10, 350, 30);
        card.add(title);

        int y = 60;
        comboPatientId = new JComboBox<>(loadPatientIds());
        addLabelAndComponent(card, "Patient ID:", comboPatientId, y); y += 40;
        comboPatientId.addActionListener(e -> loadPatientData());

        textName    = addLabelAndField(card, "Full Name:", y);            y += 40;
        comboGender = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        addLabelAndComponent(card, "Gender:", comboGender, y);            y += 40;
        textAge     = addLabelAndField(card, "Age:", y);                  y += 40;
        textContact = addLabelAndField(card, "Contact:", y);             y += 40;
        textCitizen = addLabelAndField(card, "Citizenship No.:", y);     y += 40;
        comboRoom   = new JComboBox<>(new Integer[]{100, 203, 200, 205, 300, 120});
        addLabelAndComponent(card, "Room No.:", comboRoom, y);           y += 40;
        textAddress   = addLabelAndField(card, "Address:", y);           y += 40;
        textCondition = addLabelAndField(card, "Disease/Condition:", y); y += 40;
        textDeposit   = addLabelAndField(card, "Deposit:", y);           y += 40;

        // Date label and refresh button
        JLabel lblDate = new JLabel("Date & Time:");
        lblDate.setBounds(20, y, 120, 25);
        card.add(lblDate);

        labelDate = new JLabel(now());
        labelDate.setBounds(150, y, 200, 25);
        card.add(labelDate);

        buttonRefreshDate = new JButton("Refresh");
        buttonRefreshDate.setBounds(370, y, 90, 25);
        buttonRefreshDate.setFocusPainted(false);
        buttonRefreshDate.addActionListener(e -> labelDate.setText(now()));
        card.add(buttonRefreshDate);

        // Update button
        y += 50;
        buttonUpdate = new JButton("Update");
        buttonUpdate.setBounds(230, y, 140, 40);
        buttonUpdate.setBackground(new Color(59, 130, 246));
        buttonUpdate.setForeground(Color.WHITE);
        buttonUpdate.setFont(new Font("Segoe UI", Font.BOLD, 14));
        buttonUpdate.setFocusPainted(false);
        buttonUpdate.addActionListener(e -> updatePatient());
        card.add(buttonUpdate);

        // Pre-fill first patient if available
        if (comboPatientId.getItemCount() > 0) {
            loadPatientData();
        }
    }

    private String now() {
        return LocalDateTime.now().format(DT_FMT);
    }

    private Integer[] loadPatientIds() {
        Vector<Integer> ids = new Vector<>();
        try {
            conn c = new conn();
            Statement stmt = c.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT patient_id FROM patient_info ORDER BY patient_id");
            while (rs.next()) ids.add(rs.getInt("patient_id"));
            rs.close(); stmt.close(); c.getConnection().close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading IDs: " + ex.getMessage());
        }
        return ids.toArray(new Integer[0]);
    }

    private void loadPatientData() {
        if (comboPatientId.getSelectedIndex() == -1) return;
        int id = (Integer) comboPatientId.getSelectedItem();

        try {
            conn c = new conn();
            PreparedStatement ps = c.getConnection().prepareStatement(
                    "SELECT * FROM patient_info WHERE patient_id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                textName.setText(rs.getString("name"));
                comboGender.setSelectedItem(rs.getString("gender"));
                textAge.setText(rs.getString("age"));
                textContact.setText(rs.getString("contact"));
                textCitizen.setText(rs.getString("citizen_no"));
                comboRoom.setSelectedItem(rs.getInt("room_no"));
                textAddress.setText(rs.getString("address"));
                textCondition.setText(rs.getString("disease_condition"));
                textDeposit.setText(rs.getString("deposit"));
                labelDate.setText(rs.getString("time_date"));
            }
            rs.close(); ps.close(); c.getConnection().close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading patient: " + ex.getMessage());
        }
    }

    private void updatePatient() {
        if (comboPatientId.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "No patient selected.");
            return;
        }

        int id = (Integer) comboPatientId.getSelectedItem();

        try {
            conn c = new conn();
            PreparedStatement ps = c.getConnection().prepareStatement(
                    "UPDATE patient_info SET name=?, gender=?, age=?, contact=?, citizen_no=?, room_no=?, address=?, disease_condition=?, deposit=?, time_date=? WHERE patient_id=?"
            );
            ps.setString(1, textName.getText());
            ps.setString(2, (String) comboGender.getSelectedItem());
            ps.setString(3, textAge.getText());
            ps.setString(4, textContact.getText());
            ps.setString(5, textCitizen.getText());
            ps.setInt(6, (Integer) comboRoom.getSelectedItem());
            ps.setString(7, textAddress.getText());
            ps.setString(8, textCondition.getText());
            ps.setString(9, textDeposit.getText());
            ps.setString(10, labelDate.getText());
            ps.setInt(11, id);
            int rows = ps.executeUpdate();
            ps.close(); c.getConnection().close();

            JOptionPane.showMessageDialog(this, rows > 0 ? "Patient updated successfully." : "Update failed.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error updating: " + ex.getMessage());
        }
    }

    private JTextField addLabelAndField(JPanel p, String txt, int y) {
        JLabel l = new JLabel(txt); l.setBounds(20, y, 120, 25); p.add(l);
        JTextField tf = new JTextField(); tf.setBounds(150, y, 300, 25); p.add(tf);
        return tf;
    }

    private void addLabelAndComponent(JPanel p, String txt, JComponent comp, int y) {
        JLabel l = new JLabel(txt); l.setBounds(20, y, 120, 25); p.add(l);
        comp.setBounds(150, y, 300, 25); p.add(comp);
    }

    /** Call this from another class to open as a modal window */
    public static void showDialog(Component parent) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(parent), "Update Patient", true);
        dialog.setContentPane(new UpdatePatientDetails());
        dialog.setSize(620, 640);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }
}
