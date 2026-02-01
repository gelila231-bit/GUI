import java.util.ArrayList;

/**
 * SOLID-compliant Manager class following Single Responsibility and Dependency Inversion principles
 */
public class ManagerSOLID extends Employee implements ReportGenerator, ProductViewer {

    private final IInventoryService inventoryService;
    private final IReportService reportService;

    public ManagerSOLID(String username, String password, String employeeId, 
                       IInventoryService inventoryService, IReportService reportService) {
        super(username, password, employeeId);
        this.inventoryService = inventoryService;
        this.reportService = reportService;
    }

    public ManagerSOLID(String username, String hashedPassword, String employeeId,
                       IInventoryService inventoryService, IReportService reportService, 
                       boolean alreadyHashed) {
        super(username, hashedPassword, employeeId, alreadyHashed);
        this.inventoryService = inventoryService;
        this.reportService = reportService;
    }

    // Product Management Methods - delegating to service
    public boolean addProduct(String newProductName, String variant, String category, 
                            double productPrice, double productQuantity) {
        return inventoryService.createProduct(newProductName, variant, category, 
                                           productPrice, productQuantity);
    }

    public boolean updateProduct(String productName, double newPrice, double newQuantity) {
        return inventoryService.updateProduct(productName, newPrice, newQuantity);
    }

    public boolean deleteProduct(String productName) {
        return inventoryService.deleteProduct(productName);
    }

    public ArrayList<Product> getAllProducts() {
        return inventoryService.getAllProducts();
    }

    public boolean changeProductPrice(String productName, double newPrice) {
        return inventoryService.updateProductPrice(productName, newPrice);
    }

    public boolean changeProductQuantity(String productName, int newQuantity) {
        return inventoryService.updateProductQuantity(productName, newQuantity);
    }

    // Report Generation - delegating to service
    @Override
    public ArrayList<String[]> generateInventoryReport() {
        return reportService.generateInventoryReport(inventoryService.getInventoryData());
    }

    @Override
    public ArrayList<String[]> generateSalesReport() {
        return reportService.generateSalesReport(inventoryService.getSalesData());
    }

    // Getters for dependency injection
    public IInventoryService getInventoryService() {
        return inventoryService;
    }

    public IReportService getReportService() {
        return reportService;
    }
}
