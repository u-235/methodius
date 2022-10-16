
package gui;

import java.awt.Insets;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JToggleButton;

@SuppressWarnings("serial")
public class IToggleButton extends JToggleButton {
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
     * Creates an initially unselected toggle button without setting the text or
     * image.
     */
    public IToggleButton() {
        this(null, null, false);
    }

    /**
     * Creates an initially unselected toggle button with the specified image
     * but no text.
     * 
     * @param icon the image that the button should display
     */
    public IToggleButton(Icon icon) {
        this(null, icon, false);
    }

    /**
     * Creates a toggle button with the specified image and selection state, but
     * no text.
     * 
     * @param icon the image that the button should display
     * @param selected if true, the button is initially selected; otherwise, the
     *            button is initially unselected
     */
    public IToggleButton(Icon icon, boolean selected) {
        this(null, icon, selected);
    }

    /**
     * Creates an unselected toggle button with the specified text.
     * 
     * @param text the string displayed on the toggle button
     */
    public IToggleButton(String text) {
        this(text, null, false);
    }

    /**
     * Creates a toggle button with the specified text and selection state.
     * 
     * @param text the string displayed on the toggle button
     * @param selected if true, the button is initially selected; otherwise, the
     *            button is initially unselected
     */
    public IToggleButton(String text, boolean selected) {
        this(text, null, selected);
    }

    /**
     * Creates a toggle button where properties are taken from the Action
     * supplied.
     * 
     * @since 1.3
     */
    public IToggleButton(Action a) {
        this();
        setAction(a);
    }

    /**
     * Creates a toggle button that has the specified text and image, and that
     * is initially unselected.
     * 
     * @param text the string displayed on the button
     * @param icon the image that the button should display
     */
    public IToggleButton(String text, Icon icon) {
        this(text, icon, false);
    }

    /**
     * Creates a toggle button with the specified text, image, and selection
     * state.
     * 
     * @param text the text of the toggle button
     * @param icon the image that the button should display
     * @param selected if true, the button is initially selected; otherwise, the
     *            button is initially unselected
     */
    public IToggleButton(String text, Icon icon, boolean selected) {
        super(text, icon, selected);
        setMargin(new Insets(0, 0, 0, 0));
        setFocusPainted(false);
        setHideActionText(true);
    }

}
