package squadra;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class SuggestionsDialog extends JDialog {
    
    public SuggestionsDialog(JFrame frame){
        super(frame, "Set up suggestions", true);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0;
        String[] columns={"Suggestions"};
        Object[][] data={{"suggestion1"},{"suggestion2"}};
        DefaultTableModel model = new DefaultTableModel(data, columns);
        setLayout(new GridBagLayout());
        add(new JTable(model),gbc);
        pack();
    }
}
