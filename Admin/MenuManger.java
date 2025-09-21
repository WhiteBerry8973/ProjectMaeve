package Account.Admin;

import java.io.File;

/**
 * Class representing menu management for admin users.
 * This class can be expanded to include methods for adding, removing, and updating menu items to csv files.
 */
public class MenuManger {

   private File menuFile;

   public MenuManger(String filePath) {
       this.menuFile = new File(filePath);
   }

   // Methods for managing menu items can be added here

   public void addMenuItem(String item) {
       // Code to add a menu item to the CSV file
   }

   public void removeMenuItem(String item) {
       // Code to remove a menu item from the CSV file
   }

   public void updateMenuItem(String oldItem, String newItem) {
       // Code to update a menu item in the CSV file
   }
}
