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

                if (depositAmount <= 0) {
                    throw new DepositException("Deposit Amount Invalid");
                }
            } catch (Exception e) {
                throw new DepositException("Error Taking input: " + e.getMessage());
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
                String query1 = "select tid,balance from bank_transaction where accno='" + accountNumber
                        + "' and tid=(select max(tid) from bank_transaction where accno=" + accountNumber + ")";
                ResultSet rs = StateMent.executeQuery(query1);
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

            String query2 = "insert into bank_transaction values('" + tid + "','" + accountNumber + "','"
                    + depositAmount
                    + "','" + wamt
                    + "','"
                    + dt + "','" + balance + "')";

            int t1 = StateMent.executeUpdate(query2);

            if (t1 > 0) {
                System.out.println("Deposited Successfully: ");
            }

            Con.close();

        } catch (Exception e) {
            throw new DepositException("Errr : Transaction.java");
        }
    }

    void withdrawal() throws WithdrawalException {

        Connect Cn = null;
        Connection Con = null;
        Statement StateMent = null;

        try {

            int accountNumber = 0;
            double wamt;

            try {
                System.out.println("Enter Account No. to Withdrawal: ");
                accountNumber = Integer.parseInt(br.readLine());
                System.out.println("Enter Amount to be Withdrawal: ");
                wamt = Double.parseDouble(br.readLine());

                if (wamt <= 0) {
                    throw new DepositException("Invalid Withdraw Amount");
                }
            } catch (DepositException e) {
                System.out.println(e);
                throw new DepositException(e.getMessage());
            } catch (Exception e) {
                System.out.println(e);
                throw new DepositException(e.getMessage());
            }

            // tryConnecting to db
            try {

                Cn = new Connect();
                Con = Cn.fetchcon();
                StateMent = Con.createStatement();

            } catch (SQLException e) {

                System.out.println("Error Connecting to the db");
                System.out.println(e);

                throw new WithdrawalException("Can't Connect");
            } catch (Exception e) {

                System.out.println(e);
                System.out.println("Unknown error.....");

                throw new WithdrawalException("Can't Connect");
            }

            int tid = 0;
            double balance = 0;

            try {
                String query1 = "select tid,balance from bank_transaction where accno='" + accountNumber
                        + "' and tid=(select max(tid) from bank_transaction where accno=" + accountNumber + ")";
                ResultSet rs = StateMent.executeQuery(query1);

                if (rs.next()) {
                    tid = rs.getInt(1);
                    balance = rs.getDouble(2);
                }
            } catch (SQLException e) {
                System.out.println(e);
                throw new WithdrawalException("Error Executing Query1");
            } catch (Exception e) {
                System.out.println(e);
                throw new WithdrawalException("Unknown Error in Withdrawl");
            }

            if (balance - wamt <= 500) {
                System.out.println("Cannot Withdraw money");
                throw new WithdrawalException("Insufficient Balance");
            }

            double depositAmount = 0;

            Date date = new Date();
            String dt = String.format("%tc", date);

            balance = balance - wamt;

            int t1 = 0;
            try {

                String query2 = "insert into bank_transaction values('" + tid + "','" + accountNumber + "','"
                        + depositAmount + "','" + wamt
                        + "','" + dt + "','" + balance + "')";
                t1 = StateMent.executeUpdate(query2);

            } catch (SQLException e) {
                System.out.println(e);
                throw new WithdrawalException("Error Executing Query2");
            } catch (Exception e) {
                System.out.println(e);
                throw new WithdrawalException("Unknown Error in Withdrawl");
            }

            if (t1 > 0) {
                System.out.println("Debited Successfully: ");
            }

            Con.close();

        } catch (Exception e) {
            System.out.println(e);
            throw new WithdrawalException("Error: " + e.getMessage());
        } finally {
            try {
                Con.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    void ministatement() throws Exception {
        System.out.println("Enter Account No.: ");
        int accountNumber = Integer.parseInt(br.readLine());
        Connect Cn = new Connect();
        Connection Con = Cn.fetchcon();
        Statement StateMent = Con.createStatement();
        System.out.println("TID\tAccno\tCREDIT\t\tDEBIT\t\t\tDATE\t\t\t\t\tBALANCE");
        String query1 = "select * from bank_transaction where accno=" + accountNumber;

        ResultSet rs = StateMent.executeQuery(query1);

        File f = null;
        FileOutputStream os = null;

        try {

            f = new File("MiniStatement.txt");

            // Clear the file
            os = new FileOutputStream(f);
            os.write("".getBytes());
            os.close();

            os = new FileOutputStream(f, true);

            while (rs.next()) {
                String transactionEntry = rs.getInt(1) + "\t" + rs.getInt(2) + "\t" + rs.getDouble(3) + "\t" + "\t"
                        + "\t" + rs.getDouble(4) + "\t" + rs.getString(5) + "\t" + rs.getDouble(6);

                os.write(transactionEntry.getBytes());

                System.out.println(transactionEntry);
            }

            os.flush();
        } catch (FileNotFoundException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            try {
                os.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        Con.close();
    }

    void balcheck() throws Exception {
        System.out.println("Enter Account No.: ");
        int accountNumber = Integer.parseInt(br.readLine());
        Connect Cn = new Connect();
        Connection Con = Cn.fetchcon();
        Statement StateMent = Con.createStatement();
        String query1 = "select tid,balance from bank_transaction where accno=" + accountNumber
                + " and tid=(select max(tid) from bank_transaction where accno=" + accountNumber + ")";
        ResultSet rs = StateMent.executeQuery(query1);
        double balance = 0;
        if (rs.next())
            balance = rs.getDouble(2);
        System.out.println("Remaining Balance: " + balance);
        Con.close();
    }
}
