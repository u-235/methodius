
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

    /**
     * @inheritDoc
     */

    @Override
    public Component getListCellRendererComponent(
                    JList<? extends MSymbol> list, MSymbol value, int index,
                    boolean isSelected, boolean cellHasFocus) {
        super.setSymbol(value);
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        return this;
    }

    @Override
    public void setSymbol(MSymbol s) {
    }

    @Override
    public void invalidate() {
        sizeValid = false;
    }

    @Override
    public void validate() {
        doLayout();
    }

    @Override
    public void revalidate() {
        invalidate();
        validate();
    }
}
