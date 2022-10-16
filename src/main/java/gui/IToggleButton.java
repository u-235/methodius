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
