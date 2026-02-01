import java.util.ArrayList;

/**
 * SOLID-compliant Admin class following Single Responsibility and Dependency Inversion principles
 */
public class AdminSOLID extends User {

    private final IUserService userService;
    private final IInventoryService inventoryService;

    public AdminSOLID(String username, String password, IUserService userService, 
                      IInventoryService inventoryService) {
        super(username, password);
        this.userService = userService;
        this.inventoryService = inventoryService;
    }

    public AdminSOLID(String username, String hashedPassword, IUserService userService, 
                      IInventoryService inventoryService, boolean alreadyHashed) {
        super(username, hashedPassword, alreadyHashed);
        this.userService = userService;
        this.inventoryService = inventoryService;
    }

    @Override
    public boolean login(String username, String password) {
        if (getUsername().equals(username) && this.verifyPassword(password)) {
            setLoggedIn(true);
            return true;
        }
        return false;
    }

    // User Management - delegating to service
    public User createUser(String role, String username, String password, String employeeId) {
        User newUser = userService.createUser(role, username, password, employeeId);
        if (newUser != null) {
            userService.saveUsers();
        }
        return newUser;
    }

    public boolean deleteUser(String username) {
        if (username.equals(this.getUsername())) {
            throw new SecurityException("Cannot delete your own account");
        }
        boolean result = userService.deleteUser(username);
        if (result) {
            userService.saveUsers();
        }
        return result;
    }

    public boolean resetPassword(User user, String newPass) {
        if (user == null) return false;
        if (user.verifyPassword(newPass)) return false;

        // Password validation logic extracted to a separate validator
        if (PasswordValidator.isValid(newPass)) {
            user.setPassword(newPass);
            userService.saveUsers();
            return true;
        }
        return false;
    }

    // Delegated methods
    public ArrayList<User> getAllUsers() {
        return userService.getAllUsers();
    }

    public User authenticate(String username, String password) {
        return userService.authenticate(username, password);
    }

    public void loadUsers() {
        userService.loadUsers();
    }

    // Getters for dependency injection
    public IUserService getUserService() {
        return userService;
    }

    public IInventoryService getInventoryService() {
        return inventoryService;
    }
}
