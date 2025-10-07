package squadra;

import java.awt.GridBagLayout;
import java.io.IOException;

import javax.swing.JFrame;


public class MainGui {
    private static String filePath;
    private static String selectedCustomer;
    private static int numberOfCustomers;
    // "/Users/karlfredriksson/Documents/Maandstaat/MaaandstaatDemo.xlsm"
    public static void main(String[] args) {
        ConfigManager configs = new ConfigManager(".config.properties");
        filePath = configs.get("FILE_PATH", "");
        numberOfCustomers = Integer.parseInt(configs.get("NUMBER_OF_CUSTOMERS", "2"));
        String[] customers = new String[numberOfCustomers];

        // Create the main frame
        JFrame frame = new JFrame("Maandstaat Incrementer");
        StartupDialog dialog = new StartupDialog(frame, filePath, numberOfCustomers);
        dialog.setVisible(true); // <- This actually shows the dialog

        // Only proceed if the user clicked OK
        if (dialog.isConfirmed()) {
            filePath = dialog.getFilePath();
            numberOfCustomers = dialog.getNumberOfCustomers();
            configs.set("FILE_PATH", filePath, true);
            configs.set("NUMBER_OF_CUSTOMERS", Integer.toString(numberOfCustomers), true);

            try {
                customers = MaandStaatManipulator.getCustomers(filePath, numberOfCustomers);
            } catch (IOException e) {
                System.out.println("Failed to find Maandstaat sheet\n" + e);
                return;
            }
            // Now you can initialize your main GUI
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 300);
            frame.setVisible(true);
        } else {
            System.out.println("Startup canceled.");
            System.exit(0);
        }
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(411, 200);
        frame.setLayout(new GridBagLayout());

        MainPanel mainPanel= new MainPanel(frame, selectedCustomer, customers, filePath);
  

        // Show the frame
        frame.setLocationRelativeTo(null); // center on screen
        frame.setVisible(true);
    }

}
