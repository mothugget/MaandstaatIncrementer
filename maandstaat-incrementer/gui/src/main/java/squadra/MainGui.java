package squadra;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class MainGui {
    public static void main(String[] args) {
        String filePath = "/Users/karlfredriksson/Documents/Maandstaat/MaaandstaatDemo.xlsm";
        int numberOfCustomers = 2;
        String[] customers = new String[numberOfCustomers];
        try {
            customers = MaandStaatManipulator.getCustomers(filePath, numberOfCustomers);
        } catch (IOException e) {
            System.out.println("Failed to find Maandstaat sheet\n" + e);
            return;
        }
        // Create the main frame
        JFrame frame = new JFrame("Maandstaat Incrementer");
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
        // Description label and text field
        JLabel descriptionLabel = new JLabel("Description:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        frame.add(descriptionLabel, gbc);

        JTextField descriptionField = new JTextField(21);
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

        JTextField hoursField = new JTextField(5);
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

        // Action listener for button
        publishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String description = descriptionField.getText();
                String hours = hoursField.getText();
                JOptionPane.showMessageDialog(frame,
                        "Description: " + description + "\nHours: " + hours,
                        "Published Task",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Show the frame
        frame.setLocationRelativeTo(null); // center on screen
        frame.setVisible(true);
    }

}
