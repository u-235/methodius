package gui;

import java.awt.Insets;
import java.beans.ConstructorProperties;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class IButton extends JButton
{
    @Override
    protected void configurePropertiesFromAction(Action a) {
        AbstractButtonX.configurePropertiesFromAction(this, a);
        super.configurePropertiesFromAction(a);
    }

    @Override
    protected void actionPropertyChanged(Action action, String propertyName) {
        if (AbstractButtonX.actionPropertyChanged(this, action, propertyName))
            super.actionPropertyChanged(action, propertyName);
    }

    /**
     * Creates a button with no set text or icon.
     */
    public IButton() {
        this(null, null);
    }

    /**
     * Creates a button with an icon.
     * 
     * @param icon the Icon image to display on the button
     */
    public IButton(Icon icon) {
        this(null, icon);
    }

    /**
     * Creates a button with text.
     * 
     * @param text the text of the button
     */
    @ConstructorProperties({ "text" })
    public IButton(String text) {
        this(text, null);
    }

    /**
     * Creates a button where properties are taken from the <code>Action</code>
     * supplied.
     * 
     * @param a the <code>Action</code> used to specify the new button
     * 
     * @since 1.3
     */
    public IButton(Action a) {
        this();
        setAction(a);
    }

    /**
     * Creates a button with initial text and an icon.
     * 
     * @param text the text of the button
     * @param icon the Icon image to display on the button
     */
    public IButton(String text, Icon icon) {
        super(text, icon);
        setMargin(new Insets(0, 0, 0, 0));
        setFocusPainted(false);
        setHideActionText(true);
    }
}
