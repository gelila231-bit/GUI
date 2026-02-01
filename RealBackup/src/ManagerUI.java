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

<<<<<<< HEAD
        initComponents();
        loadProducts();
    }

    private void initComponents() {
        // Top panel with search and status
        JPanel topPanel = new JPanel(new BorderLayout());

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search:"));
        searchField = new JTextField(20);
        searchField.addActionListener(e -> searchProducts());
        JButton searchBtn = new JButton("Search");
        searchBtn.addActionListener(e -> searchProducts());
        JButton clearSearchBtn = new JButton("Clear");
        clearSearchBtn.addActionListener(e -> {
            searchField.setText("");
            loadProducts();
        });

        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
        searchPanel.add(clearSearchBtn);

        // Status label
        statusLabel = new JLabel("Ready");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 0));

        topPanel.add(searchPanel, BorderLayout.CENTER);
        topPanel.add(statusLabel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Table with better styling
        tableModel = new DefaultTableModel(
                new String[] { "Product ID", "Name", "Variant", "Category", "Price", "Quantity", "Status" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        productTable = new JTable(tableModel);
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productTable.setRowHeight(25);

        // Column widths
        productTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        productTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        productTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        productTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        productTable.getColumnModel().getColumn(4).setPreferredWidth(80);
        productTable.getColumnModel().getColumn(5).setPreferredWidth(80);
        productTable.getColumnModel().getColumn(6).setPreferredWidth(80);

=======
        // Table
        tableModel = new DefaultTableModel(new String[]{"Product ID", "Name", "Price", "Quantity"}, 0);
        productTable = new JTable(tableModel);
>>>>>>> parent of 3a0d056 (major changes)
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

<<<<<<< HEAD
        productPanel.add(addProductBtn);
        productPanel.add(updateProductBtn);
        productPanel.add(deleteProductBtn);
        productPanel.add(refreshBtn);

        // Report buttons (separate section)
        JPanel reportPanel = new JPanel(new FlowLayout());
        reportPanel.setBorder(BorderFactory.createTitledBorder("Reports & Alerts"));
        JButton inventoryReportBtn = new JButton("View Inventory Report");
        JButton salesReportBtn = new JButton("View Sales Report");
        JButton stockAlertBtn = new JButton("Stock Alerts (<15)");

        styleButton(inventoryReportBtn, new Color(155, 89, 182));
        styleButton(salesReportBtn, new Color(230, 126, 34));
        styleButton(stockAlertBtn, new Color(231, 76, 60));

        reportPanel.add(inventoryReportBtn);
        reportPanel.add(salesReportBtn);
        reportPanel.add(stockAlertBtn);

        mainButtonPanel.add(productPanel, BorderLayout.CENTER);
        mainButtonPanel.add(reportPanel, BorderLayout.SOUTH);
=======
        add(buttonPanel, BorderLayout.SOUTH);
>>>>>>> parent of 3a0d056 (major changes)

        // Actions
        addProductBtn.addActionListener(this::handleAddProduct);
        updateProductBtn.addActionListener(this::handleUpdateProduct);
        deleteProductBtn.addActionListener(this::handleDeleteProduct);
        refreshBtn.addActionListener(e -> loadProducts());
        inventoryReportBtn.addActionListener(e -> showInventoryReport());
        salesReportBtn.addActionListener(e -> showSalesReport());
        stockAlertBtn.addActionListener(e -> showStockAlerts());

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
<<<<<<< HEAD
            String status = "";
            double quantity = p.getProductQuantity();
            if (quantity == 0) {
                status = "Out of Stock";
            } else if (quantity < 15) {
                status = "Low Stock (<15)";
            } else {
                status = "In Stock";
            }

            tableModel.addRow(new Object[] {
                    p.getProductID(),
                    p.getProductName(),
                    p.getVariant(),
                    p.getCategory(),
                    String.format("$%.2f", p.getProductPrice()),
                    quantity,
                    status
            });
        }

        statusLabel.setText("Loaded " + products.size() + " products");
    }

    private void searchProducts() {
        String searchTerm = searchField.getText().trim().toLowerCase();
        if (searchTerm.isEmpty()) {
            loadProducts();
            return;
        }

        tableModel.setRowCount(0);
        ArrayList<Product> allProducts = manager.getInventoryManager().getAllProducts();
        int foundCount = 0;

        for (Product p : allProducts) {
            if (p.getProductName().toLowerCase().contains(searchTerm) ||
                    p.getProductID().toLowerCase().contains(searchTerm) ||
                    p.getVariant().toLowerCase().contains(searchTerm) ||
                    p.getCategory().toLowerCase().contains(searchTerm)) {

                String status = "";
                double quantity = p.getProductQuantity();
                if (quantity == 0) {
                    status = "Out of Stock";
                } else if (quantity < 15) {
                    status = "Low Stock (<15)";
                } else {
                    status = "In Stock";
                }

                tableModel.addRow(new Object[] {
                        p.getProductID(),
                        p.getProductName(),
                        p.getVariant(),
                        p.getCategory(),
                        String.format("$%.2f", p.getProductPrice()),
                        quantity,
                        status
                });
                foundCount++;
            }
        }

        statusLabel.setText("Found " + foundCount + " products matching '" + searchTerm + "'");
=======
            tableModel.addRow(new Object[]{p.getProductID(), p.getProductName(), p.getProductPrice(), p.getProductQuantity()});
        }
>>>>>>> parent of 3a0d056 (major changes)
    }

    private void showStockAlerts() {
        JDialog alertDialog = new JDialog(this, "Stock Alerts (<15 items)", false);
        alertDialog.setSize(700, 500);
        alertDialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new BorderLayout());

        // Get low stock products
        ArrayList<Product> lowStockProducts = manager.getInventoryManager().getLowStockProducts(15);

        if (lowStockProducts.isEmpty()) {
            JLabel noAlertLabel = new JLabel("✅ No products with low stock (<15 items)");
            noAlertLabel.setFont(new Font("Arial", Font.BOLD, 16));
            noAlertLabel.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(noAlertLabel, BorderLayout.CENTER);
        } else {
            // Create table for low stock products
            String[] columns = { "Product ID", "Name", "Variant", "Category", "Current Stock", "Status" };
            DefaultTableModel alertModel = new DefaultTableModel(columns, 0);
            JTable alertTable = new JTable(alertModel);
            alertTable.setRowHeight(25);

            for (Product p : lowStockProducts) {
                String status = p.getProductQuantity() == 0 ? "OUT OF STOCK" : "LOW STOCK";
                alertModel.addRow(new Object[] {
                        p.getProductID(),
                        p.getProductName(),
                        p.getVariant(),
                        p.getCategory(),
                        p.getProductQuantity(),
                        status
                });
            }

            panel.add(new JScrollPane(alertTable), BorderLayout.CENTER);

            // Add summary label
            JLabel summaryLabel = new JLabel("⚠️ " + lowStockProducts.size() + " products need restocking!");
            summaryLabel.setFont(new Font("Arial", Font.BOLD, 14));
            summaryLabel.setForeground(Color.RED);
            panel.add(summaryLabel, BorderLayout.NORTH);
        }

        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> alertDialog.dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(closeBtn);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        alertDialog.add(panel);
        alertDialog.setVisible(true);
    }

    public static void main(String[] args) {
        InventoryManager1 inv = new InventoryManager1();
        inv.loadFromDisk();
        Manager manager = new Manager("manager", "Manager@123", "M001", inv);
        SwingUtilities.invokeLater(() -> new ManagerUI(manager).setVisible(true));
    }
}
