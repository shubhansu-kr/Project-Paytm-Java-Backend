class App {
    public static void main(String args[]) throws Exception {
        Authentication auth = new Authentication();

        try {
            int valid = auth.ValidateUser();
            if (valid == 1) {
                System.out.println("ROOT ACCESS GRANTED");
                System.out.println("***********************************Welcome to Bank Online Banking System*************************************");
    
                Service s = new Service();
                s.menu();
            }
        } catch (Exception e) {
            System.out.println("Invalid login credentials");
            System.out.println(e);
        }
    }
}