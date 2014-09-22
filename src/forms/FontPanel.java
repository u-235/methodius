
package forms;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.LayoutManager2;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import utils.config.ConfigNode;
import logic.Actions;
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
    ConfigNode                  config;

    public FontPanel(ActionMap am) {
        LayoutManager2 pLay;

        config = Application.application().config();

        actions = am;

        pLay = new BorderLayout();
        this.setLayout(pLay);

        view = new MSymbolView();
        list = new JList<MSymbol>();
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                        new JScrollPane(view), new JScrollPane(list));
        if (config != null) {
            split.setDividerLocation(config.node("/frame").getInt("vert", -1));
        }
        split.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (config == null) return;

                if (evt.getPropertyName().equals(
                                JSplitPane.DIVIDER_LOCATION_PROPERTY)) {
                    config.node("/frame").putInt("vert",
                                    (Integer) evt.getNewValue());
                }
            }
        });
        this.add(split, BorderLayout.CENTER);

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
        if (font != null) {
            //TODO set fonts in constructor
            Font f = listRender.getSampleFont();
            if (f==null)f = new Font("Dialog", Font.PLAIN, 24);
            f=f.deriveFont((float) font.getHeight());
            
            view.setSampleFont(f);
            listRender.setSampleFont(f);
        }
        list.setSelectedIndex(index);
    }

    void onSelect() {
        MSymbol symbol;

        Action act = actions.get(Actions.ON_SELECTED_SYMBOL_CHANGE);

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
