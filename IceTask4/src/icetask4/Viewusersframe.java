
package icetask4;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;


public class Viewusersframe extends JFrame {

    public Viewusersframe() {
        setTitle("All Users");
        setSize(620, 380);
        setLocationRelativeTo(null);
        setResizable(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        buildUI();
        setVisible(true);
    }

    private void buildUI() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(30, 30, 40));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel title = new JLabel("📋 All Registered Users", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setForeground(Color.WHITE);
        panel.add(title, BorderLayout.NORTH);

        String[] columns = {"#", "Username", "Role", "Status", "Created At"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;   // read-only table
            }
        };

        
        for (int i = 0; i < DataStorage.usernames.size(); i++) {
            String username  = DataStorage.usernames.get(i);
            String role      = DataStorage.roleName(DataStorage.roles.get(i));
            String status    = DataStorage.locked.get(i) ? "🔒 Locked" : "✅ Active";

            String ts = DataStorage.timestamps.get(i)
                            .toString()
                            .replace("T", "  ")
                            .substring(0, 18);

            model.addRow(new Object[]{ i + 1, username, role, status, ts });
        }

        JTable table = new JTable(model);
        table.setBackground(new Color(40, 40, 55));
        table.setForeground(Color.WHITE);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setRowHeight(28);
        table.getTableHeader().setBackground(new Color(0, 100, 180));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        table.setSelectionBackground(new Color(0, 120, 215));
        table.setGridColor(new Color(60, 60, 80));

        DefaultTableCellRenderer centre = new DefaultTableCellRenderer();
        centre.setHorizontalAlignment(SwingConstants.CENTER);
        for (int col = 0; col < table.getColumnCount(); col++) {
            table.getColumnModel().getColumn(col).setCellRenderer(centre);
        }

        table.getColumnModel().getColumn(0).setPreferredWidth(30);
        table.getColumnModel().getColumn(1).setPreferredWidth(120);
        table.getColumnModel().getColumn(2).setPreferredWidth(80);
        table.getColumnModel().getColumn(3).setPreferredWidth(90);
        table.getColumnModel().getColumn(4).setPreferredWidth(160);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(new Color(40, 40, 55));
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton refreshBtn = new JButton("🔄 Refresh");
        refreshBtn.setBackground(new Color(0, 100, 180));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFocusPainted(false);
        refreshBtn.addActionListener(e -> {
            dispose();
            new Viewusersframe();
        });
        panel.add(refreshBtn, BorderLayout.SOUTH);

        add(panel);
    }
}
