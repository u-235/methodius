
package microfont;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import microfont.edit.MFontEdit;
import microfont.edit.MSymbolEdit;
import utils.event.ListenerChain;

/**
 * Класс для выпуска отменяемых операций {@linkplain MFontEdit шрифта} и
 * {@linkplain MSymbolEdit символов}.
 * <p>
 * Отменяемые операции выпускаются для шрифта, выбранного методом
 * {@link #setFont(MFont)} и символов, принадлежащих этому шрифту. Для удобства
 * можно выбрать редактируемый символ при помощи
 * {@link #setEditedSymbol(MSymbol)}. Оба метода производят
 * {@linkplain #firePropertyChange(String, Object, Object) выпуск сообщения} об
 * изменении свойств.
 * <p>
 * Перед изменением шрифта необходимо вызвать {@link #fontEdit(String)} для
 * подготовки сообщения. Последующий вызов этого метода или {@link #endEdit()}
 * приведёт к генерации сообщения отменяемой операции шрифта. Перед изменениями
 * символов <b>методами шрифта</b> нужно вызвать {@link #nestedEdit(MSymbol)}.
 * <p>
 * Так же перед измением символа <b>не методами шрифта</b> для подготовки
 * сообщения нужно вызвать {@link #symbolEdit(String, MSymbol)} или
 * {@link #symbolEdit(String)}, если был выбран
 * {@linkplain #setEditedSymbol(MSymbol) редактируемый символ}. Последующий
 * вызов этих методов или <code>endEdit</code> приведёт к генерации сообщения
 * отменяемой операции символа.
 * <p>
 * Таким образом, методы <code>fontEdit</code>, <code>symbolEdit</code> и
 * <code>endEdit</code> генерируют сообщение для предыдущего вызова
 * <code>fontEdit</code> или <code>symbolEdit</code>.
 */
public class Document {
    /** Получатели сообщений. */
    private ListenerChain      listeners              = new ListenerChain();
    /**
     * Изменяемый шрифт.
     * 
     * @see #setFont(MFont)
     */
    private MFont              font;
    /**
     * Текущий изменяемый символ.
     * 
     * @see #setEditedSymbol(MSymbol)
     */
    private MSymbol            editedSymbol;
    /** Объект с изменениями шрифта. */
    private MFontEdit          undoFont;
    /** Объект с изменениями символа. */
    private MSymbolEdit        undoSymbol;
    /**
     * Имя сообщения <code>PropertyChangeEvent</code> при изменении
     * отслеживаемого шрифта.
     * 
     * @see #setFont(MFont)
     */
    public static final String PROPERTY_FONT          = "doc.font";
    /**
     * Имя сообщения <code>PropertyChangeEvent</code> при изменении
     * редактируемого символа.
     * 
     * @see #setEditedSymbol(MSymbol)
     */
    public static final String PROPERTY_EDITED_SYMBOL = "doc.symbol";

    /**
     * Добавление получателя сообщений отменяемых операций.
     * 
     * @param listener Получатель сообщений отменяемых операций.
     * @see #fireUndoEvent(UndoableEditEvent)
     * @see #removeUndoableEditListener(UndoableEditListener)
     */
    public void addUndoableEditListener(UndoableEditListener listener) {
        listeners.add(UndoableEditListener.class, listener);
    }

    /**
     * 
     * Удаление получателя сообщений отменяемых операций.
     * 
     * @param listener Получатель сообщений отменяемых операций.
     * @see #fireUndoEvent(UndoableEditEvent)
     * @see #addUndoableEditListener(UndoableEditListener)
     */
    public void removeUndoableEditListener(UndoableEditListener listener) {
        listeners.remove(UndoableEditListener.class, listener);
    }

    /**
     * Выпуск сообщений отменяемых операций.
     * 
     * @param change выпускаемое сообщение.
     * @see #addUndoableEditListener(UndoableEditListener)
     */
    protected void fireUndoEvent(UndoableEditEvent change) {
        Object[] listenerArray;

        if (listeners == null) return;

        listenerArray = listeners.getListenerList();
        for (int i = 0; i < listenerArray.length; i += 2) {
            if (listenerArray[i] == UndoableEditListener.class)
                ((UndoableEditListener) listenerArray[i + 1])
                                .undoableEditHappened(change);
        }
    }

    /**
     * Добавление получателя события изменения свойств символов.
     * 
     * @param listener Добавляемый получатель события.
     * @see #firePropertyChange(PropertyChangeEvent)
     * @see #removePropertyChangeListener(PropertyChangeListener)
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        listeners.add(PropertyChangeListener.class, listener);
    }

    /**
     * Удаление получателя события изменения свойств символов.
     * 
     * @param listener Удаляемый получатель события.
     * @see #firePropertyChange(PropertyChangeEvent)
     * @see #addPropertyChangeListener(PropertyChangeListener)
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        listeners.remove(PropertyChangeListener.class, listener);
    }

    /**
     * Генерация события изменения свойств символов. Получатели добавляются
     * функцией {@link #addPropertyChangeListener(PropertyChangeListener)}.
     * 
     * @param event Выпускаемое событие.
     */
    protected void firePropertyChange(PropertyChangeEvent event) {
        Object[] listenerArray;

        listenerArray = listeners.getListenerList();
        for (int i = 0; i < listenerArray.length; i += 2) {
            if (listenerArray[i] == PropertyChangeListener.class)
                ((PropertyChangeListener) listenerArray[i + 1])
                                .propertyChange(event);
        }
    }

    /**
     * Генерация события изменения свойств символов. Получатели добавляются
     * функцией {@link #addPropertyChangeListener(PropertyChangeListener)}.
     * 
     * @param property Название изменившегося свойства. Может быть
     *            {@link #PROPERTY_EDITED_SYMBOL} или {@link #PROPERTY_FONT}
     * @param oldValue Старое значение свойства.
     * @param newValue Новое значение свойства.
     * @see #firePropertyChange(PropertyChangeEvent)
     */
    protected void firePropertyChange(String property, Object oldValue,
                    Object newValue) {
        // Это не самый лучший способ сравнения, но в данном случае он
        // работает как надо.
        if (oldValue == newValue) return;

        firePropertyChange(new PropertyChangeEvent(this, property, oldValue,
                        newValue));
    }

    /**
     * Подготавливает документ к возможным изменениям шрифта. Если документ уже
     * содержал какие-либо изменения, то выпускается сообщение отменяемой
     * операции.<br>
     * Перед изменениями символа методами шрифта вызовите
     * {@link #nestedEdit(MSymbol)}.
     * 
     * @param operation Название операции. Должно быть представлено текстом,
     *            понятным человеку и на языке текущей локализации.
     * @see #setFont(MFont)
     * @see #endEdit()
     * @see #nestedEdit(MSymbol)
     * @see #symbolEdit(String, MSymbol)
     */
    public synchronized void fontEdit(String operation) {
        if (font == null) return;

        endEdit();
        undoFont = new MFontEdit(font, operation);
    }

    /**
     * Подготавливает документ к возможным изменениям символа. Если документ уже
     * содержал какие-либо изменения, то выпускается сообщение отменяемой
     * операции.
     * 
     * @param operation Название операции. Должно быть представлено текстом,
     *            понятным человеку и на языке текущей локализации.
     * @param sym Изменяемый символ. Этот символ должен принадлежать шрифту.
     * @see #setEditedSymbol(MSymbol)
     * @see #symbolEdit(String)
     * @see #endEdit()
     */
    public synchronized void symbolEdit(String operation, MSymbol sym) {
        if (font == null || !font.isBelong(sym)) return;

        endEdit();
        undoSymbol = new MSymbolEdit(sym, operation);
    }

    /**
     * Подготавливает документ к возможным изменениям символа. Если документ уже
     * содержал какие-либо изменения, то выпускается сообщение отменяемой
     * операции.
     * 
     * @param operation Название операции. Должно быть представлено текстом,
     *            понятным человеку и на языке текущей локализации.
     * @see #setEditedSymbol(MSymbol)
     * @see #symbolEdit(String, MSymbol)
     * @see #endEdit()
     */
    public synchronized void symbolEdit(String operation) {
        if (editedSymbol != null) symbolEdit(operation, editedSymbol);
    }

    /**
     * Подготовка к изменениям символа методами шрифта. Этот метод нужно
     * вызывать в период действия {@link #fontEdit(String)}.
     * 
     * @param sym Изменяемый символ. Этот символ должен принадлежать шрифту.
     */
    public synchronized void nestedEdit(MSymbol sym) {
        if (undoFont == null || font == null || !font.isBelong(sym)) return;

        undoFont.addEdit(new MSymbolEdit(sym, null));
    }

    /**
     * Завершает изменения, подготовленные методами {@link #fontEdit(String)} и
     * {@link #symbolEdit(String, MSymbol)} и при необходимости выпускает
     * сообщение отменяемых операций.
     */
    public synchronized void endEdit() {
        if (undoFont != null) {
            undoFont.end();
            if (undoFont.canUndo())
                fireUndoEvent(new UndoableEditEvent(this, undoFont));
        } else if (undoSymbol != null) {
            undoSymbol.end();
            if (undoSymbol.canUndo())
                fireUndoEvent(new UndoableEditEvent(this, undoSymbol));
        }

        undoFont = null;
        undoSymbol = null;
    }

    /**
     * Возвращает шрифт, отслеживаемый документом.
     * 
     * @see #setFont(MFont)
     */
    public synchronized MFont getFont() {
        return font;
    }

    /**
     * Устанавливает шрифт, отслеживаемый документом.<br>
     * Выпускается сообщение <code>PropertyChangeEvent</code> с именем
     * {@link #PROPERTY_FONT}. Так же редактируемый символ устанавливается в
     * <code>null</code> и выпускается сообщение
     * <code>PropertyChangeEvent</code> с именем {@link #PROPERTY_EDITED_SYMBOL}
     * .
     * 
     * @param mf Отслеживаемый шрифт.
     * @see #setEditedSymbol(MSymbol)
     * @see #symbolEdit(String, MSymbol)
     * @see #getFont()
     */
    public synchronized void setFont(MFont mf) {
        MFont oldFont = font;

        if (font != null) {
            endEdit();
        }

        font = mf;
        setEditedSymbol(null);

        firePropertyChange(PROPERTY_FONT, oldFont, font);
    }

    /**
     * Возвращает символ, выбранный как редактируемый.
     * 
     * @see #setEditedSymbol(MSymbol)
     */
    public synchronized MSymbol getEditedSymbol() {
        return editedSymbol;
    }

    /**
     * Устанавливает редактируемый символ.<br>
     * Выпускается сообщение <code>PropertyChangeEvent</code> с именем
     * {@link #PROPERTY_EDITED_SYMBOL}.
     * 
     * @param sym редактируемый символ. Должен принадлежать отслеживаемому
     *            шрифту.
     * @see #setFont(MFont)
     * @see #symbolEdit(String)
     * @see #getEditedSymbol()
     */
    public synchronized void setEditedSymbol(MSymbol sym) {
        if (sym != null && (font == null || !font.isBelong(sym))) return;

        MSymbol oldSymbol = editedSymbol;
        editedSymbol = sym;

        firePropertyChange(PROPERTY_EDITED_SYMBOL, oldSymbol, sym);
    }
}
