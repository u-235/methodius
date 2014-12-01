
package microfont;

import static org.junit.Assert.*;
import org.junit.Test;

public class MSymbolTest extends PixselMapTest {

    @Override
    public AbstractPixselMap createAbstractPixselMap(int width, int height,
                    byte[] src) {
        return new MSymbol(0, width, height, src);
    }

    @Override
    public PixselMap createPixselMap(int width, int height, byte[] src) {
        return new MSymbol(0, width, height, src);
    }

    public MSymbol createMSymbol(int code, int width, int height, byte[] src) {
        return new MSymbol(code, width, height, src);
    }

    @Test
    public void testGetCode() {
        MSymbol src;

        src = createMSymbol(0, 5, 7, null);
        assertTrue(src.getCode() == 0);

        src = createMSymbol(17, 5, 7, null);
        assertTrue(src.getCode() == 17);
    }

    @Test
    public void testSetCode() {
        fail("Not yet implemented");
    }

    @Test
    public void testIsUnicode() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetUnicode() {
        fail("Not yet implemented");
    }

    @Test
    public void testClearUnicode() {
        fail("Not yet implemented");
    }

    @Test
    public void testSetUnicode() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetOwner() {
        fail("Not yet implemented");
    }

    @Test
    public void testCopyMSymbol() {
        fail("Not yet implemented");
    }

    @Test
    public void testCopyAbstractPixselMap() {
        fail("Not yet implemented");
    }

    @Override
    @Test
    public void testSetSizeIntInt() {
        fail("Not yet implemented");
    }

    @Override
    @Test
    public void testSetSizeDimension() {
        fail("Not yet implemented");
    }

    @Override
    @Test
    public void testSetWidth() {
        fail("Not yet implemented");
    }

    @Override
    @Test
    public void testSetHeight() {
        fail("Not yet implemented");
    }

    @Override
    @Test
    public void testChangeWidth() {
        fail("Not yet implemented");
    }

    @Override
    @Test
    public void testChangeHeight() {
        fail("Not yet implemented");
    }

    @Override
    @Test
    public void testRemoveColumns() {
        fail("Not yet implemented");
    }

    @Override
    @Test
    public void testRemoveRows() {
        fail("Not yet implemented");
    }

    @Override
    @Test
    public void testRemoveLeft() {
        fail("Not yet implemented");
    }

    @Override
    @Test
    public void testRemoveRight() {
        fail("Not yet implemented");
    }

    @Override
    @Test
    public void testRemoveTop() {
        fail("Not yet implemented");
    }

    @Override
    @Test
    public void testRemoveBottom() {
        fail("Not yet implemented");
    }

    @Override
    @Test
    public void testAddColumns() {
        fail("Not yet implemented");
    }

    @Override
    @Test
    public void testAddRows() {
        fail("Not yet implemented");
    }

    @Override
    @Test
    public void testAddLeft() {
        fail("Not yet implemented");
    }

    @Override
    @Test
    public void testAddRight() {
        fail("Not yet implemented");
    }

    @Override
    @Test
    public void testAddTop() {
        fail("Not yet implemented");
    }

    @Override
    @Test
    public void testAddBottom() {
        fail("Not yet implemented");
    }

    @Override
    @Test
    public void testRotate() {
        fail("Not yet implemented");
    }

}
