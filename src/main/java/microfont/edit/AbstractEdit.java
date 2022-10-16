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

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

/**
 * Реализация отменяемых операций <code>UndoableEdit</code>.
 */
public class AbstractEdit implements UndoableEdit {
    private static final String DEFAULT_UNDO_NAME = "Undo";
    private static final String DEFAULT_REDO_NAME = "Redo";
    private static String       undoName          = DEFAULT_UNDO_NAME;
    private static String       redoName          = DEFAULT_REDO_NAME;

    private String              operation;
    private boolean             isUndo;
    private boolean             isEmpty;
    private boolean             progress;

    /**
     * Создание отменяемой операции.
     * 
     * @param operation Название операции. Должно быть представлено текстом,
     *            понятным человеку, на языке текущей локализации.
     */
    public AbstractEdit(String operation) {
        this.operation = operation;
        isUndo = true;
        isEmpty = false;
        progress = true;
    }

    @Override
    public void undo() throws CannotUndoException {
        if (!canUndo()) throw new CannotUndoException();
        isUndo = false;
    }

    @Override
    public boolean canUndo() {
        return isUndo && !isEmpty;
    }

    @Override
    public void redo() throws CannotRedoException {
        if (!canRedo()) throw new CannotRedoException();
        isUndo = true;
    }

    @Override
    public boolean canRedo() {
        return !isUndo && !isEmpty;
    }

    public boolean isLive() {
        return !isEmpty;
    }

    @Override
    public void die() {
        if (isEmpty) return;

        isEmpty = true;
        end();
    }

    @Override
    public boolean addEdit(UndoableEdit anEdit) {
        return false;
    }

    @Override
    public boolean replaceEdit(UndoableEdit anEdit) {
        return false;
    }

    @Override
    public boolean isSignificant() {
        return true;
    }

    @Override
    public String getPresentationName() {
        return operation;
    }

    @Override
    public String getUndoPresentationName() {
        if (operation != null) return undoName + " " + operation;
        return undoName;
    }

    /**
     * Устанавливает название отмены операций.
     * 
     * @param name Понятное человеку общее название отмены операций. Например,
     *            на английском языке это <b>Undo</b>, на русском -
     *            <b>Отменить</b>.
     * @see #getUndoPresentationName()
     * @see #getPresentationName()
     */
    public static void setUndoName(String name) {
        if (name != null) undoName = name;
        else undoName = DEFAULT_UNDO_NAME;
    }

    @Override
    public String getRedoPresentationName() {
        if (operation != null) return redoName + " " + operation;
        return redoName;
    }

    /**
     * 
     * Устанавливает название повтора операций.
     * 
     * @param name Понятное человеку общее название повтора операций. Например,
     *            на английском языке это <b>Redo</b>, на русском -
     *            <b>Повторить</b> или <b>Вернуть</b>.
     * @see #getRedoPresentationName()
     * @see #getPresentationName()
     */
    public static void setRedoName(String name) {
        if (name != null) redoName = name;
        else redoName = DEFAULT_REDO_NAME;
    }

    /**
     * Возвращает <code>true</code> если объект может принимать данные.<br>
     * После вызова {@link #end} возвращает </false>.
     */
    public boolean isInProgress() {
        return progress;
    }

    /**
     * Завершает состояние приёма данных. Последующие вызовы не имеют влияния.
     * 
     * @see #isInProgress()
     * @see #addEdit(UndoableEdit)
     */
    public void end() {
        progress = false;
    }
}
