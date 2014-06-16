
package utils.config;

import static org.junit.Assert.*;
import org.junit.Test;
import utils.config.ConfigNode;

public class ConfigNodeTest {
    public ConfigNode doNode() {
        return new ConfigNode(null, null) {
        };
    }

    class ConfigListener implements ConfigChangeListener {
        ConfigChangeEvent change = null;

        @Override
        public void configChange(ConfigChangeEvent e) {
            change = e;
        }
    }

    class NodeListener implements NodeChangeListener {
        NodeChangeEvent added   = null;
        NodeChangeEvent removed = null;

        @Override
        public void childAdded(NodeChangeEvent e) {
            added = e;
        }

        @Override
        public void childRemoved(NodeChangeEvent e) {
            removed = e;
        }
    }

    public boolean exist(String[] a, String f) {
        for (String s : a) {
            if (s.equals(f)) return true;
        }
        return false;
    }

    @Test
    public void testParent() {
        ConfigNode root = doNode();
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
        ConfigNode root = doNode();
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
        ConfigNode root = doNode();
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
        ConfigNode root = doNode();
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
        ConfigNode root = doNode();
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
        ConfigNode root = doNode();

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
        ConfigNode root = doNode();
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
        ConfigNode root = doNode();
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
        ConfigNode root = doNode();
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
        ConfigNode root = doNode();

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

        root.put("a", "v1");
        root.put("b", "v2");
        root.put("c", "v3");

        assertTrue(root.keys().length == 3);

        String ks[] = root.keys();
        assertTrue(exist(ks, "a"));
        assertTrue(exist(ks, "b"));
        assertTrue(exist(ks, "c"));

        assertFalse(exist(ks, "v1"));
        assertFalse(exist(ks, "v2"));
        assertFalse(exist(ks, "v3"));
    }

    @Test
    public void testRemove() {
        ConfigNode root = doNode();

        boolean result = false;
        try {
            ConfigNode removed = root.node("removed");
            removed.removeNode();

            removed.remove("key");
        } catch (IllegalStateException e) {
            result = true;
        }
        assertTrue(result);

        result = false;
        try {
            root.remove(null);
        } catch (NullPointerException e) {
            result = true;
        }
        assertTrue(result);

        root.put("key", "value");
        root.putComment("key", "comm");
        root.remove("key");

        assertSame(null, root.get("key", null));
        assertSame(null, root.getComment("key"));
    }

    @Test
    public void testClear() {
        ConfigNode root = doNode();

        boolean result = false;
        try {
            ConfigNode removed = root.node("removed");
            removed.removeNode();

            removed.clear();
        } catch (IllegalStateException e) {
            result = true;
        }
        assertTrue(result);

        root.put("a", "v1");
        root.put("b", "v2");
        root.put("c", "v3");
        root.node("child");

        root.clear();
        assertTrue(root.keys().length == 0);
        assertTrue(root.nodeExists("child"));
    }

    @Test
    public void testPut() {
        ConfigNode root = doNode();
        ConfigNode node = root.node("child");

        boolean result = false;
        try {
            ConfigNode removed = root.node("removed");
            removed.removeNode();

            removed.put("key", "value");
        } catch (IllegalStateException e) {
            result = true;
        }
        assertTrue(result);

        result = false;
        try {
            root.put(null, "value");
        } catch (NullPointerException e) {
            result = true;
        }
        assertTrue(result);

        result = false;
        try {
            root.put("key", null);
        } catch (NullPointerException e) {
            result = true;
        }
        assertTrue(result);

        node.put("test key", "some value");
        assertSame("some value", node.get("test key", null));
    }

    @Test
    public void testGet() {
        ConfigNode root = doNode();
        ConfigNode node = root.node("child");

        boolean result = false;
        try {
            ConfigNode removed = root.node("removed");
            removed.removeNode();

            removed.get("key", "default");
        } catch (IllegalStateException e) {
            result = true;
        }
        assertTrue(result);

        result = false;
        try {
            root.get(null, "value");
        } catch (NullPointerException e) {
            result = true;
        }
        assertTrue(result);

        result = true;
        try {
            root.get("key", null);
        } catch (NullPointerException e) {
            result = false;
        }
        assertTrue(result);

        node.put("test key", "some value");
        assertSame("some value", node.get("test key", null));
        assertSame("default value", node.get("not exist key", "default value"));
        assertSame(null, node.get("not exist key", null));
    }

    @Test
    public void testPutByte() {
        ConfigNode root = doNode();

        boolean result = false;
        try {
            ConfigNode removed = root.node("removed");
            removed.removeNode();

            removed.putByte("key", (byte) 0);
        } catch (IllegalStateException e) {
            result = true;
        }
        assertTrue(result);

        result = false;
        try {
            root.putByte(null, (byte) 0);
        } catch (NullPointerException e) {
            result = true;
        }
        assertTrue(result);

        root.putByte("zero", (byte) 0);
        root.putByte("pos", (byte) 13);
        root.putByte("neg", (byte) -7);

        assertTrue(root.getByte("zero", (byte) -7) == 0);
        assertTrue(root.getByte("pos", (byte) -7) == 13);
        assertTrue(root.getByte("neg", (byte) 17) == -7);
    }

    @Test
    public void testGetByte() {
        ConfigNode root = doNode();

        boolean result = false;
        try {
            ConfigNode removed = root.node("removed");
            removed.removeNode();

            removed.getByte("key", (byte) 0);
        } catch (IllegalStateException e) {
            result = true;
        }
        assertTrue(result);

        result = false;
        try {
            root.getByte(null, (byte) 0);
        } catch (NullPointerException e) {
            result = true;
        }
        assertTrue(result);

        root.putByte("key", (byte) 13);
        assertTrue(root.getByte("key", (byte) -7) == 13);
        assertTrue(root.getByte("not def", (byte) 29) == 29);
    }

    @Test
    public void testPutShort() {
        ConfigNode root = doNode();

        boolean result = false;
        try {
            ConfigNode removed = root.node("removed");
            removed.removeNode();

            removed.putShort("key", (short) 0);
        } catch (IllegalStateException e) {
            result = true;
        }
        assertTrue(result);

        result = false;
        try {
            root.putShort(null, (short) 0);
        } catch (NullPointerException e) {
            result = true;
        }
        assertTrue(result);

        root.putShort("zero", (short) 0);
        root.putShort("pos", (short) 13);
        root.putShort("neg", (short) -7);

        assertTrue(root.getShort("zero", (short) -7) == 0);
        assertTrue(root.getShort("pos", (short) -7) == 13);
        assertTrue(root.getShort("neg", (short) 17) == -7);
    }

    @Test
    public void testGetShort() {
        ConfigNode root = doNode();

        boolean result = false;
        try {
            ConfigNode removed = root.node("removed");
            removed.removeNode();

            removed.getShort("key", (short) 0);
        } catch (IllegalStateException e) {
            result = true;
        }
        assertTrue(result);

        result = false;
        try {
            root.getShort(null, (short) 0);
        } catch (NullPointerException e) {
            result = true;
        }
        assertTrue(result);

        root.putShort("key", (short) 13);
        assertTrue(root.getShort("key", (short) -7) == 13);
        assertTrue(root.getShort("not def", (short) 29) == 29);
    }

    @Test
    public void testPutInt() {
        ConfigNode root = doNode();

        boolean result = false;
        try {
            ConfigNode removed = root.node("removed");
            removed.removeNode();

            removed.putInt("key", 0);
        } catch (IllegalStateException e) {
            result = true;
        }
        assertTrue(result);

        result = false;
        try {
            root.putInt(null, 0);
        } catch (NullPointerException e) {
            result = true;
        }
        assertTrue(result);

        root.putInt("zero", 0);
        root.putInt("pos", 13);
        root.putInt("neg", -7);

        assertTrue(root.getInt("zero", -7) == 0);
        assertTrue(root.getInt("pos", -7) == 13);
        assertTrue(root.getInt("neg", 17) == -7);
    }

    @Test
    public void testGetInt() {
        ConfigNode root = doNode();

        boolean result = false;
        try {
            ConfigNode removed = root.node("removed");
            removed.removeNode();

            removed.getInt("key", 0);
        } catch (IllegalStateException e) {
            result = true;
        }
        assertTrue(result);

        result = false;
        try {
            root.getInt(null, 0);
        } catch (NullPointerException e) {
            result = true;
        }
        assertTrue(result);

        root.putInt("key", 13);
        assertTrue(root.getInt("key", -7) == 13);
        assertTrue(root.getInt("not def", 29) == 29);
    }

    @Test
    public void testPutLong() {
        ConfigNode root = doNode();

        boolean result = false;
        try {
            ConfigNode removed = root.node("removed");
            removed.removeNode();

            removed.putLong("key", 0);
        } catch (IllegalStateException e) {
            result = true;
        }
        assertTrue(result);

        result = false;
        try {
            root.putLong(null, 0);
        } catch (NullPointerException e) {
            result = true;
        }
        assertTrue(result);

        root.putLong("zero", 0);
        root.putLong("pos", 13);
        root.putLong("neg", -7);

        assertTrue(root.getLong("zero", -7) == 0);
        assertTrue(root.getLong("pos", -7) == 13);
        assertTrue(root.getLong("neg", 17) == -7);
    }

    @Test
    public void testGetLong() {
        ConfigNode root = doNode();

        boolean result = false;
        try {
            ConfigNode removed = root.node("removed");
            removed.removeNode();

            removed.getLong("key", 0);
        } catch (IllegalStateException e) {
            result = true;
        }
        assertTrue(result);

        result = false;
        try {
            root.getLong(null, 0);
        } catch (NullPointerException e) {
            result = true;
        }
        assertTrue(result);

        root.putLong("key", 13);
        assertTrue(root.getLong("key", -7) == 13);
        assertTrue(root.getLong("not def", 29) == 29);
    }

    @Test
    public void testPutFloat() {
        ConfigNode root = doNode();

        boolean result = false;
        try {
            ConfigNode removed = root.node("removed");
            removed.removeNode();

            removed.putFloat("key", 0);
        } catch (IllegalStateException e) {
            result = true;
        }
        assertTrue(result);

        result = false;
        try {
            root.putFloat(null, 0);
        } catch (NullPointerException e) {
            result = true;
        }
        assertTrue(result);

        root.putFloat("zero", 0);
        root.putFloat("pos", (float) 13.666);
        root.putFloat("neg", (float) -7.0707);

        assertTrue(root.getFloat("zero", -7) == 0);
        assertTrue(root.getFloat("pos", -7) == (float) 13.666);
        assertTrue(root.getFloat("neg", 17) == (float) -7.0707);
    }

    @Test
    public void testGetFloat() {
        ConfigNode root = doNode();

        boolean result = false;
        try {
            ConfigNode removed = root.node("removed");
            removed.removeNode();

            removed.getFloat("key", 0);
        } catch (IllegalStateException e) {
            result = true;
        }
        assertTrue(result);

        result = false;
        try {
            root.getFloat(null, 0);
        } catch (NullPointerException e) {
            result = true;
        }
        assertTrue(result);

        root.putFloat("key", (float) 13.777);
        assertTrue(root.getFloat("key", (float) -7.002) == (float) 13.777);
        assertTrue(root.getFloat("not def", (float) 29.001) == (float) 29.001);
    }

    @Test
    public void testPutDouble() {
        ConfigNode root = doNode();

        boolean result = false;
        try {
            ConfigNode removed = root.node("removed");
            removed.removeNode();

            removed.putDouble("key", 0);
        } catch (IllegalStateException e) {
            result = true;
        }
        assertTrue(result);

        result = false;
        try {
            root.putDouble(null, 0);
        } catch (NullPointerException e) {
            result = true;
        }
        assertTrue(result);

        root.putDouble("zero", 0);
        root.putDouble("pos", 13.666);
        root.putDouble("neg", -7.0707);

        assertTrue(root.getDouble("zero", -7) == 0);
        assertTrue(root.getDouble("pos", -7) == 13.666);
        assertTrue(root.getDouble("neg", 17) == -7.0707);
    }

    @Test
    public void testGetDouble() {
        ConfigNode root = doNode();

        boolean result = false;
        try {
            ConfigNode removed = root.node("removed");
            removed.removeNode();

            removed.getDouble("key", 0);
        } catch (IllegalStateException e) {
            result = true;
        }
        assertTrue(result);

        result = false;
        try {
            root.getDouble(null, 0);
        } catch (NullPointerException e) {
            result = true;
        }
        assertTrue(result);

        root.putDouble("key", 13.777);
        assertTrue(root.getDouble("key", -7.002) == 13.777);
        assertTrue(root.getDouble("not def", 29.00) == 29.00);
    }

    @Test
    public void testPutBoolean() {
        ConfigNode root = doNode();

        boolean result = false;
        try {
            ConfigNode removed = root.node("removed");
            removed.removeNode();

            removed.putBoolean("key", false);
        } catch (IllegalStateException e) {
            result = true;
        }
        assertTrue(result);

        result = false;
        try {
            root.putBoolean(null, true);
        } catch (NullPointerException e) {
            result = true;
        }
        assertTrue(result);

        root.putBoolean("yes", true);
        root.putBoolean("no", false);
        root.putBoolean("tr", true);

        assertTrue(root.getBoolean("yes", false));
        assertFalse(root.getBoolean("no", true));
        assertTrue(root.getBoolean("tr", false));
    }

    @Test
    public void testGetBoolean() {
        ConfigNode root = doNode();

        boolean result = false;
        try {
            ConfigNode removed = root.node("removed");
            removed.removeNode();

            removed.getBoolean("key", true);
        } catch (IllegalStateException e) {
            result = true;
        }
        assertTrue(result);

        result = false;
        try {
            root.getBoolean(null, true);
        } catch (NullPointerException e) {
            result = true;
        }
        assertTrue(result);

        root.putBoolean("key", true);
        assertTrue(root.getBoolean("key", false));

        root.putBoolean("key", false);
        assertFalse(root.getBoolean("key", true));

        assertTrue(root.getBoolean("no rec", true));
        assertFalse(root.getBoolean("no rec", false));
    }

    @Test
    public void testPutByteArray() {
        ConfigNode root = doNode();
        byte[] aa = new byte[] { 17, 25, 83, 101 };
        byte[] bb = new byte[] { 1, 3, 7, -9, 12, 19 };
        byte[] cc = new byte[] { 0, -3, 54, 111, 127 };

        boolean result = false;
        try {
            ConfigNode removed = root.node("removed");
            removed.removeNode();

            removed.putByteArray("key", aa);
        } catch (IllegalStateException e) {
            result = true;
        }
        assertTrue(result);

        result = false;
        try {
            root.putByteArray(null, cc);
        } catch (NullPointerException e) {
            result = true;
        }
        assertTrue(result);
        result = false;
        try {
            root.putByteArray("key", null);
        } catch (NullPointerException e) {
            result = true;
        }
        assertTrue(result);

        root.putByteArray("ar1", aa);
        root.putByteArray("ar2", bb);
        root.putByteArray("ar3", cc);

        assertArrayEquals(aa, root.getByteArray("ar1", null));
        assertArrayEquals(bb, root.getByteArray("ar2", null));
        assertArrayEquals(cc, root.getByteArray("ar3", null));
    }

    @Test
    public void testGetByteArray() {
        ConfigNode root = doNode();
        byte[] aa = new byte[] { 17, 25, 83, 101 };
        byte[] bb = new byte[] { 1, 3, 7, -9, 12, 19 };

        boolean result = false;
        try {
            ConfigNode removed = root.node("removed");
            removed.removeNode();

            removed.getByteArray("key", aa);
        } catch (IllegalStateException e) {
            result = true;
        }
        assertTrue(result);

        result = false;
        try {
            root.getByteArray(null, aa);
        } catch (NullPointerException e) {
            result = true;
        }
        assertTrue(result);

        root.putByteArray("key", aa);
        assertArrayEquals(aa, root.getByteArray("key", null));
        assertArrayEquals(bb, root.getByteArray("no key", bb));
        assertArrayEquals(null, root.getByteArray("no key", null));
    }

    @Test
    public void testPutCommentString() {
        ConfigNode root = doNode();

        boolean result = false;
        try {
            ConfigNode removed = root.node("removed");
            removed.removeNode();

            removed.putComment("");
        } catch (IllegalStateException e) {
            result = true;
        }
        assertTrue(result);

        root.putComment("comment for node");

        assertSame("comment for node", root.getComment());
    }

    @Test
    public void testGetComment() {
        ConfigNode root = doNode();

        boolean result = false;
        try {
            ConfigNode removed = root.node("removed");
            removed.removeNode();

            removed.getComment();
        } catch (IllegalStateException e) {
            result = true;
        }
        assertTrue(result);

        assertSame(null, root.getComment());

        root.putComment("comment for node");

        assertSame("comment for node", root.getComment());
    }

    @Test
    public void testRemoveComment() {
        ConfigNode root = doNode();

        boolean result = false;
        try {
            ConfigNode removed = root.node("removed");
            removed.removeNode();

            removed.removeComment();
        } catch (IllegalStateException e) {
            result = true;
        }
        assertTrue(result);

        root.putComment("comment for node");
        root.removeComment();

        assertSame(null, root.getComment());
    }

    @Test
    public void testPutCommentStringString() {
        ConfigNode root = doNode();

        boolean result = false;
        try {
            ConfigNode removed = root.node("removed");
            removed.removeNode();

            removed.putComment("", "");
        } catch (IllegalStateException e) {
            result = true;
        }
        assertTrue(result);

        root.put("key", "");
        root.putComment("key", "short comm");
        assertSame(root.getComment("key"), "short comm");

        root.putComment("no key", "comm");
        assertSame(root.getComment("no key"), "comm");
    }

    @Test
    public void testGetCommentString() {
        ConfigNode root = doNode();

        boolean result = false;
        try {
            ConfigNode removed = root.node("removed");
            removed.removeNode();

            removed.getComment("k");
        } catch (IllegalStateException e) {
            result = true;
        }
        assertTrue(result);

        result = false;
        try {
            root.getComment(null);
        } catch (NullPointerException e) {
            result = true;
        }
        assertTrue(result);

        root.putComment("key", "short comm");
        assertSame(root.getComment("key"), "short comm");
    }

    @Test
    public void testRemoveCommentString() {
        ConfigNode root = doNode();

        boolean result = false;
        try {
            ConfigNode removed = root.node("removed");
            removed.removeNode();

            removed.removeComment("k");
        } catch (IllegalStateException e) {
            result = true;
        }
        assertTrue(result);

        result = false;
        try {
            root.removeComment(null);
        } catch (NullPointerException e) {
            result = true;
        }
        assertTrue(result);

        root.put("key", "value");
        root.putComment("key", "short comm");
        root.removeComment("key");
        assertSame(null, root.getComment("key"));
        assertSame("value", root.get("key", null));
    }

    @Test
    public void testAddConfigChangeListener() {
        ConfigNode root = doNode();
        ConfigListener ccl = new ConfigListener();

        boolean result = false;
        try {
            ConfigNode removed = root.node("removed");
            removed.removeNode();

            removed.addConfigChangeListener(ccl);
        } catch (IllegalStateException e) {
            result = true;
        }
        assertTrue(result);

        root.addConfigChangeListener(ccl);
        root.fireConfigChangeEvent("key", "val");
        assertTrue(ccl.change != null);
        assertSame("key", ccl.change.getKey());
        assertSame("val", ccl.change.getNewValue());
    }

    @Test
    public void testRemoveConfigChangeListener() {
        ConfigNode root = doNode();
        ConfigListener ccl = new ConfigListener();

        boolean result = false;
        try {
            ConfigNode removed = root.node("removed");
            removed.removeNode();

            removed.removeConfigChangeListener(ccl);
        } catch (IllegalStateException e) {
            result = true;
        }
        assertTrue(result);

        root.addConfigChangeListener(ccl);
        root.addConfigChangeListener(ccl);
        root.removeConfigChangeListener(ccl);

        root.fireConfigChangeEvent("key", "val");

        assertTrue(ccl.change == null);
    }

    @Test
    public void testAddNodeChangeListener() {
        ConfigNode root = doNode();
        NodeListener ncl = new NodeListener();

        boolean result = false;
        try {
            ConfigNode removed = root.node("removed");
            removed.removeNode();

            removed.addNodeChangeListener(ncl);
        } catch (IllegalStateException e) {
            result = true;
        }
        assertTrue(result);

        root.addNodeChangeListener(ncl);

        root.fireChildAddedEvent(null);
        root.fireChildRemovedEvent(null);

        assertTrue(ncl.added != null);
        assertTrue(ncl.removed != null);
    }

    @Test
    public void testRemoveNodeChangeListener() {
        ConfigNode root = doNode();
        NodeListener ncl = new NodeListener();

        boolean result = false;
        try {
            ConfigNode removed = root.node("removed");
            removed.removeNode();

            removed.removeNodeChangeListener(ncl);
        } catch (IllegalStateException e) {
            result = true;
        }
        assertTrue(result);

        root.addNodeChangeListener(ncl);
        root.addNodeChangeListener(ncl);
        root.removeNodeChangeListener(ncl);

        root.fireChildAddedEvent(null);
        root.fireChildRemovedEvent(null);

        assertTrue(ncl.added == null);
        assertTrue(ncl.removed == null);
    }

}
