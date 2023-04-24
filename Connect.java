import java.sql.*;

class Connect {
    Connection con = null;

    public Connection fetchcon() {
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "123456");
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Error connecting");
        }
        return con;
    }
}