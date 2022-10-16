
package gui;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class IMenu extends JMenu {
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
     * Constructs a new <code>JMenu</code> with no text.
     */
    public IMenu() {
        this("");
    }

    /**
     * Constructs a new <code>JMenu</code> with the supplied string as its text.
     * 
     * @param s the text for the menu label
     */
    public IMenu(String s) {
        super(s);
    }

    /**
     * Constructs a menu whose properties are taken from the <code>Action</code>
     * supplied.
     * 
     * @param a an <code>Action</code>
     * 
     * @since 1.3
     */
    public IMenu(Action a) {
        this();
        setAction(a);
    }

    /**
     * Creates a new menu item with the specified text and appends it to the end
     * of this menu.
     * 
     * @param s the string for the menu item to be added
     */
    @Override
    public JMenuItem add(String s) {
        return add(new IMenuItem(s));
    }

    /**
     * Creates a new menu item attached to the specified <code>Action</code>
     * object and appends it to the end of this menu.
     * 
     * @param a the <code>Action</code> for the menu item to be added
     * @see Action
     */
    @Override
    public JMenuItem add(Action a) {
        return add(new IMenuItem(a));
    }
}
