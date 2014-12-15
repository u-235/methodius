
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
        MSymbol sym;

        sym = createMSymbol(0, 5, 7, null);
        assertTrue(sym.getCode() == 0);

        sym = createMSymbol(17, 5, 7, null);
        assertTrue(sym.getCode() == 17);
    }

    @Test
    public void testSetCode() {
        MSymbol sym;

        sym = createMSymbol(0, 5, 7, null);
        sym.setCode(11);
        assertTrue(sym.getCode() == 11);

        sym = createMSymbol(17, 5, 7, null);
        sym.setCode(17);
        assertTrue(sym.getCode() == 17);
    }

    @Test
    public void testIsUnicode() {
        MSymbol sym;

        sym = createMSymbol(0, 5, 7, null);
        assertFalse(sym.isUnicode());

        try {
            sym.setUnicode(0x20);
        } catch (DisallowOperationException e) {
            fail("Unexpected exception");
        }
        assertTrue(sym.isUnicode());
    }

    @Test
    public void testGetUnicode() {
        MSymbol sym;

        sym = createMSymbol(0, 5, 7, null);
        assertFalse(sym.isUnicode());

        try {
            sym.setUnicode(0x20);
        } catch (DisallowOperationException e) {
            fail("Unexpected exception");
        }
        assertTrue(sym.getUnicode() == 0x20);
    }

    @Test
    public void testClearUnicode() {
        MSymbol sym;

        sym = createMSymbol(0, 5, 7, null);
        try {
            sym.setUnicode(0x20);
            sym.clearUnicode();
        } catch (DisallowOperationException e) {
            fail("Unexpected exception");
        }
        assertFalse(sym.isUnicode());
    }

    @Test
    public void testSetUnicode() {
        // Калька с testGetUnicode()
        MSymbol sym;

        sym = createMSymbol(0, 5, 7, null);
        assertFalse(sym.isUnicode());

        try {
            sym.setUnicode(0x20);
        } catch (DisallowOperationException e) {
            fail("Unexpected exception");
        }
        assertTrue(sym.getUnicode() == 0x20);
    }

    @Test
    public void testGetOwner() {
        MSymbol sym;

        sym = createMSymbol(0, 5, 7, null);
        assertTrue(sym.getOwner() == null);

        MFont font = new MFont();
        font.add(sym);
        assertTrue(sym.getOwner() == font);

        font.remove(sym);
        assertTrue(sym.getOwner() == null);
    }

    @Override
    @Test
    public void testCopy() {
        super.testCopy();
        
        MSymbol src, dst;

        src = createMSymbol(13, 9, 11, left);
        dst = createMSymbol(0, 7, 9, null);

        try {
            dst.copy(src);
        } catch (DisallowOperationException e) {
            fail("Unexpected exception");
        }
        assertEquals(src, dst);

        dst = createMSymbol(0, 7, 9, null);
        MFont font = new MFont();
        font.setFixsed(true);
        font.setWidth(9);
        font.setHeight(11);
        font.add(dst);
        boolean result = true;
        try {
            dst.copy(src);
        } catch (DisallowOperationException e) {
            result = false;
        }
        assertTrue(result);

        // Попытка скопировать символ неправильной ширины в моноширинный шрифт.
        dst = createMSymbol(0, 11, 11, null);
        font = new MFont();
        font.setFixsed(true);
        font.setWidth(11);
        font.setHeight(11);
        font.add(dst);
        result = true;
        try {
            dst.copy(src);
        } catch (DisallowOperationException e) {
            result = false;
        }
        assertFalse(result);

        // Попытка скопировать символ другой ширины в пропорциональный шрифт.
        dst = createMSymbol(0, 11, 11, null);
        font = new MFont();
        font.setFixsed(false);
        font.setWidth(11);
        font.setHeight(11);
        font.add(dst);
        result = true;
        try {
            dst.copy(src);
        } catch (DisallowOperationException e) {
            result = false;
        }
        assertTrue(result);

        // Попытка скопировать символ другой высоты.
        dst = createMSymbol(0, 9, 11, null);
        font = new MFont();
        font.setFixsed(false);
        font.setWidth(9);
        font.setHeight(13);
        font.add(dst);
        result = true;
        try {
            dst.copy(src);
        } catch (DisallowOperationException e) {
            result = false;
        }
        assertFalse(result);
    }

    @Override
    @Test
    public void testSetSizeIntInt() {
        super.testSetSizeIntInt();
        fail("Not yet implemented");
    }

    @Override
    @Test
    public void testSetSizeDimension() {
        super.testSetSizeDimension();
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
