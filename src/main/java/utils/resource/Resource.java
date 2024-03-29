/*
 * Copyright 2013-2022 © Nick Egorrov, nicegorov@yandex.ru.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package utils.resource;

import java.awt.Image;
import java.awt.MediaTracker;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import utils.event.ListenerChain;

public class Resource implements Cloneable {
    /**  */
    public static final String    LOGGER                     = "resource.logger";
    public static final String    TEXT_NAME_KEY              = "text";
    public static final String    TEXT_TIP_KEY               = "tooltip";
    public static final String    TEXT_HELP_KEY              = "help";
    public static final String    TEXT_IMAGE_KEY             = "image";

    public static final String    ICON_KEY                   = "Icon";
    public static final String    ICON_DISABLED_KEY          = "DisabledIcon";
    public static final String    ICON_DISABLED_SELECTED_KEY = "DisabledSelectedIcon";
    public static final String    ICON_SELECTED_KEY          = "SelectedIcon";
    public static final String    ICON_ROLLOVED_KEY          = "RollovedIcon";
    public static final String    ICON_ROLLOVED_SELECTED_KEY = "RollovedSelectedIcon";
    public static final String    ICON_PRESSED_KEY           = "PressedIcon";

    public static final String    PROPERTY_LOCALE            = "res.prop.locale";
    public static final String    PROPERTY_LOADER            = "res.prop.loader";
    public static final String    PROPERTY_BASENAME          = "res.prop.basename";
    public static final String    PROPERTY_ICONPATH          = "res.prop.iconpath";

    protected static final Logger log;
    private Locale                locale;
    private ClassLoader           loader;
    private String                baseName;
    ResourceBundle                rBundle;
    private URL                   iconPath;

    private ListenerChain         listeners                  = new ListenerChain();

    static {
        log = Logger.getLogger(LOGGER);
    }

    public Resource(String baseName, Locale locale, ClassLoader loader) {
        this.baseName = baseName;
        this.locale = locale;
        this.loader = loader;
        iconPath = loader.getResource("");

        updateBundle();
    }

    public Resource(String baseName, Locale locale) {
        this(baseName, locale, ClassLoader.getSystemClassLoader());
    }

    public Resource(String baseName, String language, String country) {
        this(baseName, new Locale(language, country));
    }

    public Resource(String baseName) {
        this(baseName, Locale.getDefault());
    }

    private void updateBundle() {
        rBundle = ResourceBundle.getBundle(baseName, locale, loader);
    }

    @Override
    public Resource clone() {
        return new Resource(baseName, locale, loader);
    }

    public String getBaseName() {
        return baseName;
    }

    public void setBaseName(String bn) {
        String old = baseName;
        baseName = bn;

        updateBundle();

        fireEvent(PROPERTY_BASENAME, old, baseName);
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale l) {
        Locale old = locale;
        locale = l;

        updateBundle();

        fireEvent(PROPERTY_LOCALE, old, locale);
    }

    public ClassLoader getClassLoader() {
        return loader;
    }

    public void setClassLoader(ClassLoader cl) {
        ClassLoader old = loader;
        loader = cl;

        updateBundle();

        fireEvent(PROPERTY_LOADER, old, loader);
    }

    public URL getIconPath() {
        return iconPath;
    }

    public void setIconPath(URL ip) {
        URL old = iconPath;
        iconPath = ip;

        fireEvent(PROPERTY_ICONPATH, old, iconPath);
    }

    public void setIconPath(String ip) {
        URL old = iconPath;
        iconPath = loader.getResource(ip);

        fireEvent(PROPERTY_ICONPATH, old, iconPath);
    }

    public ImageIcon getIcon(String name) {
        URL link;

        if (iconPath == null) return null;

        try {
            link = new URL(iconPath, name);
        } catch (MalformedURLException e1) {
            log.log(Level.WARNING,
                            "Malformed URL. Path is {0}\nFile name is {1}",
                            new Object[] { iconPath.toString(), name });
            return null;
        }

        ImageIcon ret = new ImageIcon(link);
        if (ret.getImageLoadStatus() != MediaTracker.COMPLETE) {
            log.log(Level.CONFIG, "Can''t load icon {0}", link);
            return null;
        }
        return ret;
    }

    public ImageIcon getIcon(String name, String state) {
        String imgState;
        StringBuffer imgName;

        if (name == null) return null;

        imgState = imageSuffix(state);
        try {
            imgName = new StringBuffer(getString(name, TEXT_IMAGE_KEY));
            int i = imgName.lastIndexOf(".");
            if (imgState != null && i > 0) imgName.insert(i, imgState);
        } catch (NullPointerException e) {
            return null;
        }

        return getIcon(imgName.toString());
    }

    public String getString(String key, String type) {
        String name;
        name = key;

        try {
            return rBundle.getString(name + "." + type);
        } catch (Exception e) {
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

    public void addListener(PropertyChangeListener listener) {
        listeners.add(PropertyChangeListener.class, listener);
    }

    public void removeListener(PropertyChangeListener listener) {
        listeners.remove(PropertyChangeListener.class, listener);
    }

    protected String imageSuffix(String type) {
        if (ICON_DISABLED_KEY.equals(type)) return "-d";
        else if (ICON_DISABLED_SELECTED_KEY.equals(type)) return "-ds";
        else if (ICON_PRESSED_KEY.equals(type)) return "-p";
        else if (ICON_ROLLOVED_KEY.equals(type)) return "-r";
        else if (ICON_ROLLOVED_SELECTED_KEY.equals(type)) return "-rs";
        else if (ICON_SELECTED_KEY.equals(type)) return "-s";
        else return null;
    }

    protected void fireEvent(PropertyChangeEvent event) {
        Object[] listenerArray;

        if (listeners == null) return;

        listenerArray = listeners.getListenerList();
        for (int i = 0; i < listenerArray.length; i += 2) {
            if (listenerArray[i] == PropertyChangeListener.class)
                ((PropertyChangeListener) listenerArray[i + 1])
                                .propertyChange(event);
        }
    }

    protected void fireEvent(String prop, int oldValue, int newValue) {
        if (oldValue == newValue) return;
        fireEvent(new PropertyChangeEvent(this, prop, oldValue, newValue));
    }

    protected void fireEvent(String prop, boolean oldValue, boolean newValue) {
        if (oldValue == newValue) return;
        fireEvent(new PropertyChangeEvent(this, prop, oldValue, newValue));
    }

    protected void fireEvent(String prop, Object oldValue, Object newValue) {
        if (oldValue == null) {
            if (newValue == null) return;
        } else {
            if (newValue != null && oldValue.equals(newValue)) return;
        }

        fireEvent(new PropertyChangeEvent(this, prop, oldValue, newValue));
    }
}
