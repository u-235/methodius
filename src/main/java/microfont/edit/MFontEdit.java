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

package microfont.edit;

import microfont.MFont;

/**
 * Отменяемая операция для {@linkplain MFont шрифта}.
 * 
 * @author Nick Egorov
 */
public class MFontEdit extends AbstractEdit {
    MFont owner;
    MFont before;
    MFont after;

    public MFontEdit(MFont mf, String operation) {
        super(operation);
        owner = mf;
        before = mf.clone();
    }

    @Override
    public void undo() {
        super.undo();

        owner.copy(before);
    }

    @Override
    public void redo() {
        super.redo();

        owner.copy(after);
    }

    @Override
    public void die() {
        super.die();
        owner = null;
        before = null;
        after = null;
    }

    @Override
    public void end() {
        super.end();

        if (owner.equals(before)) {
            die();
            return;
        }

        after = owner.clone();
    }
}
