import java.util.ArrayList;

public interface IUserService {
    User createUser(String role, String username, String password, String employeeId);
    boolean deleteUser(String username);
    boolean resetPassword(User user, String newPassword);
    ArrayList<User> getAllUsers();
    User authenticate(String username, String password);
    void saveUsers();
    void loadUsers();
}
