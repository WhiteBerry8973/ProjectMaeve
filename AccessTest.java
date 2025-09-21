import java.util.Scanner;

import Admin.AdminLogin;

public class AccessTest {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("--- Admin Login Test ---");
        System.out.print("Enter username: ");
        String username = input.nextLine();
        System.out.print("Enter password: ");
        String password = input.nextLine();

        AdminLogin admin = new AdminLogin(username, password);
        String result = admin.login();
        System.out.println(result);
    }
}