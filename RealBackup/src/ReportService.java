import java.util.ArrayList;

/**
 * Service implementation following Single Responsibility Principle
 * Handles all report generation operations
 */
public class ReportService implements IReportService {
    
    private final NewReportClass reportGenerator;
    
    public ReportService() {
        this.reportGenerator = new NewReportClass();
    }
    
    @Override
    public ArrayList<String[]> generateInventoryReport(ArrayList<String> inventoryData) {
        return reportGenerator.getInventoryReport(inventoryData);
    }
    
    @Override
    public ArrayList<String[]> generateSalesReport(ArrayList<String> salesData) {
        return reportGenerator.getSalesReport(salesData);
    }
}
