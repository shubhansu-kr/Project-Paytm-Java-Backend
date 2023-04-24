import java.io.*;
import java.sql.*;

class AuthenticationException extends Exception {
    AuthenticationException() {
    }

    AuthenticationException(String s) {
        super(s);
    }

    public String getMessage() {
        System.out.println("AuthenticationException: File: Authentication.java Method: Authentication()");
        return super.getMessage();
    }
}

class Authentication {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public int ValidateUser() throws AuthenticationException{
        int t = 0;
        try {

            System.out.println("Enter UserID");
            String userid = br.readLine();
            System.out.println("Enter Password");
            String password = br.readLine();
            Connect c = new Connect();
            Connection con = c.fetchcon();
            Statement stmt = con.createStatement();
            String q1 = "select * from bank_authentication where userid='" + userid + "' and password='" + password
                    + "'";
            ResultSet rs = stmt.executeQuery(q1);
            if (rs.next())
                t = 1;
            con.close();
        } catch (SQLException e) {
            System.out.println("UserId PassWord not Match");
            System.out.println(e);
            throw new AuthenticationException("Invalid Credentials");
        }
        catch (Exception e) {
            System.out.println(e);
            throw new AuthenticationException("Auth Error");
        }
        return t;
    }
}
