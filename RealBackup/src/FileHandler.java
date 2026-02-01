import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A generic FileHandler to manage text-based persistence.
 * 
 * @param <T> The type of object being handled (e.g., String or TransactionRec).
 */
public class FileHandler<T> {

    /**
     * Writes a list of objects to a file using their toString() method.
     */
    public boolean writeToFile(String filename, ArrayList<T> content, boolean append) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, append))) {
            for (T item : content) {
                if (item != null) {
                    writer.write(item.toString());
                    writer.newLine();
                }
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error writing to file " + filename + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * Reads a file line by line and returns an ArrayList of Strings.
     */
    public ArrayList<String> readFromFile(String filename) {
        ArrayList<String> lines = new ArrayList<>();
        File file = new File(filename);

        // Check if file exists to avoid FileNotFoundException
        if (!file.exists()) {
            return lines; // Return empty list
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Only add lines that aren't just whitespace
                if (!line.trim().isEmpty()) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading from file " + filename + ": " + e.getMessage());
            return new ArrayList<>(); // Return empty list on error to prevent NullPointerExceptions
        }
        return lines;
    }
}