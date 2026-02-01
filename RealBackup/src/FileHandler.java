import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileHandler<T> {

    public boolean writeToFile(String filename, ArrayList<T> content, boolean append) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, append))) {
            for (T item : content) {
                writer.write(item.toString());
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public ArrayList<String> readFromFile(String filename) {
        ArrayList<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            return new ArrayList<>();
        }
        return lines;
    }
}