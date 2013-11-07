package utils.event;

import java.util.EventObject;

/**
 * Объект для расширенного описания событий. Это базовый объект для
 * {@link ListenerChain}. Добавлены методы
 * <ul>
 * <li>{@link #getReason()}
 * <li>{@link #getNewValue()}
 * <li>{@link #getOldValue()}
 * </ul>
 * 
 * <p>
 * Описываем событие. В основном, это нужно для избежания ошибок использования
 * типов и для задания констант причин изменений.
 * 
 * <pre>
 * public class MyEvent extends DataEvent
 * {
 *     // Причины изменений.
 *     public static final int DELETE = 1;
 *     public static final int CREATE = 2;
 *     public static final int CHANGE = 3;
 * 
 *     public MyEvent(Object source, int reason, Object oldValue, Object newValue) {
 *         super(source, reason, oldValue, newValue);
 *     }
 * }
 * </pre>
 * 
 * @author Николай Егоров
 * 
 */
public class DataEvent extends EventObject
{
    private static final long serialVersionUID = -4684310850435480580L;
    private int               reason;
    private Object            oldValue;
    private Object            newValue;

    public DataEvent(Object source, int reason, Object oldValue, Object newValue) {
        super(source);
        this.reason = reason;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    /**
     * Возвращает число, символизирующее причину изменений.
     */
    public int getReason() {
        return this.reason;
    }

    /**
     * Возвращает старое значение.
     */
    public Object getOldValue() {
        return this.oldValue;
    }

    /**
     * Возвращает новое значение.
     */
    public Object getNewValue() {
        return this.newValue;
    }
}
