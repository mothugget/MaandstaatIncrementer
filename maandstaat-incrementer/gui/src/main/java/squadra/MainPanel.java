package squadra;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

public class MainPanel extends JPanel {
    private String selectedCustomer;
    private MaskFormatter dateMask;
    private final MainPanelListener mainPanelListener;
    private final JFrame frame;
    private LocalDate date;
    private final JFormattedTextField dateField;
    private final JTextField descriptionField;
    private final JFormattedTextField hoursField;
    private final JFormattedTextField kmField;
    private final JTextField locationField;
    private static int gridyi;

    public MainPanel(JFrame frame, String[] customers, String filePath,
            MainPanelListener mainPanelListener, LocalDate date) {
        this.mainPanelListener = mainPanelListener;
        this.frame = frame;
        this.date = date;
        gridyi = 0;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(5, 5, 5, 5); // spacing
        gbc.gridx = 0;
        gbc.gridy = gridyi;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel dateLabel = new JLabel("Date (YYYY-MM-DD):");
        this.add(dateLabel, gbc);
        try {
            dateMask = new MaskFormatter("####-##-##");
        } catch (Exception e) {
            System.err.println(e);
        }
        dateMask.setPlaceholderCharacter('_');
        dateField = new JFormattedTextField(dateMask);
        dateField.setColumns(10);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        dateField.setText(date.toString());
        this.add(dateField, gbc);

        // Create a JComboBox fed by the array
        JComboBox<String> customerDropdown = new JComboBox<>(customers);

        // Add to your frame (or panel)
        gridyi++;
        gbc.gridx = 0;
        gbc.gridy = gridyi;
        gbc.anchor = GridBagConstraints.EAST;
        this.add(new JLabel("Customer:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        this.add(customerDropdown, gbc);
        selectedCustomer = (String) customerDropdown.getSelectedItem();
        customerDropdown.addActionListener(e -> {
            selectedCustomer = (String) customerDropdown.getSelectedItem();
            System.out.println("Selected customer: " + selectedCustomer);
        });

        // Description label and text field
        JLabel descriptionLabel = new JLabel("Description:");
        gridyi++;
        gbc.gridx = 0;
        gbc.gridy = gridyi;
        gbc.anchor = GridBagConstraints.EAST;
        this.add(descriptionLabel, gbc);

        descriptionField = new JTextField(21);
        descriptionField.setText("Test");
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;

        java.util.List<String> suggestions = Arrays.asList(
                "Stand Up", "Deep Dive", "Refinement", "Planning", "Planning poker");
        JPopupMenu suggestionMenu = new JPopupMenu();
        descriptionField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String text = descriptionField.getText().trim().toLowerCase();
                suggestionMenu.removeAll();

                if (text.isEmpty()) {
                    suggestionMenu.setVisible(false);
                    return;
                }

                // Filter matching suggestions
                suggestions.stream()
                        .filter(s -> s.toLowerCase().startsWith(text))
                        .forEach(s -> {
                            JMenuItem item = new JMenuItem(s);
                            item.addActionListener(ev -> {
                                descriptionField.setText(s);
                                suggestionMenu.setVisible(false);
                            });
                            suggestionMenu.add(item);
                        });

                if (suggestionMenu.getComponentCount() > 0) {
                    suggestionMenu.show(descriptionField, 0, descriptionField.getHeight());
                } else {
                    suggestionMenu.setVisible(false);
                }
            }
        });

        this.add(descriptionField, gbc);
        // Hours label and text field
        JLabel hoursLabel = new JLabel("Hours:");
        gridyi++;
        gbc.gridx = 0;
        gbc.gridy = gridyi;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        this.add(hoursLabel, gbc);

        NumberFormat floatFormat = NumberFormat.getNumberInstance();
        hoursField = new JFormattedTextField(floatFormat);
        hoursField.setColumns(5);
        hoursField.setValue(0.5);
        hoursField.setMinimumSize(hoursField.getPreferredSize());
        hoursField.setMaximumSize(hoursField.getPreferredSize());
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        this.add(hoursField, gbc);

        JLabel kmLabel = new JLabel("KM:");
        gridyi++;
        gbc.gridx = 0;
        gbc.gridy = gridyi;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        this.add(kmLabel, gbc);

        NumberFormat intFormat = NumberFormat.getIntegerInstance();
        kmField = new JFormattedTextField(intFormat);
        kmField.setColumns(5);
        kmField.setMinimumSize(kmField.getPreferredSize());
        kmField.setMaximumSize(kmField.getPreferredSize());
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        this.add(kmField, gbc);

        JLabel locationLabel = new JLabel("Location:");
        gridyi++;
        gbc.gridx = 0;
        gbc.gridy = gridyi;
        gbc.anchor = GridBagConstraints.WEST;
        this.add(locationLabel, gbc);

        locationField = new JTextField(21);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        this.add(locationField, gbc);

        JButton suggestionsButton = new JButton("Suggestions");
        gridyi++;
        gbc.gridx = 0;
        gbc.gridy = gridyi;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 0;
        this.add(suggestionsButton, gbc);
        // Action mainPanelListener for button
        suggestionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                try {
                    if (mainPanelListener != null) {
                        mainPanelListener.onSuggestionsRequested();
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
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 0;
        this.add(publishButton, gbc);
        // Action mainPanelListener for button
        publishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                try {
                    if (mainPanelListener != null) {
                        mainPanelListener.onPublish();
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
    }

    public String getDescriptionValue() {
        return descriptionField.getText();
    }

    public float getHoursValue() {
        Number value = (Number) hoursField.getValue();
        return (value != null) ? value.floatValue() : 0f;
    }

    public int getKmValue() {
        Number value = (Number) kmField.getValue();
        return (value != null) ? value.intValue() : 0;
    }

    public String getLocationValue() {
        return locationField.getText();
    }

    public String getSelectedCustomerValue() {
        return selectedCustomer;
    }

    public LocalDate getDateValue() {
        String dateString = dateField.getText();
        return LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public void resetKmValue() {
        kmField.setValue(0);
        kmField.setText("");
    }

    public void resetLocationValue() {
        locationField.setText(null);
    }
}