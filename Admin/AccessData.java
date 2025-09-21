package Admin;

/**
 * Class representing access data for admin users.
 * This class can read and write admin.csv file.
 */
public class AccessData {
    
    private String username;
    private String password;
    private String[] permissions;

    public AccessData(String username, String password, String[] permissions) {
        this.username = username;
        this.password = password;
        this.permissions = permissions;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String[] getPermissions() {
        return permissions;
    }

    @Override
    public String toString() {
        return username + "\n" + password + "\n" + String.join(", ", permissions);
    }
}