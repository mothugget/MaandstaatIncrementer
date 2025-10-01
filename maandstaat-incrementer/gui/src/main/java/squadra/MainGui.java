package squadra;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.NumberFormat;


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

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // spacing

        // Create a JComboBox fed by the array
        JComboBox<String> customerDropdown = new JComboBox<>(customers);

        // Add to your frame (or panel)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        frame.add(new JLabel("Customer:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        frame.add(customerDropdown, gbc);
        selectedCustomer = (String) customerDropdown.getSelectedItem();
        customerDropdown.addActionListener(e -> {
            selectedCustomer = (String) customerDropdown.getSelectedItem();
            System.out.println("Selected customer: " + selectedCustomer);
        });

        // Description label and text field
        JLabel descriptionLabel = new JLabel("Description:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        frame.add(descriptionLabel, gbc);

        JTextField descriptionField = new JTextField(21);
        descriptionField.setText("Enter description here");
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        frame.add(descriptionField, gbc);

        // Hours label and text field
        JLabel hoursLabel = new JLabel("Hours:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        frame.add(hoursLabel, gbc);

        NumberFormat floatFormat = NumberFormat.getNumberInstance();
        JFormattedTextField hoursField = new JFormattedTextField(floatFormat);
        hoursField.setColumns(5);
        hoursField.setText("0.5");
        hoursField.setMinimumSize(hoursField.getPreferredSize());
        hoursField.setMaximumSize(hoursField.getPreferredSize());
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        frame.add(hoursField, gbc);

        // Publish button
        JButton publishButton = new JButton("Publish");
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 0;
        frame.add(publishButton, gbc);
        MaandStaatManipulator manipulator = new MaandStaatManipulator();
        // Action listener for button
        publishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                try{
                String description = descriptionField.getText();
                float hours = Float.parseFloat(hoursField.getText());
                manipulator.updateFile(filePath,hours,description,selectedCustomer);
                JOptionPane.showMessageDialog(frame,
                        "Description: " + description + "\nHours: " + hours,
                        "Published Task",
                        JOptionPane.INFORMATION_MESSAGE);
                }catch (Exception e){
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(frame,
                    "Some error:\n"+e.toString(),
                    e.getClass().getName(),
                    JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Show the frame
        frame.setLocationRelativeTo(null); // center on screen
        frame.setVisible(true);
    }

}
