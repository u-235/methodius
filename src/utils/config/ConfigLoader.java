
package utils.config;

import java.io.Closeable;
import java.io.IOException;

public interface ConfigLoader extends Closeable {
    public void load() throws IOException,  InterruptedException;
}
