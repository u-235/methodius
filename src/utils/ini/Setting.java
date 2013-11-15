
package utils.ini;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.prefs.BackingStoreException;
import java.util.prefs.NodeChangeEvent;
import java.util.prefs.NodeChangeListener;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;
import utils.event.ListenerChain;

public class Setting extends Preferences {
    HashMap<String, String> store     = new HashMap<String, String>();
    protected ListenerChain listeners = new ListenerChain();

    @Override
    public void put(String key, String value) {
        // TODO Auto-generated method stub
        store.put(key, value);
    }

    @Override
    public String get(String key, String def) {
        String rv;
        // TODO Auto-generated method stub
        rv = store.get(key);
        if (rv == null) return def;
        return rv;
    }

    @Override
    public void remove(String key) {
        // TODO Auto-generated method stub
        store.remove(key);
    }

    @Override
    public void clear() throws BackingStoreException {
        // TODO Auto-generated method stub
        store.clear();
    }

    @Override
    public void putInt(String key, int value) {
        // TODO Auto-generated method stub
        put(key, Integer.toString(value));
    }

    @Override
    public int getInt(String key, int def) {
        int rv;
        // TODO Auto-generated method stub
        try {
            rv = Integer.getInteger(store.get(key));
        } catch (Exception e) {
            rv = def;
        }
        return rv;
    }

    @Override
    public void putLong(String key, long value) {
        // TODO Auto-generated method stub
        put(key, Long.toString(value));
    }

    @Override
    public long getLong(String key, long def) {
        long rv;
        // TODO Auto-generated method stub
        try {
            rv = Long.parseLong(store.get(key));
        } catch (Exception e) {
            rv = def;
        }
        return rv;
    }

    @Override
    public void putBoolean(String key, boolean value) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean getBoolean(String key, boolean def) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void putFloat(String key, float value) {
        // TODO Auto-generated method stub

    }

    @Override
    public float getFloat(String key, float def) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void putDouble(String key, double value) {
        // TODO Auto-generated method stub

    }

    @Override
    public double getDouble(String key, double def) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void putByteArray(String key, byte[] value) {
        // TODO Auto-generated method stub

    }

    @Override
    public byte[] getByteArray(String key, byte[] def) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String[] keys() throws BackingStoreException {
        // TODO Auto-generated method stub
        return store.keySet().toArray(new String[store.keySet().size()]);
    }

    @Override
    public String[] childrenNames() throws BackingStoreException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Preferences parent() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Preferences node(String pathName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean nodeExists(String pathName) throws BackingStoreException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void removeNode() throws BackingStoreException {
        // TODO Auto-generated method stub

    }

    @Override
    public String name() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String absolutePath() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isUserNode() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public String toString() {
        return (isUserNode() ? "User" : "System") + " Preference Node: "
                        + absolutePath();
    }

    @Override
    public void flush() throws BackingStoreException {
        // TODO Auto-generated method stub

    }

    @Override
    public void sync() throws BackingStoreException {
        // TODO Auto-generated method stub

    }

    @Override
    public void addPreferenceChangeListener(PreferenceChangeListener pcl) {
        listeners.add(PreferenceChangeListener.class, pcl);
    }

    @Override
    public void removePreferenceChangeListener(PreferenceChangeListener pcl) {
        listeners.remove(PreferenceChangeListener.class, pcl);
    }

    protected void firePreferenceChange(PreferenceChangeEvent event) {
        Object[] listenerArray;

        listenerArray = listeners.getListenerList();
        for (int i = 0; i < listenerArray.length; i += 2) {
            if (listenerArray[i] == PreferenceChangeListener.class) {
                ((PreferenceChangeListener) listenerArray[i + 1])
                                .preferenceChange(event);
            }
        }
    }

    @Override
    public void addNodeChangeListener(NodeChangeListener ncl) {
        listeners.add(NodeChangeListener.class, ncl);
    }

    @Override
    public void removeNodeChangeListener(NodeChangeListener ncl) {
        listeners.remove(NodeChangeListener.class, ncl);
    }

    protected void fireNodeChange(NodeChangeEvent event) {
        Object[] listenerArray;

        listenerArray = listeners.getListenerList();
        for (int i = 0; i < listenerArray.length; i += 2) {
            if (listenerArray[i] == NodeChangeListener.class) {
                ((NodeChangeListener) listenerArray[i + 1]).childAdded(event);
            }
        }
    }

    protected void fireNodeAdded(Preferences child) {
        fireNodeChange(new NodeChangeEvent(this, child));
    }

    protected void fireNodeRemoved(Preferences child) {
        fireNodeChange(new NodeChangeEvent(this, child));
    }

    @Override
    public void exportNode(OutputStream os) throws IOException,
                    BackingStoreException {
        // TODO Auto-generated method stub

    }

    @Override
    public void exportSubtree(OutputStream os) throws IOException,
                    BackingStoreException {
        // TODO Auto-generated method stub

    }

}
