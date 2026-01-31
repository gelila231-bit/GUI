import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class SalesEmployeeUI extends JFrame {
    private SalesEmployee salesEmployee;
    private JTable transactionTable;
    private DefaultTableModel tableModel;

    public SalesEmployeeUI(SalesEmployee salesEmployee) {
        this.salesEmployee = salesEmployee;
        setTitle("Sales Employee Dashboard");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Layout
        setLayout(new BorderLayout());

        // Table
        tableModel = new DefaultTableModel(new String[]{"Product", "Quantity", "Price", "Total"}, 0);
        transactionTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(transactionTable);
        add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton sellProductBtn = new JButton("Sell Product");
        JButton refreshBtn = new JButton("Refresh Transactions");
        JButton totalSalesBtn = new JButton("View Total Sales");

        buttonPanel.add(sellProductBtn);
        buttonPanel.add(refreshBtn);
        buttonPanel.add(totalSalesBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        // Actions
        sellProductBtn.addActionListener(this::handleSellProduct);
        refreshBtn.addActionListener(e -> loadTransactions());
        totalSalesBtn.addActionListener(e -> showTotalSales());

        // Initial load
        loadTransactions();
    }

    private void handleSellProduct(ActionEvent e) {
        String productName = JOptionPane.showInputDialog(this, "Enter product name:");
        int quantity = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter quantity:"));

        if (salesEmployee.sellProduct(productName, quantity)) {
            JOptionPane.showMessageDialog(this, "Sale recorded successfully!");
            loadTransactions();
        } else {
            JOptionPane.showMessageDialog(this, "Sale failed. Check stock or product name.");
        }
    }

    private void showTotalSales() {
        double total = salesEmployee.calculateTotalSales();
        JOptionPane.showMessageDialog(this, "Total Sales Revenue: $" + total);
    }

    private void loadTransactions() {
        tableModel.setRowCount(0);
        ArrayList<TransactionRec> transactions = salesEmployee.getAllTransactions();
        for (TransactionRec t : transactions) {
            tableModel.addRow(new Object[]{
                    t.getProductName(),
                    t.getQuantity(),
                    t.getPrice(),
                    t.calculateTotalInTransaction()
            });
        }
    }


    public static void main(String[] args) {
        InventoryManager1 inv = new InventoryManager1();
        inv.loadFromDisk();
        FileHandler<TransactionRec> fileHandler = new FileHandler<>();
        SalesEmployee salesEmployee = new SalesEmployee("sales", "Sales@123", "S001", fileHandler, inv);

        SwingUtilities.invokeLater(() -> new SalesEmployeeUI(salesEmployee).setVisible(true));
    }
}

