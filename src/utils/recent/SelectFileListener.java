
package utils.recent;

import java.io.File;
import java.util.EventListener;

/**
 * 
 * 
 */
public interface SelectFileListener extends EventListener {
    void fileSelected(File f);
}
