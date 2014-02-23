
package gui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractAction;
import utils.resource.Resource;

public abstract class ActionX extends AbstractAction implements
PropertyChangeListener {
    private static final long  serialVersionUID                 = 1L;

    private Resource           resource;
    private String             name;
    protected boolean          selected;

    // public static final String LARGE_ICON_KEY = "SwingLargeIconKey";
    public static final String LARGE_ICON_DISABLED_KEY          = "SwingLargeDisableIconKey";
    public static final String LARGE_ICON_DISABLED_SELECTED_KEY = "SwingLargeDisableSelectedIconKey";
    public static final String LARGE_ICON_SELECTED_KEY          = "SwingLargeSelectedIconKey";
    public static final String LARGE_ICON_ROLLOVER_KEY          = "SwingLargeRollovedIconKey";
    public static final String LARGE_ICON_ROLLOVER_SELECTED_KEY = "SwingLargeRollovedSelectedIconKey";
    public static final String LARGE_ICON_PRESSED_KEY           = "SwingLargePressedIconKey";

    public ActionX(String name, Resource res) {
        this.name = new String(name.trim());
        resource = res;
        resource.addListener(this);
        applyPropers();
    }

    public void applyPropers() {
        putValue("selected", false);
        putValue(NAME, resource.getString(name, Resource.TEXT_NAME_KEY));
        putValue(SHORT_DESCRIPTION,
                        resource.getString(name, Resource.TEXT_TIP_KEY));

        putValue(LARGE_ICON_KEY, resource.getIcon(name, Resource.ICON_KEY));
        putValue(LARGE_ICON_DISABLED_KEY,
                        resource.getIcon(name, Resource.ICON_DISABLED_KEY));
        putValue(LARGE_ICON_DISABLED_SELECTED_KEY, resource.getIcon(name,
                        Resource.ICON_DISABLED_SELECTED_KEY));
        putValue(LARGE_ICON_PRESSED_KEY,
                        resource.getIcon(name, Resource.ICON_PRESSED_KEY));
        putValue(LARGE_ICON_ROLLOVER_KEY,
                        resource.getIcon(name, Resource.ICON_ROLLOVED_KEY));
        putValue(LARGE_ICON_ROLLOVER_SELECTED_KEY, resource.getIcon(name,
                        Resource.ICON_ROLLOVED_SELECTED_KEY));
        putValue(LARGE_ICON_SELECTED_KEY,
                        resource.getIcon(name, Resource.ICON_SELECTED_KEY));
    }

    public void setSelected(boolean newValue) {
        boolean oldValue = this.selected;

        if (oldValue != newValue) {
            this.selected = newValue;
            firePropertyChange("selected", Boolean.valueOf(oldValue),
                            Boolean.valueOf(newValue));
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        // TODO Auto-generated method stub

    }
}
