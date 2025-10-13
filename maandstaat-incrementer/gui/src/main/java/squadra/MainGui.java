package squadra;

import java.io.IOException;
import java.time.LocalDate;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class MainGui implements MainPanelListener, SuggestionsDialogListener {
    private static String filePath;
    private static int numberOfCustomers;
    private final JFrame frame;
    private final MaandStaatManipulator manipulator;
    private static String description;
    private static String selectedCustomer;
    private static float hours;
    private static int kilometer;
    private static String location;
    private static LocalDate date;
    private static MainPanel mainPanel;
    private static SuggestionManager suggestionsMap;
    private final String[] defaultSuggestions;
    private final ConfigManager configs;

    @Override
    public void onSuggestionsRequested() {
        System.out.println("MainGui received suggestion event");
        selectedCustomer = mainPanel.getSelectedCustomerValue();
        String[] suggestions = suggestionsMap.getSuggestions(selectedCustomer);
        System.out.println(selectedCustomer);
        SuggestionsDialog suggestionDialog = new SuggestionsDialog(frame, selectedCustomer, suggestions, this);
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
        selectedCustomer = mainPanel.getSelectedCustomerValue();
        date = mainPanel.getDateValue();
        try {
            String dialogText = manipulator.updateFile(filePath, hours, description, kilometer, location,
                    selectedCustomer,
                    date);
            JOptionPane.showMessageDialog(frame,
                    dialogText,
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

    @Override
    public void onOk(String customer, DefaultTableModel model) {
        System.out.println(customer);
        int rowCount = model.getRowCount();
        String[] dirtySuggestions = new String[rowCount];
        for (int i = 0; i < model.getRowCount(); i++) {
            Object value = model.getValueAt(i, 0);
            System.out.println(value);
            dirtySuggestions[i] = value != null ?value.toString():"";
        }
        suggestionsMap.setSuggestions(customer, dirtySuggestions);
        configs.set("CUSTOMER_SUGGESTIONS", suggestionsMap.getCustomerSuggestionsJson(), true);
    }

    // "/Users/karlfredriksson/Documents/Maandstaat/MaaandstaatDemo.xlsm"
    public MainGui() {
        defaultSuggestions = new String[] { "Stand Up", "Deep Dive", "Refinement", "Planning", "Planning poker" };
        manipulator = new MaandStaatManipulator();
        suggestionsMap = new SuggestionManager();
        configs = new ConfigManager(".config.properties");
        filePath = configs.get("FILE_PATH", "");
        numberOfCustomers = Integer.parseInt(configs.get("NUMBER_OF_CUSTOMERS", "1"));
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

        String customerSuggestionsJson = configs.get("CUSTOMER_SUGGESTIONS", "{}");
        try {
            suggestionsMap.setCustomerSuggestionsFromJson(customerSuggestionsJson);
        } catch (Exception e) {
            System.err.println(e);
            return;
        }
        for (String customer : customers) {

            if (!suggestionsMap.doesCustomerExist(customer)) {
                System.out.println(customer);
                suggestionsMap.setSuggestions(customer, defaultSuggestions);
            }
        }
        configs.set("CUSTOMER_SUGGESTIONS", suggestionsMap.getCustomerSuggestionsJson(), true);

        System.out.println(suggestionsMap.getCustomerSuggestionsJson());

        mainPanel = new MainPanel(frame, customers, filePath, this, LocalDate.now(), suggestionsMap);
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
