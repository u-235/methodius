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

package logic;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Locale;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import utils.config.ConfigNode;
import utils.resource.Resource;

public class Dialogs {
    Resource     resource;
    ConfigNode   config;
    JFileChooser chooser;

    Dialogs(Resource res, ConfigNode cfg) {
        resource = res;
        resource.addListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (Resource.PROPERTY_LOCALE.equals(evt.getPropertyName())) {
                    updateLocale((Locale) evt.getNewValue());
                }
            }
        });

        config = cfg;
    }

    protected void updateLocale(Locale loc) {
        // TODO Auto-generated method stub
    }

    synchronized JFileChooser getChooser() {
        if (chooser == null) {
            chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                            "MicroFont (*.mfnt)", "mfnt");
            chooser.setFileFilter(filter);
            chooser.addPropertyChangeListener(
                            JFileChooser.DIRECTORY_CHANGED_PROPERTY,
                            new PropertyChangeListener() {
                                @Override
                                public void propertyChange(
                                                PropertyChangeEvent evt) {
                                    Application.application()
                                                    .dir()
                                                    .last((File) evt.getNewValue());
                                }
                            });
            chooser.setCurrentDirectory(Application.application().dir().last());
        }
        return chooser;
    }

    public synchronized JFileChooser chooserOpen() {
        JFileChooser csr = getChooser();
        csr.setDialogTitle(resource.getText("dialogs.open.title"));
        csr.setAcceptAllFileFilterUsed(false);
        csr.setFileSelectionMode(JFileChooser.FILES_ONLY);
        return csr;
    }

    public synchronized JFileChooser chooserSave() {
        JFileChooser csr = getChooser();
        csr.setDialogTitle(resource.getText("dialogs.save.title"));
        csr.setAcceptAllFileFilterUsed(false);
        csr.setFileSelectionMode(JFileChooser.FILES_ONLY);
        return csr;
    }
}
