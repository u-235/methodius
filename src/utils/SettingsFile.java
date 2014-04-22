
package utils;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public interface SettingsFile {
    public void load(Preferences owner);
    
    public void beginFlush(String path) throws BackingStoreException;

    public void endFlush(String path) throws BackingStoreException;

    public void flushKey(String key, String value) throws BackingStoreException;

    public void beginSync(String path) throws BackingStoreException;

    public void syncKey(String key, String value) throws BackingStoreException;
}
