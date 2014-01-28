
package microfont.events;

import java.util.EventListener;

/**
 * Интерфейс получателя сообщений от карты пикселей.
 */
public interface PixselMapListener extends EventListener {
    /**
     * Метод получателя сообщений при изменении пикселей.
     * 
     * @param event Сообщение об изменении пикселей.
     */
    public void pixselChanged(PixselMapEvent event);
}
