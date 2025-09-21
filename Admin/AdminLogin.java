package Account.Admin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;

/**
 * Class representing access data for admin users.
 * This class can read and write admin.csv file.
 */
public class AdminLogin {
    
    private String username;
    private String password;
    File f = null;
    FileReader fr = null;
    BufferedReader br = null;
    Scanner input = new Scanner(System.in);

    public AdminLogin(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String login() {

        try {
            f = new File("./files/admin.csv");
            fr = new FileReader(f);
            br = new BufferedReader(fr);
            String s;
            
            while ((s = br.readLine()) != null) {
                String[] parts = s.split(",");
                String Username = parts[0];
                String Password = parts[1];
                if (Username.equals(username) && Password.equals(password)) {
                    return "Login successful!";
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            try {
                br.close(); fr.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        return "Login failed: Invalid username or password.";
    }

    
}