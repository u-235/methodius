
package utils.config;

public abstract class AbstractConfig extends ConfigNode {
    
    protected AbstractConfig(ConfigNode parent, String name) {
        super(parent, name);
        // TODO Auto-generated constructor stub
    }

    public abstract void load();

    public abstract void save();
}
