
package utils.config;

import static org.junit.Assert.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.junit.Before;
import org.junit.Test;

public class RootNodeTest extends ConfigNodeTest {
    static String TEST_FILE="testRootNode.txt";
    
    class TestRoot extends RootNode {
        TestRoot() {
            super();
        }

        TestRoot(String name) {
            super(name);
        }

        TestRoot(File name) {
            super(name);
        }

        @Override
        protected void loadS(InputStream in) throws IOException {
            // TODO Auto-generated method stub

        }

        @Override
        protected void saveS(OutputStream out) throws IOException {
            // TODO Auto-generated method stub

        }
    }
    
    @Before
    public void beforeTests(){

        try {
            FileOutputStream out = new FileOutputStream(new File(TEST_FILE));
            out.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testRootNode() {
        RootNode root = new TestRoot();

        assertSame(null, root.fName);
        assertSame(null, root.file);
    }

    @Test
    public void testRootNodeString() {
        RootNode root = new TestRoot("name");

        assertSame("name", root.fName);
        assertSame(null, root.file);
    }

    @Test
    public void testRootNodeFile() { 
        File file=new File("name");
        RootNode root = new TestRoot(file);

        assertSame(null, root.fName);
        assertSame(file, root.file);
    }

    @Test
    public void testLoad() {
        RootNode root = new TestRoot(TEST_FILE);
        
        root.load();
        fail("Not yet implemented");
    }

    @Test
    public void testLoadString() {
        fail("Not yet implemented");
    }

    @Test
    public void testLoadFile() {
        fail("Not yet implemented");
    }

    @Test
    public void testSave() {
        RootNode root = new TestRoot(TEST_FILE);
        
        root.save();
        fail("Not yet implemented");
    }

    @Test
    public void testSaveString() {
        fail("Not yet implemented");
    }

    @Test
    public void testSaveFile() {
        fail("Not yet implemented");
    }

}
