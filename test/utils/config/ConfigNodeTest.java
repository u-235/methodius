
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

        boolean result = false;
        try {
            ConfigNode removed = root.node("removed");
            removed.removeNode();

            removed.parent();
        } catch (IllegalStateException e) {
            result = true;
        }
        assertTrue(result);

        assertNull(root.parent());

        assertSame(node.parent(), root);
    }

    @Test
    public void testRoot() {
        ConfigNode root = new TestNode(null, null);
        ConfigNode node = root.node("test_root");

        boolean result = false;
        try {
            ConfigNode removed = root.node("removed");
            removed.removeNode();

            removed.root();
        } catch (IllegalStateException e) {
            result = true;
        }
        assertTrue(result);

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

        boolean result = false;
        try {
            ConfigNode removed = root.node("removed");
            removed.removeNode();

            removed.isEmpty();
        } catch (IllegalStateException e) {
            result = true;
        }
        assertTrue(result);

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
        ConfigNode root = new TestNode(null, null);

        boolean result = false;
        try {
            ConfigNode removed = root.node("removed");
            removed.removeNode();

            removed.childrenNames();
        } catch (IllegalStateException e) {
            result = true;
        }
        assertTrue(result);

        root.node("a");
        root.node("b");
        root.node("c");

        String[] nn = root.childrenNames();

        assertTrue(nn.length == 3);
    }

    @Test
    public void testNode() {
        ConfigNode root = new TestNode(null, null);
        ConfigNode first;

        boolean result = false;
        try {
            ConfigNode removed = root.node("removed");
            removed.removeNode();

            removed.node("ex");
        } catch (IllegalStateException e) {
            result = true;
        }
        assertTrue(result);

        result = false;
        try {
            root.node(null);
        } catch (NullPointerException e) {
            result = true;
        }
        assertTrue(result);

        result = false;
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

        boolean result = false;
        try {
            ConfigNode removed = root.node("removed");
            removed.removeNode();

            removed.nodeExists("ex");
        } catch (IllegalStateException e) {
            result = true;
        }
        assertTrue(result);

        result = false;
        try {
            root.node(null);
        } catch (NullPointerException e) {
            result = true;
        }
        assertTrue(result);

        result = false;
        try {
            root.nodeExists("/bad/path/");
        } catch (IllegalArgumentException e) {
            result = true;
        }
        assertTrue(result);

        result = false;
        try {
            root.nodeExists("/bad//path");
        } catch (IllegalArgumentException e) {
            result = true;
        }
        assertTrue(result);

        result = false;
        try {
            root.nodeExists("//");
        } catch (IllegalArgumentException e) {
            result = true;
        }
        assertTrue(result);

        result = true;
        try {
            root.nodeExists("/");
        } catch (IllegalArgumentException e) {
            result = false;
        }
        assertTrue(result);

        assertTrue(root.nodeExists("/"));
        assertTrue(root.nodeExists(""));

        assertFalse(root.nodeExists("some_node"));
        node = root.node("some_node");
        assertTrue(root.nodeExists("some_node"));

        node.node("child_node");
        assertTrue(node.nodeExists("child_node"));
        assertTrue(node.nodeExists("/some_node/child_node"));
        assertTrue(root.nodeExists("some_node/child_node"));
    }

    @Test
    public void testRemoveNode() {
        ConfigNode root = new TestNode(null, null);
        ConfigNode node = root.node("child");
        node.node("a");
        node.node("b");
        node.node("c");

        boolean result = false;
        try {
            root.removeNode();
        } catch (UnsupportedOperationException e) {
            result = true;
        }
        assertTrue(result);

        assertTrue(root.nodeExists("child"));
        assertTrue(node.nodeExists("a"));
        assertTrue(node.nodeExists("b"));
        assertTrue(node.nodeExists("c"));
        node.removeNode();
        assertFalse(root.nodeExists("child"));
        assertFalse(root.nodeExists("child/a"));
        assertFalse(root.nodeExists("child/b"));
        assertFalse(root.nodeExists("child/c"));
        assertNotSame(node, root.node("child"));

        node = root.node("child");
        node.node("a");
        node.node("b");
        node.node("c");
        assertTrue(root.nodeExists("child"));
        assertTrue(node.nodeExists("a"));
        assertTrue(node.nodeExists("b"));
        assertTrue(node.nodeExists("c"));
        node.node("b").removeNode();
        assertTrue(root.nodeExists("child"));
        assertTrue(root.nodeExists("child/a"));
        assertFalse(root.nodeExists("child/b"));
        assertTrue(root.nodeExists("child/c"));
    }

    @Test
    public void testKeys() {
        ConfigNode root = new TestNode(null, null);
        ConfigNode node;

        boolean result = false;
        try {
            ConfigNode removed = root.node("removed");
            removed.removeNode();

            removed.keys();
        } catch (IllegalStateException e) {
            result = true;
        }
        assertTrue(result);

        assertTrue(root.keys().length == 0);

        root.put("a", "v");
        root.put("b", "v");
        root.put("c", "v");

        assertTrue(root.keys().length == 3);

        fail("Not yet implemented");
    }

    @Test
    public void testRemove() {
        ConfigNode root = new TestNode(null, null);
        ConfigNode node;

        boolean result = false;
        try {
            ConfigNode removed = root.node("removed");
            removed.removeNode();

            // TODO attempt operation with removed
        } catch (IllegalStateException e) {
            result = true;
        }
        assertTrue(result);

        fail("Not yet implemented");
    }

    @Test
    public void testClear() {
        ConfigNode root = new TestNode(null, null);
        ConfigNode node;

        boolean result = false;
        try {
            ConfigNode removed = root.node("removed");
            removed.removeNode();

            // TODO attempt operation with removed
        } catch (IllegalStateException e) {
            result = true;
        }
        assertTrue(result);

        fail("Not yet implemented");
    }

    @Test
    public void testPut() {
        ConfigNode root = new TestNode(null, null);
        ConfigNode node;

        boolean result = false;
        try {
            ConfigNode removed = root.node("removed");
            removed.removeNode();

            // TODO attempt operation with removed
        } catch (IllegalStateException e) {
            result = true;
        }
        assertTrue(result);

        fail("Not yet implemented");
    }

    @Test
    public void testGet() {
        ConfigNode root = new TestNode(null, null);
        ConfigNode node;

        boolean result = false;
        try {
            ConfigNode removed = root.node("removed");
            removed.removeNode();

            // TODO attempt operation with removed
        } catch (IllegalStateException e) {
            result = true;
        }
        assertTrue(result);

        fail("Not yet implemented");
    }

    @Test
    public void testPutByte() {
        ConfigNode root = new TestNode(null, null);
        ConfigNode node;

        boolean result = false;
        try {
            ConfigNode removed = root.node("removed");
            removed.removeNode();

            // TODO attempt operation with removed
        } catch (IllegalStateException e) {
            result = true;
        }
        assertTrue(result);

        fail("Not yet implemented");
    }

    @Test
    public void testGetByte() {
        ConfigNode root = new TestNode(null, null);
        ConfigNode node;

        boolean result = false;
        try {
            ConfigNode removed = root.node("removed");
            removed.removeNode();

            // TODO attempt operation with removed
        } catch (IllegalStateException e) {
            result = true;
        }
        assertTrue(result);

        fail("Not yet implemented");
    }

    @Test
    public void testPutShort() {
        ConfigNode root = new TestNode(null, null);
        ConfigNode node;

        boolean result = false;
        try {
            ConfigNode removed = root.node("removed");
            removed.removeNode();

            // TODO attempt operation with removed
        } catch (IllegalStateException e) {
            result = true;
        }
        assertTrue(result);

        fail("Not yet implemented");
    }

    @Test
    public void testGetShort() {
        ConfigNode root = new TestNode(null, null);
        ConfigNode node;

        boolean result = false;
        try {
            ConfigNode removed = root.node("removed");
            removed.removeNode();

            // TODO attempt operation with removed
        } catch (IllegalStateException e) {
            result = true;
        }
        assertTrue(result);

        fail("Not yet implemented");
    }

    @Test
    public void testPutInt() {
        ConfigNode root = new TestNode(null, null);
        ConfigNode node;

        boolean result = false;
        try {
            ConfigNode removed = root.node("removed");
            removed.removeNode();

            // TODO attempt operation with removed
        } catch (IllegalStateException e) {
            result = true;
        }
        assertTrue(result);

        fail("Not yet implemented");
    }

    @Test
    public void testGetInt() {
        ConfigNode root = new TestNode(null, null);
        ConfigNode node;

        boolean result = false;
        try {
            ConfigNode removed = root.node("removed");
            removed.removeNode();

            // TODO attempt operation with removed
        } catch (IllegalStateException e) {
            result = true;
        }
        assertTrue(result);

        fail("Not yet implemented");
    }

    @Test
    public void testPutLong() {
        ConfigNode root = new TestNode(null, null);
        ConfigNode node;

        boolean result = false;
        try {
            ConfigNode removed = root.node("removed");
            removed.removeNode();

            // TODO attempt operation with removed
        } catch (IllegalStateException e) {
            result = true;
        }
        assertTrue(result);

        fail("Not yet implemented");
    }

    @Test
    public void testGetLong() {
        ConfigNode root = new TestNode(null, null);
        ConfigNode node;

        boolean result = false;
        try {
            ConfigNode removed = root.node("removed");
            removed.removeNode();

            // TODO attempt operation with removed
        } catch (IllegalStateException e) {
            result = true;
        }
        assertTrue(result);

        fail("Not yet implemented");
    }

    @Test
    public void testPutFloat() {
        ConfigNode root = new TestNode(null, null);
        ConfigNode node;

        boolean result = false;
        try {
            ConfigNode removed = root.node("removed");
            removed.removeNode();

            // TODO attempt operation with removed
        } catch (IllegalStateException e) {
            result = true;
        }
        assertTrue(result);

        fail("Not yet implemented");
    }

    @Test
    public void testGetFloat() {
        ConfigNode root = new TestNode(null, null);
        ConfigNode node;

        boolean result = false;
        try {
            ConfigNode removed = root.node("removed");
            removed.removeNode();

            // TODO attempt operation with removed
        } catch (IllegalStateException e) {
            result = true;
        }
        assertTrue(result);

        fail("Not yet implemented");
    }

    @Test
    public void testPutDouble() {
        ConfigNode root = new TestNode(null, null);
        ConfigNode node;

        boolean result = false;
        try {
            ConfigNode removed = root.node("removed");
            removed.removeNode();

            // TODO attempt operation with removed
        } catch (IllegalStateException e) {
            result = true;
        }
        assertTrue(result);

        fail("Not yet implemented");
    }

    @Test
    public void testGetDouble() {
        ConfigNode root = new TestNode(null, null);
        ConfigNode node;

        boolean result = false;
        try {
            ConfigNode removed = root.node("removed");
            removed.removeNode();

            // TODO attempt operation with removed
        } catch (IllegalStateException e) {
            result = true;
        }
        assertTrue(result);

        fail("Not yet implemented");
    }

    @Test
    public void testPutBoolean() {
        ConfigNode root = new TestNode(null, null);
        ConfigNode node;

        boolean result = false;
        try {
            ConfigNode removed = root.node("removed");
            removed.removeNode();

            // TODO attempt operation with removed
        } catch (IllegalStateException e) {
            result = true;
        }
        assertTrue(result);

        fail("Not yet implemented");
    }

    @Test
    public void testGetBoolean() {
        ConfigNode root = new TestNode(null, null);
        ConfigNode node;

        boolean result = false;
        try {
            ConfigNode removed = root.node("removed");
            removed.removeNode();

            // TODO attempt operation with removed
        } catch (IllegalStateException e) {
            result = true;
        }
        assertTrue(result);

        fail("Not yet implemented");
    }

    @Test
    public void testPutByteArray() {
        ConfigNode root = new TestNode(null, null);
        ConfigNode node;

        boolean result = false;
        try {
            ConfigNode removed = root.node("removed");
            removed.removeNode();

            // TODO attempt operation with removed
        } catch (IllegalStateException e) {
            result = true;
        }
        assertTrue(result);

        fail("Not yet implemented");
    }

    @Test
    public void testGetByteArray() {
        ConfigNode root = new TestNode(null, null);
        ConfigNode node;

        boolean result = false;
        try {
            ConfigNode removed = root.node("removed");
            removed.removeNode();

            // TODO attempt operation with removed
        } catch (IllegalStateException e) {
            result = true;
        }
        assertTrue(result);

        fail("Not yet implemented");
    }

    @Test
    public void testPutCommentString() {
        ConfigNode root = new TestNode(null, null);
        ConfigNode node;

        boolean result = false;
        try {
            ConfigNode removed = root.node("removed");
            removed.removeNode();

            // TODO attempt operation with removed
        } catch (IllegalStateException e) {
            result = true;
        }
        assertTrue(result);

        fail("Not yet implemented");
    }

    @Test
    public void testGetComment() {
        ConfigNode root = new TestNode(null, null);
        ConfigNode node;

        boolean result = false;
        try {
            ConfigNode removed = root.node("removed");
            removed.removeNode();

            // TODO attempt operation with removed
        } catch (IllegalStateException e) {
            result = true;
        }
        assertTrue(result);

        fail("Not yet implemented");
    }

    @Test
    public void testRemoveComment() {
        ConfigNode root = new TestNode(null, null);
        ConfigNode node;

        boolean result = false;
        try {
            ConfigNode removed = root.node("removed");
            removed.removeNode();

            // TODO attempt operation with removed
        } catch (IllegalStateException e) {
            result = true;
        }
        assertTrue(result);

        fail("Not yet implemented");
    }

    @Test
    public void testPutCommentStringString() {
        ConfigNode root = new TestNode(null, null);
        ConfigNode node;

        boolean result = false;
        try {
            ConfigNode removed = root.node("removed");
            removed.removeNode();

            removed.putComment("", "");
        } catch (IllegalStateException e) {
            result = true;
        }
        assertTrue(result);

        fail("Not yet implemented");
    }

    @Test
    public void testGetCommentString() {
        ConfigNode root = new TestNode(null, null);
        ConfigNode node;

        boolean result = false;
        try {
            ConfigNode removed = root.node("removed");
            removed.removeNode();

            // TODO attempt operation with removed
        } catch (IllegalStateException e) {
            result = true;
        }
        assertTrue(result);

        fail("Not yet implemented");
    }

    @Test
    public void testRemoveCommentString() {
        ConfigNode root = new TestNode(null, null);
        ConfigNode node;

        boolean result = false;
        try {
            ConfigNode removed = root.node("removed");
            removed.removeNode();

            // TODO attempt operation with removed
        } catch (IllegalStateException e) {
            result = true;
        }
        assertTrue(result);

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
