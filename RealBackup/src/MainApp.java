import javax.swing.*;
import java.util.ArrayList;

public class MainApp {
    private static Admin admin;

    public static void main(String[] args) {
        // Initialize core components
        FileHandler<String> fileHandler = new FileHandler<>();
        InventoryManager1 inv = new InventoryManager1();
        inv.loadFromDisk();

        // Create admin and load users
        admin = new Admin("admin", "Admin@123", fileHandler, inv);
        admin.loadUsersFromFile();

        // Start login flow
        SwingUtilities.invokeLater(MainApp::showLoginDialog);
    }

    private static void showLoginDialog() {
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        Object[] fields = {
                "Username:", usernameField,
                "Password:", passwordField
        };

        int option = JOptionPane.showConfirmDialog(null, fields, "Login", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            // Check authentication directly from loaded users
            ArrayList<User> users = admin.getAllUsers();
            for (User u : users) {
                if (u.login(username, password)) {
                    JOptionPane.showMessageDialog(null, "Login successful! Welcome " + u.getUsername());
                    launchDashboard(u);
                    return;
                }
            }
            JOptionPane.showMessageDialog(null, "Login failed. Please try again.");
            showLoginDialog();
        }
    }

    private static void launchDashboard(User user) {
        if (user instanceof Admin) {
            SwingUtilities.invokeLater(() -> new AdminUI((Admin) user).setVisible(true));
        } else if (user instanceof Manager) {
            SwingUtilities.invokeLater(() -> new ManagerUI((Manager) user).setVisible(true));
        } else if (user instanceof SalesEmployee) {
            SwingUtilities.invokeLater(() -> new SalesEmployeeUI((SalesEmployee) user).setVisible(true));
        } else {
            JOptionPane.showMessageDialog(null, "Unknown role. Cannot launch dashboard.");
        }
    }
}
