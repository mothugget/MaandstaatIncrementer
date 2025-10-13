package squadra;
import javax.swing.table.DefaultTableModel;

public interface SuggestionsDialogListener {
    void onOk(String customer, DefaultTableModel model);
}
