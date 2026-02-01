public class Employee extends User {
    private String employeeID;

    public Employee(String username, String password, String employeeID) {
        super(username, password);
        this.employeeID = employeeID;
    }

    public Employee(String username, String hashedPassword, String employeeID, boolean alreadyHashed) {
        super(username, hashedPassword, alreadyHashed);
        this.employeeID = employeeID;
    }

    @Override
    public boolean login(String username, String password) {
        if (getUsername().equals(username) && verifyPassword(password)) {
            setLoggedIn(true);
            return true;
        }
        return false;
    }

    protected String getRoleName() {
        return "Employee";
    }

    public String getEmployeeID() {
        return employeeID;
    }

    @Override
    protected String getIdForFile() {
        return employeeID;
    }
}