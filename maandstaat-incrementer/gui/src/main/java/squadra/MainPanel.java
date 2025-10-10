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
    private String selection;
    private MaskFormatter dateMask;
    private final MainPanelListener listener;
    private final JFrame frame;
    private LocalDate date;
    private final JFormattedTextField dateField;
    private final JTextField descriptionField;
    private final JFormattedTextField hoursField;

    public MainPanel(JFrame frame, String selectedCustomer, String[] customers, String filePath,
            MainPanelListener listener, LocalDate date) {
        this.selection = selectedCustomer;
        this.listener = listener;
        this.frame = frame;
        this.date = date;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(5, 5, 5, 5); // spacing

        gbc.gridx = 0;
        gbc.gridy = 0;
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
        gbc.gridx = 0;
        gbc.gridy = 1;
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
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        this.add(descriptionLabel, gbc);

        descriptionField = new JTextField(21);
        descriptionField.setText("Enter description here");
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
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        this.add(hoursLabel, gbc);

        NumberFormat floatFormat = NumberFormat.getNumberInstance();
        hoursField = new JFormattedTextField(floatFormat);
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
        gbc.gridy = 4;
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
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 0;
        this.add(publishButton, gbc);
        // Action listener for button
        publishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                try {
                    if (listener != null) {
                        listener.onPublish();
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

    public String getDescription() {
        return descriptionField.getText();
    }

    public float getHours(){
        return Float.parseFloat(hoursField.getText());
    }

    public String getSelection(){
        return selection;
    }
    public LocalDate getDate(){
        return date;
    }
}