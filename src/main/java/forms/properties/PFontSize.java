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

package forms.properties;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import microfont.MFont;
import microfont.Metrics;
import microfont.gui.MSymbolEditor;
import utils.config.ConfigNode;
import utils.resource.Resource;
import forms.FontProperties;
import forms.PanelControl;

public class PFontSize extends PanelControl implements PropertyChangeListener {
    protected MFont       mFont;
    private JCheckBox     vFixsed;
    private JSpinner      vWidth;
    private JSpinner      vHeight;
    private JSpinner      vLeft;
    private JSpinner      vRight;
    private JSpinner      vBase;
    private JSpinner      vAscent;
    private JSpinner      vCapital;
    private JSpinner      vDescent;
    private JLabel        vMinSize;
    private JLabel        vMaxSize;
    private MSymbolEditor vSizeView;

    final static int      LABEL_TYPE     = 0;
    final static int      LABEL_WIDTH    = 1;
    final static int      LABEL_HEIGHT   = 2;
    final static int      LABEL_LEFT     = 3;
    final static int      LABEL_RIGHT    = 4;
    final static int      LABEL_BASE     = 5;
    final static int      LABEL_ASCENT   = 6;
    final static int      LABEL_CAPITAL  = 7;
    final static int      LABEL_DESCENT  = 8;
    final static int      LABEL_SIZE_MAX = 9;
    final static int      LABEL_SIZE_MIN = 10;

    JLabel[]              labels;
    String[]              resNames;

    public PFontSize(Resource res, ConfigNode config) {
        view = new JPanel();
        GridBagConstraints c = new GridBagConstraints();

        labels = new JLabel[11];
        for (int i = 0; i < labels.length; i++) {
            labels[i] = new JLabel("ghh");
        }

        resNames = new String[11];
        resNames[LABEL_HEIGHT] = "properties.tab.fontsize.height";
        resNames[LABEL_WIDTH] = "properties.tab.fontsize.width";
        resNames[LABEL_TYPE] = "properties.tab.fontsize.type";
        resNames[LABEL_ASCENT] = "properties.tab.fontsize.ascent";
        resNames[LABEL_BASE] = "properties.tab.fontsize.base";
        resNames[LABEL_CAPITAL] = "properties.tab.fontsize.capital";
        resNames[LABEL_DESCENT] = "properties.tab.fontsize.descent";
        resNames[LABEL_LEFT] = "properties.tab.fontsize.left";
        resNames[LABEL_RIGHT] = "properties.tab.fontsize.right";
        resNames[LABEL_SIZE_MAX] = "properties.tab.fontsize.max";
        resNames[LABEL_SIZE_MIN] = "properties.tab.fontsize.min";

        vFixsed = new JCheckBox("fixsed");
        vFixsed.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean fix = vFixsed.isSelected();
                vWidth.setEnabled(fix);
                mFont.setFixsed(fix);
            }
        });

        vWidth = new JSpinner();
        vWidth.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                JSpinner sp;
                int value;

                if (!mFont.isFixsed()) return;

                sp = (JSpinner) e.getSource();
                value = ((Integer) sp.getValue());
                if (value <= 0) sp.setValue(1);
                else if (value > 64) sp.setValue(64);
                else mFont.setWidth(value);
            }
        });

        vHeight = new JSpinner();
        vHeight.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                JSpinner sp;
                int value;
                sp = (JSpinner) e.getSource();
                value = ((Integer) sp.getValue());
                if (value <= 0) sp.setValue(1);
                else if (value > 96) sp.setValue(96);
                else mFont.setHeight(value);
            }
        });

        vLeft = new JSpinner();
        vLeft.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                JSpinner sp;
                int value, check;

                sp = (JSpinner) e.getSource();
                value = ((Integer) sp.getValue());

                check = mFont.checkMargin(value);
                if (value != check) sp.setValue(check);
                else mFont.setMetric(Metrics.METRIC_LEFT, value);
            }
        });

        vRight = new JSpinner();
        vRight.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                JSpinner sp;
                int value, check;

                sp = (JSpinner) e.getSource();
                value = ((Integer) sp.getValue());

                check = mFont.checkMargin(value);
                if (value != check) sp.setValue(check);
                else mFont.setMetric(Metrics.METRIC_RIGHT, value);
            }
        });

        vBase = new JSpinner();
        vBase.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                JSpinner sp;
                int value, check;

                sp = (JSpinner) e.getSource();
                value = ((Integer) sp.getValue());

                check = mFont.checkBaseline(value);
                if (value != check) sp.setValue(check);
                else mFont.setMetric(Metrics.METRIC_BASELINE, value);
            }
        });

        vAscent = new JSpinner();
        vAscent.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                JSpinner sp;
                int value, check;

                sp = (JSpinner) e.getSource();
                value = ((Integer) sp.getValue());

                check = mFont.checkAscent(value);
                if (value != check) sp.setValue(check);
                else mFont.setMetric(Metrics.METRIC_ASCENT, value);
            }
        });

        vCapital = new JSpinner();
        vCapital.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                JSpinner sp;
                int value, check;

                sp = (JSpinner) e.getSource();
                value = ((Integer) sp.getValue());

                check = mFont.checkLine(value);
                if (value != check) sp.setValue(check);
                else mFont.setMetric(Metrics.METRIC_LINE, value);
            }
        });

        vDescent = new JSpinner();
        vDescent.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                JSpinner sp;
                int value, check;

                sp = (JSpinner) e.getSource();
                value = ((Integer) sp.getValue());

                check = mFont.checkDescent(value);
                if (value != check) sp.setValue(check);
                else mFont.setMetric(Metrics.METRIC_DESCENT, value);
            }
        });

        vMinSize = new JLabel("");

        vMaxSize = new JLabel("");

        vSizeView = new MSymbolEditor();

        c.ipadx = 7;
        c.fill = GridBagConstraints.BOTH;
        c.gridheight = 12;
        c.gridwidth = 1;
        c.weightx = 1.0;
        c.weighty = 1.0;
        FontProperties.addToGrid(view, new JScrollPane(vSizeView), c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0;
        c.weighty = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        FontProperties.addToGrid(view, labels[LABEL_TYPE], c);
        c.gridwidth = GridBagConstraints.REMAINDER; // end row
        FontProperties.addToGrid(view, vFixsed, c);

        c.gridwidth = 1;
        FontProperties.addToGrid(view, labels[LABEL_SIZE_MIN], c);
        c.gridwidth = GridBagConstraints.REMAINDER;
        FontProperties.addToGrid(view, vMinSize, c);

        c.gridwidth = 1;
        FontProperties.addToGrid(view, labels[LABEL_SIZE_MAX], c);
        c.gridwidth = GridBagConstraints.REMAINDER;
        FontProperties.addToGrid(view, vMaxSize, c);

        c.gridwidth = 1;
        FontProperties.addToGrid(view, labels[LABEL_WIDTH], c);
        c.gridwidth = GridBagConstraints.REMAINDER; // end row
        FontProperties.addToGrid(view, vWidth, c);

        c.gridwidth = 1;
        FontProperties.addToGrid(view, labels[LABEL_HEIGHT], c);
        c.gridwidth = GridBagConstraints.REMAINDER; // end row
        FontProperties.addToGrid(view, vHeight, c);

        c.gridwidth = 1;
        FontProperties.addToGrid(view, labels[LABEL_BASE], c);
        c.gridwidth = GridBagConstraints.REMAINDER; // end row
        FontProperties.addToGrid(view, vBase, c);

        c.gridwidth = 1;
        FontProperties.addToGrid(view, labels[LABEL_ASCENT], c);
        c.gridwidth = GridBagConstraints.REMAINDER; // end row
        FontProperties.addToGrid(view, vAscent, c);

        c.gridwidth = 1;
        FontProperties.addToGrid(view, labels[LABEL_CAPITAL], c);
        c.gridwidth = GridBagConstraints.REMAINDER; // end row
        FontProperties.addToGrid(view, vCapital, c);

        c.gridwidth = 1;
        FontProperties.addToGrid(view, labels[LABEL_DESCENT], c);
        c.gridwidth = GridBagConstraints.REMAINDER; // end row
        FontProperties.addToGrid(view, vDescent, c);

        c.gridwidth = 1;
        FontProperties.addToGrid(view, labels[LABEL_LEFT], c);
        c.gridwidth = GridBagConstraints.REMAINDER; // end row
        FontProperties.addToGrid(view, vLeft, c);

        c.gridwidth = 1;
        FontProperties.addToGrid(view, labels[LABEL_RIGHT], c);
        c.gridwidth = GridBagConstraints.REMAINDER; // end row
        FontProperties.addToGrid(view, vRight, c);

        c.gridwidth = GridBagConstraints.REMAINDER;
        FontProperties.addToGrid(view, new JLabel(""), c);

        setResource(res);
        setConfigNode(config);
    }

    @Override
    public void updateFromResource() {
        if (res == null) {
            // TODO error --> log
            return;
        }

        vFixsed.setText(res.getString("properties.tab.fontsize.fixsed",
                        Resource.TEXT_NAME_KEY));

        for (int i = 0; i < labels.length; i++) {
            labels[i].setText(res
                            .getString(resNames[i], Resource.TEXT_NAME_KEY));
        }
    }

    public MFont getMFont() {
        return mFont;
    }

    public void setMFont(MFont font) {
        if (mFont != null) mFont.removePropertyChangeListener(this);
        mFont = font;
        if (mFont != null) mFont.addPropertyChangeListener(this);
        else return;

        vFixsed.setSelected(mFont.isFixsed());
        vWidth.setValue(mFont.getWidth());
        vHeight.setValue(mFont.getHeight());
        vLeft.setValue(mFont.getMetric(Metrics.METRIC_LEFT));
        vRight.setValue(mFont.getMetric(Metrics.METRIC_RIGHT));
        vBase.setValue(mFont.getMetric(Metrics.METRIC_BASELINE));
        vAscent.setValue(mFont.getMetric(Metrics.METRIC_ASCENT));
        vCapital.setValue(mFont.getMetric(Metrics.METRIC_LINE));
        vDescent.setValue(mFont.getMetric(Metrics.METRIC_DESCENT));
        vMinSize.setText(((Integer) mFont.getMinWidth()).toString());
        vMaxSize.setText(((Integer) mFont.getMaxWidth()).toString());
        vSizeView.setPixselMap(mFont.symbolByIndex(35));
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        vLeft.setValue(mFont.getMetric(Metrics.METRIC_LEFT));
        vRight.setValue(mFont.getMetric(Metrics.METRIC_RIGHT));
        vBase.setValue(mFont.getMetric(Metrics.METRIC_BASELINE));
        vAscent.setValue(mFont.getMetric(Metrics.METRIC_ASCENT));
        vCapital.setValue(mFont.getMetric(Metrics.METRIC_LINE));
        vDescent.setValue(mFont.getMetric(Metrics.METRIC_DESCENT));
        vMinSize.setText(((Integer) mFont.getMinWidth()).toString());
        vMaxSize.setText(((Integer) mFont.getMaxWidth()).toString());
    }

    @Override
    public void updateFromConfig() {
        // TODO Auto-generated method stub
    }
}
