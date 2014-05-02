
package utils.config;

import java.util.EventObject;

/**
 * Сообщение, выпускаемое {@link ConfigNode} при добавлении или удалении
 * дочернего узла конфигурации.
 */
public class NodeChangeEvent extends EventObject {
    protected ConfigNode child;

    /**
     * Создание сообщения.
     * 
     * @param parent Родитель добавляемого или удаляемого узла.
     * @param child Узел, который был добавлен или удалён.
     */
    public NodeChangeEvent(ConfigNode parent, ConfigNode child) {
        super(parent);
        this.child = child;
    }

    /**
     * Возвращает родителя добавляемого или удаляемого узла. Фактически
     * эквивалентно вызову {@link EventObject#getSource()}.
     * 
     * @return Узел конфигурации, у которого был добавлен или удалён дочерний
     *         узел.
     */
    public ConfigNode getParent() {
        return (ConfigNode) source;
    }

    /**
     * Возвращает добавляемый или удаляемый узел.
     */
    public ConfigNode getChild() {
        return child;
    }
}
