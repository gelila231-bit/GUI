import java.util.ArrayList;

public class Admin extends User {

    private ArrayList<User> users = new ArrayList<>();
    private FileHandler<String> fileHandler;
    private InventoryManager1 masterInventory;

    public Admin(String username, String password, FileHandler<String> fileHandler, InventoryManager1 inv) {
        super(username, password);
        this.fileHandler = fileHandler;
        this.masterInventory = inv;
    }

    public Admin(String username, String hashedPassword, FileHandler<String> fileHandler,
            InventoryManager1 inv, boolean alreadyHashed) {
        super(username, hashedPassword, alreadyHashed);
        this.fileHandler = fileHandler;
        this.masterInventory = inv;
    }

    @Override
    public boolean login(String username, String password) {
        if (getUsername().equals(username) && this.verifyPassword(password)) {
            setLoggedIn(true);
            return true;
        }
        return false;
    }

    public User createUser(String role, String username, String password, String employeeId) {
        User newUser = null;
        switch (role.toLowerCase()) {
            case "manager":
                newUser = new Manager(username, password, employeeId, this.masterInventory);
                break;
            case "salesemployee":
                newUser = new SalesEmployee(username, password, employeeId,
                        new FileHandler<TransactionRec>(), this.masterInventory);
                break;
            case "admin":
                newUser = new Admin(username, password, fileHandler, this.masterInventory);
                break;
        }
        if (newUser != null) {
            users.add(newUser);
        }
        return newUser;
    }

<<<<<<< HEAD
    public boolean resetPassword(User user, String newPass) {
        if (user == null)
            return false;
        if (user.verifyPassword(newPass))
            return false;

        boolean hasDigit = false;
        boolean hasSpecial = false;
        String specialChars = "!@#$^()&_";

        for (char c : newPass.toCharArray()) {
            if (Character.isDigit(c))
                hasDigit = true;
            if (specialChars.indexOf(c) != -1)
                hasSpecial = true;
        }

        if (newPass.length() >= 6 && hasDigit && hasSpecial) {
            user.setPassword(newPass);
            saveUsersToFile();
            return true;
        }
        return false;
    }

    public boolean deleteUser(String username) {
        boolean removed = users.removeIf(user -> user.getUsername().equals(username));
        if (removed)
            saveUsersToFile();
        return removed;
    }

=======
>>>>>>> parent of 3a0d056 (major changes)
    public void saveUsersToFile() {
        ArrayList<String> dataLine = new ArrayList<>();
        for (User u : users) {
            dataLine.add(u.toFileString());
        }
        fileHandler.writeToFile("users.txt", dataLine, false);
    }

    public void loadUsersFromFile() {
        users.clear();
        ArrayList<String> lines = fileHandler.readFromFile("users.txt");

        if (lines.isEmpty()) {
            // Create default admin if file is empty
            User defaultAdmin = new Admin("admin", "Admin@123", fileHandler, this.masterInventory);
            users.add(defaultAdmin);
            saveUsersToFile();
            return;
        }

        // Inside loadUsersFromFile() loop
        for (String line : lines) {
            String[] data = line.split(",");
            if (data.length < 3)
                continue; // Safety check

            String role = data[0].trim();
            String username = data[1].trim();
            String password = data[2].trim();
            // Default ID if not present
            String employeeId = (data.length >= 4) ? data[3].trim() : "ADMIN-01";

            if (role.equalsIgnoreCase("admin"))
                // Ensure this calls the 'true' constructor
                users.add(new Admin(username, password, fileHandler, this.masterInventory, true));
        }
        // ... rest of your if/else logic
    }

<<<<<<< HEAD
    public ArrayList<String> viewAllUsers() {
        ArrayList<String> userList = new ArrayList<>();
        for (User u : users) {
            userList.add("Name: " + u.getUsername() + " | Role: " + u.getClass().getSimpleName());
        }
        return userList;
    }

=======
>>>>>>> parent of 3a0d056 (major changes)
    public ArrayList<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    // Authentication method
    public User authenticate(String username, String password) {
        for (User u : users) {
            if (u.login(username, password)) {
                return u;
            }
        }
        return null;
    }
}