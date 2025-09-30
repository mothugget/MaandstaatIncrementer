package squadra;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MainGui {
    public static void main(String[] args) {
        // Create the main frame
        JFrame frame = new JFrame("Task Publisher");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // spacing

        // Description label and text field
        JLabel descriptionLabel = new JLabel("Description:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        frame.add(descriptionLabel, gbc);

        JTextField descriptionField = new JTextField(20);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        frame.add(descriptionField, gbc);

        // Hours label and text field
        JLabel hoursLabel = new JLabel("Hours:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        frame.add(hoursLabel, gbc);

        JTextField hoursField = new JTextField(5);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        frame.add(hoursField, gbc);

        // Publish button
        JButton publishButton = new JButton("Publish");
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
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
