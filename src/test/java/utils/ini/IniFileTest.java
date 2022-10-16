
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
