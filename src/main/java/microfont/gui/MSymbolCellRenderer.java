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

package microfont.gui;

import java.awt.Component;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import microfont.MSymbol;

/**
 * 
 *
 */
public class MSymbolCellRenderer extends MSymbolView implements
                ListCellRenderer<MSymbol> {
    private static final long serialVersionUID = 1L;

    @Override
    public Component getListCellRendererComponent(
                    JList<? extends MSymbol> list, MSymbol value, int index,
                    boolean isSelected, boolean cellHasFocus) {
        setPixselMap(value);
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        return this;
    }
}
