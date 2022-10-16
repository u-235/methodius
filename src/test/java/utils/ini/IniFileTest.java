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

package utils.ini;

import static org.junit.Assert.*;
import java.io.File;
import org.junit.Test;
import utils.config.ConfigNode;
import utils.config.RootNode;
import utils.config.RootNodeTest;

public class IniFileTest extends RootNodeTest {
    @Override
    public ConfigNode doNode() {
        return new IniFile();
    }

    @Override
    public RootNode doRoot() {
        return new IniFile();
    }

    @Override
    public RootNode doRoot(String name) {
        return new IniFile(name);
    }

    @Override
    public RootNode doRoot(File name) {
        return new IniFile(name);
    }

    @Test
    public void testLoadS() {
        fail("Not yet implemented");
    }

    @Test
    public void testSaveS() {
        fail("Not yet implemented");
    }

    @Test
    public void testIniFileString() {
        fail("Not yet implemented");
    }

    @Test
    public void testIniFileStringIniStyle() {
        fail("Not yet implemented");
    }

    @Test
    public void testIniFileFile() {
        fail("Not yet implemented");
    }

    @Test
    public void testIniFileFileIniStyle() {
        fail("Not yet implemented");
    }

    @Test
    public void testSaveConfigNodeSaver() {
        fail("Not yet implemented");
    }
}
