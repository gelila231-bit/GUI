import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class ReportUI extends JFrame {
    private ReportGenerator reportGenerator;
    private JTable reportTable;
    private DefaultTableModel tableModel;

    public ReportUI(ReportGenerator reportGenerator) {
        this.reportGenerator = reportGenerator;
        setTitle("Reports Dashboard");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Layout
        setLayout(new BorderLayout());

        // Table
        tableModel = new DefaultTableModel();
        reportTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(reportTable);
        add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton inventoryReportBtn = new JButton("Inventory Report");
        JButton salesReportBtn = new JButton("Sales Report");

        buttonPanel.add(inventoryReportBtn);
        buttonPanel.add(salesReportBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        // Actions
        inventoryReportBtn.addActionListener(e -> showInventoryReport());
        salesReportBtn.addActionListener(e -> showSalesReport());
    }

    private void showInventoryReport() {
        ArrayList<String[]> report = reportGenerator.generateInventoryReport();
        tableModel.setColumnIdentifiers(new String[]{"Name", "ID", "Price", "Quantity"});
        tableModel.setRowCount(0);
        for (String[] row : report) {
            tableModel.addRow(row);
        }
    }

    private void showSalesReport() {
        ArrayList<String[]> report = reportGenerator.generateSalesReport();
        tableModel.setColumnIdentifiers(new String[]{"Date", "Product", "Qty", "Revenue"});
        tableModel.setRowCount(0);
        for (String[] row : report) {
            tableModel.addRow(row);
        }
    }

    public static void main(String[] args) {
        InventoryManager1 inv = new InventoryManager1();
        inv.loadFromDisk();
        Manager manager = new Manager("manager", "Manager@123", "M001", inv);

        SwingUtilities.invokeLater(() -> new ReportUI(manager).setVisible(true));
    }
}
