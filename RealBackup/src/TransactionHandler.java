public interface TransactionHandler {
    void recordTransaction(Product product, int quantity, double price);
    boolean sellProduct(String productName, int quantity);
    boolean processReturn(Product product, int quantity);
}