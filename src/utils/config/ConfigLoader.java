
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
