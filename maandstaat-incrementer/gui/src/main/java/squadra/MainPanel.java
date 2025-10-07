package squadra;

import java.text.NumberFormat;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;


import java.io.IOException;

import javax.swing.JFrame;

public class MainPanel {
    private String selection;

    public MainPanel(JFrame frame, String selectedCustomer, String[] customers, String filePath) {
        this.selection=selectedCustomer;
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
        selection = (String) customerDropdown.getSelectedItem();
        customerDropdown.addActionListener(e -> {
            selection = (String) customerDropdown.getSelectedItem();
            System.out.println("Selected customer: " + selection);
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
                try {
                    String description = descriptionField.getText();
                    float hours = Float.parseFloat(hoursField.getText());
                    manipulator.updateFile(filePath, hours, description, selection);
                    JOptionPane.showMessageDialog(frame,
                            "Description: " + description + "\nHours: " + hours,
                            "Published Task",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(frame,
                            "Some error:\n" + e.toString(),
                            e.getClass().getName(),
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

}
