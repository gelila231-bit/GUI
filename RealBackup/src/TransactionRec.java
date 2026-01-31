public class TransactionRec {
    private Product product;
    private String productName;
    private int transactionSaleQuantity;
    private double transactionSalePrice;

    public TransactionRec(String productName, int quantity, double price) {
        this.productName = productName;
        this.transactionSaleQuantity = quantity;
        this.transactionSalePrice = price;
    }

    public TransactionRec(Product product, int quantity) {
        this.product = product;
        this.productName = product.getProductName();
        this.transactionSaleQuantity = quantity;
        this.transactionSalePrice = product.getProductPrice();
    }

    public int getQuantity() {
        return transactionSaleQuantity;
    }

    public double getPrice() {
        return transactionSalePrice;
    }

    public Product getProduct() {
        return product;
    }

    public String getProductName() {
        return productName;
    }

    public double calculateTotalInTransaction() {
        return transactionSaleQuantity * transactionSalePrice;
    }

    @Override
    public String toString() {
        return productName + "," +
                transactionSaleQuantity + "," +
                transactionSalePrice + "," +
                calculateTotalInTransaction();
    }

    public static TransactionRec fromString(String line) {
        try {
            String[] parts = line.split(",");
            if (parts.length >= 3) {
                String name = parts[0].trim();
                int qty = Integer.parseInt(parts[1].trim());
                double price = Double.parseDouble(parts[2].trim());
                return new TransactionRec(name, qty, price);
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
