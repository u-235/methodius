
package forms;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import utils.resource.Resource;
import microfont.MFont;

public class FontProperties {
    public static final int ACTION_CANCEL = 0;
    public static final int ACTION_OK     = 1;
    private Resource        res;
    private JDialog         form;
    private JTabbedPane     tab;
    private MFont           font;
    private int             exitCode;
    private PFontGeneral    pGeneral;
    private PFontAuthor     pAuthor;
    private PFontSize       pSize;
    private JCheckBox       btnReadonly;
    private JButton         btnOk;
    private JButton         btnCancel;

    public FontProperties(JFrame parent, Resource res) {
        this.res = res;

        form = new JDialog(parent, true);
        form.setLocationRelativeTo(parent);
        form.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        form.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
                exitCode = ACTION_CANCEL;
                stop();
            }

            @Override
            public void windowClosed(WindowEvent e) {
            }

            @Override
            public void windowActivated(WindowEvent e) {
            }
        });
        form.setLayout(new BorderLayout());

        pGeneral = new PFontGeneral(res);
        pAuthor = new PFontAuthor(res);
        pSize = new PFontSize(res);

        tab = new JTabbedPane();
        tab.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        tab.add(pGeneral);
        tab.add(pAuthor);
        tab.add(pSize);
        tab.add(doMap());

        form.add(tab, BorderLayout.CENTER);
        form.add(doButtons(), BorderLayout.SOUTH);

        updateApperance();

        exitCode = ACTION_CANCEL;
    }

    void updateApperance() {
        form.setTitle(res.getText("properties.form"));
        form.setIconImages(res.getImages("properties.form"));

        tab.setTitleAt(0, res.getText("properties.tab.general"));
        tab.setToolTipTextAt(0, res.getToolTip("properties.tab.general"));
        tab.setTitleAt(1, res.getText("properties.tab.author"));
        tab.setToolTipTextAt(1, res.getToolTip("properties.tab.author"));
        tab.setTitleAt(2, res.getText("properties.tab.size"));
        tab.setToolTipTextAt(2, res.getToolTip("properties.tab.size"));
        tab.setTitleAt(3, res.getText("properties.tab.charset"));
        tab.setToolTipTextAt(3, res.getToolTip("properties.tab.charset"));

        btnReadonly.setText(res.getText("properties.form.readonly"));
        btnReadonly.setToolTipText(res.getToolTip("properties.form.readonly"));
        btnOk.setText(res.getText("properties.form.ok"));
        btnOk.setToolTipText(res.getToolTip("properties.form.ok"));
        btnCancel.setText(res.getText("properties.form.cancel"));
        btnCancel.setToolTipText(res.getToolTip("properties.form.cancel"));
    }

    /**
     * Добавляет компонент <b>c</b> к <b>parent</b> используя менеджер
     * {@linkplain GridBagLayout}
     * 
     * @param parent Контейнер для компонентов.
     * @param c Добавляемый компонент.
     * @param cnst Константы размещения компонента.
     */
    public static void addToGrid(JComponent parent, Component c,
                    GridBagConstraints cnst) {
        LayoutManager lm = parent.getLayout();

        if (lm == null || !(lm instanceof GridBagLayout)) {
            lm = new GridBagLayout();
            parent.setLayout(lm);
        }

        ((GridBagLayout) lm).setConstraints(c, cnst);
        parent.add(c);
    }

    JPanel doMap() {
        JPanel ret;

        ret = new JPanel();
        ret.setLayout(new BoxLayout(ret, BoxLayout.Y_AXIS));

        return ret;
    }

    JToolBar doButtons() {
        JToolBar ret;
        javax.swing.JSeparator separ;

        ret = new JToolBar();
        ret.setFloatable(false);

        btnReadonly = new JCheckBox();
        btnReadonly.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setReadOnly(((JCheckBox) e.getSource()).isSelected());
            }
        });
        btnReadonly.setSelected(true);
        setReadOnly(true);

        btnOk = new JButton();
        btnOk.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                exitCode = ACTION_OK;
                stop();
            }
        });

        btnCancel = new JButton();
        btnCancel.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                exitCode = ACTION_CANCEL;
                stop();
            }
        });

        Dimension size = btnCancel.getPreferredSize();
        btnOk.setPreferredSize(size);

        ret.add(btnReadonly);
        separ = new JSeparator();
        separ.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        ret.add(separ);
        ret.add(btnOk);
        separ = new JToolBar.Separator(new Dimension(12, 1));
        separ.setMaximumSize(new Dimension(12, Integer.MAX_VALUE));
        ret.add(separ);
        ret.add(btnCancel);

        return ret;
    }

    public void setReadOnly(boolean selected) {
        pGeneral.setReadOnly(selected);
        pAuthor.setReadOnly(selected);
        pSize.setReadOnly(selected);
    }

    public int start(MFont font) {
        exitCode = ACTION_CANCEL;
        if (font == null) return exitCode;
        setMFont(font);
        form.pack();
        form.setVisible(true);
        return exitCode;
    }

    public void stop() {
        form.dispose();
    }

    public void setMFont(MFont font) {
        this.font = font;
        pGeneral.setMFont(this.font);
        pAuthor.setMFont(this.font);
        pSize.setMFont(this.font);

        setReadOnly(true);
    }
}
