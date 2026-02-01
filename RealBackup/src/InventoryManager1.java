import java.util.ArrayList;

public class InventoryManager1 {
    private ArrayList<Product> products = new ArrayList<>();
    private FileHandler<Product> filehandler = new FileHandler<>();
    private productIdGenerator idGenerator;

    public boolean createProduct(String newProductName, String variant, String category,
            double productPrice, double productQuantity) {
        idGenerator = new productIdGenerator();
        String productId = idGenerator.generateId(newProductName, variant, category);

        products.add(new Product(newProductName, productId, productPrice, productQuantity));
        saveToDisk();
        return true;
    }

    public ArrayList<Product> readProduct(String productName) {
        ArrayList<Product> matchList = new ArrayList<>();
        for (Product product : products) {
            if (product.getProductName().equalsIgnoreCase(productName)) {
                matchList.add(product);
            }
        }
        return matchList.isEmpty() ? null : matchList;
    }

    public Product findProductById(String productID) {
        for (Product product : products) {
            if (product.getProductID().equalsIgnoreCase(productID)) {
                return product;
            }
        }
        return null;
    }

    public String getSelectedProductID(ArrayList<Product> matches, String productName) {
        if (matches == null || matches.isEmpty()) {
            return null;
        } else if (matches.size() == 1) {
            return matches.get(0).getProductID();
        } else {
            // Multiple matches: let the UI decide which ID to use
            return null;
        }
    }

    public boolean updateProduct(String productName, double newPrice, double newQuantity) {
        ArrayList<Product> matches = readProduct(productName);
        String targetId = getSelectedProductID(matches, productName);
        if (targetId == null)
            return false;

        Product target = findProductById(targetId);
        if (target == null)
            return false;

        target.setProductPrice(newPrice);
        target.setProductQuantity(newQuantity);
        saveToDisk();
        return true;
    }

    public boolean updateProductPrice(String productName, double newPrice) {
        ArrayList<Product> matches = readProduct(productName);
        String targetId = getSelectedProductID(matches, productName);
        if (targetId == null)
            return false;

        Product target = findProductById(targetId);
        if (target == null)
            return false;

        target.setProductPrice(newPrice);
        saveToDisk();
        return true;
    }

    public boolean updateProductQuantity(String productName, int newQuantity) {
        ArrayList<Product> matches = readProduct(productName);
        String targetId = getSelectedProductID(matches, productName);
        if (targetId == null)
            return false;

        Product target = findProductById(targetId);
        if (target == null)
            return false;

        target.setProductQuantity(newQuantity);
        saveToDisk();
        return true;
    }

    public boolean deleteProduct(String productName) {
        ArrayList<Product> matches = readProduct(productName);
        String targetId = getSelectedProductID(matches, productName);
        if (targetId == null)
            return false;

        Product target = findProductById(targetId);
        if (target == null)
            return false;

        products.remove(target);
        saveToDisk();
        return true;
    }

    public ArrayList<Product> getAllProducts() {
        return new ArrayList<>(products);
    }

    public void saveToDisk() {
        filehandler.writeToFile("inventory.txt", products, false);
    }

    public void loadFromDisk() {
        ArrayList<String> lines = filehandler.readFromFile("inventory.txt");
        int maxIdFound = 1000;

        for (String line : lines) {
            Product p = Product.fromString(line);
            if (p != null) {
                products.add(p);

                // Extract the numeric part of the ID (e.g., "1005" from "CAT-NAM-VAR-1005")
                try {
                    String[] parts = p.getProductID().split("-");
                    int idNum = Integer.parseInt(parts[parts.length - 1]);
                    if (idNum > maxIdFound)
                        maxIdFound = idNum;
                } catch (Exception e) {
                    /* Skip malformed IDs */ }
            }
        }
        // Update the generator so the next ID is unique
        productIdGenerator.setCounter(maxIdFound);
    }

    public ArrayList<String> getInventoryData() {
        return filehandler.readFromFile("inventory.txt");
    }

    public ArrayList<String> getSalesData() {
        return filehandler.readFromFile("transaction.txt");
    }
}
