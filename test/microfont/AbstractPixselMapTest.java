
package microfont;

import static org.junit.Assert.*;
import org.junit.Test;

public class AbstractPixselMapTest {

    public AbstractPixselMap createAbstractPixselMap(int width, int height,
                    byte[] src) {
        return new AbstractPixselMap(width, height, src);
    }

    @Test
    public void testEqualsObject() {
        AbstractPixselMap first, second;

        first = createAbstractPixselMap(7, 9, null);
        second = createAbstractPixselMap(7, 9, null);
        assertFalse(first.equals(null));
        assertTrue(first.equals(second));

        second = createAbstractPixselMap(5, 7, new byte[] { 0, 8, 0, 9, 127 });
        assertFalse(first.equals(second));
        first = createAbstractPixselMap(5, 7, new byte[] { 0, 8, 0, 9, 127 });
        assertTrue(first.equals(second));
    }

    @Test
    public void testGetSize() {
        AbstractPixselMap apm;

        apm = createAbstractPixselMap(5, 2, null);
        assertTrue(apm.getSize().width == 5 && apm.getSize().height == 2);

        apm = createAbstractPixselMap(7, 11, null);
        assertTrue(apm.getSize().width == 7 && apm.getSize().height == 11);
    }

    @Test
    public void testGetWidth() {
        AbstractPixselMap apm;

        apm = createAbstractPixselMap(5, 2, null);
        assertTrue(apm.getWidth() == 5);

        apm = createAbstractPixselMap(11, 2, null);
        assertTrue(apm.getWidth() == 11);
    }

    @Test
    public void testGetHeight() {
        AbstractPixselMap apm;

        apm = createAbstractPixselMap(2, 7, null);
        assertTrue(apm.getHeight() == 7);

        apm = createAbstractPixselMap(7, 19, null);
        assertTrue(apm.getHeight() == 19);
    }

    @Test
    public void testCopy() {
        AbstractPixselMap dst, src;
        boolean result;

        src = createAbstractPixselMap(5, 7, new byte[] { 0, 8, 0, 9, 127 });
        dst = createAbstractPixselMap(5, 7, null);

        // AbstractPixselMap не позволяет изменения размеров.
        result = false;
        try {
            dst.copy(src);
        } catch (DisallowOperationException e) {
            result = true;
        }
        assertFalse(result);
        assertEquals(src, dst);

        src = createAbstractPixselMap(7, 11, null);
        result = false;
        try {
            dst.copy(src);
        } catch (DisallowOperationException e) {
            result = true;
        }
        assertTrue(result);
    }

    @Test
    public void testGetBytes() {
        AbstractPixselMap apm;
        byte[] array = new byte[] { 127, 17, 111, 37, 0 };

        apm = createAbstractPixselMap(5, 7, array);
        assertArrayEquals(array, apm.getBytes());
    }

    @Test
    public void testEmptyLeft() {
        AbstractPixselMap apm;

        apm = createAbstractPixselMap(5, 7, new byte[] { -1, 0, 0, 0, 0 });
        assertTrue(apm.emptyLeft() == 0);
        apm = createAbstractPixselMap(5, 7, new byte[] { 0, -1, 0, 0, 0 });
        assertTrue(apm.emptyLeft() == 0);
        apm = createAbstractPixselMap(5, 7, new byte[] { 64, 0, 0, 0, 0 });
        assertTrue(apm.emptyLeft() == 1);
    }

    @Test
    public void testEmptyRight() {
        AbstractPixselMap apm;

        apm = createAbstractPixselMap(5, 7, new byte[] { 16, 0, 0, 0, 0 });
        assertTrue(apm.emptyRight() == 0);
        apm = createAbstractPixselMap(5, 7, new byte[] { 0, 64, 0, 0, 0 });
        assertTrue(apm.emptyRight() == 0);
        apm = createAbstractPixselMap(5, 7, new byte[] { 8, 0, 0, 0, 0 });
        assertTrue(apm.emptyRight() == 1);
    }

    @Test
    public void testEmptyTop() {
        AbstractPixselMap apm;

        apm = createAbstractPixselMap(5, 7, new byte[] { 1, 0, 0, 0, 0 });
        assertTrue(apm.emptyTop() == 0);
        apm = createAbstractPixselMap(5, 7, new byte[] { 8, 0, 0, 0, 0 });
        assertTrue(apm.emptyTop() == 0);
        apm = createAbstractPixselMap(5, 7, new byte[] { 64, 0, 0, 0, 0 });
        assertTrue(apm.emptyTop() == 1);
    }

    @Test
    public void testEmptyBottom() {
        AbstractPixselMap apm;

        apm = createAbstractPixselMap(5, 7, new byte[] { 1, 0, 0, 0, 0 });
        assertTrue(apm.emptyBottom() == 6);
        apm = createAbstractPixselMap(5, 7, new byte[] { 64, 0, 0, 0, 0 });
        assertTrue(apm.emptyBottom() == 5);
        apm = createAbstractPixselMap(5, 7, new byte[] { 0, 0, 0, 0, 1 });
        assertTrue(apm.emptyBottom() == 0);
    }

}
