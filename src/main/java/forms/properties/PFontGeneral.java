
package forms.properties;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import forms.PanelControl;
import microfont.MFont;
import utils.config.ConfigNode;
import utils.resource.Resource;

public class PFontGeneral extends PanelControl implements
                PropertyChangeListener {
    protected MFont       mFont;
    protected JPanel      namePanel;
    protected JPanel      protoPanel;
    protected JScrollPane scroll;
    protected JTextField  name;
    protected JTextField  proto;
    protected JTextArea   description;

    public PFontGeneral(Resource res, ConfigNode config) {
        view = new JPanel();
        view.setLayout(new BoxLayout(view, BoxLayout.Y_AXIS));

        name = new JTextField(24);
        name.addFocusListener(new FocusListener() {
            @Override
            public void focusLost(FocusEvent e) {
                if (mFont == null) return;
                mFont.setName(name.getText().trim());
            }

            @Override
            public void focusGained(FocusEvent e) {
                //
            }
        });
        namePanel = new JPanel();
        namePanel.add(name);
        namePanel.setBorder(new TitledBorder(""));

        proto = new JTextField(24);
        proto.addFocusListener(new FocusListener() {
            @Override
            public void focusLost(FocusEvent e) {
                if (mFont == null) return;
                mFont.setPrototype(proto.getText().trim());
            }

            @Override
            public void focusGained(FocusEvent e) {
                //
            }
        });
        protoPanel = new JPanel();
        protoPanel.add(proto);
        protoPanel.setBorder(new TitledBorder(""));

        description = new JTextArea(2, 24);
        description.addFocusListener(new FocusListener() {
            @Override
            public void focusLost(FocusEvent e) {
                if (mFont == null) return;
                mFont.setDescriptin(description.getText().trim());
            }

            @Override
            public void focusGained(FocusEvent e) {
                //
            }
        });
        description.setWrapStyleWord(true);
        description.setLineWrap(true);
        description.setDragEnabled(true);
        scroll = new JScrollPane(description);
        scroll.setBorder(new TitledBorder(""));

        view.add(namePanel, 0);
        view.add(protoPanel, 1);
        view.add(scroll, 2);

        setResource(res);
        setConfigNode(config);
    }

    @Override
    public void updateFromResource() {
        Border border;
        TitledBorder tBorder;
        String text;

        if (res == null) {
            // TODO error --> log
            return;
        }

        text = res.getString("properties.tab.general.name",
                        Resource.TEXT_TIP_KEY);
        namePanel.setToolTipText(text);
        border = namePanel.getBorder();
        if (border instanceof TitledBorder) {
            tBorder = (TitledBorder) border;
            text = res.getString("properties.tab.general.name",
                            Resource.TEXT_NAME_KEY);
            tBorder.setTitle(text);
        } else {
            // TODO error --> log
        }

        text = res.getString("properties.tab.general.prototype",
                        Resource.TEXT_TIP_KEY);
        protoPanel.setToolTipText(text);
        border = protoPanel.getBorder();
        if (border instanceof TitledBorder) {
            tBorder = (TitledBorder) border;
            text = res.getString("properties.tab.general.prototype",
                            Resource.TEXT_NAME_KEY);
            tBorder.setTitle(text);
        } else {
            // TODO error --> log
        }

        text = res.getString("properties.tab.general.description",
                        Resource.TEXT_TIP_KEY);
        scroll.setToolTipText(text);
        border = scroll.getBorder();
        if (border instanceof TitledBorder) {
            tBorder = (TitledBorder) border;
            text = res.getString("properties.tab.general.description",
                            Resource.TEXT_NAME_KEY);
            tBorder.setTitle(text);
        } else {
            // TODO error --> log
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
        name.setText(mFont.getName());
        proto.setText(mFont.getPrototype());
        description.setText(mFont.getDescriptin());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // TODO Auto-generated method stub
    }

    @Override
    public void updateFromConfig() {
        // TODO Auto-generated method stub
    }
}
