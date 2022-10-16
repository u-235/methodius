
package microfont;

import static org.junit.Assert.*;
import java.awt.Dimension;
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

        MSymbol tst;

        tst = createMSymbol(0, 7, 9, null);

        // Попытка изменить ширину символа в моноширинном шрифте.
        tst = createMSymbol(0, 11, 11, null);
        MFont font = new MFont();
        font.setFixsed(true);
        font.setWidth(11);
        font.setHeight(11);
        font.add(tst);
        boolean result = true;
        try {
            tst.setSize(9, 11);
        } catch (DisallowOperationException e) {
            result = false;
        }
        assertFalse(result);

        // Попытка изменить ширину символа в пропорциональном шрифте.
        tst = createMSymbol(0, 11, 11, null);
        font = new MFont();
        font.setFixsed(false);
        font.setWidth(11);
        font.setHeight(11);
        font.add(tst);
        result = true;
        try {
            tst.setSize(9, 11);
        } catch (DisallowOperationException e) {
            result = false;
        }
        assertTrue(result);

        // Попытка изменить высоту символа.
        tst = createMSymbol(0, 9, 11, null);
        font = new MFont();
        font.setFixsed(false);
        font.setWidth(9);
        font.setHeight(13);
        font.add(tst);
        result = true;
        try {
            tst.setSize(9, 11);
        } catch (DisallowOperationException e) {
            result = false;
        }
        assertFalse(result);
    }

    @Override
    @Test
    public void testSetSizeDimension() {
        super.testSetSizeDimension();

        MSymbol tst;

        tst = createMSymbol(0, 7, 9, null);

        // Попытка изменить ширину символа в моноширинном шрифте.
        tst = createMSymbol(0, 11, 11, null);
        MFont font = new MFont();
        font.setFixsed(true);
        font.setWidth(11);
        font.setHeight(11);
        font.add(tst);
        boolean result = true;
        try {
            tst.setSize(new Dimension(9, 11));
        } catch (DisallowOperationException e) {
            result = false;
        }
        assertFalse(result);

        // Попытка изменить ширину символа в пропорциональном шрифте.
        tst = createMSymbol(0, 11, 11, null);
        font = new MFont();
        font.setFixsed(false);
        font.setWidth(11);
        font.setHeight(11);
        font.add(tst);
        result = true;
        try {
            tst.setSize(new Dimension(9, 11));
        } catch (DisallowOperationException e) {
            result = false;
        }
        assertTrue(result);

        // Попытка изменить высоту символа.
        tst = createMSymbol(0, 9, 11, null);
        font = new MFont();
        font.setFixsed(false);
        font.setWidth(9);
        font.setHeight(13);
        font.add(tst);
        result = true;
        try {
            tst.setSize(new Dimension(9, 11));
        } catch (DisallowOperationException e) {
            result = false;
        }
        assertFalse(result);
    }

    @Override
    @Test
    public void testSetWidth() {
        super.testSetWidth();

        MSymbol tst;

        tst = createMSymbol(0, 7, 9, null);

        // Попытка изменить ширину символа в моноширинном шрифте.
        tst = createMSymbol(0, 11, 11, null);
        MFont font = new MFont();
        font.setFixsed(true);
        font.setWidth(11);
        font.setHeight(11);
        font.add(tst);
        boolean result = true;
        try {
            tst.setWidth(9);
        } catch (DisallowOperationException e) {
            result = false;
        }
        assertFalse(result);

        // Попытка изменить ширину символа в пропорциональном шрифте.
        tst = createMSymbol(0, 11, 11, null);
        font = new MFont();
        font.setFixsed(false);
        font.setWidth(11);
        font.setHeight(11);
        font.add(tst);
        result = true;
        try {
            tst.setWidth(9);
        } catch (DisallowOperationException e) {
            result = false;
        }
        assertTrue(result);
    }

    @Override
    @Test
    public void testSetHeight() {
        super.testSetHeight();

        MSymbol tst;

        tst = createMSymbol(0, 7, 9, null);

        // Попытка изменить высоту символа.
        tst = createMSymbol(0, 9, 11, null);
        MFont font = new MFont();
        font.setFixsed(false);
        font.setWidth(9);
        font.setHeight(13);
        font.add(tst);
        boolean result = true;
        try {
            tst.setHeight(11);
        } catch (DisallowOperationException e) {
            result = false;
        }
        assertFalse(result);
    }

    @Override
    @Test
    public void testChangeWidth() {
        super.testChangeWidth();

        MSymbol tst;

        tst = createMSymbol(0, 7, 9, null);

        // Попытка изменить ширину символа в моноширинном шрифте.
        tst = createMSymbol(0, 11, 11, null);
        MFont font = new MFont();
        font.setFixsed(true);
        font.setWidth(11);
        font.setHeight(11);
        font.add(tst);
        boolean result = true;
        try {
            tst.changeWidth(2, 3);
        } catch (DisallowOperationException e) {
            result = false;
        }
        assertFalse(result);

        // Попытка изменить ширину символа в пропорциональном шрифте.
        tst = createMSymbol(0, 11, 11, null);
        font = new MFont();
        font.setFixsed(false);
        font.setWidth(11);
        font.setHeight(11);
        font.add(tst);
        result = true;
        try {
            tst.changeWidth(2, 3);
        } catch (DisallowOperationException e) {
            result = false;
        }
        assertTrue(result);
    }

    @Override
    @Test
    public void testChangeHeight() {
        super.testChangeHeight();

        MSymbol tst;

        tst = createMSymbol(0, 7, 9, null);

        // Попытка изменить высоту символа.
        tst = createMSymbol(0, 9, 11, null);
        MFont font = new MFont();
        font.setFixsed(false);
        font.setWidth(9);
        font.setHeight(13);
        font.add(tst);
        boolean result = true;
        try {
            tst.changeHeight(3, 5);
        } catch (DisallowOperationException e) {
            result = false;
        }
        assertFalse(result);
    }

    @Override
    @Test
    public void testRemoveColumns() {
        super.testRemoveColumns();

        MSymbol tst;

        tst = createMSymbol(0, 7, 9, null);

        // Попытка изменить ширину символа в моноширинном шрифте.
        tst = createMSymbol(0, 11, 11, null);
        MFont font = new MFont();
        font.setFixsed(true);
        font.setWidth(11);
        font.setHeight(11);
        font.add(tst);
        boolean result = true;
        try {
            tst.removeColumns(2, 3);
        } catch (DisallowOperationException e) {
            result = false;
        }
        assertFalse(result);

        // Попытка изменить ширину символа в пропорциональном шрифте.
        tst = createMSymbol(0, 11, 11, null);
        font = new MFont();
        font.setFixsed(false);
        font.setWidth(11);
        font.setHeight(11);
        font.add(tst);
        result = true;
        try {
            tst.removeColumns(2, 3);
        } catch (DisallowOperationException e) {
            result = false;
        }
        assertTrue(result);
    }

    @Override
    @Test
    public void testRemoveRows() {
        super.testRemoveRows();

        MSymbol tst;

        tst = createMSymbol(0, 7, 9, null);

        // Попытка изменить высоту символа.
        tst = createMSymbol(0, 9, 11, null);
        MFont font = new MFont();
        font.setFixsed(false);
        font.setWidth(9);
        font.setHeight(13);
        font.add(tst);
        boolean result = true;
        try {
            tst.removeRows(3, 5);
        } catch (DisallowOperationException e) {
            result = false;
        }
        assertFalse(result);
    }

    @Override
    @Test
    public void testRemoveLeft() {
        super.testRemoveLeft();

        MSymbol tst;

        tst = createMSymbol(0, 7, 9, null);

        // Попытка изменить ширину символа в моноширинном шрифте.
        tst = createMSymbol(0, 11, 11, null);
        MFont font = new MFont();
        font.setFixsed(true);
        font.setWidth(11);
        font.setHeight(11);
        font.add(tst);
        boolean result = true;
        try {
            tst.removeLeft(2);
        } catch (DisallowOperationException e) {
            result = false;
        }
        assertFalse(result);

        // Попытка изменить ширину символа в пропорциональном шрифте.
        tst = createMSymbol(0, 11, 11, null);
        font = new MFont();
        font.setFixsed(false);
        font.setWidth(11);
        font.setHeight(11);
        font.add(tst);
        result = true;
        try {
            tst.removeLeft(2);
        } catch (DisallowOperationException e) {
            result = false;
        }
        assertTrue(result);
    }

    @Override
    @Test
    public void testRemoveRight() {
        super.testRemoveRight();

        MSymbol tst;

        tst = createMSymbol(0, 7, 9, null);

        // Попытка изменить ширину символа в моноширинном шрифте.
        tst = createMSymbol(0, 11, 11, null);
        MFont font = new MFont();
        font.setFixsed(true);
        font.setWidth(11);
        font.setHeight(11);
        font.add(tst);
        boolean result = true;
        try {
            tst.removeRight(2);
        } catch (DisallowOperationException e) {
            result = false;
        }
        assertFalse(result);

        // Попытка изменить ширину символа в пропорциональном шрифте.
        tst = createMSymbol(0, 11, 11, null);
        font = new MFont();
        font.setFixsed(false);
        font.setWidth(11);
        font.setHeight(11);
        font.add(tst);
        result = true;
        try {
            tst.removeRight(2);
        } catch (DisallowOperationException e) {
            result = false;
        }
        assertTrue(result);
    }

    @Override
    @Test
    public void testRemoveTop() {
        super.testRemoveTop();

        MSymbol tst;

        tst = createMSymbol(0, 7, 9, null);

        // Попытка изменить высоту символа.
        tst = createMSymbol(0, 9, 11, null);
        MFont font = new MFont();
        font.setFixsed(false);
        font.setWidth(9);
        font.setHeight(13);
        font.add(tst);
        boolean result = true;
        try {
            tst.removeTop(5);
        } catch (DisallowOperationException e) {
            result = false;
        }
        assertFalse(result);
    }

    @Override
    @Test
    public void testRemoveBottom() {
        super.testRemoveBottom();

        MSymbol tst;

        tst = createMSymbol(0, 7, 9, null);

        // Попытка изменить высоту символа.
        tst = createMSymbol(0, 9, 11, null);
        MFont font = new MFont();
        font.setFixsed(false);
        font.setWidth(9);
        font.setHeight(13);
        font.add(tst);
        boolean result = true;
        try {
            tst.removeBottom(3);
        } catch (DisallowOperationException e) {
            result = false;
        }
        assertFalse(result);
    }

    @Override
    @Test
    public void testAddColumns() {
        super.testAddColumns();

        MSymbol tst;

        tst = createMSymbol(0, 7, 9, null);

        // Попытка изменить ширину символа в моноширинном шрифте.
        tst = createMSymbol(0, 11, 11, null);
        MFont font = new MFont();
        font.setFixsed(true);
        font.setWidth(11);
        font.setHeight(11);
        font.add(tst);
        boolean result = true;
        try {
            tst.addColumns(2, 3);
        } catch (DisallowOperationException e) {
            result = false;
        }
        assertFalse(result);

        // Попытка изменить ширину символа в пропорциональном шрифте.
        tst = createMSymbol(0, 11, 11, null);
        font = new MFont();
        font.setFixsed(false);
        font.setWidth(11);
        font.setHeight(11);
        font.add(tst);
        result = true;
        try {
            tst.addColumns(2, 3);
        } catch (DisallowOperationException e) {
            result = false;
        }
        assertTrue(result);
    }

    @Override
    @Test
    public void testAddRows() {
        super.testAddRows();

        MSymbol tst;

        tst = createMSymbol(0, 7, 9, null);

        // Попытка изменить высоту символа.
        tst = createMSymbol(0, 9, 11, null);
        MFont font = new MFont();
        font.setFixsed(false);
        font.setWidth(9);
        font.setHeight(13);
        font.add(tst);
        boolean result = true;
        try {
            tst.addRows(3, 5);
        } catch (DisallowOperationException e) {
            result = false;
        }
        assertFalse(result);
    }

    @Override
    @Test
    public void testAddLeft() {
        super.testAddLeft();

        MSymbol tst;

        tst = createMSymbol(0, 7, 9, null);

        // Попытка изменить ширину символа в моноширинном шрифте.
        tst = createMSymbol(0, 11, 11, null);
        MFont font = new MFont();
        font.setFixsed(true);
        font.setWidth(11);
        font.setHeight(11);
        font.add(tst);
        boolean result = true;
        try {
            tst.addLeft(2);
        } catch (DisallowOperationException e) {
            result = false;
        }
        assertFalse(result);

        // Попытка изменить ширину символа в пропорциональном шрифте.
        tst = createMSymbol(0, 11, 11, null);
        font = new MFont();
        font.setFixsed(false);
        font.setWidth(11);
        font.setHeight(11);
        font.add(tst);
        result = true;
        try {
            tst.addLeft(2);
        } catch (DisallowOperationException e) {
            result = false;
        }
        assertTrue(result);
    }

    @Override
    @Test
    public void testAddRight() {
        super.testAddRight();

        MSymbol tst;

        tst = createMSymbol(0, 7, 9, null);

        // Попытка изменить ширину символа в моноширинном шрифте.
        tst = createMSymbol(0, 11, 11, null);
        MFont font = new MFont();
        font.setFixsed(true);
        font.setWidth(11);
        font.setHeight(11);
        font.add(tst);
        boolean result = true;
        try {
            tst.addRight(2);
        } catch (DisallowOperationException e) {
            result = false;
        }
        assertFalse(result);

        // Попытка изменить ширину символа в пропорциональном шрифте.
        tst = createMSymbol(0, 11, 11, null);
        font = new MFont();
        font.setFixsed(false);
        font.setWidth(11);
        font.setHeight(11);
        font.add(tst);
        result = true;
        try {
            tst.addRight(2);
        } catch (DisallowOperationException e) {
            result = false;
        }
        assertTrue(result);
    }

    @Override
    @Test
    public void testAddTop() {
        super.testAddTop();

        MSymbol tst;

        tst = createMSymbol(0, 7, 9, null);

        // Попытка изменить высоту символа.
        tst = createMSymbol(0, 9, 11, null);
        MFont font = new MFont();
        font.setFixsed(false);
        font.setWidth(9);
        font.setHeight(13);
        font.add(tst);
        boolean result = true;
        try {
            tst.addTop(3);
        } catch (DisallowOperationException e) {
            result = false;
        }
        assertFalse(result);
    }

    @Override
    @Test
    public void testAddBottom() {
        super.testAddBottom();

        MSymbol tst;

        tst = createMSymbol(0, 7, 9, null);

        // Попытка изменить высоту символа.
        tst = createMSymbol(0, 9, 11, null);
        MFont font = new MFont();
        font.setFixsed(false);
        font.setWidth(9);
        font.setHeight(13);
        font.add(tst);
        boolean result = true;
        try {
            tst.addBottom(5);
        } catch (DisallowOperationException e) {
            result = false;
        }
        assertFalse(result);
    }

    @Override
    @Test
    public void testRotate() {
        super.testRotate();
        fail("Not yet implemented");
    }

}
