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

import java.io.Closeable;
import java.io.IOException;

/**
 * Интерфейс загрузчика конфигурации из потока.
 */
public interface ConfigLoader extends Closeable {
    /**
     * Загрузка конфигурации из потока.
     * 
     * @throws IOException
     * @throws InterruptedException Если во время загрузки задача была прервана.
     */
    public void load() throws IOException, InterruptedException;
}
