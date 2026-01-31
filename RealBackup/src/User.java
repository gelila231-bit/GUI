import java.security.MessageDigest;

public abstract class User {
    private String username;
    private String hashedPassword;
    private boolean loggedIn;

    public User(String username, String password) {
        this.username = username;
        this.hashedPassword = hashPassword(password);
        this.loggedIn = false;
    }

    public User(String username, String hashedPassword, boolean alreadyHashed) {
        this.username = username;
        this.hashedPassword = alreadyHashed ? hashedPassword : hashPassword(hashedPassword);
        this.loggedIn = false;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String newpass) {
        this.hashedPassword = hashPassword(newpass);
    }

    public abstract boolean login(String username, String password);

    public boolean isLoggedIn() {
        return loggedIn;
    }

    protected void setLoggedIn(boolean status) {
        this.loggedIn = status;
    }

    public void logout() {
        this.loggedIn = false;
    }

    public boolean verifyPassword(String inputPassword) {
        String inputHash = hashPassword(inputPassword);
        return hashedPassword != null && hashedPassword.equals(inputHash);
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(password.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    protected String getIdForFile() {
        return "N/A";
    }

    public String toFileString() {
        return getClass().getSimpleName().toLowerCase() + "," + username + "," + hashedPassword + "," + getIdForFile();
    }

    public String getShortHash() {
        return hashedPassword.substring(0, 12);
    }
}
