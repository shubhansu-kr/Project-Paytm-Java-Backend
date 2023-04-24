import java.io.*;
// import java.io.*;
import java.sql.*;
import java.util.Date;

class createAccountException extends Exception {
    createAccountException() {
    }

    createAccountException(String s) {
        super(s);
    }

    public String getMessage() {
        System.out.println("CreateAccountException: File: UserAccount.java Method: CreateAccount()");
        return super.getMessage();
    }
}

class AccountDetailsException extends Exception {
    AccountDetailsException() {
    }

    AccountDetailsException(String s) {
        super(s);
    }

    public String getMessage() {
        System.out.println("AccountDetailsException: File: UserAccount.java Method: AccDetails()");
        return super.getMessage();
    }
}

class UserAccount {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public void createAccount() throws createAccountException {

        int accountNumber = 0;

        Connect Cn = null;
        Connection Con = null;
        Statement StateMent = null;

        try {

            // Try Connecting to the db
            try {
                Cn = new Connect();
                Con = Cn.fetchcon();
                StateMent = Con.createStatement();

            } catch (SQLException e) {
                System.out.println("Error Connecting to the db");
                System.out.println(e);

                System.out.println("Exiting Program....");
                throw new createAccountException("Can't Connect");
            } catch (Exception e) {
                System.out.println(e);
                System.out.println("Unknown error.....");
                throw new createAccountException("Can't Connect");
            }

            // Try executing the query1
            try {
                String query1 = "select max(accno) from bank_user";
                ResultSet Rset = StateMent.executeQuery(query1);

                if (Rset.next()) {
                    accountNumber = Rset.getInt(1);
                }

            } catch (SQLException e) {
                System.out.println("Error executing the first query");
                System.out.println(e);

                System.out.println("Exiting Program....");
                throw new createAccountException("Query1 Fail");
            } catch (Exception e) {
                System.out.println(e);
                System.out.println("Unknown error.....");
                throw new createAccountException("Query1 Fail");
            }

            accountNumber = accountNumber + 1;
            String name = "", addr = "", phno = "", aid = "", dob = "", email = "";
            double openingBalance = 0;
            try {
                // Take user input to insert into db
                System.out.println("New Account Number is: " + accountNumber);
                System.out.println("Enter Account Holder Name:");
                name = br.readLine();
                System.out.println("Enter Address Details: ");
                addr = br.readLine();
                System.out.println("Enter Contact Number: ");
                phno = br.readLine();
                System.out.println("Enter AadhaarID: ");
                aid = br.readLine();
                System.out.println("Enter DATE OF BIRTH: ");
                dob = br.readLine();
                System.out.println("Enter email address: ");
                email = br.readLine();
                System.out.println("Enter Account Opening Balance: ");
                openingBalance = Double.parseDouble(br.readLine());
            } catch (Exception e) {
                System.out.println("Error Taking input");
                throw new createAccountException("Error Taking input in createUser");
            }

            // Try using executing query2
            int valid1 = 0;
            try {
                String query2 = "insert into bank_user values('" + accountNumber + "','" + name + "','" + addr + "','"
                        + phno
                        + "','" + aid
                        + "','" + dob + "','" + email + "')";

                valid1 = StateMent.executeUpdate(query2);

            } catch (SQLException e) {
                System.out.println("Error executing the second query");
                System.out.println(e);

                System.out.println("Exiting Program....");
                throw new createAccountException("Query2 Fail");
            } catch (Exception e) {
                System.out.println(e);
                System.out.println("Unknown error.....");
                throw new createAccountException("Query2 Fail");
            }

            int transactionId = 1;

            double depositAccount = openingBalance;
            double withdrawlAccount = 0;

            Date date = new Date();
            String depositDate = String.format("%tc", date);

            double balance = openingBalance;

            // Try executing query3
            int valid2 = 0;
            try {
                String query1 = "select max(tid) from bank_transaction";
                ResultSet Rset = StateMent.executeQuery(query1);

                if (Rset.next()) {
                    accountNumber = Rset.getInt(1)+1;
                }

                String q3 = "insert into bank_transaction values('" + transactionId + "','" + accountNumber + "','"
                        + depositAccount + "','" + withdrawlAccount
                        + "','"
                        + depositDate + "','" + balance + "')";

                valid2 = StateMent.executeUpdate(q3);
            } catch (SQLException e) {
                System.out.println(e);
                System.out.println("Query3 Exception");
                throw new createAccountException("Query3 Fail");
            } catch (Exception e) {
                System.out.println(e);
                System.out.println("Unknown error.....");
                throw new createAccountException("Query3");
            }

            if (valid1 > 0 && valid2 > 0) {
                System.out.println("Account Created Successfully: ");
            } else {
                throw new createAccountException("Account Not Created");
            }
            // Con.close();
        } finally {
            try {
                Con.close();
            } catch (Exception e) {
                System.out.println("Connection closing error");
            }
        }
    }

    public void AccDetails() throws AccountDetailsException {

        Connect Cn = null;
        Connection Con = null;
        Statement StateMent = null;

        try {
            int accountNumber = 0;

            try {

                System.out.println("Enter Account Number: ");
                accountNumber = Integer.parseInt(br.readLine());

            } catch (Exception e) {
                throw new AccountDetailsException("account number Parse int error");
            }

            // Try connecting to the db
            try {
                Cn = new Connect();
                Con = Cn.fetchcon();
                StateMent = Con.createStatement();

            } catch (SQLException e) {
                System.out.println("Error Connecting to the db");
                System.out.println(e);

                System.out.println("Exiting Program....");
                throw new AccountDetailsException("Can't Connect");
            } catch (Exception e) {

                System.out.println(e);
                System.out.println("Unknown error.....");
                throw new AccountDetailsException("Can't Connect");

            }

            // Try executing Query1
            try {
                String query1 = "select * from bank_user where accno=" + accountNumber;
                ResultSet Rset = StateMent.executeQuery(query1);
                if (Rset.next()) {
                    System.out.println("Account Number: " + Rset.getInt(1));
                    System.out.println("Account Holder Name: " + Rset.getString(2));
                    System.out.println("Address: " + Rset.getString(3));
                    System.out.println("Contact Number: " + Rset.getString(4));
                    System.out.println("Aadhaar ID: " + Rset.getString(5));
                    System.out.println("Date of Birth: " + Rset.getString(6));
                    System.out.println("email address: " + Rset.getString(7));
                }

            } catch (SQLException e) {
                System.out.println("Invalid Account Number::");
                System.out.println(e);
                throw new AccountDetailsException("Invalid Acc No.");
            } catch (Exception e) {
                System.out.println("Query1 : Account details");
                System.out.println(e);
                throw new AccountDetailsException("Unknow Account details exception");
            }

        } finally {
            try {
                Con.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }

    }

    void changepass() throws Exception {

        try {
            System.out.println("Enter USERID: ");
            String uid = br.readLine();
            System.out.println("Enter Old Password: ");
            String opass = br.readLine();
            System.out.println("Enter New Password: ");
            String npass = br.readLine();
            Connect Cn = new Connect();
            Connection Con = Cn.fetchcon();
            Statement StateMent = Con.createStatement();
            String query1 = "update bank_authentication set password='" + npass + "' where userid='" + uid
                    + "' and password='"
                    + opass + "'";
            int valid1 = StateMent.executeUpdate(query1);
            if (valid1 > 0) {
                System.out.println("Password Updated Successfully");
            } else
                System.out.println("Incorrect old Password");
            Con.close();
        } catch (Exception e) {
            throw new Exception("Error Changing pass");
        }
    }

}
