import java.util.ArrayList;

/**
 * SOLID-compliant SalesEmployee class following Single Responsibility and Dependency Inversion principles
 */
public class SalesEmployeeSOLID extends Employee implements TransactionHandler, ProductViewer {

    private final FileHandler<TransactionRec> fileHandler;
    private final ArrayList<TransactionRec> transactionsRecord;
    private final IInventoryService inventoryService;

    public SalesEmployeeSOLID(String username, String password, String employeeID,
                             FileHandler<TransactionRec> fileHandler,
                             IInventoryService inventoryService) {
        super(username, password, employeeID);
        this.transactionsRecord = new ArrayList<>();
        this.inventoryService = inventoryService;
        this.fileHandler = fileHandler;
        loadTransactionsFromDisk();
    }

    public SalesEmployeeSOLID(String username, String hashedPassword, String employeeID,
                             FileHandler<TransactionRec> fileHandler,
                             IInventoryService inventoryService, boolean alreadyHashed) {
        super(username, hashedPassword, employeeID, alreadyHashed);
        this.transactionsRecord = new ArrayList<>();
        this.fileHandler = fileHandler;
        this.inventoryService = inventoryService;
        loadTransactionsFromDisk();
    }

    @Override
    public void recordTransaction(Product product, int quantity, double price) {
        TransactionRec tranRec = new TransactionRec(product.getProductName(), quantity, price);
        transactionsRecord.add(tranRec);
        ArrayList<TransactionRec> toSave = new ArrayList<>();
        toSave.add(tranRec);
        fileHandler.writeToFile("transaction.txt", toSave, true);
    }

    @Override
    public boolean sellProduct(String productName, int quantity) {
        if (quantity <= 0) return false;

        ArrayList<Product> matchesList = inventoryService.readProduct(productName);
        String productID = inventoryService.getSelectedProductID(matchesList, productName);

        if (productID == null) return false;

        Product product = inventoryService.findProductById(productID);

        if (product.getProductQuantity() < quantity) {
            return false;
        }

        product.setProductQuantity(product.getProductQuantity() - quantity);
        // Note: InventoryService should handle saving

        double price = product.getProductPrice();
        recordTransaction(product, quantity, price);

        return true;
    }

    @Override
    public boolean processReturn(Product product, int quantity) {
        if (product == null || quantity <= 0) return false;

        product.setProductQuantity(product.getProductQuantity() + quantity);
        // Note: InventoryService should handle saving

        TransactionRec returnRec = new TransactionRec(product.getProductName(), -quantity, product.getProductPrice());
        transactionsRecord.add(returnRec);

        ArrayList<TransactionRec> toSave = new ArrayList<>();
        toSave.add(returnRec);
        fileHandler.writeToFile("transaction.txt", toSave, true);

        return true;
    }

    public double calculateTotalSales() {
        double totalSales = 0.0;
        for (TransactionRec T : transactionsRecord) {
            totalSales += T.calculateTotalInTransaction();
        }
        return totalSales;
    }

    public void loadTransactionsFromDisk() {
        ArrayList<String> lines = fileHandler.readFromFile("transaction.txt");
        this.transactionsRecord.clear();
        for (String line : lines) {
            try {
                TransactionRec record = TransactionRec.fromString(line);
                if (record != null) {
                    this.transactionsRecord.add(record);
                }
            } catch (Exception e) {
                continue;
            }
        }
    }

    public ArrayList<TransactionRec> getTransactionsRecord() {
        return new ArrayList<>(transactionsRecord);
    }

    public ArrayList<TransactionRec> getAllTransactions() {
        return getTransactionsRecord();
    }
    
    public IInventoryService getInventoryService() {
        return inventoryService;
    }
}
