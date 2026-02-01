import java.util.ArrayList;

public class NewReportClass {

    public NewReportClass() {
    }

    public ArrayList<String[]> getInventoryReport(ArrayList<String> lines) {
        ArrayList<String[]> report = new ArrayList<>();
        if (lines == null || lines.isEmpty())
            return report;

        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length >= 6) {
                // New format: name,id,variant,category,price,quantity
                String name = parts[0].trim();
                String id = parts[1].trim();
                String variant = parts[2].trim();
                String category = parts[3].trim();
                String price = parts[4].trim();
                String left = parts[5].trim();
                report.add(new String[] { name, id, variant, category, price, left });
            } else if (parts.length >= 4) {
                // Legacy format: name,id,price,quantity
                String name = parts[0].trim();
                String id = parts[1].trim();
                String price = parts[2].trim();
                String left = parts[3].trim();
                report.add(new String[] { name, id, "", "", price, left });
            }
        }
        return report;
    }

    public ArrayList<String[]> getUsersReport(ArrayList<String> lines) {
        ArrayList<String[]> report = new ArrayList<>();
        if (lines == null || lines.isEmpty())
            return report;

        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length >= 4) {
                String role = parts[0].trim();
                String username = parts[1].trim();
                String password = parts[2].trim();
                String employeeId = parts[3].trim();
                report.add(new String[] { role, username, password, employeeId });
            }
        }
        return report;
    }

    public ArrayList<String[]> getSalesReport(ArrayList<String> lines) {
        ArrayList<String[]> report = new ArrayList<>();
        if (lines == null || lines.isEmpty())
            return report;

        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length >= 4) {
                try {
                    String date = parts[0].trim();
                    String product = parts[1].trim();
                    String qty = parts[2].trim();
                    String priceAtSale = parts[3].trim();
                    double revenue = Integer.parseInt(qty) * Double.parseDouble(priceAtSale);
                    report.add(new String[] { date, product, qty, String.format("%.2f", revenue) });
                } catch (NumberFormatException e) {
                    continue;
                }
            }
        }
        return report;
    }

    public double calculateTotalRevenue(ArrayList<String> lines) {
        double grandTotal = 0;
        if (lines == null || lines.isEmpty())
            return grandTotal;

        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length >= 4) {
                try {
                    int qty = Integer.parseInt(parts[2].trim());
                    double priceAtSale = Double.parseDouble(parts[3].trim());
                    grandTotal += qty * priceAtSale;
                } catch (NumberFormatException e) {
                    continue;
                }
            }
        }
        return grandTotal;
    }
}
