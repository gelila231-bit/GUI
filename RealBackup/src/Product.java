public class Product {
    private String productName;
    private String productID;
    private double productPrice;
    private double productQuantity;

    public Product(String productName, String productID, double productPrice, double productQuantity) {
        this.productID = productID;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductID() {
        return productID;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public double getProductQuantity() {
        return productQuantity;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public void setProductQuantity(double productQuantity) {
        this.productQuantity = productQuantity;
    }

    @Override
    public String toString() {
        return productName + "," + productID + "," + productPrice + "," + productQuantity;
    }

    public static Product fromString(String line) {
        try {
            String[] parts = line.split(",");
            if (parts.length == 4) {
                String name = parts[0].trim();
                String id = parts[1].trim();
                double price = Double.parseDouble(parts[2].trim());
                double quantity = Double.parseDouble(parts[3].trim());
                return new Product(name, id, price, quantity);
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}