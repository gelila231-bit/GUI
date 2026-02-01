import java.util.ArrayList;

/**
 * Service implementation following Single Responsibility Principle
 * Handles all user-related operations
 */
public class UserService implements IUserService {
    
    private final ArrayList<User> users = new ArrayList<>();
    private final FileHandler<String> fileHandler;
    private final IInventoryService inventoryService;
    
    public UserService(IInventoryService inventoryService) {
        this.inventoryService = inventoryService;
        this.fileHandler = new FileHandler<>();
        loadUsers();
    }
    
    @Override
    public User createUser(String role, String username, String password, String employeeId) {
        return UserFactory.createUser(role, username, password, employeeId, inventoryService);
    }
    
    @Override
    public boolean deleteUser(String username) {
        return users.removeIf(user -> user.getUsername().equals(username));
    }
    
    @Override
    public boolean resetPassword(User user, String newPassword) {
        if (PasswordValidator.isValid(newPassword)) {
            user.setPassword(newPassword);
            return true;
        }
        return false;
    }
    
    @Override
    public ArrayList<User> getAllUsers() {
        return new ArrayList<>(users);
    }
    
    @Override
    public User authenticate(String username, String password) {
        for (User u : users) {
            if (u.login(username, password)) {
                return u;
            }
        }
        return null;
    }
    
    @Override
    public void saveUsers() {
        ArrayList<String> dataLine = new ArrayList<>();
        for (User u : users) {
            dataLine.add(u.toFileString());
        }
        fileHandler.writeToFile("users.txt", dataLine, false);
    }
    
    @Override
    public void loadUsers() {
        users.clear();
        ArrayList<String> lines = fileHandler.readFromFile("users.txt");
        
        if (lines.isEmpty()) {
            // Create default admin if file is empty
            User defaultAdmin = new AdminSOLID("admin", "Admin@123", this, inventoryService);
            users.add(defaultAdmin);
            saveUsers();
            return;
        }
        
        // Load users from file
        for (String line : lines) {
            String[] data = line.split(",");
            if (data.length < 3) continue;
            
            String role = data[0].trim();
            String username = data[1].trim();
            String password = data[2].trim();
            String employeeId = data.length >= 4 ? data[3].trim() : "DEFAULT-01";
            
            User user = UserFactory.createUser(role, username, password, employeeId, inventoryService);
            if (user != null) {
                users.add(user);
            }
        }
    }
}
