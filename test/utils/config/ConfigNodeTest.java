
package utils.config;

import utils.config.ConfigNode;
import static org.junit.Assert.*;
import org.junit.Test;

public class ConfigNodeTest {
    class TestNode extends ConfigNode {
        public TestNode(ConfigNode parent, String name) {
            super(parent, name);
        }
    }

    @Test
    public void testParent() {
        ConfigNode root = new TestNode(null, null);
        ConfigNode node = root.node("test_parent");

        assertNull(root.parent());

        assertSame(node.parent(), root);
    }

    @Test
    public void testRoot() {
        ConfigNode root = new TestNode(null, null);
        ConfigNode node = root.node("test_root");

        assertEquals(root.root(), root);

        assertSame(node.root(), root);

        assertNotSame(node.root(), node);
    }

    @Test
    public void testIsEmpty() {
        ConfigNode root = new TestNode(null, null);
        ConfigNode noChild = root.node("no_child");
        ConfigNode withChild = root.node("with_child");
        withChild.node("child");

        assertTrue(noChild.isEmpty());
        assertTrue(withChild.isEmpty());

        noChild.putComment("some text");
        withChild.putComment("some text");
        assertFalse(noChild.isEmpty());
        assertFalse(withChild.isEmpty());
        noChild.removeComment();
        withChild.removeComment();
        assertTrue(noChild.isEmpty());
        assertTrue(withChild.isEmpty());

        noChild.putComment("");
        withChild.putComment("");
        assertTrue(noChild.isEmpty());
        assertTrue(withChild.isEmpty());

        noChild.putComment(null);
        withChild.putComment(null);
        assertTrue(noChild.isEmpty());
        assertTrue(withChild.isEmpty());
    }

    @Test
    public void testName() {
        ConfigNode root = new TestNode(null, null);
        ConfigNode node;

        assertTrue(root.name().equals(""));

        node = root.node("relative");
        assertTrue(node.name().equals("relative"));

        node = root.node("some/nested/relative");
        assertTrue(node.name().equals("relative"));

        node = root.node("/absolute_name");
        assertTrue(node.name().equals("absolute_name"));

        node = root.node("/absolute_path/absolute_name");
        assertTrue(node.name().equals("absolute_name"));

        node = root.node("/some/lonng/absolute/path/absolute_name");
        assertTrue(node.name().equals("absolute_name"));
    }

    @Test
    public void testAbsolutePath() {
        ConfigNode root = new TestNode(null, null);
        ConfigNode node;

        assertTrue(root.absolutePath().equals("/"));

        node = root.node("relative");
        assertTrue(node.absolutePath().equals("/relative"));

        node = root.node("nested/relative");
        assertTrue(node.absolutePath().equals("/nested/relative"));
        node = node.node("child");
        assertTrue(node.absolutePath().equals("/nested/relative/child"));

        node = root.node("/absolute_name");
        assertTrue(node.absolutePath().equals("/absolute_name"));

        node = root.node("/absolute_path/absolute_name");
        assertTrue(node.absolutePath().equals("/absolute_path/absolute_name"));
    }

    @Test
    public void testChildrenNames() {
        fail("Not yet implemented");
    }

    @Test
    public void testNode() {
        ConfigNode root = new TestNode(null, null);
        ConfigNode first;

        boolean result = false;
        try {
            root.node("/bad/path/");
        } catch (IllegalArgumentException e) {
            result = true;
        }
        assertTrue(result);

        result = false;
        try {
            root.node("/bad//path");
        } catch (IllegalArgumentException e) {
            result = true;
        }
        assertTrue(result);

        result = false;
        try {
            root.node("//");
        } catch (IllegalArgumentException e) {
            result = true;
        }
        assertTrue(result);

        result = true;
        try {
            root.node("/");
        } catch (IllegalArgumentException e) {
            result = false;
        }
        assertTrue(result);

        first = root.node("");
        assertSame(root, first);

        first = root.node("/some/node");
        assertSame(first, root.node("/some/node"));
        assertSame(first, first.node("/some/node"));
        assertSame(first, first.node(""));

        ConfigNode second = root.node("/some");
        assertSame(first, second.node("node"));
    }

    @Test
    public void testNodeExists() {
        ConfigNode root = new TestNode(null, null);
        ConfigNode node;

        assertTrue(root.nodeExists("/"));
        assertTrue(root.nodeExists(""));
    }

    @Test
    public void testRemoveNode() {
        fail("Not yet implemented");
    }

    @Test
    public void testKeys() {
        fail("Not yet implemented");
    }

    @Test
    public void testRemove() {
        fail("Not yet implemented");
    }

    @Test
    public void testClear() {
        fail("Not yet implemented");
    }

    @Test
    public void testPut() {
        fail("Not yet implemented");
    }

    @Test
    public void testGet() {
        fail("Not yet implemented");
    }

    @Test
    public void testPutByte() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetByte() {
        fail("Not yet implemented");
    }

    @Test
    public void testPutShort() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetShort() {
        fail("Not yet implemented");
    }

    @Test
    public void testPutInt() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetInt() {
        fail("Not yet implemented");
    }

    @Test
    public void testPutLong() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetLong() {
        fail("Not yet implemented");
    }

    @Test
    public void testPutFloat() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetFloat() {
        fail("Not yet implemented");
    }

    @Test
    public void testPutDouble() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetDouble() {
        fail("Not yet implemented");
    }

    @Test
    public void testPutBoolean() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetBoolean() {
        fail("Not yet implemented");
    }

    @Test
    public void testPutByteArray() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetByteArray() {
        fail("Not yet implemented");
    }

    @Test
    public void testPutCommentString() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetComment() {
        fail("Not yet implemented");
    }

    @Test
    public void testRemoveComment() {
        fail("Not yet implemented");
    }

    @Test
    public void testPutCommentStringString() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetCommentString() {
        fail("Not yet implemented");
    }

    @Test
    public void testRemoveCommentString() {
        fail("Not yet implemented");
    }

    @Test
    public void testAddConfigChangeListener() {
        fail("Not yet implemented");
    }

    @Test
    public void testRemoveConfigChangeListener() {
        fail("Not yet implemented");
    }

    @Test
    public void testAddNodeChangeListener() {
        fail("Not yet implemented");
    }

    @Test
    public void testRemoveNodeChangeListener() {
        fail("Not yet implemented");
    }

}
