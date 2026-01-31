public interface ProductViewer {

    default String viewProductDetails(Product p) {
        return "Product ID: " + p.getProductID() +
                ", Name: " + p.getProductName() +
                ", Price: " + p.getProductPrice() +
                ", Quantity: " + p.getProductQuantity();
    }

    default Product searchProduct(String id, InventoryManager1 inv) {
        return inv.findProductById(id);
    }

    default double checkProductPrice(Product p) {
        return p.getProductPrice();
    }

    default double checkProductQuantity(Product p) {
        return p.getProductQuantity();
    }
}
