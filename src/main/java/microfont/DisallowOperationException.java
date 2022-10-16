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

package microfont;

/**
 * Исключение, показывающее, что произошла попытка выполнить действие, которое
 * запрещено текущей конфигурацией.
 * <p>
 * Например, можно изменить любой размер символа, если он не принадлежит шрифту.
 * Однако, если символ принадлежит шрифту, то попытка изменить высоту символа
 * выбросит это исключение.
 */
public class DisallowOperationException extends Exception {
    private static final long serialVersionUID = 5920575077510951839L;

    /**
     * Создаёт исключение с пустым сообщением. Метод {@link #getMessage()}
     * вернёт <code>null</code>.
     */
    public DisallowOperationException() {
        super();
    }

    /**
     * Создаёт исключение с описанием <code>message</code>.
     */
    public DisallowOperationException(String message) {
        super(message);
    }
}
