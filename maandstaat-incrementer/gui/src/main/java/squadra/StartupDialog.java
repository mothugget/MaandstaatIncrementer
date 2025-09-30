package squadra;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StartupDialog extends JDialog {
    private JTextField pathField;
    private JFormattedTextField customerField;
    private boolean confirmed = false;

    public StartupDialog(Frame parent) {
        super(parent, "Startup Configuration", true); // modal dialog
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Path label + text field
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("File Path:"), gbc);
        pathField = new JTextField(20);
        gbc.gridx = 1;
        add(pathField, gbc);

        // Number of customers label + int field
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Number of Customers:"), gbc);
        customerField = new JFormattedTextField(java.text.NumberFormat.getIntegerInstance());
        customerField.setColumns(5);
        customerField.setValue(1); // default
        gbc.gridx = 1;
        add(customerField, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        add(buttonPanel, gbc);

        // Button actions
        okButton.addActionListener(e -> {
            confirmed = true;
            dispose();
        });

        cancelButton.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        pack();
        setLocationRelativeTo(parent);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public String getFilePath() {
        return pathField.getText();
    }

    public int getNumberOfCustomers() {
        Number value = (Number) customerField.getValue();
        return value != null ? value.intValue() : 0;
    }

    // Usage example
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StartupDialog dialog = new StartupDialog(null);
            dialog.setVisible(true);

            if (dialog.isConfirmed()) {
                String path = dialog.getFilePath();
                int customers = dialog.getNumberOfCustomers();
                System.out.println("Path: " + path);
                System.out.println("Customers: " + customers);

                // Now you can start your main GUI
                JFrame mainFrame = new JFrame("Main GUI");
                mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                mainFrame.setSize(400, 300);
                mainFrame.setVisible(true);
            } else {
                System.out.println("Startup canceled, exiting app.");
                System.exit(0);
            }
        });
    }
}
