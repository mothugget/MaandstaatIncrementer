package squadra;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class MainGui implements MainPanelListener {
    private static String filePath;
    private static String selectedCustomer;
    private static int numberOfCustomers;
    private final JFrame frame;

    @Override
    public void onSuggestionsRequested() {
        System.out.println("MainGui received suggestion event");

        SuggestionsDialog suggestionDialog = new SuggestionsDialog(frame);
        suggestionDialog.setSize(411, 200);
        suggestionDialog.setLocationRelativeTo(null);
        suggestionDialog.setVisible(true);
    }

    public void onPublish() {
        System.out.println("MainGui received suggestion event");

        SuggestionsDialog suggestionDialog = new SuggestionsDialog(frame);
        suggestionDialog.setSize(411, 200);
        suggestionDialog.setVisible(true);
    }

    // "/Users/karlfredriksson/Documents/Maandstaat/MaaandstaatDemo.xlsm"
    public MainGui() {
        ConfigManager configs = new ConfigManager(".config.properties");
        filePath = configs.get("FILE_PATH", "");
        numberOfCustomers = Integer.parseInt(configs.get("NUMBER_OF_CUSTOMERS", "2"));
        String[] customers = new String[numberOfCustomers];

        // Create the main frame
        frame = new JFrame("Maandstaat Incrementer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        StartupDialog dialog = new StartupDialog(frame, filePath, numberOfCustomers);
        dialog.setVisible(true); // <- This actually shows the dialog

        // Only proceed if the user clicked OK
        if (dialog.isConfirmed()) {
            dialog.dispose();
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

        } else {
            System.out.println("Startup canceled.");
            System.exit(0);
        }


        MainPanel mainPanel = new MainPanel(frame, selectedCustomer, customers, filePath, this);
        // Show the frame
        frame.setSize(411, 200);
        
        frame.setContentPane(mainPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainGui::new);
    }

}
