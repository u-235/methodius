package utils;

import java.util.prefs.AbstractPreferences;
import java.util.prefs.BackingStoreException;

public class Settings extends AbstractPreferences {

    protected Settings(AbstractPreferences parent, String name) {
        super(parent, name);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void putSpi(String key, String value) {
        // TODO Auto-generated method stub

    }

    @Override
    protected String getSpi(String key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void removeSpi(String key) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void removeNodeSpi() throws BackingStoreException {
        // TODO Auto-generated method stub

    }

    @Override
    protected String[] keysSpi() throws BackingStoreException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String[] childrenNamesSpi() throws BackingStoreException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected AbstractPreferences childSpi(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void syncSpi() throws BackingStoreException {
        // TODO Auto-generated method stub

    }

    @Override
    protected void flushSpi() throws BackingStoreException {
        // TODO Auto-generated method stub

    }

}
