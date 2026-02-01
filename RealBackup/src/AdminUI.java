import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class AdminUI extends JFrame {
    private Admin admin;
    private JTable userTable;
    private DefaultTableModel tableModel;

    public AdminUI(Admin admin) {
        this.admin = admin;
        setTitle("Admin Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Layout
        setLayout(new BorderLayout());

        // Table
        tableModel = new DefaultTableModel(new String[]{"Role", "Username", "Employee ID"}, 0);
        userTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(userTable);
        add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addUserBtn = new JButton("Add User");
        JButton deleteUserBtn = new JButton("Delete User");
        JButton resetPassBtn = new JButton("Reset Password");
        JButton refreshBtn = new JButton("Refresh");

        buttonPanel.add(addUserBtn);
        buttonPanel.add(deleteUserBtn);
        buttonPanel.add(resetPassBtn);
        buttonPanel.add(refreshBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        // Actions
        addUserBtn.addActionListener(this::handleAddUser);
        deleteUserBtn.addActionListener(this::handleDeleteUser);
        resetPassBtn.addActionListener(this::handleResetPassword);
        refreshBtn.addActionListener(e -> loadUsers());

        // Initial load
        loadUsers();
    }

    private void handleAddUser(ActionEvent e) {
        String role = JOptionPane.showInputDialog(this, "Enter role (admin/manager/salesemployee):");
        String username = JOptionPane.showInputDialog(this, "Enter username:");
        String password = JOptionPane.showInputDialog(this, "Enter password:");
        String employeeId = JOptionPane.showInputDialog(this, "Enter employee ID:");

        User newUser = admin.createUser(role, username, password, employeeId);
        if (newUser != null) {
            admin.saveUsersToFile();
            loadUsers();
            JOptionPane.showMessageDialog(this, "User added successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add user.");
        }
    }

    private void handleDeleteUser(ActionEvent e) {
        String username = JOptionPane.showInputDialog(this, "Enter username to delete:");
        if (admin.deleteUser(username)) {
            admin.saveUsersToFile();
            loadUsers();
            JOptionPane.showMessageDialog(this, "User deleted successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "User not found.");
        }
    }

    private void handleResetPassword(ActionEvent e) {
        String username = JOptionPane.showInputDialog(this, "Enter username to reset password:");
        String newPass = JOptionPane.showInputDialog(this, "Enter new password:");

<<<<<<< HEAD
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
=======
        ArrayList<User> users = admin.viewAllUsers();
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                if (admin.resetPassword(u, newPass)) {
                    admin.saveUsersToFile();
                    JOptionPane.showMessageDialog(this, "Password reset successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Password reset failed. Check rules.");
                }
>>>>>>> parent of 3a0d056 (major changes)
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "User not found.");
    }

    private void loadUsers() {
        tableModel.setRowCount(0);
        ArrayList<User> users = admin.getAllUsers();
        for (User u : users) {
            String role = u.getClass().getSimpleName();
            tableModel.addRow(new Object[]{role, u.getUsername(), u.getIdForFile()});
        }
    }


    public static void main(String[] args) {
        FileHandler<String> fileHandler = new FileHandler<>();
        InventoryManager1 inv = new InventoryManager1();
        Admin admin = new Admin("admin", "Admin@123", fileHandler, inv);
        admin.loadUsersFromFile();

        SwingUtilities.invokeLater(() -> new AdminUI(admin).setVisible(true));
    }
}
