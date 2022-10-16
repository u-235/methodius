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

import java.util.logging.Level;
import microfont.AbstractMFont;
import microfont.DisallowOperationException;
import microfont.MSymbol;

public class MSymbolEdit extends AbstractEdit {
    MSymbol owner;
    MSymbol before;
    MSymbol after;

    public MSymbolEdit(MSymbol mSymbol, String operation) {
        super(operation);
        owner = mSymbol;
        before = mSymbol.clone();
    }

    @Override
    public void undo() {
        super.undo();
        try {
            owner.copy(before);
        } catch (NullPointerException e) {
            AbstractMFont.logger().log(Level.SEVERE, "copy fail in undo", e);
        } catch (DisallowOperationException e) {
            AbstractMFont.logger().log(Level.SEVERE, "copy fail in undo", e);
        }
    }

    @Override
    public void redo() {
        super.redo();
        try {
            owner.copy(after);
        } catch (NullPointerException e) {
            AbstractMFont.logger().log(Level.SEVERE, "copy fail in redo", e);
        } catch (DisallowOperationException e) {
            AbstractMFont.logger().log(Level.SEVERE, "copy fail in redo", e);
        }
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
