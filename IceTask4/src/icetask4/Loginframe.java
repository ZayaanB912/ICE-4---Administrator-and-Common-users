package icetask4;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;


public class Loginframe extends JFrame {


    private JTextField     usernameField;
    private JPasswordField pinField;
    private JLabel         statusLabel;
    private JButton        loginButton;

    
    private static final int MAX_ATTEMPTS    = 3;
    private static final int LOCKOUT_MINUTES = 3;


    public Loginframe() {
        setTitle("Data Centre Security System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 280);
        setLocationRelativeTo(null);   // centre on screen
        setResizable(false);

        buildUI();
        setVisible(true);
    }


    private void buildUI() {

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        panel.setBackground(new Color(30, 30, 40));   

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets  = new Insets(6, 6, 6, 6);
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.anchor  = GridBagConstraints.WEST;

        
        JLabel title = new JLabel("🔒 Data Centre Login", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(title, gbc);

        
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(styledLabel("Username:"), gbc);

        usernameField = new JTextField(15);
        styleTextField(usernameField);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(styledLabel("PIN:"), gbc);

        pinField = new JPasswordField(15);
        styleTextField(pinField);
        gbc.gridx = 1;
        panel.add(pinField, gbc);

        
        loginButton = new JButton("Login");
        loginButton.setBackground(new Color(0, 120, 215));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        loginButton.setFocusPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(loginButton, gbc);

        
        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setForeground(new Color(255, 80, 80));
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        gbc.gridy = 4;
        panel.add(statusLabel, gbc);

        
        loginButton.addActionListener(e -> attemptLogin());

        
        pinField.addActionListener(e -> attemptLogin());

        add(panel);
    }

    
    private void attemptLogin() {
        String username = usernameField.getText().trim();
        String pin      = new String(pinField.getPassword()).trim();

        
        int index = DataStorage.findUser(username);

        
        if (index == -1) {
            statusLabel.setText("❌ Invalid username or PIN.");
            pinField.setText("");
            return;
        }

        
        if (DataStorage.locked.get(index)) {
            statusLabel.setText("🔒 Account locked. Please wait " + LOCKOUT_MINUTES + " min.");
            pinField.setText("");
            return;
        }

        // ── Step 4: Wrong PIN ──
        if (!DataStorage.pins.get(index).equals(pin)) {
            int fails = DataStorage.failCounts.get(index) + 1; 
            DataStorage.failCounts.set(index, fails);

            int remaining = MAX_ATTEMPTS - fails;

            if (fails >= MAX_ATTEMPTS) {
                
                DataStorage.locked.set(index, true);
                statusLabel.setForeground(new Color(255, 60, 60));
                statusLabel.setText("🔒 System Locked! Too many failed attempts.");
                loginButton.setEnabled(false);
                startLockoutTimer(index);
            } else {
                
                statusLabel.setForeground(new Color(255, 150, 0));
                statusLabel.setText("⚠️ Wrong PIN. " + remaining + " attempt(s) left.");
            }

            pinField.setText("");
            return;
        }

        DataStorage.failCounts.set(index, 0);
        statusLabel.setForeground(new Color(80, 200, 80));
        statusLabel.setText("✅ Login successful!");

        
        int role = DataStorage.roles.get(index);
        new Menuframe(username, role);
        dispose();
    }


    private void startLockoutTimer(int userIndex) {
        long delayMs = LOCKOUT_MINUTES * 60 * 1000L;  

        Timer lockoutTimer = new Timer();
        lockoutTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                
                DataStorage.locked.set(userIndex, false);
                DataStorage.failCounts.set(userIndex, 0);

                
                SwingUtilities.invokeLater(() -> {
                    statusLabel.setForeground(new Color(80, 200, 80));
                    statusLabel.setText("✅ Lockout expired. You may try again.");
                    loginButton.setEnabled(true);
                });

                lockoutTimer.cancel();  
            }
        }, delayMs);
    }

    
    private JLabel styledLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(Color.LIGHT_GRAY);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
        return lbl;
    }

    
    private void styleTextField(JTextField field) {
        field.setBackground(new Color(50, 50, 65));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(80, 80, 120)),
            BorderFactory.createEmptyBorder(4, 6, 4, 6)
        ));
    }
}
