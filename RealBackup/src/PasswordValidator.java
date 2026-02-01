/**
 * Utility class following Single Responsibility Principle for password validation
 */
public class PasswordValidator {
    
    private static final String SPECIAL_CHARS = "!@#$^()&_";
    private static final int MIN_LENGTH = 6;
    
    public static boolean isValid(String password) {
        if (password == null || password.length() < MIN_LENGTH) {
            return false;
        }
        
        boolean hasDigit = false;
        boolean hasSpecial = false;
        
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                hasDigit = true;
            }
            if (SPECIAL_CHARS.indexOf(c) != -1) {
                hasSpecial = true;
            }
        }
        
        return hasDigit && hasSpecial;
    }
    
    public static String getRequirements() {
        return "Password must be at least " + MIN_LENGTH + " characters long, " +
               "contain at least one digit, and one special character (" + SPECIAL_CHARS + ")";
    }
}
