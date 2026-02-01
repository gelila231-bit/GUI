import java.util.ArrayList;

public class SalesEmployee extends Employee implements TransactionHandler, ProductViewer {

    private FileHandler<TransactionRec> fileHandler;
    private ArrayList<TransactionRec> transactionsRecord;
    private InventoryManager1 inventoryManager;

    public SalesEmployee(String username, String password, String employeeID,
                         FileHandler<TransactionRec> fileHandler,
                         InventoryManager1 inventoryManager) {
        super(username, password, employeeID);
        this.transactionsRecord = new ArrayList<>();
        this.inventoryManager = inventoryManager;
        this.fileHandler = fileHandler;
        loadTransactionsFromDisk();
    }

    public SalesEmployee(String username, String hashedPassword, String employeeId,
                         FileHandler<TransactionRec> fileHandler,
                         InventoryManager1 inventoryManager,
                         boolean alreadyHashed) {
        super(username, hashedPassword, employeeId, alreadyHashed);
        this.transactionsRecord = new ArrayList<>();
        this.fileHandler = fileHandler;
        this.inventoryManager = inventoryManager;
    }

    @Override
    public void recordTransaction(Product product, int quantity, double price) {
        TransactionRec tranRec = new TransactionRec(product.getProductName(), quantity, price);
        transactionsRecord.add(tranRec);
        ArrayList<TransactionRec> toSave = new ArrayList<>();
        toSave.add(tranRec);
        fileHandler.writeToFile("Transaction.txt", toSave, true);
    }

    @Override
    public boolean sellProduct(String productName, int quantity) {
        if (quantity <= 0) return false;

        ArrayList<Product> matchesList = inventoryManager.readProduct(productName);
        String productID = inventoryManager.getSelectedProductID(matchesList, productName);

        if (productID == null) return false;

        Product product = inventoryManager.findProductById(productID);

        if (product.getProductQuantity() < quantity) {
            return false;
        }

        product.setProductQuantity(product.getProductQuantity() - quantity);
        inventoryManager.saveToDisk();

        double price = product.getProductPrice();
        recordTransaction(product, quantity, price);

        return true;
    }

    @Override
    public boolean processReturn(Product product, int quantity) {
        if (product == null || quantity <= 0) return false;

        product.setProductQuantity(product.getProductQuantity() + quantity);
        inventoryManager.saveToDisk();

        TransactionRec returnRec = new TransactionRec(product.getProductName(), -quantity, product.getProductPrice());
        transactionsRecord.add(returnRec);

        ArrayList<TransactionRec> toSave = new ArrayList<>();
        toSave.add(returnRec);
        fileHandler.writeToFile("Transaction.txt", toSave, true);

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
        ArrayList<String> lines = fileHandler.readFromFile("Transaction.txt");
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
}

