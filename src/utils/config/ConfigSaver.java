
package utils.config;

import java.io.Closeable;
import java.io.IOException;

public interface ConfigSaver extends Closeable {
    public void save() throws IOException,  InterruptedException;
}
