
package microfont.gui;

import java.awt.Component;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import microfont.MSymbol;

/**
 * 
 *
 */
public class MSymbolCellRenderer extends MSymbolView implements
                ListCellRenderer<MSymbol> {
    private static final long serialVersionUID = 1L;

    @Override
    public Component getListCellRendererComponent(
                    JList<? extends MSymbol> list, MSymbol value, int index,
                    boolean isSelected, boolean cellHasFocus) {
        setPixselMap(value);
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        return this;
    }
}
