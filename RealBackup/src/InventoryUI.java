import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class InventoryUI extends JFrame {
    private InventoryManager1 inventoryManager;
    private JTable productTable;
    private DefaultTableModel tableModel;

    public InventoryUI(InventoryManager1 inventoryManager) {
        this.inventoryManager = inventoryManager;
        setTitle("Inventory Dashboard");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Layout
        setLayout(new BorderLayout());

        // Table
        tableModel = new DefaultTableModel(new String[]{"Product ID", "Name", "Price", "Quantity"}, 0);
        productTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(productTable);
        add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addProductBtn = new JButton("Add Product");
        JButton updateProductBtn = new JButton("Update Product");
        JButton deleteProductBtn = new JButton("Delete Product");
        JButton refreshBtn = new JButton("Refresh");

        buttonPanel.add(addProductBtn);
        buttonPanel.add(updateProductBtn);
        buttonPanel.add(deleteProductBtn);
        buttonPanel.add(refreshBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        // Actions
        addProductBtn.addActionListener(this::handleAddProduct);
        updateProductBtn.addActionListener(this::handleUpdateProduct);
        deleteProductBtn.addActionListener(this::handleDeleteProduct);
        refreshBtn.addActionListener(e -> loadProducts());

        // Initial load
        loadProducts();
    }

    private void handleAddProduct(ActionEvent e) {
        String name = JOptionPane.showInputDialog(this, "Enter product name:");
        String variant = JOptionPane.showInputDialog(this, "Enter variant:");
        String category = JOptionPane.showInputDialog(this, "Enter category:");
        double price = Double.parseDouble(JOptionPane.showInputDialog(this, "Enter price:"));
        double quantity = Double.parseDouble(JOptionPane.showInputDialog(this, "Enter quantity:"));

        if (inventoryManager.createProduct(name, variant, category, price, quantity)) {
            JOptionPane.showMessageDialog(this, "Product added successfully!");
            loadProducts();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add product.");
        }
    }

    private void handleUpdateProduct(ActionEvent e) {
        String name = JOptionPane.showInputDialog(this, "Enter product name to update:");
        double price = Double.parseDouble(JOptionPane.showInputDialog(this, "Enter new price:"));
        double quantity = Double.parseDouble(JOptionPane.showInputDialog(this, "Enter new quantity:"));

        if (inventoryManager.updateProduct(name, price, quantity)) {
            JOptionPane.showMessageDialog(this, "Product updated successfully!");
            loadProducts();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update product.");
        }
    }

    private void handleDeleteProduct(ActionEvent e) {
        String name = JOptionPane.showInputDialog(this, "Enter product name to delete:");
        if (inventoryManager.deleteProduct(name)) {
            JOptionPane.showMessageDialog(this, "Product deleted successfully!");
            loadProducts();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to delete product.");
        }
    }

    private void loadProducts() {
        tableModel.setRowCount(0);
        ArrayList<Product> products = inventoryManager.getAllProducts();
        for (Product p : products) {
            tableModel.addRow(new Object[]{p.getProductID(), p.getProductName(), p.getProductPrice(), p.getProductQuantity()});
        }
    }

    public static void main(String[] args) {
        InventoryManager1 inv = new InventoryManager1();
        inv.loadFromDisk();
        SwingUtilities.invokeLater(() -> new InventoryUI(inv).setVisible(true));
    }
}
