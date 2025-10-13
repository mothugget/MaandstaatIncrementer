package squadra;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class SuggestionsDialog extends JDialog {

    public SuggestionsDialog(JFrame frame,String customer, String [] suggestions) {
        super(frame, "Set up suggestions", true);

        String[] columns = { customer };
        Object[][] data = java.util.Arrays.stream(suggestions)
        .map(s -> new Object[]{s})
        .toArray(Object[][]::new);

        DefaultTableModel suggestionsTableModel = new DefaultTableModel(data, columns) {
            // Make all cells editable
            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };

        JTable suggestionsTable = new JTable(suggestionsTableModel);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
        suggestionsTable.setDefaultRenderer(Object.class, centerRenderer);

        JTableHeader header = suggestionsTable.getTableHeader();
        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) header.getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
        Font currentFont = header.getFont();
        header.setFont(currentFont.deriveFont(Font.BOLD, currentFont.getSize() + 4));

        JScrollPane scrollPane = new JScrollPane(suggestionsTable);
        scrollPane.setPreferredSize(suggestionsTable.getPreferredSize());
        DefaultTableModel model = (DefaultTableModel) suggestionsTable.getModel();

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // top, left, bottom, right
        tablePanel.add(scrollPane, BorderLayout.CENTER);

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

        setLayout(new BorderLayout());
        add(tablePanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);
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

        setLocationRelativeTo(frame);
    }

}
