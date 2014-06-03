
package utils.config;

import java.util.EventObject;

/**
 * Сообщение, выпускаемое {@linkplain ConfigNode} после добавления, удаления или
 * изменения записи конфигурации.
 */
public class ConfigChangeEvent extends EventObject {
    private static final long serialVersionUID = 8098307480574279265L;
    protected String          key;
    protected String          value;

    /**
     * Создание сообщения.
     * 
     * @param node Узел конфигурации, чья запись была изменена.
     * @param key Имя записи конфигурации.
     * @param value Новое значение записи или {@code null}, если запись была
     *            удалена.
     */
    public ConfigChangeEvent(ConfigNode node, String key, String value) {
        super(node);
        this.key = key;
        this.value = value;
    }

    /**
     * Возврашает узел конфигурации, который выпустил сообщение. Фактически
     * эквивалентно вызову {@link EventObject#getSource()}.
     * 
     * @return Узел конфигурации, чья запись была изменена.
     */
    public ConfigNode getNode() {
        return (ConfigNode) source;
    }

    /**
     * Возвращает ключ записи конфигурации.
     * 
     * @return Имя записи конфигурации.
     */
    public String getKey() {
        return key;
    }

    /**
     * Возвращает новое значение записи конфигурации.
     * 
     * @return Новое значение записи или {@code null}, если запись была удалена.
     */
    public String getNewValue() {
        return value;
    }
}
