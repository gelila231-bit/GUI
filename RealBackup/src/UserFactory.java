/**
 * Factory class following Open/Closed Principle for user creation
 * This allows adding new user types without modifying existing code
 */
public class UserFactory {
    
    public static User createUser(String role, String username, String password, 
                                String employeeId, IInventoryService inventoryService) {
        
        UserRole userRole = UserRole.fromString(role);
        
        switch (userRole) {
            case ADMIN:
                return new AdminSOLID(username, password, 
                                    new UserService(inventoryService), inventoryService);
            case MANAGER:
                return new ManagerSOLID(username, password, employeeId, 
                                      inventoryService, new ReportService());
            case SALES_EMPLOYEE:
                return new SalesEmployeeSOLID(username, password, employeeId,
                                             new FileHandler<>(), inventoryService);
            default:
                throw new IllegalArgumentException("Unknown role: " + role);
        }
    }
    
    public enum UserRole {
        ADMIN("admin"),
        MANAGER("manager"), 
        SALES_EMPLOYEE("salesemployee");
        
        private final String value;
        
        UserRole(String value) {
            this.value = value;
        }
        
        public static UserRole fromString(String role) {
            for (UserRole r : UserRole.values()) {
                if (r.value.equalsIgnoreCase(role)) {
                    return r;
                }
            }
            throw new IllegalArgumentException("Unknown role: " + role);
        }
        
        public String getValue() {
            return value;
        }
    }
}
