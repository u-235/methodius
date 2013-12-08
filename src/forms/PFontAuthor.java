
package forms;

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
import utils.resource.Resource;
import microfont.MFont;

@SuppressWarnings("serial")
public class PFontAuthor extends JPanel implements PropertyChangeListener {
    Resource           res;
    MFont              mFont;
    boolean            readOnly=false;
    private JTextArea vName;

    public PFontAuthor(Resource res) {
        super();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        setResource(res);

        vName = new JTextArea(2,24);
        vName.addFocusListener(new FocusListener() {
            @Override
            public void focusLost(FocusEvent e) {
                if (mFont == null) return;
                mFont.setAuthor(vName.getText().trim());
            }

            @Override
            public void focusGained(FocusEvent e) {
            }
        });

        vName.setWrapStyleWord(true);
        vName.setLineWrap(true);
        JScrollPane scroll = new JScrollPane(vName);
        add(scroll);
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
        vName.setText(mFont.getAuthor());
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        //this.readOnly = readOnly;
        updateReadOnly();
    }

    void updateReadOnly() {
        vName.setEditable(!readOnly);
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        // TODO Auto-generated method stub

    }
}
