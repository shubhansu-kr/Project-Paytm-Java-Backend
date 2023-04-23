import java.sql.*;

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
            connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/result","root","2021");
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return connection;
    }
}

public class Main {

    public static void main(String args[]) {

        // PreparedStatement pstmt;
        Statement stmt;
        ResultSet rs;

        try {

            stmt = DatabaseConnectivity.getDatabase().createStatement();
            rs = stmt.executeQuery("select * from branch");

            while (rs.next()){
                System.out.println(rs.getInt(1) + "  " + rs.getString(2) + "  " + rs.getString(3));
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}