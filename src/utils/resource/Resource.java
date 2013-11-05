package utils.resource;

import java.awt.Image;
import java.net.URL;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;

import utils.event.ListenerChain;

public class Resource implements Cloneable
{
    ResourceBundle                  rBundle;
    Class<? extends Resource>       imgClass;
    private String                  prefix;
    private String                  baseName;
    private String                  imgType = "gif";
    private String                  imgPath = "/icons/16/";
    private HashMap<String, String> iconStateMap;

    private class Chain extends ListenerChain<ResourceEvent>
    {
        @Override
        protected void listenerCall(EventListener listener,
                        ResourceEvent event) {
            ((ResourceListener) listener).onResourceEvent(event);
        }

    }

    private Chain               listeners                  = new Chain();

    public static final String  TEXT_NAME_KEY              = "text";
    public static final String  TEXT_TIP_KEY               = "tooltip";
    public static final String  TEXT_HELP_KEY              = "help";
    private static final String TEXT_IMAGE_KEY             = "image";

    public static final String  ICON_KEY                   = "Icon";
    public static final String  ICON_DISABLED_KEY          = "DisabledIcon";
    public static final String  ICON_DISABLED_SELECTED_KEY = "DisabledSelectedIcon";
    public static final String  ICON_SELECTED_KEY          = "SelectedIcon";
    public static final String  ICON_ROLLOVED_KEY          = "RollovedIcon";
    public static final String  ICON_ROLLOVED_SELECTED_KEY = "RollovedSelectedIcon";
    public static final String  ICON_PRESSED_KEY           = "SPressedIcon";

    public Resource(String baseName, Locale locale) {
        this.baseName = baseName;
        rBundle = ResourceBundle.getBundle(baseName, locale);
        imgClass = this.getClass();

        iconStateMap = new HashMap<String, String>();
        iconStateMap.put(ICON_KEY, "");
        iconStateMap.put(ICON_DISABLED_KEY, "-d");
        iconStateMap.put(ICON_DISABLED_SELECTED_KEY, "-ds");
        iconStateMap.put(ICON_PRESSED_KEY, "-p");
        iconStateMap.put(ICON_ROLLOVED_KEY, "-r");
        iconStateMap.put(ICON_ROLLOVED_SELECTED_KEY, "-rs");
        iconStateMap.put(ICON_SELECTED_KEY, "-s");
    }

    public Resource(String baseName, String language, String country) {
        this(baseName, new Locale(language, country));
    }

    public Resource(String baseName) {
        this(baseName, Locale.getDefault());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Resource clone() {
        Resource ret = null;
        ret = new Resource(baseName, this.rBundle.getLocale());
        ret.imgClass = imgClass;
        ret.iconStateMap = (HashMap<String, String>) iconStateMap.clone();
        return ret;
    }

    public ImageIcon getIcon(String name) {
        URL url;
        if (name == null) return null;
        url = imgClass.getResource(name);
        if (url == null) return null;
        return new ImageIcon(url);
    }

    public ImageIcon getIcon(String name, String state) {
        String imgName, imgState;

        if (name == null) return null;

        imgState = iconStateMap.get(state);
        if (imgState == null) {
            throw (new IllegalArgumentException("Invalid icon name of state : "
                            + state));
        }

        imgName = imgPath + getString(name, TEXT_IMAGE_KEY) + imgState + "."
                        + imgType;
        return getIcon(imgName);
    }

    public String getString(String key, String type) {
        String name;
        if (prefix != null) name = prefix + "." + key;
        else name = key;

        try {
            return rBundle.getString(name + "." + type);
        }
        catch (Exception e) {
            return null;
        }
    }

    public String getText(String key) {
        return getString(key, TEXT_NAME_KEY);
    }

    public String getToolTip(String key) {
        return getString(key, TEXT_TIP_KEY);
    }

    public String getHelp(String key) {
        return getString(key, TEXT_HELP_KEY);
    }

    public Image getImage(String key, String type) {
        // TODO Auto-generated method stub
        return getIcon(key, type).getImage();
    }

    /**
     * 
     * @param key
     * @return
     */
    public List<? extends Image> getImages(String key) {
        // TODO Auto-generated method stub
        return null;
    }

    public Resource withPrefix(String prefix) {
        Resource ret;
        ret = this.clone();
        ret.setPrefix(prefix);
        return ret;
    }

    public void setPrefix(String prefix) {
        String old = this.prefix;
        this.prefix = prefix;
        fireEvent(ResourceEvent.PREFIX, old, this.prefix);
    }

    public void addListener(ResourceListener listener) {
        listeners.add(listener);
    }

    public void removeListener(ResourceListener listener) {
        listeners.remove(listener);
    }

    protected void fireEvent(int reason, int oldValue, int newValue) {
        if (oldValue == newValue) return;
        listeners.fire(new ResourceEvent(this, reason, oldValue, newValue));
    }

    protected void fireEvent(int reason, boolean oldValue, boolean newValue) {
        if (oldValue == newValue) return;
        listeners.fire(new ResourceEvent(this, reason, oldValue, newValue));
    }

    protected void fireEvent(int reason, String oldValue, String newValue) {
        if (oldValue == null) {
            if (newValue == null) return;
        }
        else {
            if (newValue != null && oldValue.equals(newValue)) return;
        }

        listeners.fire(new ResourceEvent(this, reason, oldValue, newValue));
    }
}
