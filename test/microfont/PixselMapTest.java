package microfont;

import static org.junit.Assert.*;
import org.junit.Test;

public class PixselMapTest extends AbstractPixselMapTest {

    @Override
    public AbstractPixselMap createAbstractPixselMap(int width, int height,
                    byte[] src) {
        return new PixselMap(width, height, src);
    }
    
    public PixselMap createPixselMap(int width, int height,
                    byte[] src) {
        return new PixselMap(width, height, src);
    }

    @Override
    @Test
    public void testEqualsObject() {
        super.testEqualsObject();
        fail("Not yet implemented");
    }

    @Override
    @Test
    public void testCopy() {
        PixselMap dst, src;
        boolean result;

        src = createPixselMap(5, 7, new byte[] { 0, 8, 0, 9, 127 });
        dst = createPixselMap(5, 7, null);

        // PixselMap позволяет изменения размеров.
        result = false;
        try {
            dst.copy(src);
        } catch (DisallowOperationException e) {
            result = true;
        }
        assertFalse(result);
        assertEquals(src, dst);

        src = createPixselMap(7, 11, null);
        result = false;
        try {
            dst.copy(src);
        } catch (DisallowOperationException e) {
            result = true;
        }
        assertFalse(result);
    }

    @Test
    public void testSetSizeIntInt() {
        fail("Not yet implemented");
    }

    @Test
    public void testSetSizeDimension() {
        fail("Not yet implemented");
    }

    @Test
    public void testSetWidth() {
        fail("Not yet implemented");
    }

    @Test
    public void testSetHeight() {
        fail("Not yet implemented");
    }

    @Test
    public void testSetPixsel() {
        fail("Not yet implemented");
    }

    @Test
    public void testSetArray() {
        fail("Not yet implemented");
    }

    @Test
    public void testShift() {
        fail("Not yet implemented");
    }

    @Test
    public void testShiftRight() {
        fail("Not yet implemented");
    }

    @Test
    public void testShiftLeft() {
        fail("Not yet implemented");
    }

    @Test
    public void testShiftUp() {
        fail("Not yet implemented");
    }

    @Test
    public void testShiftDown() {
        fail("Not yet implemented");
    }

    @Test
    public void testReflectVerticale() {
        fail("Not yet implemented");
    }

    @Test
    public void testReflectHorizontale() {
        fail("Not yet implemented");
    }

    @Test
    public void testChangeHeight() {
        fail("Not yet implemented");
    }

    @Test
    public void testRemoveColumns() {
        fail("Not yet implemented");
    }

    @Test
    public void testRemoveRows() {
        fail("Not yet implemented");
    }

    @Test
    public void testRemoveLeft() {
        fail("Not yet implemented");
    }

    @Test
    public void testRemoveRight() {
        fail("Not yet implemented");
    }

    @Test
    public void testRemoveTop() {
        fail("Not yet implemented");
    }

    @Test
    public void testRemoveBottom() {
        fail("Not yet implemented");
    }

    @Test
    public void testAddColumns() {
        fail("Not yet implemented");
    }

    @Test
    public void testAddRows() {
        fail("Not yet implemented");
    }

    @Test
    public void testAddLeft() {
        fail("Not yet implemented");
    }

    @Test
    public void testAddRight() {
        fail("Not yet implemented");
    }

    @Test
    public void testAddTop() {
        fail("Not yet implemented");
    }

    @Test
    public void testAddBottom() {
        fail("Not yet implemented");
    }

    @Test
    public void testRotate() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetRectangle() {
        fail("Not yet implemented");
    }

    @Test
    public void testSetRectangle() {
        fail("Not yet implemented");
    }

    @Test
    public void testNegRectangle() {
        fail("Not yet implemented");
    }

    @Test
    public void testOverlay() {
        fail("Not yet implemented");
    }

    @Test
    public void testPlace() {
        fail("Not yet implemented");
    }

    @Test
    public void testOr() {
        fail("Not yet implemented");
    }

    @Test
    public void testAnd() {
        fail("Not yet implemented");
    }

    @Test
    public void testHor() {
        fail("Not yet implemented");
    }
}
