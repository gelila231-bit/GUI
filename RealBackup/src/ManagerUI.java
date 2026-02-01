import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class ManagerUI extends JFrame {
    private Manager manager;
    private JTable productTable;
    private DefaultTableModel tableModel;
    private JLabel statusLabel;
    private JTextField searchField;

    public ManagerUI(Manager manager) {
        this.manager = manager;
        setTitle("Manager Dashboard - " + manager.getEmployeeID());
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set modern look
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            // Use default
        }

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
        tableModel = new DefaultTableModel(new String[] { "Product ID", "Name", "Price", "Quantity", "Status" }, 0) {
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
        productTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        productTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        productTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        productTable.getColumnModel().getColumn(4).setPreferredWidth(100);

        JScrollPane scrollPane = new JScrollPane(productTable);
        add(scrollPane, BorderLayout.CENTER);

        // Button panel with better organization
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createButtonPanel() {
        JPanel mainButtonPanel = new JPanel(new BorderLayout());

        // Product management buttons
        JPanel productPanel = new JPanel(new FlowLayout());
        JButton addProductBtn = new JButton("Add Product");
        JButton updateProductBtn = new JButton("Update Product");
        JButton deleteProductBtn = new JButton("Delete Product");
        JButton refreshBtn = new JButton("Refresh");

        styleButton(addProductBtn, new Color(46, 204, 113));
        styleButton(updateProductBtn, new Color(52, 152, 219));
        styleButton(deleteProductBtn, new Color(231, 76, 60));
        styleButton(refreshBtn, new Color(149, 165, 166));

        productPanel.add(addProductBtn);
        productPanel.add(updateProductBtn);
        productPanel.add(deleteProductBtn);
        productPanel.add(refreshBtn);

        // Report buttons (separate section)
        JPanel reportPanel = new JPanel(new FlowLayout());
        reportPanel.setBorder(BorderFactory.createTitledBorder("Reports"));
        JButton inventoryReportBtn = new JButton("View Inventory Report");
        JButton salesReportBtn = new JButton("View Sales Report");

        styleButton(inventoryReportBtn, new Color(155, 89, 182));
        styleButton(salesReportBtn, new Color(230, 126, 34));

        reportPanel.add(inventoryReportBtn);
        reportPanel.add(salesReportBtn);

        mainButtonPanel.add(productPanel, BorderLayout.CENTER);
        mainButtonPanel.add(reportPanel, BorderLayout.SOUTH);

        // Actions
        addProductBtn.addActionListener(this::handleAddProduct);
        updateProductBtn.addActionListener(this::handleUpdateProduct);
        deleteProductBtn.addActionListener(this::handleDeleteProduct);
        refreshBtn.addActionListener(e -> loadProducts());
        inventoryReportBtn.addActionListener(e -> showInventoryReport());
        salesReportBtn.addActionListener(e -> showSalesReport());

        return mainButtonPanel;
    }

    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void handleAddProduct(ActionEvent e) {
        JDialog dialog = new JDialog(this, "Add New Product", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        JTextField nameField = new JTextField();
        JTextField variantField = new JTextField();
        JTextField categoryField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField quantityField = new JTextField();

        panel.add(new JLabel("Product Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Variant:"));
        panel.add(variantField);
        panel.add(new JLabel("Category:"));
        panel.add(categoryField);
        panel.add(new JLabel("Price:"));
        panel.add(priceField);
        panel.add(new JLabel("Quantity:"));
        panel.add(quantityField);

        JButton addBtn = new JButton("Add Product");
        JButton cancelBtn = new JButton("Cancel");

        addBtn.addActionListener(event -> {
            try {
                String name = nameField.getText().trim();
                String variant = variantField.getText().trim();
                String category = categoryField.getText().trim();
                double price = Double.parseDouble(priceField.getText().trim());
                double quantity = Double.parseDouble(quantityField.getText().trim());

                if (name.isEmpty() || variant.isEmpty() || category.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Please fill in all fields.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (manager.addProduct(name, variant, category, price, quantity)) {
                    statusLabel.setText("Product added successfully!");
                    loadProducts();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to add product.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid number input. Please enter valid numbers.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelBtn.addActionListener(event -> dialog.dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addBtn);
        buttonPanel.add(cancelBtn);

        dialog.setLayout(new BorderLayout());
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void handleUpdateProduct(ActionEvent e) {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product to update.", "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String currentName = productTable.getValueAt(selectedRow, 1).toString();
        String currentPrice = productTable.getValueAt(selectedRow, 2).toString();
        String currentQuantity = productTable.getValueAt(selectedRow, 3).toString();

        JDialog dialog = new JDialog(this, "Update Product", true);
        dialog.setSize(350, 200);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        JTextField priceField = new JTextField(currentPrice);
        JTextField quantityField = new JTextField(currentQuantity);

        panel.add(new JLabel("Product:"));
        panel.add(new JLabel(currentName));
        panel.add(new JLabel("New Price:"));
        panel.add(priceField);
        panel.add(new JLabel("New Quantity:"));
        panel.add(quantityField);

        JButton updateBtn = new JButton("Update");
        JButton cancelBtn = new JButton("Cancel");

        updateBtn.addActionListener(event -> {
            try {
                double price = Double.parseDouble(priceField.getText().trim());
                double quantity = Double.parseDouble(quantityField.getText().trim());

                if (manager.updateProduct(currentName, price, quantity)) {
                    statusLabel.setText("Product updated successfully!");
                    loadProducts();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to update product.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid number input.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelBtn.addActionListener(event -> dialog.dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(updateBtn);
        buttonPanel.add(cancelBtn);

        dialog.setLayout(new BorderLayout());
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void handleDeleteProduct(ActionEvent e) {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product to delete.", "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String productName = productTable.getValueAt(selectedRow, 1).toString();

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete '" + productName + "'?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (manager.deleteProduct(productName)) {
                statusLabel.setText("Product deleted successfully!");
                loadProducts();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete product.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showInventoryReport() {
        ArrayList<String[]> report = manager.generateInventoryReport();

        JDialog reportDialog = new JDialog(this, "Inventory Report", false);
        reportDialog.setSize(600, 400);
        reportDialog.setLocationRelativeTo(this);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        StringBuilder reportText = new StringBuilder();
        reportText.append("INVENTORY REPORT\n");
        reportText.append("================\n\n");

        for (String[] row : report) {
            reportText.append(String.join(" | ", row)).append("\n");
        }

        textArea.setText(reportText.toString());

        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> reportDialog.dispose());

        reportDialog.setLayout(new BorderLayout());
        reportDialog.add(new JScrollPane(textArea), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(closeBtn);
        reportDialog.add(buttonPanel, BorderLayout.SOUTH);

        reportDialog.setVisible(true);
    }

    private void showSalesReport() {
        ArrayList<String[]> report = manager.generateSalesReport();

        JDialog reportDialog = new JDialog(this, "Sales Report", false);
        reportDialog.setSize(600, 400);
        reportDialog.setLocationRelativeTo(this);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        StringBuilder reportText = new StringBuilder();
        reportText.append("SALES REPORT\n");
        reportText.append("============\n\n");

        for (String[] row : report) {
            reportText.append(String.join(" | ", row)).append("\n");
        }

        textArea.setText(reportText.toString());

        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> reportDialog.dispose());

        reportDialog.setLayout(new BorderLayout());
        reportDialog.add(new JScrollPane(textArea), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(closeBtn);
        reportDialog.add(buttonPanel, BorderLayout.SOUTH);

        reportDialog.setVisible(true);
    }

    private void loadProducts() {
        tableModel.setRowCount(0);
        ArrayList<Product> products = manager.getInventoryManager().getAllProducts();

        for (Product p : products) {
            String status = "";
            double quantity = p.getProductQuantity();
            if (quantity == 0) {
                status = "Out of Stock";
            } else if (quantity < 10) {
                status = "Low Stock";
            } else {
                status = "In Stock";
            }

            tableModel.addRow(new Object[] {
                    p.getProductID(),
                    p.getProductName(),
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
                    p.getProductID().toLowerCase().contains(searchTerm)) {

                String status = "";
                double quantity = p.getProductQuantity();
                if (quantity == 0) {
                    status = "Out of Stock";
                } else if (quantity < 10) {
                    status = "Low Stock";
                } else {
                    status = "In Stock";
                }

                tableModel.addRow(new Object[] {
                        p.getProductID(),
                        p.getProductName(),
                        String.format("$%.2f", p.getProductPrice()),
                        quantity,
                        status
                });
                foundCount++;
            }
        }

        statusLabel.setText("Found " + foundCount + " products matching '" + searchTerm + "'");
    }

    public static void main(String[] args) {
        InventoryManager1 inv = new InventoryManager1();
        inv.loadFromDisk();
        Manager manager = new Manager("manager", "Manager@123", "M001", inv);
        SwingUtilities.invokeLater(() -> new ManagerUI(manager).setVisible(true));
    }
}
