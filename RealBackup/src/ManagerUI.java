import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class ManagerUI extends JFrame {
    private Manager manager;
    private JTable productTable;
    private DefaultTableModel tableModel;

    public ManagerUI(Manager manager) {
        this.manager = manager;
        setTitle("Manager Dashboard");
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
        JButton inventoryReportBtn = new JButton("Inventory Report");
        JButton salesReportBtn = new JButton("Sales Report");

        buttonPanel.add(addProductBtn);
        buttonPanel.add(updateProductBtn);
        buttonPanel.add(deleteProductBtn);
        buttonPanel.add(refreshBtn);
        buttonPanel.add(inventoryReportBtn);
        buttonPanel.add(salesReportBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        // Actions
        addProductBtn.addActionListener(this::handleAddProduct);
        updateProductBtn.addActionListener(this::handleUpdateProduct);
        deleteProductBtn.addActionListener(this::handleDeleteProduct);
        refreshBtn.addActionListener(e -> loadProducts());
        inventoryReportBtn.addActionListener(e -> showInventoryReport());
        salesReportBtn.addActionListener(e -> showSalesReport());

        // Initial load
        loadProducts();
    }

    private void handleAddProduct(ActionEvent e) {
        try {
            String name = JOptionPane.showInputDialog(this, "Enter product name:");
            String variant = JOptionPane.showInputDialog(this, "Enter variant:");
            String category = JOptionPane.showInputDialog(this, "Enter category:");
            double price = Double.parseDouble(JOptionPane.showInputDialog(this, "Enter price:"));
            double quantity = Double.parseDouble(JOptionPane.showInputDialog(this, "Enter quantity:"));

            if (manager.addProduct(name, variant, category, price, quantity)) {
                JOptionPane.showMessageDialog(this, "Product added successfully!");
                loadProducts();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add product.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number input. Please try again.");
        }
    }

    private void handleUpdateProduct(ActionEvent e) {
        try {
            String name = JOptionPane.showInputDialog(this, "Enter product name to update:");
            double price = Double.parseDouble(JOptionPane.showInputDialog(this, "Enter new price:"));
            double quantity = Double.parseDouble(JOptionPane.showInputDialog(this, "Enter new quantity:"));

            if (manager.updateProduct(name, price, quantity)) {
                JOptionPane.showMessageDialog(this, "Product updated successfully!");
                loadProducts();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update product.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number input. Please try again.");
        }
    }

    private void handleDeleteProduct(ActionEvent e) {
        String name = JOptionPane.showInputDialog(this, "Enter product name to delete:");
        if (manager.deleteProduct(name)) {
            JOptionPane.showMessageDialog(this, "Product deleted successfully!");
            loadProducts();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to delete product.");
        }
    }

    private void showInventoryReport() {
        ArrayList<String[]> report = manager.generateInventoryReport();
        JTextArea textArea = new JTextArea();
        for (String[] row : report) {
            textArea.append(String.join(" | ", row) + "\n");
        }
        JOptionPane.showMessageDialog(this, new JScrollPane(textArea), "Inventory Report", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showSalesReport() {
        ArrayList<String[]> report = manager.generateSalesReport();
        JTextArea textArea = new JTextArea();
        for (String[] row : report) {
            textArea.append(String.join(" | ", row) + "\n");
        }
        JOptionPane.showMessageDialog(this, new JScrollPane(textArea), "Sales Report", JOptionPane.INFORMATION_MESSAGE);
    }

    private void loadProducts() {
        tableModel.setRowCount(0);
        ArrayList<Product> products = manager.getInventoryManager().getAllProducts(); // safer access
        for (Product p : products) {
            tableModel.addRow(new Object[]{p.getProductID(), p.getProductName(), p.getProductPrice(), p.getProductQuantity()});
        }
    }

    public static void main(String[] args) {
        InventoryManager1 inv = new InventoryManager1();
        inv.loadFromDisk();
        Manager manager = new Manager("manager", "Manager@123", "M001", inv);
        SwingUtilities.invokeLater(() -> new ManagerUI(manager).setVisible(true));
    }
}
