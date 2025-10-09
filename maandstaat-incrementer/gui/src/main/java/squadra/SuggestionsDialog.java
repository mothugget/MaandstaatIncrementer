package squadra;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

public class SuggestionsDialog extends JDialog {

    public SuggestionsDialog(JFrame frame) {
        super(frame, "Set up suggestions", true);

        String[] columns = { "Suggestions" };
        Object[][] data = { { "suggestion1" }, { "suggestion2" }, { "" } };

        DefaultTableModel suggestionsTableModel = new DefaultTableModel(data, columns) {
            // Make all cells editable
            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };

        JTable suggestionsTable = new JTable(suggestionsTableModel);
        JScrollPane scrollPane = new JScrollPane(suggestionsTable);
        scrollPane.setPreferredSize(suggestionsTable.getPreferredSize());
        DefaultTableModel model = (DefaultTableModel) suggestionsTable.getModel();

        // Listen for changes in the table
        suggestionsTableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    int lastRow = suggestionsTableModel.getRowCount() - 1;
                    int lastCol = e.getColumn();
                    Object value = suggestionsTableModel.getValueAt(lastRow, lastCol);

                    // If the user typed something in the last row, add a new empty row
                    if (lastRow == e.getFirstRow() && value != null && !value.toString().trim().isEmpty()) {
                        suggestionsTableModel.addRow(new Object[] { "" });
                    }
                }
            }
        });

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        scrollPane.setPreferredSize(new Dimension(400, 200));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1; // Give extra horizontal space
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;

        add(scrollPane, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 8;
        add(buttonPanel, gbc);

        // Button actions
        okButton.addActionListener(e -> {
            for (int i = 0; i < model.getRowCount(); i++) {
                Object value = model.getValueAt(i, 0);
                if (value != null && !value.toString().trim().isEmpty()) {
                    System.out.println("Suggestion " + i + ": " + value);
                }
            }
            dispose();
        });

        cancelButton.addActionListener(e -> {
            dispose();
        });

        pack();
        setLocationRelativeTo(frame);
    }

    // For testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SuggestionsDialog dialog = new SuggestionsDialog(new JFrame());
            dialog.setVisible(true);
        });
    }
}
