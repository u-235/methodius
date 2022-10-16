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

package utils.config;

import java.util.EventObject;

/**
 * Сообщение, выпускаемое {@link ConfigNode} при добавлении или удалении
 * дочернего узла конфигурации.
 */
public class NodeChangeEvent extends EventObject {
    private static final long serialVersionUID = 1965484138604443640L;
    protected ConfigNode      child;

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
