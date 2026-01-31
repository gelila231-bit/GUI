public class productIdGenerator {
    private static int counter = 1000;

    public String generateId(String name, String variant, String category) {
        String c = category.length() >= 3 ? category.substring(0, 3).toUpperCase() : category.toUpperCase();
        String v = variant.length() >= 3 ? variant.substring(0, 3).toUpperCase() : variant.toUpperCase();
        String n = name.length() >= 3 ? name.substring(0, 3).toUpperCase() : name.toUpperCase();
        return c + "-" + n + "-" + v + "-" + counter++;
    }
}
