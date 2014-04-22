
package utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.prefs.AbstractPreferences;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class Settings extends AbstractPreferences {
    private SettingsFile              file;
    private HashMap<String, String>   storage;
    private HashMap<String, Settings> childs;

    public Settings(SettingsFile f) {
        this(null, "");
        file = f;
        file.load(this);
    }

    protected Settings(AbstractPreferences parent, String name) {
        super(parent, name);
        storage = new HashMap<String, String>();
        childs = new HashMap<String, Settings>();
    }

    @Override
    protected void putSpi(String key, String value) {
        storage.put(key, value);
    }

    @Override
    protected String getSpi(String key) {
        return storage.get(key);
    }

    @Override
    protected void removeSpi(String key) {
        storage.remove(key);
    }

    @Override
    protected void removeNodeSpi() throws BackingStoreException {
        Preferences parent = parent();
        if (parent instanceof Settings)
            ((Settings) parent).childs.remove(name());
    }

    @Override
    protected String[] keysSpi() throws BackingStoreException {
        return storage.keySet().toArray(new String[storage.size()]);
    }

    @Override
    protected String[] childrenNamesSpi() throws BackingStoreException {
        return childs.keySet().toArray(new String[childs.size()]);
    }

    @Override
    protected AbstractPreferences childSpi(String name) {
        Settings ret = childs.get(name);
        if (ret == null) {
            ret = new Settings(this, name);
            childs.put(name, ret);
        }
        return ret;
    }

    @Override
    protected void syncSpi() throws BackingStoreException {
        // TODO Auto-generated method stub
    }

    @Override
    protected void flushSpi() throws BackingStoreException {
        Iterator<String> keys = storage.keySet().iterator();
        String name;
        
        file.beginFlush(absolutePath());
        while (keys.hasNext()) {
            name = keys.next();
            file.flushKey(name, storage.get(name));
        }
        file.endFlush(absolutePath());
    }

}
