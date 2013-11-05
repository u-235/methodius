package utils.event;

import java.util.EventListener;
import java.util.EventObject;

/**
 * Класс для хранения {@linkplain DataEventListener получателей} событий при
 * изменении данных и для генерации этих событий.
 * 
 * <p>
 * 
 * <pre>
 * public class MyObject
 * {
 *     private class Chain extends ListenerChain&lt;MyEvent&gt;
 *     {
 *         &#064;Override
 *         protected void listenerFire(DataEventListener listener, MyEvent event) {
 *             ((MyListener) listener).onMyEvent(event);
 *         }
 *     }
 * 
 *     private Chain listeners = new Chain();
 * }
 * </pre>
 * 
 * @param <E> Класс описания события. Базовым классом является {@link DataEvent}
 *            .
 * @author Николай Егоров
 */
public abstract class ListenerChain<E extends EventObject>
{
    /**
     * Вспомогательный класс, представляющий {@linkplain DataEventListener
     * получателя событий} как элемент цепочки.
     * 
     */
    protected class Element
    {
        /**
         * Следующий элемент цепочки. Если текущий элемент последний, то
         * <code>next</code> равен <code>null</code>.
         */
        Element           next;
        /** Собственно получатель событий. */
        EventListener listener;

        protected Element(EventListener listener) {
            this.listener = listener;
            next = null;
        }
    }

    /** Первый элемент цепочки получателей событий. */
    Element firstElement;

    protected Element getPrev(EventListener tested) {
        Element ret = null;
        Element turn = firstElement;

        while (turn != null) {
            if (turn.listener == tested) return ret;
            ret = turn;
            turn = turn.next;
        }

        return turn;
    }

    /**
     * Добавляет слушателя в цепочку. Если слушатель уже был в цепочке, то он
     * удаляется и вставляется вновь.
     * 
     * @param toAdd Добавляемый слушатель.
     * @see #remove(DataEventListener)
     */
    public void add(EventListener toAdd) {
        Element turn;

        if (toAdd == null) return;

        remove(toAdd);

        if (firstElement == null) {
            firstElement = new Element(toAdd);
            return;
        }

        turn = firstElement;
        while (turn.next != null) {
            turn = turn.next;
        }

        turn.next = new Element(toAdd);

        return;
    }

    /**
     * Удаляет слушателя из цепочки.
     * 
     * @param toRemove Удаляемый слушатель.
     */
    public void remove(EventListener toRemove) {
        Element prev;
        Element removed;

        if (firstElement == null) return;

        prev = getPrev(toRemove);

        if (prev != null) {
            removed = prev.next;
            prev.next = removed.next;
            removed.next = null;
        }
        else if (firstElement.listener == toRemove) {
            removed = firstElement;
            firstElement = removed.next;
            removed.next = null;
        }
    }

    /**
     * Распространяет событие по цепочке.
     * <p>
     * <b>Важно!</b> Метод использует
     * {@link #listenerCall(DataEventListener, DataEvent)}, который Вы должны
     * определить в каждом наследнике этого класс.
     * 
     * @param event Выстреливаемое событие. Если <code>event</code> равно
     *            <code>null</code>, то метод завершается ничего не делая.
     */
    public void fire(E event) {
        Element turn = firstElement;

        if (event == null) return;

        while (turn != null) {
            listenerCall(turn.listener, event);
            turn = turn.next;
        }
    }

    /**
     * Вызов метода слушателя для обработки сообщения. Поскольку интерфейс
     * {@link DataEventListener} несодержит описания метода обработки, этот
     * метод нужен как переходник.
     * 
     * @param listener Слушатель сообщения.
     * @param event Передаваемое сообщение.
     * @see #fire(DataEvent)
     */
    abstract protected void listenerCall(EventListener listener, E event);
}
