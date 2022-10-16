/*
 * Copyright 2013-2022 © Nick Egorrov, nicegorov@yandex.ru.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package gui;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;

@SuppressWarnings("serial")
public class ICheckBoxMenuItem extends JCheckBoxMenuItem {
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
     * Creates an initially unselected check box menu item with no set text or
     * icon.
     */
    public ICheckBoxMenuItem() {
        this(null, null, false);
    }

    /**
     * Creates an initially unselected check box menu item with an icon.
     * 
     * @param icon the icon of the CheckBoxMenuItem.
     */
    public ICheckBoxMenuItem(Icon icon) {
        this(null, icon, false);
    }

    /**
     * Creates an initially unselected check box menu item with text.
     * 
     * @param text the text of the CheckBoxMenuItem
     */
    public ICheckBoxMenuItem(String text) {
        this(text, null, false);
    }

    /**
     * Creates a menu item whose properties are taken from the Action supplied.
     * 
     * @since 1.3
     */
    public ICheckBoxMenuItem(Action a) {
        super(a);
    }

    /**
     * Creates an initially unselected check box menu item with the specified
     * text and icon.
     * 
     * @param text the text of the CheckBoxMenuItem
     * @param icon the icon of the CheckBoxMenuItem
     */
    public ICheckBoxMenuItem(String text, Icon icon) {
        this(text, icon, false);
    }

    /**
     * Creates a check box menu item with the specified text and selection
     * state.
     * 
     * @param text the text of the check box menu item.
     * @param b the selected state of the check box menu item
     */
    public ICheckBoxMenuItem(String text, boolean b) {
        this(text, null, b);
    }

    /**
     * Creates a check box menu item with the specified text, icon, and
     * selection state.
     * 
     * @param text the text of the check box menu item
     * @param icon the icon of the check box menu item
     * @param b the selected state of the check box menu item
     */
    public ICheckBoxMenuItem(String text, Icon icon, boolean b) {
        super(text, icon, b);
    }
}
