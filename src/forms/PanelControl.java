
package forms;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;
import utils.config.ConfigChangeEvent;
import utils.config.ConfigChangeListener;
import utils.config.ConfigNode;
import utils.resource.Resource;

public abstract class PanelControl {
    protected Resource       res;
    protected ConfigNode     config;
    protected JComponent     view;
    private ResourceListener resListener    = new ResourceListener();
    private ConfigListener   configListener = new ConfigListener();

    public JComponent view() {
        return view;
    }

    public abstract void updateFromResource();

    public abstract void updateFromConfig();

    public final Resource getResource() {
        return res;
    }

    public void setResource(Resource resource) {
        Resource old = res;

        if (res != null) {
            res.removeListener(resListener);
        }
        res = resource;

        if (res != null) {
            res.addListener(resListener);
        }

        if (old != res) {
            updateFromResource();
        }
    }

    public ConfigNode getConfigNode() {
        return config;
    }

    public void setConfigNode(ConfigNode configNode) {
        ConfigNode old = config;
        if (config != null) {
            config.removeConfigChangeListener(configListener);
        }
        config = configNode;

        if (config != null) {
            config.addConfigChangeListener(configListener);
        }

        if (old != config) {
            updateFromResource();
        }
    }

    private class ResourceListener implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            updateFromResource();
        }
    }

    private class ConfigListener implements ConfigChangeListener {
        @Override
        public void configChange(ConfigChangeEvent event) {
            updateFromResource();
        }
    }
}
