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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractListModel;
import javax.swing.Timer;
import microfont.MFont;
import microfont.MSymbol;
import microfont.events.PixselMapEvent;
import microfont.events.PixselMapListener;

/**
 * Класс для представления {@linkplain MFont шрифта} в JList.
 * 
 */
public class MListModel extends AbstractListModel<MSymbol> implements
                PropertyChangeListener, PixselMapListener {
    private static final long serialVersionUID = 1L;
    /** */
    private MFont             font;
    private Timer             delay;
    private int               firstIndex, lastIndex;

    public MListModel() {
        final MListModel p = this;
        delay = new Timer(400, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                synchronized (this) {
                    fireContentsChanged(p, firstIndex, firstIndex);
                    firstIndex = Integer.MAX_VALUE;
                    lastIndex = -1;
                }
            }
        });
        delay.setRepeats(false);
    }

    private void delayUpdate(int index) {
        synchronized (delay) {
            firstIndex = firstIndex > index ? index : firstIndex;
            lastIndex = lastIndex < index ? index : lastIndex;
            if (!delay.isRunning()) delay.start();
            else delay.restart();
        }
    }

    /**
     * 
     * @return
     */
    public MFont getFont() {
        return font;
    }

    /**
     * 
     * @param font
     */
    public void setFont(MFont font) {
        int oldInd = 0;

        if (this.font != null) {
            oldInd = this.font.length() - 1;
            this.font.removePropertyChangeListener(this);
            this.font.removePixselMapListener(this);
        }

        this.font = font;

        if (oldInd > 0) fireIntervalRemoved(this, 0, oldInd);

        if (font != null) {
            font.addPropertyChangeListener(this);
            font.addPixselMapListener(this);
            int i = this.font.length() - 1;
            if (i < 0) i = 0;
            fireIntervalAdded(this, 0, i);
        }
    }

    @Override
    public int getSize() {
        if (font == null) return 0;

        return font.length();
    }

    @Override
    public MSymbol getElementAt(int index) {
        if (font == null) return null;
        if (index < 0) index = 0;
        if (index >= font.length()) index = font.length();
        return font.symbolByIndex(index);
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        MFont font;

        if ((event.getSource() instanceof MFont)) {
            font = (MFont) event.getSource();
            fireContentsChanged(this, 0, font.length() - 1);
        }
    }

    @Override
    public void pixselChanged(PixselMapEvent e) {
        delayUpdate(font.indexAt((MSymbol) e.getSource()));
    }
}
