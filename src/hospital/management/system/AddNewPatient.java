package hospital.management.system;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Vector;

public class AddNewPatient extends JPanel {
    private JTextField tName,tAge,tContact,tCitizen,tAddress,tCondition,tDeposit;
    private JComboBox<String> cGender,cRoom;
    private JLabel priceLbl,statusLbl,dateLbl,idLbl;
    private JButton btnAdd,btnRefresh;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public AddNewPatient(){
        setPreferredSize(new Dimension(600,600));
        setLayout(null);
        setBackground(new Color(247,247,250));

        JPanel card=new JPanel(null);
        card.setBounds(0,0,600,600);
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(new Color(229,231,235),1),
                BorderFactory.createEmptyBorder(40,40,40,40)));
        add(card);

        JLabel title=new JLabel("Add New Patient");
        title.setFont(new Font("Segoe UI",Font.BOLD,24));
        title.setBounds(20,10,300,30);
        card.add(title);

        idLbl=new JLabel("Patient ID : "+nextId());
        idLbl.setBounds(330,10,200,30);
        idLbl.setFont(new Font("Segoe UI",Font.BOLD,15));
        idLbl.setForeground(new Color(120,120,120));
        card.add(idLbl);

        int y=60;
        tName       =field(card,"Full Name:",y);           y+=40;
        cGender     =combo(card,"Gender:",new String[]{"Male","Female","Other"},y); y+=40;
        tAge        =field(card,"Age:",y);                 y+=40;
        tContact    =field(card,"Contact:",y);             y+=40;
        tCitizen    =field(card,"Citizenship No.:",y);     y+=40;

        JLabel rlbl=new JLabel("Room No.:"); rlbl.setBounds(20,y,120,25); card.add(rlbl);
        cRoom=new JComboBox<>(); cRoom.setBounds(150,y,180,25); card.add(cRoom);

        statusLbl=new JLabel(); statusLbl.setBounds(340,y,100,25); card.add(statusLbl);
        JLabel plbl=new JLabel("Price:"); plbl.setBounds(20,y+30,120,25); card.add(plbl);
        priceLbl=new JLabel(); priceLbl.setBounds(150,y+30,180,25); card.add(priceLbl);

        loadRooms();
        cRoom.addActionListener(e->roomDetails());

        y+=70;
        tAddress    =field(card,"Address:",y);             y+=40;
        tCondition  =field(card,"Disease/Condition:",y);   y+=40;
        tDeposit    =field(card,"Deposit:",y);             y+=45;

        JLabel dlbl=new JLabel("Date & Time:"); dlbl.setBounds(20,y,120,25); card.add(dlbl);
        dateLbl=new JLabel(now()); dateLbl.setBounds(150,y,180,25); card.add(dateLbl);
        btnRefresh=new JButton("Refresh"); btnRefresh.setBounds(340,y,90,25);
        btnRefresh.addActionListener(e->dateLbl.setText(now())); card.add(btnRefresh);

        y+=40;
        btnAdd=new JButton("Add"); btnAdd.setBounds(200,y,120,35);
        btnAdd.setBackground(new Color(59,130,246)); btnAdd.setForeground(Color.WHITE);
        btnAdd.addActionListener(e->savePatient()); card.add(btnAdd);
    }

    private JTextField field(JPanel p,String txt,int y){
        JLabel l=new JLabel(txt); l.setBounds(20,y,120,25); p.add(l);
        JTextField f=new JTextField(); f.setBounds(150,y,280,25); p.add(f); return f;
    }
    private JComboBox<String> combo(JPanel p,String txt,String[] items,int y){
        JLabel l=new JLabel(txt); l.setBounds(20,y,120,25); p.add(l);
        JComboBox<String> cb=new JComboBox<>(items); cb.setBounds(150,y,180,25); p.add(cb); return cb;
    }

    private void loadRooms(){
        try{
            conn c=new conn();
            Statement st=c.getConnection().createStatement();
            ResultSet rs=st.executeQuery("SELECT room_no FROM room WHERE availability=true");
            cRoom.removeAllItems();
            Vector<String> list=new Vector<>();
            while(rs.next()) list.add(rs.getString(1));
            if(list.isEmpty()) cRoom.addItem("No Room");
            else for(String r:list) cRoom.addItem(r);
            rs.close(); st.close(); c.getConnection().close();
            roomDetails();
        }catch(Exception ex){System.out.println(ex);}
    }
    private void roomDetails(){
        String room=(String)cRoom.getSelectedItem();
        if(room==null||room.equals("No Room")){priceLbl.setText("");statusLbl.setText("");return;}
        try{
            conn c=new conn();
            Statement st=c.getConnection().createStatement();
            ResultSet rs=st.executeQuery("SELECT price,availability FROM room WHERE room_no='"+room+"'");
            if(rs.next()){
                priceLbl.setText(rs.getString("price"));
                boolean avail=rs.getBoolean("availability");
                statusLbl.setText(avail?"Available":"Occupied");
                statusLbl.setForeground(avail?new Color(0,128,0):Color.RED);
            }
            rs.close(); st.close(); c.getConnection().close();
        }catch(Exception ex){System.out.println(ex);}
    }

    private int nextId(){
        try{
            conn c=new conn();
            Statement st=c.getConnection().createStatement();
            ResultSet rs=st.executeQuery("SELECT COUNT(*) FROM patient_info");
            rs.next(); int id=rs.getInt(1)+1;
            rs.close(); st.close(); c.getConnection().close(); return id;
        }catch(Exception ex){return 1;}
    }

    private String now(){ return LocalDateTime.now().format(FMT); }

    private void savePatient(){
        String room=(String)cRoom.getSelectedItem();
        if(room==null||room.equals("No Room")){
            JOptionPane.showMessageDialog(this,"No available room."); return;
        }
        try{
            conn c=new conn();
            Connection con=c.getConnection();
            con.setAutoCommit(false);

            PreparedStatement chk=con.prepareStatement("SELECT availability FROM room WHERE room_no=?");
            chk.setString(1,room); ResultSet rs=chk.executeQuery();
            if(!rs.next()||!rs.getBoolean(1)){JOptionPane.showMessageDialog(this,"Room now occupied.");con.rollback();return;}

            PreparedStatement ins=con.prepareStatement(
                    "INSERT INTO patient_info(name,gender,age,contact,citizen_no,room_no,address,disease_condition,deposit,time_date) VALUES(?,?,?,?,?,?,?,?,?,?)");
            ins.setString(1,tName.getText());
            ins.setString(2,(String)cGender.getSelectedItem());
            ins.setString(3,tAge.getText());
            ins.setString(4,tContact.getText());
            ins.setString(5,tCitizen.getText());
            ins.setString(6,room);
            ins.setString(7,tAddress.getText());
            ins.setString(8,tCondition.getText());
            ins.setString(9,tDeposit.getText());
            ins.setString(10,now());
            ins.executeUpdate();

            PreparedStatement upd=con.prepareStatement("UPDATE room SET availability=false WHERE room_no=?");
            upd.setString(1,room); upd.executeUpdate();

            con.commit();
            JOptionPane.showMessageDialog(this,"Patient added.");
            clear(); loadRooms(); idLbl.setText("Patient ID : "+nextId());
        }catch(Exception ex){ex.printStackTrace();}
    }
    private void clear(){
        tName.setText(""); tAge.setText(""); tContact.setText("");
        tCitizen.setText(""); tAddress.setText(""); tCondition.setText(""); tDeposit.setText("");
        cGender.setSelectedIndex(0); dateLbl.setText(now());
    }
}
