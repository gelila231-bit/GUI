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
            // ✅ Create default admin if file is empty
            User defaultAdmin = new Admin("admin", "Admin@123", fileHandler, this.masterInventory);
            users.add(defaultAdmin);
            saveUsersToFile();
            return;
        }

        for (String line : lines) {
            String[] data = line.split(",");
            String role = data[0].trim();
            String username = data[1].trim();
            String password = data[2].trim();

            if (role.equalsIgnoreCase("admin")) {
                users.add(new Admin(username, password, fileHandler, this.masterInventory, true));
            } else if (role.equalsIgnoreCase("manager") && data.length >= 4) {
                String employeeId = data[3].trim();
                users.add(new Manager(username, password, employeeId, this.masterInventory, true));
            } else if (role.equalsIgnoreCase("salesemployee") && data.length >= 4) {
                String employeeId = data[3].trim();
                users.add(new SalesEmployee(username, password, employeeId,
                        new FileHandler<TransactionRec>(), this.masterInventory, true));
            }
        }
    }

    public ArrayList<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    // ✅ Authentication method
    public User authenticate(String username, String password) {
        for (User u : users) {
            if (u.login(username, password)) {
                return u;
            }
        }
        return null;
    }
}