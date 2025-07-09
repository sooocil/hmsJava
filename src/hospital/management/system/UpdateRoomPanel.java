package hospital.management.system;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

class UpdateRoomPanel extends JPanel {
    private JComboBox<Integer> comboRoomNo;
    private JTextField txtType;
    private JCheckBox chkAvailable;
    private JButton btnSave;

    UpdateRoomPanel() {
        setLayout(null);
        setPreferredSize(new Dimension(400, 250));
        setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        JLabel lblTitle = new JLabel("Update Room");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setBounds(10, 10, 200, 30);
        add(lblTitle);

        int y=60;
        comboRoomNo = new JComboBox<>(loadRoomIds());
        addLabelAndComponent("Room No:", comboRoomNo, y); y+=40;

        txtType = new JTextField();
        addLabelAndComponent("Room Type:", txtType, y); y+=40;

        chkAvailable = new JCheckBox("Available");
        chkAvailable.setBounds(120, y, 120, 25);
        add(chkAvailable);

        btnSave = new JButton("Save");
        btnSave.setBounds(150, y+40, 100, 35);
        btnSave.setBackground(new Color(59,130,246));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);
        btnSave.addActionListener(e->saveRoom());
        add(btnSave);

        comboRoomNo.addActionListener(e->loadRoomData());
        if(comboRoomNo.getItemCount()>0) loadRoomData();
    }

    private Integer[] loadRoomIds(){
        Vector<Integer> ids=new Vector<>();
        try{
            conn c=new conn();
            Statement st=c.getConnection().createStatement();
            ResultSet rs=st.executeQuery("SELECT room_no FROM room ORDER BY room_no");
            while(rs.next()) ids.add(rs.getInt("room_no"));
            rs.close();st.close();c.getConnection().close();
        }catch(Exception ex){JOptionPane.showMessageDialog(this,"Error: "+ex.getMessage());}
        return ids.toArray(new Integer[0]);
    }

    private void loadRoomData(){
        if(comboRoomNo.getSelectedIndex()==-1) return;
        int no=(Integer)comboRoomNo.getSelectedItem();
        try{
            conn c=new conn();
            PreparedStatement ps=c.getConnection().prepareStatement("SELECT * FROM room WHERE room_no=?");
            ps.setInt(1,no);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                txtType.setText(rs.getString("room_type"));
                chkAvailable.setSelected(rs.getBoolean("availability"));
            }
            rs.close();ps.close();c.getConnection().close();
        }catch(Exception ex){JOptionPane.showMessageDialog(this,"Error: "+ex.getMessage());}
    }

    private void saveRoom(){
        if(comboRoomNo.getSelectedIndex()==-1){JOptionPane.showMessageDialog(this,"No room selected");return;}
        try{
            int no=(Integer)comboRoomNo.getSelectedItem();
            conn c=new conn();
            PreparedStatement ps=c.getConnection().prepareStatement("UPDATE room SET room_type=?, availability=? WHERE room_no=?");
            ps.setString(1,txtType.getText());
            ps.setBoolean(2,chkAvailable.isSelected());
            ps.setInt(3,no);
            int rows = ps.executeUpdate();
            ps.close();c.getConnection().close();
            JOptionPane.showMessageDialog(this,rows>0?"Room updated":"Update failed");
        }catch(Exception ex){JOptionPane.showMessageDialog(this,"Error: "+ex.getMessage());}
    }

    private void addLabelAndComponent(String text,JComponent comp,int y){
        JLabel lbl = new JLabel(text);
        lbl.setBounds(10,y,100,25); add(lbl);
        comp.setBounds(120,y,200,25); add(comp);
    }
}
