
package forms;

import java.awt.BorderLayout;
import java.awt.LayoutManager2;
import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import logic.Application;
import microfont.MFont;
import microfont.MSymbol;
import microfont.gui.MListModel;
import microfont.gui.MSymbolCellRenderer;
import microfont.gui.MSymbolView;

public class FontPanel extends JPanel {
    MFont                       font;
    MSymbol                     symbol;
    protected int               listIndex;
    private MSymbolView         view;
    private JList<MSymbol>      list;
    private MListModel          listModel;
    private MSymbolCellRenderer listRender;
    private MSymbol             selectedSymbol;
    ActionMap                   actions;

    public FontPanel(ActionMap am) {
        LayoutManager2 pLay;

        actions = am;

        pLay = new BorderLayout();
        this.setLayout(pLay);

        view = new MSymbolView();
        list = new JList<MSymbol>();
        this.add(new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                        new JScrollPane(view), new JScrollPane(list)),
                        BorderLayout.CENTER);

        listModel = new MListModel();
        listRender = new MSymbolCellRenderer();
        list.setModel(listModel);
        list.setCellRenderer(listRender);
        list.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                onSelect();
            }
        });
        // list.addMouseListener(new MouseAdapter() {
        // @Override
        // public void mousePressed(MouseEvent e) {
        // JList<?> lst = (JList<?>) e.getSource();
        // listIndex = lst.locationToIndex(e.getPoint());
        // }
        // });
    }

    public void setMFont(MFont font) {
        int index;
        this.font = font;
        index = list.getSelectedIndex();
        if (index < 0) index = 0;
        listModel.setFont(font);
        list.setSelectedIndex(index);
    }

    void onSelect() {
        MSymbol symbol;

        Action act = actions.get(Application.ON_SYMBOL_CHANGE);

        listIndex = list.getSelectedIndex();
        symbol = listModel.getElementAt(listIndex);
        if (selectedSymbol == symbol) return;

        selectedSymbol = symbol;

        act.actionPerformed(new ActionEvent(list, ActionEvent.ACTION_PERFORMED,
                        ""));
        view.setPixselMap(selectedSymbol);
    }

    public MSymbol getSelectedSymbol() {
        return selectedSymbol;
    }
}
