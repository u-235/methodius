/*
 * Copyright 2013-2022 © Nick Egorrov, nicegorov@yandex.ru.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package forms;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import microfont.MFont;
import utils.config.ConfigNode;
import utils.resource.Resource;
import forms.properties.PFontGeneral;
import forms.properties.PFontSize;

public class FontProperties extends PanelControl {
    public static final int ACTION_CANCEL = 0;
    public static final int ACTION_OK     = 1;
    private JDialog         form;
    private JTabbedPane     tab;
    private MFont           font;
    private int             exitCode;
    private PFontGeneral    pGeneral;
    private PFontSize       pSize;
    private JButton         btnOk;
    private JButton         btnCancel;

    public FontProperties(JFrame parent, Resource res, ConfigNode config) {
        form = new JDialog(parent, true);
        form.setLocationRelativeTo(parent);
        form.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        form.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitCode = ACTION_CANCEL;
                stop();
            }
        });
        form.setLayout(new BorderLayout());

        pGeneral = new PFontGeneral(res, config);
        pSize = new PFontSize(res, config);

        tab = new JTabbedPane();
        tab.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        tab.add(pGeneral.view());
        tab.add(pSize.view());
        tab.add(doMap());

        form.add(tab, BorderLayout.CENTER);
        form.add(doButtons(), BorderLayout.SOUTH);

        setResource(res);
        setConfigNode(config);

        exitCode = ACTION_CANCEL;
    }

    @Override
    public void updateFromResource() {
        form.setTitle(res.getText("properties.form"));

        tab.setTitleAt(0, res.getText("properties.tab.general"));
        tab.setToolTipTextAt(0, res.getToolTip("properties.tab.general"));
        tab.setTitleAt(1, res.getText("properties.tab.size"));
        tab.setToolTipTextAt(1, res.getToolTip("properties.tab.size"));
        tab.setTitleAt(2, res.getText("properties.tab.charset"));
        tab.setToolTipTextAt(2, res.getToolTip("properties.tab.charset"));

        btnOk.setText(res.getText("properties.form.ok"));
        btnOk.setToolTipText(res.getToolTip("properties.form.ok"));
        btnCancel.setText(res.getText("properties.form.cancel"));
        btnCancel.setToolTipText(res.getToolTip("properties.form.cancel"));
    }

    /**
     * Добавляет компонент <b>c</b> к <b>owner</b> используя менеджер
     * {@linkplain GridBagLayout}
     * 
     * @param owner Контейнер для компонентов.
     * @param c Добавляемый компонент.
     * @param cnst Константы размещения компонента.
     */
    public static void addToGrid(JComponent parent, Component c,
                    GridBagConstraints cnst) {
        LayoutManager lm = parent.getLayout();

        if (!(lm instanceof GridBagLayout)) {
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
        pSize.setMFont(this.font);
    }

    @Override
    public void updateFromConfig() {
        // TODO Auto-generated method stub
    }
}
