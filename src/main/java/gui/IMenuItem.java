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
import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class IMenuItem extends JMenuItem {
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
     * Creates a <code>JMenuItem</code> with no set text or icon.
     */
    public IMenuItem() {
        this(null, (Icon) null);
    }

    /**
     * Creates a <code>JMenuItem</code> with the specified icon.
     * 
     * @param icon the icon of the <code>JMenuItem</code>
     */
    public IMenuItem(Icon icon) {
        this(null, icon);
    }

    /**
     * Creates a <code>JMenuItem</code> with the specified text.
     * 
     * @param text the text of the <code>JMenuItem</code>
     */
    public IMenuItem(String text) {
        this(text, (Icon) null);
    }

    /**
     * Creates a menu item whose properties are taken from the specified
     * <code>Action</code>.
     * 
     * @param a the action of the <code>JMenuItem</code>
     * @since 1.3
     */
    public IMenuItem(Action a) {
        this();
        setAction(a);
    }

    /**
     * Creates a <code>JMenuItem</code> with the specified text and icon.
     * 
     * @param text the text of the <code>JMenuItem</code>
     * @param icon the icon of the <code>JMenuItem</code>
     */
    public IMenuItem(String text, Icon icon) {
        super(text, icon);
    }

    /**
     * Creates a <code>JMenuItem</code> with the specified text and keyboard
     * mnemonic.
     * 
     * @param text the text of the <code>JMenuItem</code>
     * @param mnemonic the keyboard mnemonic for the <code>JMenuItem</code>
     */
    public IMenuItem(String text, int mnemonic) {
        super(text, mnemonic);
    }
}
