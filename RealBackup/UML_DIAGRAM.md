# UML Class Diagram - Inventory Management System

```mermaid
classDiagram
    %% User Hierarchy
    class User {
        -String username
        -String hashedPassword
        -boolean loggedIn
        +User(String username, String password)
        +User(String username, String hashedPassword, boolean alreadyHashed)
        +String getUsername()
        +void setPassword(String newpass)
        +boolean verifyPassword(String password)
        +boolean isLoggedIn()
        +void setLoggedIn(boolean loggedIn)
        +abstract boolean login(String username, String password)
        +String toFileString()
        +String getShortHash()
        +String getIdForFile()
    }

    class Employee {
        -String employeeID
        +Employee(String username, String password, String employeeID)
        +Employee(String username, String hashedPassword, String employeeID, boolean alreadyHashed)
        +String getEmployeeID()
        +boolean login(String username, String password)
    }

    class Admin {
        -ArrayList~User~ users
        -FileHandler~String~ fileHandler
        -InventoryManager1 masterInventory
        +Admin(String username, String password, FileHandler fileHandler, InventoryManager1 inv)
        +Admin(String username, String hashedPassword, FileHandler fileHandler, InventoryManager1 inv, boolean alreadyHashed)
        +boolean login(String username, String password)
        +User createUser(String role, String username, String password, String employeeId)
        +boolean resetPassword(User user, String newPass)
        +boolean deleteUser(String username)
        +void saveUsersToFile()
        +void loadUsersFromFile()
        +ArrayList~User~ getAllUsers()
        +User authenticate(String username, String password)
    }

    class Manager {
        -InventoryManager1 inventoryManager
        -NewReportClass reportClass
        +Manager(String username, String password, String employeeId, InventoryManager1 inventoryManager)
        +Manager(String username, String hashedPassword, String employeeId, InventoryManager1 inventoryManager, boolean alreadyHashed)
        +boolean addProduct(String newProductName, String variant, String category, double productPrice, double productQuantity)
        +boolean updateProduct(String productName, double newPrice, double newQuantity)
        +boolean deleteProduct(String productName)
        +ArrayList~Product~ getAllProducts()
        +boolean changeProductPrice(String productName, double newPrice)
        +boolean changeProductQuantity(String productName, int newQuantity)
        +InventoryManager1 getInventoryManager()
        +ArrayList~String[]~ generateInventoryReport()
        +ArrayList~String[]~ generateSalesReport()
    }

    class SalesEmployee {
        -FileHandler~TransactionRec~ fileHandler
        -ArrayList~TransactionRec~ transactionsRecord
        -InventoryManager1 inventoryManager
        +SalesEmployee(String username, String password, String employeeID, FileHandler fileHandler, InventoryManager1 inventoryManager)
        +SalesEmployee(String username, String hashedPassword, String employeeID, FileHandler fileHandler, InventoryManager1 inventoryManager, boolean alreadyHashed)
        +void recordTransaction(Product product, int quantity, double price)
        +boolean sellProduct(String productName, int quantity)
        +boolean processReturn(Product product, int quantity)
        +double calculateTotalSales()
        +void loadTransactionsFromDisk()
        +ArrayList~TransactionRec~ getTransactionsRecord()
        +ArrayList~TransactionRec~ getAllTransactions()
        +InventoryManager1 getInventoryManager()
    }

    %% Core Classes
    class Product {
        -String productName
        -String productID
        -String variant
        -String category
        -double productPrice
        -double productQuantity
        +Product(String productName, String productID, String variant, String category, double productPrice, double productQuantity)
        +String getProductName()
        +String getProductID()
        +String getVariant()
        +String getCategory()
        +double getProductPrice()
        +double getProductQuantity()
        +void setProductPrice(double productPrice)
        +void setProductQuantity(double productQuantity)
        +String toString()
        +static Product fromString(String line)
    }

    class InventoryManager1 {
        -ArrayList~Product~ products
        -FileHandler~Product~ filehandler
        -productIdGenerator idGenerator
        +boolean createProduct(String newProductName, String variant, String category, double productPrice, double productQuantity)
        +ArrayList~Product~ readProduct(String productName)
        +ArrayList~Product~ readProduct(String productName, String variant, String category)
        +Product findProductById(String productID)
        +String getSelectedProductID(ArrayList~Product~ matches, String productName)
        +ArrayList~Product~ getLowStockProducts(int threshold)
        +boolean isLowStock(String productID, int threshold)
        +boolean updateProduct(String productName, double newPrice, double newQuantity)
        +boolean updateProductPrice(String productName, double newPrice)
        +boolean updateProductQuantity(String productName, int newQuantity)
        +boolean deleteProduct(String productName)
        +ArrayList~Product~ getAllProducts()
        +ArrayList~String~ getInventoryData()
        +ArrayList~String~ getSalesData()
        +void saveToDisk()
        +void loadFromDisk()
    }

    class TransactionRec {
        -String productName
        -int quantity
        -double priceAtSale
        -String date
        +TransactionRec(String productName, int quantity, double priceAtSale)
        +TransactionRec(String productName, int quantity, double priceAtSale, String date)
        +String getProductName()
        +int getQuantity()
        +double getPriceAtSale()
        +String getDate()
        +double calculateTotalInTransaction()
        +String toString()
        +static TransactionRec fromString(String line)
    }

    %% Utility Classes
    class FileHandler~T~ {
        +FileHandler()
        +ArrayList~T~ readFromFile(String filename)
        +void writeToFile(String filename, ArrayList~T~ data, boolean append)
        +void writeToFile(String filename, T data, boolean append)
    }

    class productIdGenerator {
        -static int counter
        +String generateId(String name, String variant, String category)
        +static void setCounter(int lastId)
    }

    class NewReportClass {
        +NewReportClass()
        +ArrayList~String[]~ getInventoryReport(ArrayList~String~ lines)
        +ArrayList~String[]~ getUsersReport(ArrayList~String~ lines)
        +ArrayList~String[]~ getSalesReport(ArrayList~String~ lines)
        +double calculateTotalRevenue(ArrayList~String~ lines)
    }

    class PasswordValidator {
        -static String SPECIAL_CHARS
        -static int MIN_LENGTH
        +static boolean isValid(String password)
        +static String getRequirements()
    }

    %% UI Classes
    class MainApp {
        -static Admin admin
        +static void main(String[] args)
        +static void showLoginDialog()
        +static void launchDashboard(User user)
    }

    class AdminUI {
        -Admin admin
        -JTable userTable
        -DefaultTableModel tableModel
        -JLabel statusLabel
        -JTextField searchField
        +AdminUI(Admin admin)
        +void initComponents()
        +JPanel createButtonPanel()
        +void styleButton(JButton button, Color color)
        +void handleAddUser(ActionEvent e)
        +void handleDeleteUser(ActionEvent e)
        +void handleResetPassword(ActionEvent e)
        +void loadUsers()
        +void searchUsers()
        +void showSystemInfo()
    }

    class ManagerUI {
        -Manager manager
        -JTable productTable
        -DefaultTableModel tableModel
        -JLabel statusLabel
        -JTextField searchField
        +ManagerUI(Manager manager)
        +void initComponents()
        +JPanel createButtonPanel()
        +void styleButton(JButton button, Color color)
        +void handleAddProduct(ActionEvent e)
        +void handleUpdateProduct(ActionEvent e)
        +void handleDeleteProduct(ActionEvent e)
        +void loadProducts()
        +void searchProducts()
        +void showInventoryReport()
        +void showSalesReport()
        +void showStockAlerts()
    }

    class SalesEmployeeUI {
        -SalesEmployee salesEmployee
        -JTable productTable
        -JTable transactionTable
        -DefaultTableModel productModel
        -DefaultTableModel transactionModel
        -JLabel statusLabel
        -JTextField searchField
        -JLabel totalSalesLabel
        +SalesEmployeeUI(SalesEmployee salesEmployee)
        +void initComponents()
        +JPanel createButtonPanel()
        +void styleButton(JButton button, Color color)
        +void handleSellProduct(ActionEvent e)
        +void handleViewSalesSummary(ActionEvent e)
        +void loadProducts()
        +void loadTransactions()
        +void searchProducts()
        +void updateSalesSummary()
    }

    %% Interfaces
    class IInventoryService {
        <<interface>>
        +boolean createProduct(String name, String variant, String category, double price, double quantity)
        +boolean updateProduct(String productName, double newPrice, double newQuantity)
        +boolean updateProductPrice(String productName, double newPrice)
        +boolean updateProductQuantity(String productName, int newQuantity)
        +boolean deleteProduct(String productName)
        +Product findProductById(String productID)
        +ArrayList~Product~ getAllProducts()
        +ArrayList~String~ getInventoryData()
        +ArrayList~String~ getSalesData()
        +ArrayList~Product~ readProduct(String productName)
        +String getSelectedProductID(ArrayList~Product~ matches, String productName)
    }

    class IUserService {
        <<interface>>
        +User createUser(String role, String username, String password, String employeeId)
        +boolean deleteUser(String username)
        +boolean resetPassword(User user, String newPassword)
        +ArrayList~User~ getAllUsers()
        +User authenticate(String username, String password)
        +void saveUsers()
        +void loadUsers()
    }

    class IReportService {
        <<interface>>
        +ArrayList~String[]~ generateInventoryReport(ArrayList~String~ inventoryData)
        +ArrayList~String[]~ generateSalesReport(ArrayList~String~ salesData)
    }

    class ReportGenerator {
        <<interface>>
        +ArrayList~String[]~ generateInventoryReport()
        +ArrayList~String[]~ generateSalesReport()
    }

    class ProductViewer {
        <<interface>>
        +String viewProductDetails(Product p)
        +Product searchProduct(String id, InventoryManager1 inv)
        +double checkProductPrice(Product p)
        +double checkProductQuantity(Product p)
    }

    class TransactionHandler {
        <<interface>>
        +void recordTransaction(Product product, int quantity, double price)
        +boolean sellProduct(String productName, int quantity)
        +boolean processReturn(Product product, int quantity)
    }

    %% SOLID Implementation Classes
    class AdminSOLID {
        -IUserService userService
        -IInventoryService inventoryService
        +AdminSOLID(String username, String password, IUserService userService, IInventoryService inventoryService)
        +AdminSOLID(String username, String hashedPassword, IUserService userService, IInventoryService inventoryService, boolean alreadyHashed)
        +boolean login(String username, String password)
        +User createUser(String role, String username, String password, String employeeId)
        +boolean deleteUser(String username)
        +boolean resetPassword(User user, String newPass)
        +ArrayList~User~ getAllUsers()
        +User authenticate(String username, String password)
        +void loadUsers()
        +IUserService getUserService()
        +IInventoryService getInventoryService()
    }

    class ManagerSOLID {
        -IInventoryService inventoryService
        -IReportService reportService
        +ManagerSOLID(String username, String password, String employeeId, IInventoryService inventoryService, IReportService reportService)
        +ManagerSOLID(String username, String hashedPassword, String employeeId, IInventoryService inventoryService, IReportService reportService, boolean alreadyHashed)
        +boolean addProduct(String newProductName, String variant, String category, double productPrice, double productQuantity)
        +boolean updateProduct(String productName, double newPrice, double newQuantity)
        +boolean deleteProduct(String productName)
        +ArrayList~Product~ getAllProducts()
        +boolean changeProductPrice(String productName, double newPrice)
        +boolean changeProductQuantity(String productName, int newQuantity)
        +ArrayList~String[]~ generateInventoryReport()
        +ArrayList~String[]~ generateSalesReport()
        +IInventoryService getInventoryService()
        +IReportService getReportService()
    }

    class SalesEmployeeSOLID {
        -FileHandler~TransactionRec~ fileHandler
        -ArrayList~TransactionRec~ transactionsRecord
        -IInventoryService inventoryService
        +SalesEmployeeSOLID(String username, String password, String employeeID, FileHandler fileHandler, IInventoryService inventoryService)
        +SalesEmployeeSOLID(String username, String hashedPassword, String employeeID, FileHandler fileHandler, IInventoryService inventoryService, boolean alreadyHashed)
        +void recordTransaction(Product product, int quantity, double price)
        +boolean sellProduct(String productName, int quantity)
        +boolean processReturn(Product product, int quantity)
        +double calculateTotalSales()
        +void loadTransactionsFromDisk()
        +ArrayList~TransactionRec~ getTransactionsRecord()
        +ArrayList~TransactionRec~ getAllTransactions()
        +IInventoryService getInventoryService()
    }

    class UserService {
        -ArrayList~User~ users
        -FileHandler~String~ fileHandler
        -IInventoryService inventoryService
        +UserService(IInventoryService inventoryService)
        +User createUser(String role, String username, String password, String employeeId)
        +boolean deleteUser(String username)
        +boolean resetPassword(User user, String newPassword)
        +ArrayList~User~ getAllUsers()
        +User authenticate(String username, String password)
        +void saveUsers()
        +void loadUsers()
    }

    class ReportService {
        -NewReportClass reportGenerator
        +ReportService()
        +ArrayList~String[]~ generateInventoryReport(ArrayList~String~ inventoryData)
        +ArrayList~String[]~ generateSalesReport(ArrayList~String~ salesData)
    }

    class UserFactory {
        +enum UserRole { ADMIN, MANAGER, SALES_EMPLOYEE }
        +static User createUser(String role, String username, String password, String employeeId, IInventoryService inventoryService)
    }

    %% Inheritance Relationships
    User <|-- Employee
    Employee <|-- Admin
    Employee <|-- Manager
    Employee <|-- SalesEmployee

    %% Interface Implementations
    Manager ..|> ReportGenerator
    Manager ..|> ProductViewer
    SalesEmployee ..|> TransactionHandler
    SalesEmployee ..|> ProductViewer

    %% SOLID Implementations
    AdminSOLID ..|> User
    ManagerSOLID ..|> Employee
    ManagerSOLID ..|> ReportGenerator
    ManagerSOLID ..|> ProductViewer
    SalesEmployeeSOLID ..|> Employee
    SalesEmployeeSOLID ..|> TransactionHandler
    SalesEmployeeSOLID ..|> ProductViewer

    %% Service Dependencies
    AdminSOLID --> IUserService
    AdminSOLID --> IInventoryService
    ManagerSOLID --> IInventoryService
    ManagerSOLID --> IReportService
    SalesEmployeeSOLID --> IInventoryService
    UserService ..|> IUserService
    ReportService ..|> IReportService

    %% Core Dependencies
    Admin --> FileHandler
    Admin --> InventoryManager1
    Manager --> InventoryManager1
    Manager --> NewReportClass
    SalesEmployee --> FileHandler
    SalesEmployee --> InventoryManager1
    InventoryManager1 --> FileHandler
    InventoryManager1 --> productIdGenerator
    NewReportClass --> productIdGenerator

    %% UI Dependencies
    MainApp --> Admin
    MainApp --> AdminUI
    MainApp --> ManagerUI
    MainApp --> SalesEmployeeUI
    AdminUI --> Admin
    ManagerUI --> Manager
    SalesEmployeeUI --> SalesEmployee

    %% Data Relationships
    InventoryManager1 --> Product
    SalesEmployee --> TransactionRec
    TransactionRec --> Product

    %% Factory Pattern
    UserFactory --> User
    UserFactory --> AdminSOLID
    UserFactory --> ManagerSOLID
    UserFactory --> SalesEmployeeSOLID
```

## Key Design Patterns Used:

### 1. **Inheritance Hierarchy**
- `User` (abstract base class)
  - `Employee` (extends User)
    - `Admin` (extends Employee)
    - `Manager` (extends Employee)
    - `SalesEmployee` (extends Employee)

### 2. **Interface Segregation**
- `ReportGenerator` - For reporting functionality
- `ProductViewer` - For product viewing
- `TransactionHandler` - For transaction management
- `IInventoryService` - For inventory operations
- `IUserService` - For user management
- `IReportService` - For report generation

### 3. **Factory Pattern**
- `UserFactory` - Creates different user types based on role

### 4. **Service Layer Pattern**
- `UserService` - Handles user operations
- `ReportService` - Handles report generation
- SOLID versions use dependency injection

### 5. **Generic Utility Class**
- `FileHandler<T>` - Generic file operations for any type

### 6. **MVC Pattern**
- **Model:** Business logic classes (Admin, Manager, SalesEmployee, etc.)
- **View:** UI classes (AdminUI, ManagerUI, SalesEmployeeUI)
- **Controller:** MainApp (coordinates between model and view)

## Relationships Summary:

- **Composition:** Admin contains Users, Manager contains InventoryManager1
- **Association:** SalesEmployee works with Products and Transactions
- **Dependency:** UI classes depend on business logic classes
- **Implementation:** Classes implement various interfaces for functionality
- **Generalization:** Inheritance hierarchy from User to specific roles

This UML diagram shows the complete structure of your inventory management system with both the original implementation and the SOLID principles-compliant version.
