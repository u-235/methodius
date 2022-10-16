
package utils.config;

import java.io.Closeable;
import java.io.IOException;

/**
 * Интерфейс для сохранения настроек в поток.
 */
public interface ConfigSaver extends Closeable {
    /**
     * Сохранение конфигурации в поток.
     * 
     * @throws IOException
     * @throws InterruptedException Если во время сохранения задача была
     *             прервана.
     */
    public void save() throws IOException, InterruptedException;
}
