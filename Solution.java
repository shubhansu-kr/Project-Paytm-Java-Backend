// import java.net.URL;
import java.sql.DriverManager;

import java.sql.ResultSet;
import java.sql.Statement;

import java.sql.Connection;
// import java.util.*;

// import java.sql.jdbc.Connection;

class Solution{

    public static void main(String args[]){

        try {
            String URL = "jdbc:mysql://localhost:3306/result";
            String userName = "root";
            String password = "2021";

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection Cn = DriverManager.getConnection(URL, userName, password);

            Statement statement =  Cn.createStatement();

            ResultSet Rs = statement.executeQuery("select * from branch;");

            while(Rs.next()) {
                System.out.println(Rs.getString(2));
            }

        } catch (Exception e) {
            System.out.println(e);
        }

    }
}