import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class SalesEmployeeUI extends JFrame {
    private SalesEmployee salesEmployee;
    private JTable transactionTable;
    private JTable productTable;
    private DefaultTableModel transactionModel;
    private DefaultTableModel productModel;
    private JLabel statusLabel;
    private JTextField searchField;
    private JLabel totalSalesLabel;

    public SalesEmployeeUI(SalesEmployee salesEmployee) {
        this.salesEmployee = salesEmployee;
        setTitle("Sales Employee Dashboard - " + salesEmployee.getEmployeeID());
        setSize(1200, 800);
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
        loadTransactions();
        updateTotalSales();
    }

    private void initComponents() {
        // Top panel with search and status
        JPanel topPanel = new JPanel(new BorderLayout());

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search Products:"));
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

        // Status and sales info
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        statusLabel = new JLabel("Ready");
        totalSalesLabel = new JLabel("Total Sales: $0.00");
        totalSalesLabel.setFont(new Font("Arial", Font.BOLD, 14));
        totalSalesLabel.setForeground(new Color(46, 204, 113));
        infoPanel.add(statusLabel);
        infoPanel.add(Box.createHorizontalStrut(20));
        infoPanel.add(totalSalesLabel);

        topPanel.add(searchPanel, BorderLayout.CENTER);
        topPanel.add(infoPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Main content area with split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

        // Products panel
        JPanel productsPanel = createProductsPanel();

        // Transactions panel
        JPanel transactionsPanel = createTransactionsPanel();

        splitPane.setTopComponent(productsPanel);
        splitPane.setBottomComponent(transactionsPanel);
        splitPane.setDividerLocation(400);
        splitPane.setResizeWeight(0.5);

        add(splitPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createProductsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Available Products"));

        productModel = new DefaultTableModel(new String[] { "Product ID", "Name", "Price", "Stock", "Status" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        productTable = new JTable(productModel);
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productTable.setRowHeight(25);

        // Column widths
        productTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        productTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        productTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        productTable.getColumnModel().getColumn(3).setPreferredWidth(80);
        productTable.getColumnModel().getColumn(4).setPreferredWidth(100);

        panel.add(new JScrollPane(productTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createTransactionsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Recent Transactions"));

        transactionModel = new DefaultTableModel(new String[] { "Time", "Product", "Quantity", "Price", "Total" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        transactionTable = new JTable(transactionModel);
        transactionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        transactionTable.setRowHeight(25);

        // Column widths
        transactionTable.getColumnModel().getColumn(0).setPreferredWidth(120);
        transactionTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        transactionTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        transactionTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        transactionTable.getColumnModel().getColumn(4).setPreferredWidth(100);

        panel.add(new JScrollPane(transactionTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton sellProductBtn = new JButton("Sell Product");
        JButton refreshBtn = new JButton("Refresh All");
        JButton viewSalesBtn = new JButton("View Sales Summary");

        styleButton(sellProductBtn, new Color(46, 204, 113));
        styleButton(refreshBtn, new Color(52, 152, 219));
        styleButton(viewSalesBtn, new Color(155, 89, 182));

        buttonPanel.add(sellProductBtn);
        buttonPanel.add(refreshBtn);
        buttonPanel.add(viewSalesBtn);

        // Actions
        sellProductBtn.addActionListener(this::handleSellProduct);
        refreshBtn.addActionListener(e -> {
            loadProducts();
            loadTransactions();
            updateTotalSales();
        });
        viewSalesBtn.addActionListener(e -> showSalesSummary());

        return buttonPanel;
    }

    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void handleSellProduct(ActionEvent e) {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product to sell.", "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String productName = productTable.getValueAt(selectedRow, 1).toString();
        String currentStock = productTable.getValueAt(selectedRow, 3).toString();
        double price = Double.parseDouble(productTable.getValueAt(selectedRow, 2).toString().replace("$", ""));

        JDialog dialog = new JDialog(this, "Sell Product", true);
        dialog.setSize(350, 200);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));

        panel.add(new JLabel("Product:"));
        panel.add(new JLabel(productName));
        panel.add(new JLabel("Price:"));
        panel.add(new JLabel(String.format("$%.2f", price)));
        panel.add(new JLabel("Available Stock:"));
        panel.add(new JLabel(currentStock));
        panel.add(new JLabel("Quantity to Sell:"));
        JTextField quantityField = new JTextField();
        panel.add(quantityField);

        JButton sellBtn = new JButton("Sell");
        JButton cancelBtn = new JButton("Cancel");

        sellBtn.addActionListener(event -> {
            try {
                int quantity = Integer.parseInt(quantityField.getText().trim());

                if (quantity <= 0) {
                    JOptionPane.showMessageDialog(dialog, "Quantity must be greater than 0.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (salesEmployee.sellProduct(productName, quantity)) {
                    double total = price * quantity;
                    statusLabel.setText(
                            "Sold " + quantity + " x " + productName + " for $" + String.format("%.2f", total));
                    loadProducts();
                    loadTransactions();
                    updateTotalSales();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Sale failed. Check stock availability.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid quantity.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelBtn.addActionListener(event -> dialog.dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(sellBtn);
        buttonPanel.add(cancelBtn);

        dialog.setLayout(new BorderLayout());
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showSalesSummary() {
        double total = salesEmployee.calculateTotalSales();
        ArrayList<TransactionRec> transactions = salesEmployee.getAllTransactions();

        JDialog summaryDialog = new JDialog(this, "Sales Summary", false);
        summaryDialog.setSize(500, 400);
        summaryDialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new BorderLayout());

        // Summary info
        JPanel summaryPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        summaryPanel.add(new JLabel("Total Sales Revenue:"));
        JLabel totalLabel = new JLabel(String.format("$%.2f", total));
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalLabel.setForeground(new Color(46, 204, 113));
        summaryPanel.add(totalLabel);

        summaryPanel.add(new JLabel("Total Transactions:"));
        summaryPanel.add(new JLabel(String.valueOf(transactions.size())));

        int totalItemsSold = transactions.stream().mapToInt(t -> Math.abs(t.getQuantity())).sum();
        summaryPanel.add(new JLabel("Total Items Sold:"));
        summaryPanel.add(new JLabel(String.valueOf(totalItemsSold)));

        panel.add(summaryPanel, BorderLayout.NORTH);

        // Recent transactions
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        StringBuilder reportText = new StringBuilder();
        reportText.append("RECENT TRANSACTIONS\n");
        reportText.append("===================\n\n");

        int count = 0;
        for (int i = transactions.size() - 1; i >= 0 && count < 10; i--, count++) {
            TransactionRec t = transactions.get(i);
            reportText.append(String.format("%-20s %3d x $%8.2f = $%9.2f\n",
                    t.getProductName(), Math.abs(t.getQuantity()),
                    t.getPrice(), Math.abs(t.calculateTotalInTransaction())));
        }

        textArea.setText(reportText.toString());
        panel.add(new JScrollPane(textArea), BorderLayout.CENTER);

        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> summaryDialog.dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(closeBtn);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        summaryDialog.add(panel);
        summaryDialog.setVisible(true);
    }

    private void loadProducts() {
        productModel.setRowCount(0);
        ArrayList<Product> products = salesEmployee.getInventoryManager().getAllProducts();

        for (Product p : products) {
            String status = "";
            double quantity = p.getProductQuantity();
            if (quantity == 0) {
                status = "Out of Stock";
            } else if (quantity < 5) {
                status = "Low Stock";
            } else {
                status = "Available";
            }

            productModel.addRow(new Object[] {
                    p.getProductID(),
                    p.getProductName(),
                    String.format("$%.2f", p.getProductPrice()),
                    (int) quantity,
                    status
            });
        }
    }

    private void loadTransactions() {
        transactionModel.setRowCount(0);
        ArrayList<TransactionRec> transactions = salesEmployee.getAllTransactions();

        // Show last 20 transactions
        int start = Math.max(0, transactions.size() - 20);
        for (int i = transactions.size() - 1; i >= start; i--) {
            TransactionRec t = transactions.get(i);
            transactionModel.addRow(new Object[] {
                    t.getProductName().contains("_") ? t.getProductName().split("_")[1] : "Recent",
                    t.getProductName(),
                    Math.abs(t.getQuantity()),
                    String.format("$%.2f", t.getPrice()),
                    String.format("$%.2f", Math.abs(t.calculateTotalInTransaction()))
            });
        }
    }

    private void updateTotalSales() {
        double total = salesEmployee.calculateTotalSales();
        totalSalesLabel.setText("Total Sales: $" + String.format("%.2f", total));
    }

    private void searchProducts() {
        String searchTerm = searchField.getText().trim().toLowerCase();
        if (searchTerm.isEmpty()) {
            loadProducts();
            return;
        }

        productModel.setRowCount(0);
        ArrayList<Product> allProducts = salesEmployee.getInventoryManager().getAllProducts();
        int foundCount = 0;

        for (Product p : allProducts) {
            if (p.getProductName().toLowerCase().contains(searchTerm) ||
                    p.getProductID().toLowerCase().contains(searchTerm)) {

                String status = "";
                double quantity = p.getProductQuantity();
                if (quantity == 0) {
                    status = "Out of Stock";
                } else if (quantity < 5) {
                    status = "Low Stock";
                } else {
                    status = "Available";
                }

                productModel.addRow(new Object[] {
                        p.getProductID(),
                        p.getProductName(),
                        String.format("$%.2f", p.getProductPrice()),
                        (int) quantity,
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
        FileHandler<TransactionRec> fileHandler = new FileHandler<>();
        SalesEmployee salesEmployee = new SalesEmployee("sales", "Sales@123", "S001", fileHandler, inv);

        SwingUtilities.invokeLater(() -> new SalesEmployeeUI(salesEmployee).setVisible(true));
    }
}
