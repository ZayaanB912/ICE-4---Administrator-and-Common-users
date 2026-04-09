package icetask4;


import javax.swing.*;
import java.awt.*;


public class Menuframe extends JFrame {

    private String username;
    private int    role;

    public Menuframe(String username, int role) {
        this.username = username;
        this.role     = role;

        setTitle("Security System - Welcome, " + username);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 350);
        setLocationRelativeTo(null);
        setResizable(false);

        buildUI();
        setVisible(true);
    }

    private void buildUI() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(30, 30, 40));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        
        String roleLabel = DataStorage.roleName(role);
        JLabel header = new JLabel("Welcome, " + username + "  |  Role: " + roleLabel,
                                   SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 15));
        header.setForeground(Color.WHITE);
        panel.add(header, BorderLayout.NORTH);

        
        JPanel buttonPanel = new JPanel(new GridLayout(0, 1, 8, 8));
        buttonPanel.setBackground(new Color(30, 30, 40));

        
        switch (role) {
            case 1:  
                buttonPanel.add(makeButton("👤  Create New User",   () -> new Createuserframe(username)));
                buttonPanel.add(makeButton("📋  View All Users",    () -> new Viewusersframe()));
                break;

            case 2:  
                JLabel staffMsg = centreLabel("✅  Staff access granted.\nYou may access the server room.");
                buttonPanel.add(staffMsg);
                break;

            case 3:  
                JLabel visitorMsg = centreLabel("👁  Visitor access granted.\nYou may access the lobby only.");
                buttonPanel.add(visitorMsg);
                break;

            default:
                buttonPanel.add(centreLabel("⚠️  Unknown role."));
                break;
        }

        buttonPanel.add(makeButton("🚪  Logout", () -> {
            new Loginframe();
            dispose();
        }));

        panel.add(buttonPanel, BorderLayout.CENTER);
        add(panel);
    }

    private JButton makeButton(String text, Runnable action) {
        JButton btn = new JButton(text);
        btn.setBackground(new Color(0, 100, 180));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        btn.addActionListener(e -> action.run());
        return btn;
    }


    private JLabel centreLabel(String text) {
        JLabel lbl = new JLabel("<html><div style='text-align:center;'>"
                                + text.replace("\n", "<br>") + "</div></html>",
                                SwingConstants.CENTER);
        lbl.setForeground(new Color(180, 220, 180));
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
        return lbl;
    }
}
