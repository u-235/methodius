
package utils.config;

import java.util.EventListener;

/**
 * Получатель сообщений о изменении узла конфигурации. Такими изменениями
 * являются {@linkplain #childAdded(NodeChangeEvent) добавление} или
 * {@linkplain #childRemoved(NodeChangeEvent) удаление} дочернего узла.
 */
public interface NodeChangeListener extends EventListener {
    /**
     * Метод вызывается после добавления дочернего узла.
     * 
     * @param event Сообщение об изменениях.
     */
    void childAdded(NodeChangeEvent event);

    /**
     * Метод вызывается после удаления дочернего узла.
     * 
     * @param event Сообщение об изменениях.
     */
    void childRemoved(NodeChangeEvent event);
}
