import java.util.ArrayList;

public interface IReportService {
    ArrayList<String[]> generateInventoryReport(ArrayList<String> inventoryData);
    ArrayList<String[]> generateSalesReport(ArrayList<String> salesData);
}
