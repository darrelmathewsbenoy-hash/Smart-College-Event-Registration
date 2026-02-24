import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class RegisteredEventsPage extends JFrame {

    public RegisteredEventsPage() {

        setTitle("Registered Events");
        setSize(650,500);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        JLabel heading = new JLabel("Registered Events");
        heading.setBounds(180,30,300,40);
        heading.setFont(new Font("Arial",Font.BOLD,26));
        heading.setForeground(new Color(139,0,0));
        add(heading);

        int y = 100;

        for(String fullData : HomePage.registeredEvents) {

            JLabel eventLabel = new JLabel(fullData);
            eventLabel.setBounds(120,y,250,40);
            eventLabel.setFont(new Font("Arial",Font.BOLD,14));
            add(eventLabel);

            JButton deleteBtn = new JButton("DELETE FROM DB");
            deleteBtn.setBounds(380,y,150,40);
            deleteBtn.setBackground(Color.RED);
            deleteBtn.setForeground(Color.WHITE);
            add(deleteBtn);

            deleteBtn.addActionListener(e -> deleteEvent(fullData));

            y += 60;
        }

        JButton back = new JButton("Back");
        back.setBounds(20,400,80,30);
        add(back);

        back.addActionListener(e -> {
            new HomePage();
            dispose();
        });

        setVisible(true);
    }

    private void deleteEvent(String fullData) {

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "CANCEL EVENT?",
                "Confirm",
                JOptionPane.YES_NO_OPTION
        );

        if(confirm == JOptionPane.YES_OPTION) {

            try {
                Connection con = DBConnection.getConnection();

                // Split event name and email
                String[] parts = fullData.split(" - ");
                String eventName = parts[0];
                String email = parts[1];

                String table = eventName.toLowerCase()
                        .replace(" ","_")
                        .replace("-","");

                String query = "DELETE FROM " + table + " WHERE email = ?";

                PreparedStatement pst = con.prepareStatement(query);
                pst.setString(1, email);
                pst.executeUpdate();

                // Remove from UI list
                HomePage.registeredEvents.remove(fullData);

                JOptionPane.showMessageDialog(this,"EVENT CANCELLED");

                dispose();
                new RegisteredEventsPage();

            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
