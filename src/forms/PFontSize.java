
package forms;

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
import microfont.gui.MSymbolEditor;
import utils.resource.Resource;

@SuppressWarnings("serial")
public class PFontSize extends JPanel implements PropertyChangeListener {
    Resource              res;
    MFont                 mFont;
    boolean               readOnly;
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

    public PFontSize(Resource res) {
        super();
        GridBagConstraints c = new GridBagConstraints();

        setResource(res);

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

                check = mFont.checkMarginLeft(value);
                if (value != check) sp.setValue(check);
                else mFont.setMarginLeft(value);
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

                check = mFont.checkMarginRight(value);
                if (value != check) sp.setValue(check);
                else mFont.setMarginRight(value);
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
                else mFont.setBaseline(value);
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
                else mFont.setAscent(value);
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
                else mFont.setLine(value);
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
                else mFont.setDescent(value);
            }
        });

        vMinSize = new JLabel("");
        vMaxSize = new JLabel("");

        vSizeView = new MSymbolEditor();

        c.fill = GridBagConstraints.BOTH;

        c.gridheight = 11;
        c.weightx = 1.0;
        c.weighty = 1.0;
        FontProperties.addToGrid(this, new JScrollPane(vSizeView), c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0;
        c.weighty = 0;
        c.gridheight = 1;
        FontProperties.addToGrid(this, new JLabel("font type"), c);
        c.gridwidth = GridBagConstraints.REMAINDER; // end row
        FontProperties.addToGrid(this, vFixsed, c);

        c.gridwidth = 1;
        FontProperties.addToGrid(this, new JLabel("минимально"), c);
        c.gridwidth = GridBagConstraints.REMAINDER;
        FontProperties.addToGrid(this, vMinSize, c);

        c.gridwidth = 1;
        FontProperties.addToGrid(this, new JLabel("максимально"), c);
        c.gridwidth = GridBagConstraints.REMAINDER;
        FontProperties.addToGrid(this, vMaxSize, c);

        c.gridwidth = 1;
        FontProperties.addToGrid(this, new JLabel("symbol width"), c);
        c.gridwidth = GridBagConstraints.REMAINDER; // end row
        FontProperties.addToGrid(this, vWidth, c);

        c.gridwidth = 1;
        FontProperties.addToGrid(this, new JLabel("symbol height"), c);
        c.gridwidth = GridBagConstraints.REMAINDER; // end row
        FontProperties.addToGrid(this, vHeight, c);

        c.gridwidth = 1;
        FontProperties.addToGrid(this, new JLabel("baseline"), c);
        c.gridwidth = GridBagConstraints.REMAINDER; // end row
        FontProperties.addToGrid(this, vBase, c);

        c.gridwidth = 1;
        FontProperties.addToGrid(this, new JLabel("ascent"), c);
        c.gridwidth = GridBagConstraints.REMAINDER; // end row
        FontProperties.addToGrid(this, vAscent, c);

        c.gridwidth = 1;
        FontProperties.addToGrid(this, new JLabel("capital"), c);
        c.gridwidth = GridBagConstraints.REMAINDER; // end row
        FontProperties.addToGrid(this, vCapital, c);

        c.gridwidth = 1;
        FontProperties.addToGrid(this, new JLabel("descent"), c);
        c.gridwidth = GridBagConstraints.REMAINDER; // end row
        FontProperties.addToGrid(this, vDescent, c);

        c.gridwidth = 1;
        FontProperties.addToGrid(this, new JLabel("left margin"), c);
        c.gridwidth = GridBagConstraints.REMAINDER; // end row
        FontProperties.addToGrid(this, vLeft, c);

        c.gridwidth = 1;
        FontProperties.addToGrid(this, new JLabel("right margin"), c);
        c.gridwidth = GridBagConstraints.REMAINDER; // end row
        FontProperties.addToGrid(this, vRight, c);

        updateReadOnly();
    }

    public Resource getResource() {
        return res;
    }

    public void setResource(Resource res) {
        this.res = res;
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
        vLeft.setValue(mFont.getMarginLeft());
        vRight.setValue(mFont.getMarginRight());
        vBase.setValue(mFont.getBaseline());
        vAscent.setValue(mFont.getAscent());
        vCapital.setValue(mFont.getLine());
        vDescent.setValue(mFont.getDescent());
        vMinSize.setText(((Integer) mFont.getMinWidth()).toString());
        vMaxSize.setText(((Integer) mFont.getMaxWidth()).toString());
        vSizeView.setSymbol(mFont.symbolByIndex(35));
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        updateReadOnly();
    }

    void updateReadOnly() {
        vFixsed.setEnabled(!readOnly);
        vWidth.setEnabled(!readOnly);
        vHeight.setEnabled(!readOnly);
        vLeft.setEnabled(!readOnly);
        vRight.setEnabled(!readOnly);
        vBase.setEnabled(!readOnly);
        vAscent.setEnabled(!readOnly);
        vCapital.setEnabled(!readOnly);
        vDescent.setEnabled(!readOnly);
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        vLeft.setValue(mFont.getMarginLeft());
        vRight.setValue(mFont.getMarginRight());
        vBase.setValue(mFont.getBaseline());
        vAscent.setValue(mFont.getAscent());
        vCapital.setValue(mFont.getLine());
        vDescent.setValue(mFont.getDescent());
        vMinSize.setText(((Integer) mFont.getMinWidth()).toString());
        vMaxSize.setText(((Integer) mFont.getMaxWidth()).toString());
    }
}
