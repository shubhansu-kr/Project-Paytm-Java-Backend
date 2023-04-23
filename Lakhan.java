import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

class DatabaseConnectivity {
    private static Connection connection;
    private static DatabaseConnectivity db=null;

    private DatabaseConnectivity() {}
    public static DatabaseConnectivity getDBInstance(){
        if(db==null){
            db = new DatabaseConnectivity();
        }
        return db;
    }
    public static Connection getDatabase() {
        try{
            connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/billing","root","12345");
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return connection;
    }
}



public class ShoppingBill {
    public static void main(String []args) {
        // variables
        String id = null;
        String productName = null;
        int loyaltyPoints = 0;
        int newCustomer = 0;
        int quantity = 0;
        double price = 0.0;
        double totalPrice = 0.0;
        double overAllPrice = 0.0;
        double cgst, sgst, subtotal=0.0, discount=0.0,ldiscount=0.0;
        char choice = '\0';
        Scanner scanner = new Scanner(System.in);

        System.out.println("\t\t\t------------------Invoice-----------------");
        System.out.println("\t\t\t\t\t\t    "+"PLT MegaStore");
        System.out.println("\t\t\t\t\tModel Town,Jalandhar,Punjab\n");
        System.out.println("GSTIN: 14GSHEF8756K592"+"\t\t\t\t\t\t\tContact: (+91) 8839059121");

        //format of date and time
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        System.out.println("Date: "+formatter.format(date)+"\t\t\t\t\t\t\t (+91) 9350498328\n");
        System.out.println("Enter mobile number: ");
        long mobileNumber = scanner.nextLong();
        Customer customer=null;

        //Database connectivity
        Statement stmt;
        PreparedStatement pstmt;
        try {
            stmt = DatabaseConnectivity.getDatabase().createStatement();
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        try {
            ResultSet rs = stmt.executeQuery("select * from customers where mobile="+mobileNumber);
            if(rs.next()==false){
                scanner.nextLine();
                System.out.print("Enter name: ");
                String customerName = scanner.nextLine();
                customer = new Customer(customerName);
                newCustomer = 1;
            }else{
                do{
                    String name = rs.getString("name");
                    customer = new Customer(name);
                }while(rs.next());
            }
        }catch(SQLException e){
            System.out.println("Fetch error");
            System.out.println(e.getMessage());
        }
        List<Product> productsList = new ArrayList<Product>();
        System.out.println("Please enter product details ----------");
        do{
            try{
                System.out.print("Enter Product ID: ");
                id = scanner.next();
                System.out.print("Enter Product Name: ");
                productName = scanner.next();
                System.out.print("Enter Quantity: ");
                quantity = scanner.nextInt();
                System.out.print("Enter Price: ");
                price = scanner.nextDouble();
                totalPrice = price * quantity;
                Product product = new Product(id, productName, quantity, price, totalPrice);
                productsList.add(product);
                System.out.println("Do you want to add more products? (y/n)");
                choice = scanner.next().charAt(0);
            }catch(InputMismatchException ime){
                System.out.println("Please enter the correct values");
            }catch(Exception e){
                System.out.println("An error occurred! Please try again.");
            }
        }while(choice == 'y' || choice == 'Y');

        Product.displayFormat();
        for(Product product : productsList) {
            product.display();
            subtotal += product.getTotalPrice();
        }
        try{
            if(subtotal >= 1000 && subtotal < 5000) {
                discount = 0.05 * subtotal;
                customer.addPoints((int)subtotal/100);
                loyaltyPoints += customer.getPoints();
            } else if(subtotal >= 5000 && subtotal < 10000) {
                discount = 0.1 * subtotal;
                customer.addPoints((int)subtotal/50);
                loyaltyPoints += customer.getPoints();
            } else if(subtotal >= 10000) {
                discount = 0.15 * subtotal;
                customer.addPoints((int)subtotal/20);
                loyaltyPoints += customer.getPoints();
            }
        }catch(NullPointerException npe){
            System.out.println(npe.getMessage());
        }

        System.out.format("\nSub Total : \t\t\t\t\t\t\t\t\t%.2f\n", subtotal);
        cgst = subtotal * 0.025;
        sgst = subtotal * 0.025;
        overAllPrice = subtotal + cgst + sgst - discount - ldiscount;
        System.out.format("CGST : \t\t\t\t\t\t\t\t\t\t    %.2f\n",cgst);
        System.out.format("SGST : \t\t\t\t\t\t\t\t\t\t\t%.2f\n",sgst);
        System.out.format("Discount : \t\t\t\t\t\t\t\t\t\t%.2f\n",discount);
        System.out.format("Total : \t\t\t\t\t\t\t\t\t\t%.2f\n",overAllPrice);
        System.out.println("\nEarned Loyalty Points: " + customer.getPoints());
        System.out.println("\t\t\t\t----------------Thank You for the Shopping!!-----------------");
        System.out.println("\t\t\t\t                     Visit Again");
        try{
            if(newCustomer==1){
                pstmt = DatabaseConnectivity.getDatabase().prepareStatement("INSERT INTO customers VALUES(?,?,?,?)");
                pstmt.setLong(1,mobileNumber);
                pstmt.setString(2,customer.getName());
                pstmt.setInt(3,loyaltyPoints);
                pstmt.setInt(4,(int)subtotal);
                pstmt.execute();
            }else{
                ResultSet rs = stmt.executeQuery("select * from customers where mobile="+mobileNumber);
                rs.next();
                int totalPurchase = (int)subtotal+rs.getInt(4);
                loyaltyPoints += rs.getInt(3);
                pstmt = DatabaseConnectivity.getDatabase().prepareStatement("UPDATE customers set loyalty_points=?, total_purchase=? WHERE mobile=?");
                pstmt.setInt(1,loyaltyPoints);
                pstmt.setInt(2,totalPurchase);
                pstmt.setLong(3,mobileNumber);
                pstmt.execute();
            }
        }catch(Exception e){
            System.out.println("Update error");
            System.out.println(e);
        }
    }
}