package icetask4;


import javax.swing.*;
import java.awt.*;


public class Createuserframe extends JFrame {

    private JTextField  newUsernameField;
    private JTextField  newPinField;
    private JComboBox<String> roleCombo;
    private JLabel      statusLabel;
    private String      adminUsername;

    public Createuserframe(String adminUsername) {
        this.adminUsername = adminUsername;

        setTitle("Create New User");
        setSize(380, 320);
        setLocationRelativeTo(null);
        setResizable(false);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        buildUI();
        setVisible(true);
    }

    private void buildUI() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(30, 30, 40));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 6, 7, 6);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("➕ Create New User", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(styledLabel("Username:"), gbc);

        newUsernameField = new JTextField(14);
        styleField(newUsernameField);
        gbc.gridx = 1;
        panel.add(newUsernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(styledLabel("PIN:"), gbc);

        newPinField = new JTextField(14);
        styleField(newPinField);
        gbc.gridx = 1;
        panel.add(newPinField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(styledLabel("Role:"), gbc);

        roleCombo = new JComboBox<>(new String[]{"1 - Admin", "2 - Staff", "3 - Visitor"});
        roleCombo.setBackground(new Color(50, 50, 65));
        roleCombo.setForeground(Color.WHITE);
        gbc.gridx = 1;
        panel.add(roleCombo, gbc);

        JButton createBtn = new JButton("Create User");
        createBtn.setBackground(new Color(0, 160, 80));
        createBtn.setForeground(Color.WHITE);
        createBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        createBtn.setFocusPainted(false);
        createBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        panel.add(createBtn, gbc);


        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setForeground(new Color(80, 200, 80));
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        gbc.gridy = 5;
        panel.add(statusLabel, gbc);

        createBtn.addActionListener(e -> createUser());

        add(panel);
    }

   
    private void createUser() {
        String newUsername = newUsernameField.getText().trim();
        String newPin      = newPinField.getText().trim();
        int    role        = roleCombo.getSelectedIndex() + 1;  

        
        if (newUsername.isEmpty() || newPin.isEmpty()) {
            statusLabel.setForeground(new Color(255, 80, 80));
            statusLabel.setText("❌ Username and PIN cannot be blank.");
            return;
        }

        
        if (DataStorage.findUser(newUsername) != -1) {
            statusLabel.setForeground(new Color(255, 150, 0));
            statusLabel.setText("⚠️ Username '" + newUsername + "' already exists.");
            return;
        }

        
        DataStorage.addUser(newUsername, newPin, role);

        
        String ts = DataStorage.timestamps.get(DataStorage.usernames.size() - 1)
                        .toString().replace("T", " at ").substring(0, 22);

        statusLabel.setForeground(new Color(80, 200, 80));
        statusLabel.setText("✅ User '" + newUsername + "' created at " + ts);

        
        newUsernameField.setText("");
        newPinField.setText("");
        roleCombo.setSelectedIndex(1);  
    }


    private JLabel styledLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(Color.LIGHT_GRAY);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
        return lbl;
    }

    private void styleField(JTextField field) {
        field.setBackground(new Color(50, 50, 65));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(80, 80, 120)),
            BorderFactory.createEmptyBorder(4, 6, 4, 6)
        ));
    }
}
