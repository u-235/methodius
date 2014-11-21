
package microfont;

import static org.junit.Assert.*;
import java.awt.Dimension;
import org.junit.Test;

public class PixselMapTest extends AbstractPixselMapTest {

    @Override
    public AbstractPixselMap createAbstractPixselMap(int width, int height,
                    byte[] src) {
        return new PixselMap(width, height, src);
    }

    public PixselMap createPixselMap(int width, int height, byte[] src) {
        return new PixselMap(width, height, src);
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
        // Проверки допустимости параметров.
        boolean result = false;
        try {
            createPixselMap(7, 9, null).setSize(-1, 9);
        } catch (DisallowOperationException e) {
            //
        } catch (IllegalArgumentException e) {
            result = true;
        }
        assertTrue(result);

        result = false;
        try {
            createPixselMap(7, 9, null).setSize(7, -1);
        } catch (DisallowOperationException e) {
            //
        } catch (IllegalArgumentException e) {
            result = true;
        }
        assertTrue(result);

        result = false;
        try {
            createPixselMap(7, 9, null).setSize(-1, -1);
        } catch (DisallowOperationException e) {
            //
        } catch (IllegalArgumentException e) {
            result = true;
        }
        assertTrue(result);

        // Проверки изменения ширины.

        byte[] tst = new byte[] { 0x0, 0x8, 0x20, (byte) 0x80, (byte) 0x80,
                (byte) 0x83, 0x8, 0xe, 0x8, 0x20, 0x0, 0x0, 0x0 };
        PixselMap expected = createPixselMap(7, 11, new byte[] { 0x0, 0x2, 0x2,
                (byte) 0x82, 0x23, (byte) 0xe2, 0x20, 0x20, 0x0, 0x0 });
        PixselMap actual = createPixselMap(9, 11, tst);
        try {
            actual.setSize(7, 11);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        expected = createPixselMap(11, 11, new byte[] { 00, 0x20, 0x0, 0x2,
                0x20, (byte) 0x80, 0x3, 0x22, (byte) 0xe0, 0x0, 0x2, 0x20, 0x0,
                0x0, 0x0, 0x0 });
        actual = createPixselMap(9, 11, tst);
        try {
            actual.setSize(11, 11);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        // Проверки изменения высоты.
        expected = createPixselMap(9, 9,
                        new byte[] { 0x0, 0x8, 0x20, (byte) 0x80, (byte) 0x80,
                                (byte) 0x83, 0x8, 0xe, 0x8, 0x20, 0x0 });
        actual = createPixselMap(9, 11, tst);
        try {
            actual.setSize(9, 9);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        expected = createPixselMap(9, 12, new byte[] { 0x0, 0x8, 0x20,
                (byte) 0x80, (byte) 0x80, (byte) 0x83, 0x8, 0xe, 0x8, 0x20,
                0x0, 0x0, 0x0, 0x0, 0x0 });
        actual = createPixselMap(9, 11, tst);
        try {
            actual.setSize(9, 12);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);
    }

    @Test
    public void testSetSizeDimension() {
        // Калька с метода testSetSizeIntInt()

        // Проверки допустимости параметров.
        boolean result = false;
        try {
            createPixselMap(7, 9, null).setSize(new Dimension(-1, 9));
        } catch (DisallowOperationException e) {
            //
        } catch (IllegalArgumentException e) {
            result = true;
        }
        assertTrue(result);

        result = false;
        try {
            createPixselMap(7, 9, null).setSize(new Dimension(7, -1));
        } catch (DisallowOperationException e) {
            //
        } catch (IllegalArgumentException e) {
            result = true;
        }
        assertTrue(result);

        result = false;
        try {
            createPixselMap(7, 9, null).setSize(new Dimension(-1, -1));
        } catch (DisallowOperationException e) {
            //
        } catch (IllegalArgumentException e) {
            result = true;
        }
        assertTrue(result);

        // Проверки изменения ширины.

        byte[] tst = new byte[] { 0x0, 0x8, 0x20, (byte) 0x80, (byte) 0x80,
                (byte) 0x83, 0x8, 0xe, 0x8, 0x20, 0x0, 0x0, 0x0 };
        PixselMap expected = createPixselMap(7, 11, new byte[] { 0x0, 0x2, 0x2,
                (byte) 0x82, 0x23, (byte) 0xe2, 0x20, 0x20, 0x0, 0x0 });
        PixselMap actual = createPixselMap(9, 11, tst);
        try {
            actual.setSize(new Dimension(7, 11));
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        expected = createPixselMap(11, 11, new byte[] { 00, 0x20, 0x0, 0x2,
                0x20, (byte) 0x80, 0x3, 0x22, (byte) 0xe0, 0x0, 0x2, 0x20, 0x0,
                0x0, 0x0, 0x0 });
        actual = createPixselMap(9, 11, tst);
        try {
            actual.setSize(new Dimension(11, 11));
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        // Проверки изменения высоты.
        expected = createPixselMap(9, 9,
                        new byte[] { 0x0, 0x8, 0x20, (byte) 0x80, (byte) 0x80,
                                (byte) 0x83, 0x8, 0xe, 0x8, 0x20, 0x0 });
        actual = createPixselMap(9, 11, tst);
        try {
            actual.setSize(new Dimension(9, 9));
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        expected = createPixselMap(9, 12, new byte[] { 0x0, 0x8, 0x20,
                (byte) 0x80, (byte) 0x80, (byte) 0x83, 0x8, 0xe, 0x8, 0x20,
                0x0, 0x0, 0x0, 0x0, 0x0 });
        actual = createPixselMap(9, 11, tst);
        try {
            actual.setSize(new Dimension(9, 12));
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);
    }

    @Test
    public void testSetWidth() {
        // Калька с метода testSetSizeIntInt()

        // Проверки допустимости параметров.
        boolean result = false;
        try {
            createPixselMap(7, 9, null).setWidth(-1);
        } catch (DisallowOperationException e) {
            //
        } catch (IllegalArgumentException e) {
            result = true;
        }
        assertTrue(result);

        // Проверки изменения ширины.

        byte[] tst = new byte[] { 0x0, 0x8, 0x20, (byte) 0x80, (byte) 0x80,
                (byte) 0x83, 0x8, 0xe, 0x8, 0x20, 0x0, 0x0, 0x0 };
        PixselMap expected = createPixselMap(7, 11, new byte[] { 0x0, 0x2, 0x2,
                (byte) 0x82, 0x23, (byte) 0xe2, 0x20, 0x20, 0x0, 0x0 });
        PixselMap actual = createPixselMap(9, 11, tst);
        try {
            actual.setWidth(7);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        expected = createPixselMap(11, 11, new byte[] { 00, 0x20, 0x0, 0x2,
                0x20, (byte) 0x80, 0x3, 0x22, (byte) 0xe0, 0x0, 0x2, 0x20, 0x0,
                0x0, 0x0, 0x0 });
        actual = createPixselMap(9, 11, tst);
        try {
            actual.setWidth(11);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);
    }

    @Test
    public void testSetHeight() {
        // Калька с метода testSetSizeIntInt()

        // Проверки допустимости параметров.
        boolean result = false;
        try {
            createPixselMap(7, 9, null).setHeight(-1);
        } catch (DisallowOperationException e) {
            //
        } catch (IllegalArgumentException e) {
            result = true;
        }
        assertTrue(result);

        // Проверки изменения высоты.
        byte[] tst = new byte[] { 0x0, 0x8, 0x20, (byte) 0x80, (byte) 0x80,
                (byte) 0x83, 0x8, 0xe, 0x8, 0x20, 0x0, 0x0, 0x0 };
        PixselMap expected = createPixselMap(9, 9,
                        new byte[] { 0x0, 0x8, 0x20, (byte) 0x80, (byte) 0x80,
                                (byte) 0x83, 0x8, 0xe, 0x8, 0x20, 0x0 });
        PixselMap actual = createPixselMap(9, 11, tst);
        try {
            actual.setHeight(9);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        expected = createPixselMap(9, 12, new byte[] { 0x0, 0x8, 0x20,
                (byte) 0x80, (byte) 0x80, (byte) 0x83, 0x8, 0xe, 0x8, 0x20,
                0x0, 0x0, 0x0, 0x0, 0x0 });
        actual = createPixselMap(9, 11, tst);
        try {
            actual.setHeight(12);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);
    }

    @Test
    public void testSetPixsel() {
        PixselMap expected, actual;

        expected = createPixselMap(9, 11, null);
        actual = createPixselMap(9, 11, null);
        actual.setPixsel(-3, 1, true);
        assertEquals(expected, actual);
        actual.setPixsel(-3, 1, false);
        assertEquals(expected, actual);

        actual.setPixsel(7, -1, true);
        assertEquals(expected, actual);
        actual.setPixsel(7, -1, false);
        assertEquals(expected, actual);

        expected = createPixselMap(9, 11, new byte[] { 0x0, 0x0, 0x0, 0x40,
                0x0, 0x1, 0x4, 0x4, 0x4, 0x0, 0x0, 0x0, 0x0 });
        actual.setPixsel(3, 3, true);
        actual.setPixsel(4, 4, true);
        actual.setPixsel(5, 5, true);
        actual.setPixsel(4, 6, true);
        actual.setPixsel(3, 7, true);
        assertEquals(expected, actual);

        expected = createPixselMap(9, 11, null);
        actual.setPixsel(3, 3, false);
        actual.setPixsel(4, 4, false);
        actual.setPixsel(5, 5, false);
        actual.setPixsel(4, 6, false);
        actual.setPixsel(3, 7, false);
        assertEquals(expected, actual);
    }

    @Test
    public void testSetArray() {
        PixselMap expected, actual;

        expected = createPixselMap(9, 11, new byte[] { 0x0, 0x0, 0x0, 0x40,
                0x0, 0x1, 0x4, 0x4, 0x4, 0x0, 0x0, 0x0, 0x0 });
        actual = createPixselMap(9, 11, null);
        boolean result = false;
        try {
            actual.setArray(null);
        } catch (NullPointerException e) {
            result = true;
        }
        assertTrue(result);

        actual.setArray(new byte[] { 0x0, 0x0, 0x0, 0x40, 0x0, 0x1, 0x4, 0x4,
                0x4, 0x0, 0x0, 0x0, 0x0 });
        assertEquals(expected, actual);
    }

    @Test
    public void testShift() {
        byte[] first = { 0x0, 0x0, 0x0, 0x40, 0x0, 0x1, 0x4, 0x4, 0x4, 0x0,
                0x0, 0x0, 0x0 };
        byte[] left = { 0x0, 0x0, 0x0, 0x20, (byte) 0x80, 0x0, 0x2, 0x2, 0x2,
                0x0, 0x0, 0x0, 0x0 };
        byte[] right = { 0x0, 0x0, 0x0, (byte) 0x80, 0x0, 0x2, 0x8, 0x8, 0x8,
                0x0, 0x0, 0x0, 0x0 };
        byte[] up = { 0x0, 0x0, 0x20, (byte) 0x80, 0x0, 0x2, 0x2, 0x2, 0x0,
                0x0, 0x0, 0x0, 0x0 };
        byte[] down = { 0x0, 0x0, 0x0, 0x0, (byte) 0x80, 0x0, 0x2, 0x8, 0x8,
                0x8, 0x0, 0x0, 0x0 };
        PixselMap expected, actual;

        actual = createPixselMap(9, 11, first);
        actual.shift(PixselMap.SHIFT_LEFT, 1);
        expected = createPixselMap(9, 11, left);
        assertEquals(expected, actual);
        actual = createPixselMap(9, 11, right);
        actual.shift(PixselMap.SHIFT_LEFT, 2);
        assertEquals(expected, actual);

        actual = createPixselMap(9, 11, first);
        actual.shift(PixselMap.SHIFT_RIGHT, 1);
        expected = createPixselMap(9, 11, right);
        assertEquals(expected, actual);
        actual = createPixselMap(9, 11, left);
        actual.shift(PixselMap.SHIFT_RIGHT, 2);
        assertEquals(expected, actual);

        actual = createPixselMap(9, 11, first);
        actual.shift(PixselMap.SHIFT_UP, 1);
        expected = createPixselMap(9, 11, up);
        assertEquals(expected, actual);
        actual = createPixselMap(9, 11, down);
        actual.shift(PixselMap.SHIFT_UP, 2);
        assertEquals(expected, actual);

        actual = createPixselMap(9, 11, first);
        actual.shift(PixselMap.SHIFT_DOWN, 1);
        expected = createPixselMap(9, 11, down);
        assertEquals(expected, actual);
        actual = createPixselMap(9, 11, up);
        actual.shift(PixselMap.SHIFT_DOWN, 2);
        assertEquals(expected, actual);
    }

    @Test
    public void testShiftRight() {
        byte[] first = { 0x0, 0x0, 0x0, 0x40, 0x0, 0x1, 0x4, 0x4, 0x4, 0x0,
                0x0, 0x0, 0x0 };
        byte[] right = { 0x0, 0x0, 0x0, (byte) 0x80, 0x0, 0x2, 0x8, 0x8, 0x8,
                0x0, 0x0, 0x0, 0x0 };
        PixselMap expected, actual;

        actual = createPixselMap(9, 11, first);
        actual.shiftRight();
        expected = createPixselMap(9, 11, right);
        assertEquals(expected, actual);
    }

    @Test
    public void testShiftLeft() {
        byte[] first = { 0x0, 0x0, 0x0, 0x40, 0x0, 0x1, 0x4, 0x4, 0x4, 0x0,
                0x0, 0x0, 0x0 };
        byte[] left = { 0x0, 0x0, 0x0, 0x20, (byte) 0x80, 0x0, 0x2, 0x2, 0x2,
                0x0, 0x0, 0x0, 0x0 };
        PixselMap expected, actual;

        actual = createPixselMap(9, 11, first);
        actual.shiftLeft();
        expected = createPixselMap(9, 11, left);
        assertEquals(expected, actual);
    }

    @Test
    public void testShiftUp() {
        byte[] first = { 0x0, 0x0, 0x0, 0x40, 0x0, 0x1, 0x4, 0x4, 0x4, 0x0,
                0x0, 0x0, 0x0 };
        byte[] up = { 0x0, 0x0, 0x20, (byte) 0x80, 0x0, 0x2, 0x2, 0x2, 0x0,
                0x0, 0x0, 0x0, 0x0 };
        PixselMap expected, actual;

        actual = createPixselMap(9, 11, first);
        actual.shiftUp();
        expected = createPixselMap(9, 11, up);
        assertEquals(expected, actual);
    }

    @Test
    public void testShiftDown() {
        byte[] first = { 0x0, 0x0, 0x0, 0x40, 0x0, 0x1, 0x4, 0x4, 0x4, 0x0,
                0x0, 0x0, 0x0 };
        byte[] down = { 0x0, 0x0, 0x0, 0x0, (byte) 0x80, 0x0, 0x2, 0x8, 0x8,
                0x8, 0x0, 0x0, 0x0 };
        PixselMap expected, actual;

        actual = createPixselMap(9, 11, first);
        actual.shiftDown();
        expected = createPixselMap(9, 11, down);
        assertEquals(expected, actual);
    }

    @Test
    public void testReflectVerticale() {
        PixselMap expected = createPixselMap(9, 11, new byte[] { 0x0, 0x0, 0x0,
                0x0, 0x0, 0x0, 0x0, 0x1, 0x5, 0x11, 0x14, 0x10, 0x0 });
        PixselMap actual = createPixselMap(9, 11, new byte[] { 0x0, 0x0, 0x0,
                0x0, 0x0, 0x0, 0x0, 0x10, 0x50, 0x10, 0x41, 0x1, 0x1 });
        actual.reflectVerticale();
        assertEquals(expected, actual);
    }

    @Test
    public void testReflectHorizontale() {
        PixselMap expected = createPixselMap(9, 11, new byte[] { 0x0, 0x0, 0x0,
                0x0, 0x0, 0x0, 0x0, 0x10, 0x50, 0x10, 0x41, 0x1, 0x1 });
        PixselMap actual = createPixselMap(9, 11, new byte[] { 0x40, 0x40,
                0x41, 0x4, 0x5, 0x4, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0 });
        actual.reflectHorizontale();
        assertEquals(expected, actual);
    }

    @Test
    public void testChangeWidth() {
        byte[] wide = { 0x30, (byte) 0xe0, 0x3, 0x31, 0xc, 0x37, (byte) 0xe0,
                0x1, 0x1b, (byte) 0x8c, 0x30, (byte) 0xcc, (byte) 0xc0, 0x3,
                0xc, 0x0 };
        byte[] left = { 0xc, 0x3e, (byte) 0xc4, 0x8, 0x7, 0x18, 0x70,
                (byte) 0xb0, 0x30, 0x33, 0x3c, 0x30, 0x0 };
        byte[] leftWide = { 0x30, (byte) 0xe0, 0x3, 0x31, 0x8, 0x7, 0x60, 0x0, 0x13,
                (byte) 0x8c, 0x30, (byte) 0xcc, (byte) 0xc0, 0x3, 0xc, 0x0 };
        byte[] center = { 0x0, 0x38, (byte) 0xd0, 0x30, 0x37, 0x78,
                (byte) 0xb0, 0x31, 0x32, 0x3c, 0x30, 0x0, 0x0 };
        byte[] centerWide = { 0x0, 0x60, 0x2, 0x31, 0xc, 0x37, (byte) 0xe0,
                0x1, 0x1b, (byte) 0x8c, 0x30, (byte) 0xcc, 0x40, 0x2, 0x0, 0x0 };
        byte[] right = { 0x30, (byte) 0xf8, 0x10, 0x33, 0x3c, 0x60,
                (byte) 0x80, 0x41, (byte) 0xc2, (byte) 0xcc, (byte) 0xf0,
                (byte) 0xc0, 0x0 };
        byte[] rightWide = { 0x30, (byte) 0xe0, 0x3, 0x31, 0xc, 0x33,
                (byte) 0x80, 0x1, 0x18, (byte) 0x84, 0x30, (byte) 0xcc,
                (byte) 0xc0, 0x3, 0xc, 0x0 };
        PixselMap expected, actual;

        // Нулевое количество столбцов ничего не меняет.
        expected = createPixselMap(9, 11, center);
        actual = createPixselMap(9, 11, center);
        try {
            actual.changeWidth(4, 0);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);
        
        expected = createPixselMap(9, 11, center);
        actual = createPixselMap(9, 11, center);
        try {
            actual.changeWidth(-2, 0);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);
        
        expected = createPixselMap(9, 11, center);
        actual = createPixselMap(9, 11, center);
        try {
            actual.changeWidth(13, 0);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        // Удаление столбцов.
        expected = createPixselMap(9, 11, center);
        actual = createPixselMap(11, 11, wide);
        try {
            actual.changeWidth(4, -2);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        expected = createPixselMap(9, 11, left);
        actual = createPixselMap(11, 11, wide);
        try {
            actual.changeWidth(0, -2);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        expected = createPixselMap(9, 11, right);
        actual = createPixselMap(11, 11, wide);
        try {
            actual.changeWidth(9, -2);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        // Удаление столбцов с выходом за границы.

        expected = createPixselMap(9, 11, left);
        actual = createPixselMap(11, 11, wide);
        try {
            actual.changeWidth(-3, -5);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        expected = createPixselMap(9, 11, right);
        actual = createPixselMap(11, 11, wide);
        try {
            actual.changeWidth(9, -5);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        // Добавление столбцов.
        expected = createPixselMap(11, 11, centerWide);
        actual = createPixselMap(9, 11, center);
        try {
            actual.changeWidth(4, 2);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        expected = createPixselMap(11, 11, leftWide);
        actual = createPixselMap(9, 11, left);
        try {
            actual.changeWidth(0, 2);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        expected = createPixselMap(11, 11, rightWide);
        actual = createPixselMap(9, 11, right);
        try {
            actual.changeWidth(9, 2);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        // Добавление столбцов с выходом за границы.

        expected = createPixselMap(11, 11, leftWide);
        actual = createPixselMap(9, 11, left);
        try {
            actual.changeWidth(-3, 5);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        expected = createPixselMap(11, 11, rightWide);
        actual = createPixselMap(9, 11, right);
        try {
            actual.changeWidth(10, 1);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);
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
        byte[] src = { 0x0, 0x0, 0x0, (byte) 0x80, (byte) 0x80, (byte) 0x82,
                0x8, 0xa, 0x8, 0x0, 0x0, 0x0, 0x0 };
        byte[] dirhomb = { 0x4, 0x14, 0x44, 0x50, 0x40, 0x0, 0x0, 0x10, 0x50,
                0x10, 0x41, 0x1, 0x1 };
        byte[] rect = { 0x44, 0x45, 0x45, 0x0 };
        AbstractPixselMap expected, actual;

        actual = createPixselMap(9, 11, src).getRectangle(2, 3, 5, 5);
        expected = createAbstractPixselMap(5, 5, rect);
        assertEquals(expected, actual);

        // Попытка получения области, которая "вылезает" слева и сверху.
        // В итоге область должна быть размером 5*5
        actual = createPixselMap(9, 11, dirhomb).getRectangle(-2, -4, 7, 9);
        assertEquals(expected, actual);

        // Попытка получения области, которая "вылезает" справа и снизу.
        // В итоге область должна быть размером 5*5
        actual = createPixselMap(9, 11, dirhomb).getRectangle(4, 6, 6, 7);
        assertEquals(expected, actual);
    }

    @Test
    public void testSet() {
        fail("Not yet implemented");
    }

    @Test
    public void testNeg() {
        byte[] src = { 0x0, 0x0, 0x0, (byte) 0x80, (byte) 0x80, (byte) 0x82,
                0x8, 0xa, 0x8, 0x0, 0x0, 0x0, 0x0 };
        byte[] neg = { 0x0, (byte) 0xfc, (byte) 0xf8, 0x71, 0x63, 0x45,
                (byte) 0x87, 0x15, 0x37, 0x7e, 0x0, 0x0, 0x0 };
        PixselMap actual, expected;

        actual = createPixselMap(9, 11, src);
        actual.neg(1, 1, 6, 8);
        expected = createPixselMap(9, 11, neg);
        assertEquals(expected, actual);
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
