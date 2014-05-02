
package utils.config;

import java.util.EventListener;

/**
 * Получатель сообщений о изменении записей узла конфигурации.
 */
public interface ConfigChangeListener extends EventListener {
    /**
     * Метод вызывается после добавления, удаления или изменения записи
     * конфигурации.
     * 
     * @param event Сообщение, которое описывает изменения.
     */
    public void configChange(ConfigChangeEvent event);
}
