
package utils.recent;

import java.io.File;
import java.util.EventListener;

/**
 * Интерфейс получателя сообщений о выборе файла. Сообщения приходят от
 * {@link RecentFiles} если пользователь выбрал файл в меню.
 */
public interface SelectFileListener extends EventListener {
    /**
     * Метод, вызываемый при выборе файла.
     * 
     * @param f Выбранный файл.
     */
    void fileSelected(File f);
}
