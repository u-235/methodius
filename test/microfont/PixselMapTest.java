
package microfont;

import static org.junit.Assert.*;
import java.awt.Dimension;
import org.junit.Test;

/**
 * При тестировании наследников {@code PixselMap} необходимо сделать тесты
 * методов изменяющих размеры карты пикселей. Это вызвано тем, что
 * {@code PixselMap} позволяет эти изменения, а наследники могут запрещать.
 * <p>
 * Список методов:<br>
 * {@link #testCopy()} {@link #testSetSizeIntInt()}
 * {@link #testSetSizeDimension()} {@link #testSetWidth()}
 * {@link #testSetHeight()} {@link #testChangeWidth()}
 * {@link #testChangeHeight()} {@link #testRemoveColumns()}
 * {@link #testRemoveRows()} {@link #testRemoveLeft()}
 * {@link #testRemoveRight()} {@link #testRemoveTop()}
 * {@link #testRemoveBottom()} {@link #testAddColumns()} {@link #testAddRows()}
 * {@link #testAddLeft()} {@link #testAddRight()} {@link #testAddTop()}
 * {@link #testAddBottom()}
 * <p>
 * Особое внимание надо обратить на {@link #testRotate()}.
 */
public class PixselMapTest extends AbstractPixselMapTest {
    static final byte[] wide       = { 0x30, (byte) 0xe0, 0x3, 0x31, 0xc, 0x37,
            (byte) 0xe0, 0x1, 0x1b, (byte) 0x8c, 0x30, (byte) 0xcc,
            (byte) 0xc0, 0x3, 0xc, 0x0 };
    static final byte[] left       = { 0xc, 0x3e, (byte) 0xc4, 0x8, 0x7, 0x18,
            0x70, (byte) 0xb0, 0x30, 0x33, 0x3c, 0x30, 0x0 };
    static final byte[] leftWide   = { 0x30, (byte) 0xe0, 0x3, 0x31, 0x8, 0x7,
            0x60, 0x0, 0x13, (byte) 0x8c, 0x30, (byte) 0xcc, (byte) 0xc0, 0x3,
            0xc, 0x0              };
    static final byte[] center     = { 0x0, 0x38, (byte) 0xd0, 0x30, 0x37,
            0x78, (byte) 0xb0, 0x31, 0x32, 0x3c, 0x30, 0x0, 0x0 };
    static final byte[] centerWide = { 0x0, 0x60, 0x2, 0x31, 0xc, 0x37,
            (byte) 0xe0, 0x1, 0x1b, (byte) 0x8c, 0x30, (byte) 0xcc, 0x40, 0x2,
            0x0, 0x0              };
    static final byte[] right      = { 0x30, (byte) 0xf8, 0x10, 0x33, 0x3c,
            0x60, (byte) 0x80, 0x41, (byte) 0xc2, (byte) 0xcc, (byte) 0xf0,
            (byte) 0xc0, 0x0      };
    static final byte[] rightWide  = { 0x30, (byte) 0xe0, 0x3, 0x31, 0xc, 0x33,
            (byte) 0x80, 0x1, 0x18, (byte) 0x84, 0x30, (byte) 0xcc,
            (byte) 0xc0, 0x3, 0xc, 0x0 };
    static final byte[] high       = { 0x38, (byte) 0xd8, 0x18, 0x12, 0x34,
            0x28, 0x50, (byte) 0xe0, (byte) 0x80, 0x3, 0x5, 0x32, 0x66,
            (byte) 0x86, 0x7      };
    static final byte[] top        = { (byte) 0x86, 0x4, 0xd, 0xa, 0x14, 0x38,
            (byte) 0xe0, 0x40, (byte) 0x81, (byte) 0x8c, (byte) 0x99,
            (byte) 0xe1, 0x1      };
    static final byte[] topHigh    = { 0x0, 0x0, 0x18, 0x12, 0x34, 0x28, 0x50,
            (byte) 0xe0, (byte) 0x80, 0x3, 0x5, 0x32, 0x66, (byte) 0x86, 0x7 };
    static final byte[] midle      = { 0x38, (byte) 0xd8, 0x18, 0x12, 0x34,
            0x28, (byte) 0xd0, 0x40, (byte) 0x81, (byte) 0x8c, (byte) 0x99,
            (byte) 0xe1, 0x1      };
    static final byte[] midleHigh  = { 0x38, (byte) 0xd8, 0x18, 0x12, 0x34,
            0x28, 0x10, 0x0, 0x0, 0x3, 0x5, 0x32, 0x66, (byte) 0x86, 0x7 };
    static final byte[] bottom     = { 0x38, (byte) 0xd8, 0x18, 0x12, 0x34,
            0x28, 0x50, (byte) 0xe0, (byte) 0x80, 0x3, 0x5, 0x32, 0x6 };
    static final byte[] bottomHigh = { 0x38, (byte) 0xd8, 0x18, 0x12, 0x34,
            0x28, 0x50, (byte) 0xe0, (byte) 0x80, 0x3, 0x5, 0x32, 0x6, 0x0, 0x0 };

    static final byte[] over = { 0x12, 0x25, 0x4a, (byte) 0x94, 0x28, 0x51, (byte) 0xa2,
            0x44, (byte) 0x89, 0x12, 0x25, 0x4a, 0x4 };
    static final byte[] place = { 0x12, 0x25, 0x4a, 0x34, (byte) 0xa8, 0x51,
            (byte) 0xa4, 0x50, (byte) 0x99, 0x12, 0x25, 0x4a, 0x4 };
    static final byte[] or = { 0x12, 0x25, 0x4a, (byte) 0xb4, (byte) 0xa8, 0x51,
            (byte) 0xa6, 0x54, (byte) 0x99, 0x12, 0x25, 0x4a, 0x4 };
    static final byte[] and = { 0x12, 0x25, 0x4a, 0x14, 0x28, 0x51, (byte) 0xa0, 0x40,
            (byte) 0x89, 0x12, 0x25, 0x4a, 0x4 };
    static final byte[] xor = { 0x12, 0x25, 0x4a, (byte) 0xb4, (byte) 0xa8, 0x50,
            (byte) 0xa6, 0x54, (byte) 0x91, 0x12, 0x25, 0x4a, 0x4 };
    static final AbstractPixselMap stamp = new AbstractPixselMap(5, 5, new byte[] {
            (byte) 0xc1, 0x20, (byte) 0xc8, 0x0 });

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
        PixselMap expected, actual;

        // Нулевое количество строк ничего не меняет.
        expected = createPixselMap(9, 11, midle);
        actual = createPixselMap(9, 11, midle);
        try {
            actual.changeHeight(4, 0);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        expected = createPixselMap(9, 11, midle);
        actual = createPixselMap(9, 11, midle);
        try {
            actual.changeHeight(-2, 0);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        expected = createPixselMap(9, 11, midle);
        actual = createPixselMap(9, 11, midle);
        try {
            actual.changeHeight(13, 0);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        // Удаление строк.
        expected = createPixselMap(9, 11, midle);
        actual = createPixselMap(9, 13, high);
        try {
            actual.changeHeight(6, -2);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        expected = createPixselMap(9, 11, top);
        actual = createPixselMap(9, 13, high);
        try {
            actual.changeHeight(0, -2);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        expected = createPixselMap(9, 11, bottom);
        actual = createPixselMap(9, 13, high);
        try {
            actual.changeHeight(11, -2);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        // Удаление строк с выходом за границы.
        expected = createPixselMap(9, 11, top);
        actual = createPixselMap(9, 13, high);
        try {
            actual.changeHeight(-3, -5);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        expected = createPixselMap(9, 11, bottom);
        actual = createPixselMap(9, 13, high);
        try {
            actual.changeHeight(11, -5);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        // Добавление строк.
        expected = createPixselMap(9, 13, midleHigh);
        actual = createPixselMap(9, 11, midle);
        try {
            actual.changeHeight(6, 2);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        expected = createPixselMap(9, 13, topHigh);
        actual = createPixselMap(9, 11, top);
        try {
            actual.changeHeight(0, 2);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        expected = createPixselMap(9, 13, bottomHigh);
        actual = createPixselMap(9, 11, bottom);
        try {
            actual.changeHeight(11, 2);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        // Добавление строк с выходом за границы.
        expected = createPixselMap(9, 13, topHigh);
        actual = createPixselMap(9, 11, top);
        try {
            actual.changeHeight(-3, 5);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        expected = createPixselMap(9, 13, bottomHigh);
        actual = createPixselMap(9, 11, bottom);
        try {
            actual.changeHeight(12, 1);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);
    }

    @Test
    public void testRemoveColumns() {
        PixselMap expected, actual;

        // Нулевое количество столбцов ничего не меняет.
        expected = createPixselMap(9, 11, center);
        actual = createPixselMap(9, 11, center);
        try {
            actual.removeColumns(4, 0);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        expected = createPixselMap(9, 11, center);
        actual = createPixselMap(9, 11, center);
        try {
            actual.removeColumns(-2, 0);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        expected = createPixselMap(9, 11, center);
        actual = createPixselMap(9, 11, center);
        try {
            actual.removeColumns(13, 0);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        // Отрицательное количество столбцов ничего не меняет.
        expected = createPixselMap(9, 11, center);
        actual = createPixselMap(9, 11, center);
        try {
            actual.removeColumns(4, -3);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        // Удаление столбцов.
        expected = createPixselMap(9, 11, center);
        actual = createPixselMap(11, 11, wide);
        try {
            actual.removeColumns(4, 2);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        expected = createPixselMap(9, 11, left);
        actual = createPixselMap(11, 11, wide);
        try {
            actual.removeColumns(0, 2);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        expected = createPixselMap(9, 11, right);
        actual = createPixselMap(11, 11, wide);
        try {
            actual.removeColumns(9, 2);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        // Удаление столбцов с выходом за границы.
        expected = createPixselMap(9, 11, left);
        actual = createPixselMap(11, 11, wide);
        try {
            actual.removeColumns(-3, 5);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        expected = createPixselMap(9, 11, right);
        actual = createPixselMap(11, 11, wide);
        try {
            actual.removeColumns(9, 5);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);
    }

    @Test
    public void testRemoveRows() {
        PixselMap expected, actual;

        // Нулевое количество строк ничего не меняет.
        expected = createPixselMap(9, 11, midle);
        actual = createPixselMap(9, 11, midle);
        try {
            actual.removeRows(4, 0);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        expected = createPixselMap(9, 11, midle);
        actual = createPixselMap(9, 11, midle);
        try {
            actual.removeRows(-2, 0);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        expected = createPixselMap(9, 11, midle);
        actual = createPixselMap(9, 11, midle);
        try {
            actual.removeRows(13, 0);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        // Отрицательное количество строк ничего не меняет.
        expected = createPixselMap(9, 11, midle);
        actual = createPixselMap(9, 11, midle);
        try {
            actual.removeRows(5, -3);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        // Удаление строк.
        expected = createPixselMap(9, 11, midle);
        actual = createPixselMap(9, 13, high);
        try {
            actual.removeRows(6, 2);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        expected = createPixselMap(9, 11, top);
        actual = createPixselMap(9, 13, high);
        try {
            actual.removeRows(0, 2);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        expected = createPixselMap(9, 11, bottom);
        actual = createPixselMap(9, 13, high);
        try {
            actual.removeRows(11, 2);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        // Удаление строк с выходом за границы.
        expected = createPixselMap(9, 11, top);
        actual = createPixselMap(9, 13, high);
        try {
            actual.removeRows(-3, 5);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        expected = createPixselMap(9, 11, bottom);
        actual = createPixselMap(9, 13, high);
        try {
            actual.removeRows(11, 5);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);
    }

    @Test
    public void testRemoveLeft() {
        PixselMap expected, actual;

        // Нулевое количество столбцов ничего не меняет.
        expected = createPixselMap(9, 11, center);
        actual = createPixselMap(9, 11, center);
        try {
            actual.removeLeft(0);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        // Отрицательное количество столбцов ничего не меняет.
        expected = createPixselMap(9, 11, center);
        actual = createPixselMap(9, 11, center);
        try {
            actual.removeLeft(-3);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        // Удаление столбцов.
        expected = createPixselMap(9, 11, left);
        actual = createPixselMap(11, 11, wide);
        try {
            actual.removeLeft(2);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);
    }

    @Test
    public void testRemoveRight() {
        PixselMap expected, actual;

        // Нулевое количество столбцов ничего не меняет.
        expected = createPixselMap(9, 11, center);
        actual = createPixselMap(9, 11, center);
        try {
            actual.removeRight(0);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        // Отрицательное количество столбцов ничего не меняет.
        expected = createPixselMap(9, 11, center);
        actual = createPixselMap(9, 11, center);
        try {
            actual.removeRight(-3);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        // Удаление столбцов.
        expected = createPixselMap(9, 11, right);
        actual = createPixselMap(11, 11, wide);
        try {
            actual.removeRight(2);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);
    }

    @Test
    public void testRemoveTop() {
        PixselMap expected, actual;

        // Нулевое количество строк ничего не меняет.
        expected = createPixselMap(9, 11, midle);
        actual = createPixselMap(9, 11, midle);
        try {
            actual.removeTop(0);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        // Отрицательное количество строк ничего не меняет.
        expected = createPixselMap(9, 11, midle);
        actual = createPixselMap(9, 11, midle);
        try {
            actual.removeTop(-3);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        // Удаление строк.
        expected = createPixselMap(9, 11, top);
        actual = createPixselMap(9, 13, high);
        try {
            actual.removeTop(2);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);
    }

    @Test
    public void testRemoveBottom() {
        PixselMap expected, actual;

        // Нулевое количество строк ничего не меняет.
        expected = createPixselMap(9, 11, midle);
        actual = createPixselMap(9, 11, midle);
        try {
            actual.removeBottom(0);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        // Отрицательное количество строк ничего не меняет.
        expected = createPixselMap(9, 11, midle);
        actual = createPixselMap(9, 11, midle);
        try {
            actual.removeBottom(-3);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        // Удаление строк.
        expected = createPixselMap(9, 11, bottom);
        actual = createPixselMap(9, 13, high);
        try {
            actual.removeBottom(2);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);
    }

    @Test
    public void testAddColumns() {
        PixselMap expected, actual;

        // Нулевое количество столбцов ничего не меняет.
        expected = createPixselMap(9, 11, center);
        actual = createPixselMap(9, 11, center);
        try {
            actual.addColumns(4, 0);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        expected = createPixselMap(9, 11, center);
        actual = createPixselMap(9, 11, center);
        try {
            actual.addColumns(-2, 0);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        expected = createPixselMap(9, 11, center);
        actual = createPixselMap(9, 11, center);
        try {
            actual.addColumns(13, 0);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        // Отрицательное количество столбцов ничего не меняет.
        expected = createPixselMap(9, 11, center);
        actual = createPixselMap(9, 11, center);
        try {
            actual.addColumns(4, -2);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        // Добавление столбцов.
        expected = createPixselMap(11, 11, centerWide);
        actual = createPixselMap(9, 11, center);
        try {
            actual.addColumns(4, 2);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        expected = createPixselMap(11, 11, leftWide);
        actual = createPixselMap(9, 11, left);
        try {
            actual.addColumns(0, 2);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        expected = createPixselMap(11, 11, rightWide);
        actual = createPixselMap(9, 11, right);
        try {
            actual.addColumns(9, 2);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        // Добавление столбцов с выходом за границы.
        expected = createPixselMap(11, 11, leftWide);
        actual = createPixselMap(9, 11, left);
        try {
            actual.addColumns(-3, 5);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        expected = createPixselMap(11, 11, rightWide);
        actual = createPixselMap(9, 11, right);
        try {
            actual.addColumns(10, 1);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);
    }

    @Test
    public void testAddRows() {
        PixselMap expected, actual;

        // Нулевое количество строк ничего не меняет.
        expected = createPixselMap(9, 11, midle);
        actual = createPixselMap(9, 11, midle);
        try {
            actual.addRows(4, 0);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        expected = createPixselMap(9, 11, midle);
        actual = createPixselMap(9, 11, midle);
        try {
            actual.addRows(-2, 0);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        expected = createPixselMap(9, 11, midle);
        actual = createPixselMap(9, 11, midle);
        try {
            actual.addRows(13, 0);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        // Нулевое количество строк ничего не меняет.
        expected = createPixselMap(9, 11, midle);
        actual = createPixselMap(9, 11, midle);
        try {
            actual.addRows(6, -2);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        // Добавление строк.
        expected = createPixselMap(9, 13, midleHigh);
        actual = createPixselMap(9, 11, midle);
        try {
            actual.addRows(6, 2);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        expected = createPixselMap(9, 13, topHigh);
        actual = createPixselMap(9, 11, top);
        try {
            actual.addRows(0, 2);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        expected = createPixselMap(9, 13, bottomHigh);
        actual = createPixselMap(9, 11, bottom);
        try {
            actual.addRows(11, 2);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        // Добавление строк с выходом за границы.
        expected = createPixselMap(9, 13, topHigh);
        actual = createPixselMap(9, 11, top);
        try {
            actual.addRows(-3, 5);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        expected = createPixselMap(9, 13, bottomHigh);
        actual = createPixselMap(9, 11, bottom);
        try {
            actual.addRows(12, 1);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);
    }

    @Test
    public void testAddLeft() {
        PixselMap expected, actual;

        // Нулевое количество столбцов ничего не меняет.
        expected = createPixselMap(9, 11, center);
        actual = createPixselMap(9, 11, center);
        try {
            actual.addLeft(0);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        // Отрицательное количество столбцов ничего не меняет.
        expected = createPixselMap(9, 11, center);
        actual = createPixselMap(9, 11, center);
        try {
            actual.addLeft(-2);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        // Добавление столбцов.
        expected = createPixselMap(11, 11, leftWide);
        actual = createPixselMap(9, 11, left);
        try {
            actual.addLeft(2);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);
    }

    @Test
    public void testAddRight() {
        PixselMap expected, actual;

        // Нулевое количество столбцов ничего не меняет.
        expected = createPixselMap(9, 11, center);
        actual = createPixselMap(9, 11, center);
        try {
            actual.addRight(0);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        // Отрицательное количество столбцов ничего не меняет.
        expected = createPixselMap(9, 11, center);
        actual = createPixselMap(9, 11, center);
        try {
            actual.addRight(-2);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        // Добавление столбцов.
        expected = createPixselMap(11, 11, rightWide);
        actual = createPixselMap(9, 11, right);
        try {
            actual.addRight(2);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);
    }

    @Test
    public void testAddTop() {
        PixselMap expected, actual;

        // Нулевое количество строк ничего не меняет.
        expected = createPixselMap(9, 11, midle);
        actual = createPixselMap(9, 11, midle);
        try {
            actual.addTop(0);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        // Нулевое количество строк ничего не меняет.
        expected = createPixselMap(9, 11, midle);
        actual = createPixselMap(9, 11, midle);
        try {
            actual.addTop(-2);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        // Добавление строк.
        expected = createPixselMap(9, 13, topHigh);
        actual = createPixselMap(9, 11, top);
        try {
            actual.addTop(2);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);
    }

    @Test
    public void testAddBottom() {
        PixselMap expected, actual;

        // Нулевое количество строк ничего не меняет.
        expected = createPixselMap(9, 11, midle);
        actual = createPixselMap(9, 11, midle);
        try {
            actual.addBottom(0);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        // Нулевое количество строк ничего не меняет.
        expected = createPixselMap(9, 11, midle);
        actual = createPixselMap(9, 11, midle);
        try {
            actual.addBottom(-2);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);

        // Добавление строк.
        expected = createPixselMap(9, 13, bottomHigh);
        actual = createPixselMap(9, 11, bottom);
        try {
            actual.addBottom(2);
        } catch (DisallowOperationException e) {
            fail("unexpected exception");
        }
        assertEquals(expected, actual);
    }

    @Test
    public void testRotate() {
        byte[] src = { 0x7, 0x2, 0x4, 0x8, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x1,
                0x2, 0x6 };
        byte[] step1 = { (byte) 0x80, 0x7, 0x20, 0x0, 0x1, 0x0, 0x0, 0x0, 0x0,
                0x20, 0x0, 0x7, 0x0 };
        byte[] step2 = { 0x3, 0x2, 0x4, 0x0, 0x0, 0x0, 0x0, 0x0, (byte) 0x80,
                0x0, 0x1, 0x2, 0x7 };
        byte[] step3 = { 0x0, 0x7, 0x20, 0x0, 0x0, 0x0, 0x0, 0x0, 0x4, 0x20,
                0x0, 0xf, 0x0 };
        PixselMap expected, actual;

        // Поворот на четверть по часовой стрелке.
        actual = createPixselMap(9, 11, src);
        expected = createPixselMap(11, 9, step1);
        actual.rotate(1);
        assertEquals(expected, actual);
        // Возврат к исходному состоянию.
        expected = createPixselMap(9, 11, src);
        actual.rotate(-1);
        assertEquals(expected, actual);
        // Проверка полного поворота.
        actual.rotate(2);
        actual.rotate(2);
        assertEquals(expected, actual);
        // Полоборота.
        expected = createPixselMap(9, 11, step2);
        actual.rotate(2);
        assertEquals(expected, actual);

        actual = createPixselMap(9, 11, src);
        expected = createPixselMap(11, 9, step3);
        actual.rotate(3);
        assertEquals(expected, actual);
        actual = createPixselMap(9, 11, src);
        actual.rotate(-1);
        assertEquals(expected, actual);
        actual = createPixselMap(9, 11, src);
        actual.rotate(7);
        assertEquals(expected, actual);
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
        byte[] src = { 0x7, 0x2, 0x4, 0x8, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x1,
                0x2, 0x6 };
        byte[] setRect = { 0x7, 0x2, 0x4, (byte) 0xe8, (byte) 0xc0,
                (byte) 0x81, 0x3, 0x7, 0x0, 0x0, 0x1, 0x2, 0x6 };
        PixselMap actual, expected;

        actual = createPixselMap(9, 11, src);
        expected = createPixselMap(9, 11, setRect);
        actual.set(2, 3, 3, 4, true);
        assertEquals(expected, actual);

        // Повторная заливка ничего не меняет.
        actual.set(2, 3, 3, 4, true);
        assertEquals(expected, actual);

        actual = expected;
        expected = createPixselMap(9, 11, src);
        actual.set(2, 3, 3, 4, false);
        assertEquals(expected, actual);

        // Повтор стирания ничего не меняет.
        actual.set(2, 3, 3, 4, false);
        assertEquals(expected, actual);
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
        PixselMap expected, actual;

        actual = createPixselMap(9, 11, over);
        expected = createPixselMap(9, 11, place);
        actual.overlay(2, 3, stamp, PixselMap.OVERLAY_PLACE);
        assertEquals(expected, actual);

        actual = createPixselMap(9, 11, over);
        expected = createPixselMap(9, 11, or);
        actual.overlay(2, 3, stamp, PixselMap.OVERLAY_OR);
        assertEquals(expected, actual);

        actual = createPixselMap(9, 11, over);
        expected = createPixselMap(9, 11, and);
        actual.overlay(2, 3, stamp, PixselMap.OVERLAY_AND);
        assertEquals(expected, actual);

        actual = createPixselMap(9, 11, over);
        expected = createPixselMap(9, 11, xor);
        actual.overlay(2, 3, stamp, PixselMap.OVERLAY_XOR);
        assertEquals(expected, actual);
    }

    @Test
    public void testPlace() {
        PixselMap expected, actual;

        actual = createPixselMap(9, 11, over);
        expected = createPixselMap(9, 11, place);
        actual.place(2, 3, stamp);
        assertEquals(expected, actual);
    }

    @Test
    public void testOr() {
        PixselMap expected, actual;

        actual = createPixselMap(9, 11, over);
        expected = createPixselMap(9, 11, or);
        actual.or(2, 3, stamp);
        assertEquals(expected, actual);
    }

    @Test
    public void testAnd() {
        PixselMap expected, actual;

        actual = createPixselMap(9, 11, over);
        expected = createPixselMap(9, 11, and);
        actual.and(2, 3, stamp);
        assertEquals(expected, actual);
    }

    @Test
    public void testHor() {
        PixselMap expected, actual;
        actual = createPixselMap(9, 11, over);
        expected = createPixselMap(9, 11, xor);
        actual.xor(2, 3, stamp);
        assertEquals(expected, actual);
    }
}
