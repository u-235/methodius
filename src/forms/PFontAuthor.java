package forms;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import utils.resource.Resource;

import microfont.MFont;
import microfont.events.MFontEvent;
import microfont.events.MFontListener;

@SuppressWarnings("serial")
public class PFontAuthor extends JPanel implements MFontListener
{
    Resource           res;
    MFont              mFont;
    boolean            readOnly;
    private JTextField vName;
    private JTextArea  vComtacts;

    public PFontAuthor(Resource res) {
        super();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        setResource(res);

        vName = new JTextField(24);
        vName.addFocusListener(new FocusListener() {
            @Override
            public void focusLost(FocusEvent e) {
                if (mFont == null) return;
                mFont.setAuthorName(vName.getText().trim());
            }

            @Override
            public void focusGained(FocusEvent e) {
            }
        });

        JPanel fr = new JPanel();
        fr.add(vName);
        Border border = new TitledBorder(res.getString(
                        "properties.tab.author.name", Resource.TEXT_NAME_KEY));
        fr.setBorder(border);
        vComtacts = new JTextArea(2, 24);
        vComtacts.addFocusListener(new FocusListener() {
            @Override
            public void focusLost(FocusEvent e) {
                if (mFont == null) return;
                mFont.setAuthorMail(vComtacts.getText().trim());
            }

            @Override
            public void focusGained(FocusEvent e) {
            }
        });

        vComtacts.setWrapStyleWord(true);
        vComtacts.setLineWrap(true);
        border = new TitledBorder(res.getString(
                        "properties.tab.author.contacts",
                        Resource.TEXT_NAME_KEY));
        JScrollPane scroll = new JScrollPane(vComtacts);
        scroll.setBorder(border);

        add(fr, 0);
        add(scroll, 1);
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
        if (mFont != null) mFont.removeListener(this);
        mFont = font;
        if (mFont != null) mFont.addListener(this);
        else return;
        vName.setText(mFont.getAuthorName());
        vComtacts.setText(mFont.getAuthorMail());
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        updateReadOnly();
    }

    void updateReadOnly() {
        vName.setEditable(!readOnly);
        vComtacts.setEditable(!readOnly);
    }

    @Override
    public void mFontEvent(MFontEvent change) {
    }
}
