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
