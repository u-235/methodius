
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
import microfont.MFont;
import utils.resource.Resource;

@SuppressWarnings("serial")
public class PFontGeneral extends JPanel implements PropertyChangeListener {
    Resource           res;
    MFont              mFont;
    boolean            readOnly;
    private JTextField vName;
    private JTextField vProto;
    private JTextArea  vDescription;

    public PFontGeneral(Resource res) {
        super();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        setResource(res);

        vName = new JTextField(24);
        vName.addFocusListener(new FocusListener() {
            @Override
            public void focusLost(FocusEvent e) {
                if (mFont == null) return;
                mFont.setName(vName.getText().trim());
            }

            @Override
            public void focusGained(FocusEvent e) {
            }
        });
        JPanel namePanel = new JPanel();
        namePanel.add(vName);
        namePanel.setBorder(new TitledBorder(res.getString(
                        "properties.tab.general.name", Resource.TEXT_NAME_KEY)));

        vProto = new JTextField(24);
        vProto.addFocusListener(new FocusListener() {
            @Override
            public void focusLost(FocusEvent e) {
                if (mFont == null) return;
                mFont.setPrototype(vProto.getText().trim());
            }

            @Override
            public void focusGained(FocusEvent e) {
            }
        });
        JPanel protoPanel = new JPanel();
        protoPanel.add(vProto);
        protoPanel.setBorder(new TitledBorder(res.getString(
                        "properties.tab.general.prototype", Resource.TEXT_NAME_KEY)));

        vDescription = new JTextArea(2, 24);
        vDescription.addFocusListener(new FocusListener() {
            @Override
            public void focusLost(FocusEvent e) {
                if (mFont == null) return;
                mFont.setDescriptin(vDescription.getText().trim());
                System.out.println("focus lost text area");
                System.out.println(vDescription.getText().trim());
            }

            @Override
            public void focusGained(FocusEvent e) {
            }
        });
        vDescription.setWrapStyleWord(true);
        vDescription.setLineWrap(true);
        JScrollPane scroll = new JScrollPane(vDescription);
        scroll.setBorder(new TitledBorder(res.getString(
                        "properties.tab.general.description",
                        Resource.TEXT_NAME_KEY)));

        add(namePanel, 0);
        add(protoPanel, 1);
        add(scroll, 2);
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
        vName.setText(mFont.getName());
        vProto.setText(mFont.getPrototype());
        vDescription.setText(mFont.getDescriptin());
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        // TODO Auto-generated method stub

    }
}
