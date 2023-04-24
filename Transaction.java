import java.io.*;
import java.sql.*;
import java.util.Date;

class DepositException extends Exception {
    DepositException() {
    }

    DepositException(String s) {
        super(s);
    }

    public String getMessage() {
        System.out.println("Deposit Money Exception: File: Transaction.java Method: deposit()");
        return super.getMessage();
    }
}

class WithdrawalException extends Exception {
    WithdrawalException() {
    }

    WithdrawalException(String s) {
        super(s);
    }

    public String getMessage() {
        System.out.println("WithdrawlException: File: Transaction.java Method: withdrawl()");
        return super.getMessage();
    }
}

class Transaction {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    void deposit() throws DepositException {

        Connect Cn = null;
        Connection Con = null;
        Statement StateMent = null;

        try {
            String accountNumber;
            double depositAmount;

            try {
                System.out.println("Enter Account No. to Deopsit: ");
                accountNumber = br.readLine();
                System.out.println("Enter Amount to be deposited: ");
                depositAmount = Double.parseDouble(br.readLine());
            } catch (Exception e) {
                throw new DepositException("Error Taking input");
            }

            try {
                Cn = new Connect();
                Con = Cn.fetchcon();
                StateMent = Con.createStatement();

            } catch (SQLException e) {
                System.out.println("Error Connecting to the db");
                System.out.println(e);

                throw new DepositException("Can't Connect");
            } catch (Exception e) {

                System.out.println(e);
                System.out.println("Unknown error.....");
                throw new DepositException("Can't Connect");
            }

            double balance = 0;
            int tid = 0;

            try {
                String q1 = "select tid,balance from bank_transaction where accountNumber='" + accountNumber
                        + "' and tid=(select max(tid) from bank_transaction where accountNumber=" + accountNumber + ")";
                ResultSet rs = StateMent.executeQuery(q1);
                tid = 0;
                balance = 0;
                if (rs.next()) {
                    tid = rs.getInt(1);
                    balance = rs.getDouble(2);
                }
            } catch (SQLException e) {
                System.out.println(e);
                System.out.println("Error executing Query1");
                throw new DepositException("Query1");
            }

            tid = tid + 1;
            double wamt = 0;

            Date date = new Date();
            String dt = String.format("%tc", date);

            balance = balance + depositAmount;

            String q2 = "insert into bank_transaction values('" + tid + "','" + accountNumber + "','" + depositAmount + "','" + wamt
                    + "','"
                    + dt + "','" + balance + "')";

            int t1 = StateMent.executeUpdate(q2);

            if (t1 > 0) {
                System.out.println("Deposited Successfully: ");
            }

            Con.close();

        } catch (Exception e) {
            throw new DepositException("Errr : Transaction.java");
        }
    }

    void withdrawal() throws WithdrawalException {

        try {

            System.out.println("Enter Account No. to Withdrawal: ");
            int accountNumber = Integer.parseInt(br.readLine());
            System.out.println("Enter Amount to be Withdrawal: ");
            double wamt = Double.parseDouble(br.readLine());
            Connect Cn = new Connect();
            Connection Con = Cn.fetchcon();
            Statement StateMent = Con.createStatement();
            String q1 = "select tid,balance from bank_transaction where accountNumber='" + accountNumber
                    + "' and tid=(select max(tid) from bank_transaction where accountNumber=" + accountNumber + ")";
            ResultSet rs = StateMent.executeQuery(q1);
            int tid = 0;
            double balance = 0;
            if (rs.next()) {
                tid = rs.getInt(1);
                balance = rs.getDouble(2);
            }
            if (balance - wamt >= 500) {
                double depositAmount = 0;
                Date date = new Date();
                String dt = String.format("%tc", date);
                balance = balance - wamt;
                String q2 = "insert into bank_transaction values('" + tid + "','" + accountNumber + "','" + depositAmount + "','" + wamt
                        + "','" + dt + "','" + balance + "')";
                int t1 = StateMent.executeUpdate(q2);
                if (t1 > 0)
                    System.out.println("Debited Successfully: ");
            } else
                System.out.println("INSUFFICENT BALANCE: ");
            Con.close();

        } catch (Exception e) {
            System.out.println(e);
            throw new WithdrawalException("Error: " + e.getMessage());
        }
    }

    void ministatement() throws Exception {
        System.out.println("Enter Account No.: ");
        int accountNumber = Integer.parseInt(br.readLine());
        Connect Cn = new Connect();
        Connection Con = Cn.fetchcon();
        Statement StateMent = Con.createStatement();
        System.out.println("TID\tAccno\tCREDIT\t\tDEBIT\t\t\tDATE\t\t\t\t\tBALANCE");
        String q1 = "select * from bank_transaction where accountNumber=" + accountNumber;

        ResultSet rs = StateMent.executeQuery(q1);
        while (rs.next()) {
            String transactionEntry = rs.getInt(1) + "\t" + rs.getInt(2) + "\t" + rs.getDouble(3) + "\t" + "\t" + "\t"
                    + rs.getDouble(4) + "\t" + rs.getString(5) + "\t" + rs.getDouble(6);
            System.out.println(transactionEntry);
        }
        Con.close();
    }

    void balcheck() throws Exception {
        System.out.println("Enter Account No.: ");
        int accountNumber = Integer.parseInt(br.readLine());
        Connect Cn = new Connect();
        Connection Con = Cn.fetchcon();
        Statement StateMent = Con.createStatement();
        String q1 = "select tid,balance from bank_transaction where accountNumber=" + accountNumber
                + " and tid=(select max(tid) from bank_transaction where accountNumber=" + accountNumber + ")";
        ResultSet rs = StateMent.executeQuery(q1);
        double balance = 0;
        if (rs.next())
            balance = rs.getDouble(2);
        System.out.println("Remaining Balance: " + balance);
        Con.close();
    }
}
