import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class AdminUI extends JFrame {
    private Admin admin;
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JLabel statusLabel;
    private JTextField searchField;

    public AdminUI(Admin admin) {
        this.admin = admin;
        setTitle("Admin Dashboard - " + admin.getUsername());
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set modern look
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            // Use default
        }

        initComponents();
        loadUsers();
    }

    private void initComponents() {
        // Top panel with search and status
        JPanel topPanel = new JPanel(new BorderLayout());

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search Users:"));
        searchField = new JTextField(20);
        searchField.addActionListener(e -> searchUsers());
        JButton searchBtn = new JButton("Search");
        searchBtn.addActionListener(e -> searchUsers());
        JButton clearSearchBtn = new JButton("Clear");
        clearSearchBtn.addActionListener(e -> {
            searchField.setText("");
            loadUsers();
        });

        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
        searchPanel.add(clearSearchBtn);

        // Status label
        statusLabel = new JLabel("Ready");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 0));

        topPanel.add(searchPanel, BorderLayout.CENTER);
        topPanel.add(statusLabel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Table with better styling
        tableModel = new DefaultTableModel(new String[] { "Role", "Username", "Employee ID", "Status" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        userTable = new JTable(tableModel);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userTable.setRowHeight(25);

        // Column widths
        userTable.getColumnModel().getColumn(0).setPreferredWidth(120);
        userTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        userTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        userTable.getColumnModel().getColumn(3).setPreferredWidth(100);

        JScrollPane scrollPane = new JScrollPane(userTable);
        add(scrollPane, BorderLayout.CENTER);

        // Button panel with better organization
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createButtonPanel() {
        JPanel mainButtonPanel = new JPanel(new BorderLayout());

        // User management buttons
        JPanel userPanel = new JPanel(new FlowLayout());
        JButton addUserBtn = new JButton("Add User");
        JButton deleteUserBtn = new JButton("Delete User");
        JButton resetPassBtn = new JButton("Reset Password");
        JButton refreshBtn = new JButton("Refresh");

        styleButton(addUserBtn, new Color(46, 204, 113));
        styleButton(deleteUserBtn, new Color(231, 76, 60));
        styleButton(resetPassBtn, new Color(52, 152, 219));
        styleButton(refreshBtn, new Color(149, 165, 166));

        userPanel.add(addUserBtn);
        userPanel.add(deleteUserBtn);
        userPanel.add(resetPassBtn);
        userPanel.add(refreshBtn);

        // System info panel
        JPanel infoPanel = new JPanel(new FlowLayout());
        infoPanel.setBorder(BorderFactory.createTitledBorder("System"));
        JButton systemInfoBtn = new JButton("System Information");

        styleButton(systemInfoBtn, new Color(155, 89, 182));
        infoPanel.add(systemInfoBtn);

        mainButtonPanel.add(userPanel, BorderLayout.CENTER);
        mainButtonPanel.add(infoPanel, BorderLayout.SOUTH);

        // Actions
        addUserBtn.addActionListener(this::handleAddUser);
        deleteUserBtn.addActionListener(this::handleDeleteUser);
        resetPassBtn.addActionListener(this::handleResetPassword);
        refreshBtn.addActionListener(e -> loadUsers());
        systemInfoBtn.addActionListener(e -> showSystemInfo());

        return mainButtonPanel;
    }

    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void handleAddUser(ActionEvent e) {
        JDialog dialog = new JDialog(this, "Add New User", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));

        String[] roles = { "admin", "manager", "salesemployee" };
        JComboBox<String> roleCombo = new JComboBox<>(roles);
        JTextField usernameField = new JTextField();
        JTextField passwordField = new JTextField();
        JTextField employeeIdField = new JTextField();

        panel.add(new JLabel("Role:"));
        panel.add(roleCombo);
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Employee ID:"));
        panel.add(employeeIdField);

        JLabel infoLabel = new JLabel(
                "<html><small>Password must be 6+ chars with digit and special char (!@#$^()&_)</small></html>");
        panel.add(infoLabel);

        JButton addBtn = new JButton("Add User");
        JButton cancelBtn = new JButton("Cancel");

        addBtn.addActionListener(event -> {
            String role = (String) roleCombo.getSelectedItem();
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            String employeeId = employeeIdField.getText().trim();

            if (username.isEmpty() || password.isEmpty() || employeeId.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            User newUser = admin.createUser(role, username, password, employeeId);
            if (newUser != null) {
                admin.saveUsersToFile();
                loadUsers();
                statusLabel.setText("User '" + username + "' added successfully!");
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Failed to add user.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelBtn.addActionListener(event -> dialog.dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addBtn);
        buttonPanel.add(cancelBtn);

        dialog.setLayout(new BorderLayout());
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void handleDeleteUser(ActionEvent e) {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to delete.", "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String username = userTable.getValueAt(selectedRow, 1).toString();

        if (username.equals(admin.getUsername())) {
            JOptionPane.showMessageDialog(this, "You cannot delete your own account.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete user '" + username + "'?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (admin.deleteUser(username)) {
                admin.saveUsersToFile();
                loadUsers();
                statusLabel.setText("User '" + username + "' deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete user.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleResetPassword(ActionEvent e) {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to reset password.", "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

<<<<<<< Updated upstream
        ArrayList<User> users = admin.viewAllUsers();
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                if (admin.resetPassword(u, newPass)) {
                    admin.saveUsersToFile();
                    JOptionPane.showMessageDialog(this, "Password reset successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Password reset failed. Check rules.");
                }
=======
        String username = userTable.getValueAt(selectedRow, 1).toString();

        JDialog dialog = new JDialog(this, "Reset Password", true);
        dialog.setSize(350, 150);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));

        panel.add(new JLabel("User:"));
        panel.add(new JLabel(username));
        panel.add(new JLabel("New Password:"));
        JPasswordField passwordField = new JPasswordField();
        panel.add(passwordField);

        JButton resetBtn = new JButton("Reset Password");
        JButton cancelBtn = new JButton("Cancel");

        resetBtn.addActionListener(event -> {
            String newPass = new String(passwordField.getPassword());

            if (newPass.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Password cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
>>>>>>> Stashed changes
                return;
            }

            ArrayList<User> users = admin.getAllUsers();
            for (User u : users) {
                if (u.getUsername().equals(username)) {
                    if (admin.resetPassword(u, newPass)) {
                        admin.saveUsersToFile();
                        statusLabel.setText("Password reset successfully for '" + username + "'!");
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog,
                                "Password reset failed. Check rules (6+ chars, digit, special char).", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    return;
                }
            }
            JOptionPane.showMessageDialog(dialog, "User not found.", "Error", JOptionPane.ERROR_MESSAGE);
        });

        cancelBtn.addActionListener(event -> dialog.dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(resetBtn);
        buttonPanel.add(cancelBtn);

        dialog.setLayout(new BorderLayout());
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void loadUsers() {
        tableModel.setRowCount(0);
        ArrayList<User> users = admin.getAllUsers();

        for (User u : users) {
            String role = u.getClass().getSimpleName();
            String status = u.isLoggedIn() ? "Online" : "Offline";

            tableModel.addRow(new Object[] {
                    role,
                    u.getUsername(),
                    u.getIdForFile(),
                    status
            });
        }

        statusLabel.setText("Loaded " + users.size() + " users");
    }

    private void searchUsers() {
        String searchTerm = searchField.getText().trim().toLowerCase();
        if (searchTerm.isEmpty()) {
            loadUsers();
            return;
        }

        tableModel.setRowCount(0);
        ArrayList<User> allUsers = admin.getAllUsers();
        int foundCount = 0;

        for (User u : allUsers) {
            if (u.getUsername().toLowerCase().contains(searchTerm) ||
                    u.getClass().getSimpleName().toLowerCase().contains(searchTerm) ||
                    u.getIdForFile().toLowerCase().contains(searchTerm)) {

                String role = u.getClass().getSimpleName();
                String status = u.isLoggedIn() ? "Online" : "Offline";

                tableModel.addRow(new Object[] {
                        role,
                        u.getUsername(),
                        u.getIdForFile(),
                        status
                });
                foundCount++;
            }
        }

        statusLabel.setText("Found " + foundCount + " users matching '" + searchTerm + "'");
    }

    private void showSystemInfo() {
        JDialog infoDialog = new JDialog(this, "System Information", false);
        infoDialog.setSize(500, 400);
        infoDialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new BorderLayout());

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        StringBuilder infoText = new StringBuilder();
        infoText.append("SYSTEM INFORMATION\n");
        infoText.append("==================\n\n");

        ArrayList<User> users = admin.getAllUsers();
        int adminCount = 0, managerCount = 0, salesCount = 0;

        for (User u : users) {
            String role = u.getClass().getSimpleName();
            switch (role) {
                case "Admin":
                    adminCount++;
                    break;
                case "Manager":
                    managerCount++;
                    break;
                case "SalesEmployee":
                    salesCount++;
                    break;
            }
        }

        infoText.append("Total Users: ").append(users.size()).append("\n");
        infoText.append("Admins: ").append(adminCount).append("\n");
        infoText.append("Managers: ").append(managerCount).append("\n");
        infoText.append("Sales Employees: ").append(salesCount).append("\n\n");

        infoText.append("Current User: ").append(admin.getUsername()).append("\n");
        infoText.append("User Role: Admin\n\n");

        infoText.append("System Status: Operational\n");
        infoText.append("Last Login: ").append(new java.util.Date()).append("\n");

        textArea.setText(infoText.toString());

        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> infoDialog.dispose());

        panel.add(new JScrollPane(textArea), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(closeBtn);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        infoDialog.add(panel);
        infoDialog.setVisible(true);
    }

    public static void main(String[] args) {
        FileHandler<String> fileHandler = new FileHandler<>();
        InventoryManager1 inv = new InventoryManager1();
        Admin admin = new Admin("admin", "Admin@123", fileHandler, inv);
        admin.loadUsersFromFile();

        SwingUtilities.invokeLater(() -> new AdminUI(admin).setVisible(true));
    }
}
