import Admin.AccessData;

public class AccessTest {

    public static void main(String[] args) {
        String[] permissions = {"READ", "WRITE", "DELETE"};
        AccessData adminAccess = new AccessData("admin", "admin123", permissions);
        System.out.println(adminAccess);
    }
}