import java.util.ArrayList;

public class Manager extends Employee implements ReportGenerator, ProductViewer {

    private InventoryManager1 inventoryManager;
    private NewReportClass reportClass;

    public Manager(String username, String password, String employeeId, InventoryManager1 inventoryManager) {
        super(username, password, employeeId);
        this.inventoryManager = inventoryManager;
        this.inventoryManager.loadFromDisk();
        this.reportClass = new NewReportClass();

    }

    public Manager(String username, String hashedPassword, String employeeId, InventoryManager1 inventoryManager, boolean alreadyHashed) {
        super(username, hashedPassword, employeeId, alreadyHashed);
        this.inventoryManager = inventoryManager;
        this.reportClass = new NewReportClass();
    }

    public boolean addProduct(String newProductName, String variant, String category, double productPrice,
                              double productQuantity) {
        return inventoryManager.createProduct(newProductName, variant, category, productPrice, productQuantity);
    }

    public boolean updateProduct(String productName, double newPrice, double newQuantity) {
        return inventoryManager.updateProduct(productName, newPrice, newQuantity);
    }

    public boolean deleteProduct(String productName) {
        return inventoryManager.deleteProduct(productName);
    }
    public ArrayList<Product> getAllProducts() {
        return inventoryManager.getAllProducts();
    }


    public boolean changeProductPrice(String productName, double newPrice) {
        return inventoryManager.updateProductPrice(productName, newPrice);
    }

    public boolean changeProductQuantity(String productName, int newQuantity) {
        return inventoryManager.updateProductQuantity(productName, newQuantity);
    }

    @Override public ArrayList<String[]> generateInventoryReport() { return reportClass.getInventoryReport(inventoryManager.getInventoryData()); } @Override public ArrayList<String[]> generateSalesReport() { return reportClass.getSalesReport(inventoryManager.getSalesData()); }
}