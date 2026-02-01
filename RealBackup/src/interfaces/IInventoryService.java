import java.util.ArrayList;

public interface IInventoryService {
    boolean createProduct(String name, String variant, String category, double price, double quantity);
    boolean updateProduct(String productName, double newPrice, double newQuantity);
    boolean updateProductPrice(String productName, double newPrice);
    boolean updateProductQuantity(String productName, int newQuantity);
    boolean deleteProduct(String productName);
    Product findProductById(String productID);
    ArrayList<Product> getAllProducts();
    ArrayList<String> getInventoryData();
    ArrayList<String> getSalesData();
}
