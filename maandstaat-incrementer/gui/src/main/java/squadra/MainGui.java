package squadra;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class MainGui implements MainPanelListener {
    private static String filePath;
    private static String selectedCustomer;
    private static int numberOfCustomers;
    private final JFrame frame;
    private final MaandStaatManipulator manipulator;
    private static String description;
    private static String selection;
    private static float hours;
    private static int kilometer;
    private static String location;
    private static LocalDate date;
    private static MainPanel mainPanel;
    private static Map<String, String[]> customerSuggestions;
    private static SuggestionManager suggestionsMap;
    private final String[] defaultSuggestions;

    @Override
    public void onSuggestionsRequested() {
        System.out.println("MainGui received suggestion event");

        SuggestionsDialog suggestionDialog = new SuggestionsDialog(frame);
        suggestionDialog.setSize(300, 500);
        suggestionDialog.setLocationRelativeTo(null);
        suggestionDialog.setVisible(true);
    }

    @Override
    public void onPublish() {
        description = mainPanel.getDescriptionValue();
        hours = mainPanel.getHoursValue();
        kilometer = mainPanel.getKmValue();
        mainPanel.resetKmValue();
        location = mainPanel.getLocationValue();
        mainPanel.resetLocationValue();    
        selection = mainPanel.getSelectionValue();
        date = mainPanel.getDateValue();
        System.out.println(date.toString());
        try {
            manipulator.updateFile(filePath, hours, description, kilometer, location, selection, date);
            JOptionPane.showMessageDialog(frame,
                    "Description: " + description + "\nHours: " + hours,
                    "Published Task",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame,
                    "Failed to update file:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // "/Users/karlfredriksson/Documents/Maandstaat/MaaandstaatDemo.xlsm"
    public MainGui() {
        defaultSuggestions = new String[] { "Stand Up", "Deep Dive", "Refinement", "Planning", "Planning poker" };
        manipulator = new MaandStaatManipulator();
        suggestionsMap = new SuggestionManager();
        ConfigManager configs = new ConfigManager(".config.properties");
        filePath = configs.get("FILE_PATH", "");
        numberOfCustomers = Integer.parseInt(configs.get("NUMBER_OF_CUSTOMERS", "2"));
        String[] customers = new String[numberOfCustomers];

        // Create the main frame
        frame = new JFrame("Maandstaat Incrementer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        StartupDialog startupDialog = new StartupDialog(frame, filePath, numberOfCustomers);
        startupDialog.setVisible(true); // <- This actually shows the startupDialog

        // Only proceed if the user clicked OK
        if (startupDialog.isConfirmed()) {
            startupDialog.dispose();
            filePath = startupDialog.getFilePath();
            numberOfCustomers = startupDialog.getNumberOfCustomers();
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
        System.out.println("customer");
        for (String customer : customers) {
            if (!suggestionsMap.doesCustomerExist(customer)) {
                System.out.println(customer);
                suggestionsMap.setSuggestions(customer, defaultSuggestions);
            }
        }
        System.out.println(suggestionsMap.getCustomerSuggestionsJson());

        mainPanel = new MainPanel(frame, selectedCustomer, customers, filePath, this, LocalDate.now());
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
