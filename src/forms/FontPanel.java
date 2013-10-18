package forms;

import java.awt.BorderLayout;
import java.awt.LayoutManager2;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import microfont.MFont;
import microfont.MSymbol;
import microfont.gui.MListModel;
import microfont.gui.MSymbolCellRenderer;
import microfont.gui.MSymbolView;

public class FontPanel extends JPanel
{
    MFont                       font;
    MSymbol                     symbol;
    protected int               listIndex;
    private MSymbolView         view;
    private JList<MSymbol>               list;
    private MListModel          listModel;
    private MSymbolCellRenderer listRender;

    public FontPanel() {
        LayoutManager2 pLay;
        pLay = new BorderLayout();
        this.setLayout(pLay);

        view = new MSymbolView();
        list = new JList<MSymbol>();
        this.add(new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                        new JScrollPane(view), new JScrollPane(list)),
                        BorderLayout.CENTER);
        // this.add(new JScrollPane(view), BorderLayout.NORTH);
        // this.add(new JScrollPane(list), BorderLayout.CENTER);

        listModel = new MListModel();
        listRender = new MSymbolCellRenderer();
        list.setModel(listModel);
        list.setCellRenderer(listRender);
        list.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                onSelect((JList<MSymbol>) e.getSource());
            }
        });
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JList<?> lst = (JList<?>) e.getSource();
                listIndex = lst.locationToIndex(e.getPoint());
            }
        });
    }

    public void setMFont(MFont font) {
        this.font = font;
        listModel.setFont(font);
        list.setSelectedIndex(0);
    }

    void onSelect(JList<MSymbol> l) {
        MSymbol s;

        listIndex = l.getSelectedIndex();
        s = listModel.getElementAt(listIndex);
        view.setSymbol(s);
    }
}
