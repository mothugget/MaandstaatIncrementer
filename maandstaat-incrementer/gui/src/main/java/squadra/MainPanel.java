package squadra;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.time.LocalDate;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MainPanel extends JPanel {
    private String selection;
    private final MainPanelListener listener;
    private final JFrame frame;

    public MainPanel(JFrame frame, String selectedCustomer, String[] customers, String filePath,
            MainPanelListener listener) {
        this.selection = selectedCustomer;
        this.listener = listener;
        this.frame = frame;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(5, 5, 5, 5); // spacing

        // Create a JComboBox fed by the array
        JComboBox<String> customerDropdown = new JComboBox<>(customers);

        // Add to your frame (or panel)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        this.add(new JLabel("Customer:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        this.add(customerDropdown, gbc);
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
        this.add(descriptionLabel, gbc);

        JTextField descriptionField = new JTextField(21);
        descriptionField.setText("Enter description here");
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        this.add(descriptionField, gbc);

        // Hours label and text field
        JLabel hoursLabel = new JLabel("Hours:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        this.add(hoursLabel, gbc);

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
        this.add(hoursField, gbc);

        JButton suggestionsButton = new JButton("Suggestions");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 0;
        this.add(suggestionsButton, gbc);
        // Action listener for button
        suggestionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                try {
                    if (listener != null) {
                        listener.onSuggestionsRequested();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(frame,
                            "Some error:\n" + e.toString(),
                            e.getClass().getName(),
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        // Publish button
        JButton publishButton = new JButton("Publish");
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 0;
        this.add(publishButton, gbc);
        MaandStaatManipulator manipulator = new MaandStaatManipulator();
        // Action listener for button
        publishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                try {
                    String description = descriptionField.getText();
                    float hours = Float.parseFloat(hoursField.getText());
                    LocalDate date = LocalDate.now();
                    manipulator.updateFile(filePath, hours, description, selection, date);
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
