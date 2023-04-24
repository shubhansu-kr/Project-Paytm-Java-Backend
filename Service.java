import java.io.*;

class Service {
	public void menu() throws Exception {

		while (true) {
			
			System.out.println();

			System.out.println("Enter 1 to Create New Account.");
			System.out.println("Enter 2 for Deposit.");
			System.out.println("Enter 3 for Withdrawl.");
			System.out.println("Enter 4 for MiniStatement");
			System.out.println("Enter 5 for Balance Enquiry.");
			System.out.println("Enter 6 for Changing Password.");
			System.out.println("Enter 7 for Account Details.");
			System.out.println("Enter 8 to  Exit.");

			System.out.println();

			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

			System.out.println("Enter your Choice.");
			int choice = Integer.parseInt(br.readLine());

			UserAccount userAcc = new UserAccount();
			Transaction trans = new Transaction();

			switch (choice) {
				case 1:
					userAcc.createAccount();
					break;
				case 2:
					trans.deposit();
					break;
				case 3:
					trans.withdrawal();
					break;
				case 4:
					trans.ministatement();
					break;
				case 5:
					trans.balcheck();
					break;
				case 6:
					userAcc.changepass();
					break;
				case 7:
					userAcc.AccDetails();
					break;
				case 8:
					System.exit(0);
			}
		}

	}
}
