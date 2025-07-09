package hospital.management.system;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.Timer;
import java.util.TimerTask;


public class DashboardPanel extends JPanel {

    private JLabel lblTotalPatients   = metricLabel();
    private JLabel lblAdmittedToday   = metricLabel();
    private JLabel lblDischargedToday = metricLabel();
    private JLabel lblAvailableRooms  = metricLabel();
    private JLabel lblMostUsedRoom    = metricLabel();
    private JLabel lblTodayDeposit    = metricLabel();

    public DashboardPanel() {
        setBackground(new Color(247, 247, 250));
        setLayout(new BorderLayout(0, 20));
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel title = new JLabel("Reception Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(17, 24, 39));
        add(title, BorderLayout.NORTH);

        /*=== grid of six info cards ===*/
        JPanel grid = new JPanel(new GridLayout(2, 3, 20, 20));
        grid.setOpaque(false);
        add(grid, BorderLayout.CENTER);

        grid.add(createCard("Total Patients", lblTotalPatients, new Color(59, 130, 246)));
        grid.add(createCard("Admitted Today", lblAdmittedToday, new Color(34, 197, 94)));
        grid.add(createCard("Discharged Today", lblDischargedToday, new Color(239, 68, 68)));
        grid.add(createCard("Available Rooms", lblAvailableRooms, new Color(234, 179, 8)));
        grid.add(createCard("Most Used Room", lblMostUsedRoom, new Color(147, 51, 234)));
        grid.add(createCard("Today's Deposit", lblTodayDeposit, new Color(16, 185, 129)));

        /* refresh button */
        JButton refreshBtn = new JButton("Refresh Now");
        refreshBtn.setBackground(new Color(99, 102, 241));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFocusPainted(false);
        refreshBtn.setBorder(new EmptyBorder(10, 20, 10, 20));
        refreshBtn.addActionListener(e -> refreshMetrics());
        add(refreshBtn, BorderLayout.SOUTH);

        refreshMetrics();
//        new Timer(true).schedule(new TimerTask() { public void run() { SwingUtilities.invokeLater(() -> refreshMetrics()); } }, 60000, 60000);
    }

    /* ===== helper to build a metric card ===== */
    private JPanel createCard(String title, JLabel valueLbl, Color accent) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(new LineBorder(new Color(229, 231, 235), 1), new EmptyBorder(20, 20, 20, 20)));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        titleLbl.setForeground(new Color(75, 85, 99));

        valueLbl.setForeground(accent);

        card.add(titleLbl, BorderLayout.NORTH);
        card.add(valueLbl, BorderLayout.CENTER);
        return card;
    }

    private JLabel metricLabel() {
        JLabel l = new JLabel("-");
        l.setFont(new Font("Segoe UI", Font.BOLD, 26));
        l.setHorizontalAlignment(SwingConstants.LEFT);
        return l;
    }

    /* ===== fetch & display metrics ===== */
    private void refreshMetrics() {
        lblTotalPatients.setText(String.valueOf(fetchTotalPatients()));
        lblAdmittedToday.setText(String.valueOf(fetchAdmittedToday()));
        lblDischargedToday.setText(String.valueOf(fetchDischargedToday()));
        lblAvailableRooms.setText(String.valueOf(fetchAvailableRooms()));
        lblMostUsedRoom.setText(fetchMostUsedRoom());
        lblTodayDeposit.setText("Rs. " + fetchTodayDeposit());
    }

    private int fetchTotalPatients() {
        String sql = "SELECT COUNT(*) FROM patient_info";
        return executeIntQuery(sql);
    }

    private int fetchAdmittedToday() {
        LocalDate today = LocalDate.now();
        String sql = "SELECT COUNT(*) FROM patient_info WHERE DATE(time_date) = '" + today + "'";
        return executeIntQuery(sql);
    }

    private int fetchDischargedToday() {
        LocalDate today = LocalDate.now();
        // adjust table/column names as per your schema
        String sql = "SELECT COUNT(*) FROM discharge_info WHERE DATE(discharge_date) = '" + today + "'";
        return executeIntQuery(sql);
    }

    private int fetchAvailableRooms() {
        String sql = "SELECT COUNT(*) FROM room WHERE availability = true";
        return executeIntQuery(sql);
    }

    private String fetchMostUsedRoom() {
        String sql = "SELECT room_no, COUNT(*) AS usageCount FROM patient_info GROUP BY room_no ORDER BY usageCount DESC LIMIT 1";
        try (conn c = new conn(); Statement st = c.getConnection().createStatement(); ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return "Room " + rs.getInt("room_no");
        } catch (Exception ex) { /* ignore – default "-" will show */ }
        return "-";
    }

    private String fetchTodayDeposit() {
        LocalDate today = LocalDate.now();
        String sql = "SELECT COALESCE(SUM(deposit), 0) AS total FROM patient_info WHERE DATE(time_date) = '" + today + "'";
        try (conn c = new conn(); Statement st = c.getConnection().createStatement(); ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return String.valueOf(rs.getBigDecimal("total"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "0";
    }

    /* small helper */
    private int executeIntQuery(String sql) {
        try (conn c = new conn(); Statement st = c.getConnection().createStatement(); ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception ex) {
            ex.printStackTrace();

        }
        return 0;
    }
}
