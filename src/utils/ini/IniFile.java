
package utils.ini;

import java.net.URI;
import java.util.prefs.BackingStoreException;
import utils.Settings;
import utils.SettingsFile;

public class IniFile implements SettingsFile {
    Parser parser;
    URI    address;

    public Settings createSettings(URI adr) {
        if (parser == null) parser = new Parser();
        address = adr;
        Settings ret = new Settings(this);

        return ret;
    }

    @Override
    public void beginFlush(String path) throws BackingStoreException {
        // TODO Auto-generated method stub

    }

    @Override
    public void endFlush(String path) throws BackingStoreException {
        // TODO Auto-generated method stub

    }

    @Override
    public void flushKey(String key, String value) throws BackingStoreException {
        // TODO Auto-generated method stub

    }

    @Override
    public void beginSync(String path) throws BackingStoreException {
        // TODO Auto-generated method stub

    }

    @Override
    public void syncKey(String key, String value) throws BackingStoreException {
        // TODO Auto-generated method stub

    }
}
