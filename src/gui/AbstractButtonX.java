package gui;

import java.awt.Cursor;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.Icon;

public class AbstractButtonX
{
    public static void configurePropertiesFromAction(AbstractButton btn,
                    Action a) {
        ActionX ax;

        if (a instanceof ActionX) {
            ax = (ActionX) a;

            btn.setSelected(ax.selected);
            btn.setDisabledIcon(getIconFromAction(ax,
                            ActionX.LARGE_ICON_DISABLED_KEY));
            btn.setDisabledSelectedIcon(getIconFromAction(ax,
                            ActionX.LARGE_ICON_DISABLED_SELECTED_KEY));
            btn.setPressedIcon(getIconFromAction(ax,
                            ActionX.LARGE_ICON_PRESSED_KEY));
            btn.setRolloverIcon(getIconFromAction(ax,
                            ActionX.LARGE_ICON_ROLLOVER_KEY));
            btn.setRolloverSelectedIcon(getIconFromAction(ax,
                            ActionX.LARGE_ICON_ROLLOVER_SELECTED_KEY));
            btn.setSelectedIcon(getIconFromAction(ax,
                            ActionX.LARGE_ICON_SELECTED_KEY));
        }

        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public static boolean actionPropertyChanged(AbstractButton btn,
                    Action action, String propertyName) {
        ActionX ax;
        boolean oldAct = false;

        if (action instanceof ActionX) {
            ax = (ActionX) action;

            if (propertyName == "selected") {
                btn.setSelected(ax.selected);
            }
            else if (propertyName == ActionX.LARGE_ICON_DISABLED_KEY) {
                btn.setDisabledIcon(getIconFromAction(ax, propertyName));
            }
            else if (propertyName == ActionX.LARGE_ICON_DISABLED_SELECTED_KEY) {
                btn.setDisabledSelectedIcon(getIconFromAction(ax, propertyName));
            }
            else if (propertyName == ActionX.LARGE_ICON_PRESSED_KEY) {
                btn.setPressedIcon(getIconFromAction(ax, propertyName));
            }
            else if (propertyName == ActionX.LARGE_ICON_ROLLOVER_KEY) {
                btn.setRolloverIcon(getIconFromAction(ax, propertyName));
            }
            else if (propertyName == ActionX.LARGE_ICON_ROLLOVER_SELECTED_KEY) {
                btn.setRolloverSelectedIcon(getIconFromAction(ax, propertyName));
            }
            else if (propertyName == ActionX.LARGE_ICON_SELECTED_KEY) {
                btn.setSelectedIcon(getIconFromAction(ax, propertyName));
            }
            else oldAct = true;
        }

        return oldAct;
    }

    private static Icon getIconFromAction(ActionX ax, String propertyName) {
        return (Icon) ax.getValue(propertyName);
    }
}
