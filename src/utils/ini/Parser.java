
package utils.ini;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Parser {
    public final static int NEW_LINE      = 0;
    public final static int BEGIN_LINE    = 1;
    public final static int COMMENT       = 2;
    public final static int SECTION_BEGIN = 3;
    public final static int SECTION_END   = 4;
    public final static int KEY_BEGIN     = 5;
    public final static int KEY_END       = 6;
    public final static int VALUE         = 7;

    public static void parse(InputStream input, Handler handler, IniStyle style)
                    throws IOException {
        if (input == null || handler == null) return;
        if (style == null) style = IniStyle.flexible();

        InputStreamReader reader = new InputStreamReader(input, style.charset());
        int line = 0, col = 0;
        int ch;
        int prev, curr = 0;
        int state = NEW_LINE;
        StringBuffer buf = new StringBuffer(256);

        while ((ch = reader.read()) != -1) {
            prev = curr;
            curr = ch;

            if (state == NEW_LINE) {
                // Очистка буфера при переходе на начало строки.
                col = 0;
                line++;
                buf.delete(0, buf.length());
                state = BEGIN_LINE;
                if (style.isLineEnd(curr) && curr != prev) continue;
            }

            col++;

            switch (state) {
            case BEGIN_LINE:
                if (style.isLineEnd(curr)) {
                    state = NEW_LINE;
                    continue;
                } else if (style.isKeyCharacter(curr)) {
                    state = KEY_BEGIN;
                    buf.appendCodePoint(curr);
                    continue;
                } else if (style.isCommentMark(curr)) {
                    state = COMMENT;
                    continue;
                } else if (curr == '[') {
                    state = SECTION_BEGIN;
                    continue;
                } else if (style.isWhiteSpace(curr)) {
                    continue;
                }
                break;
            case COMMENT:
                if (style.isLineEnd(curr)) {
                    if (buf.length() > 0) handler.comment(buf.toString());
                    else handler.comment("\n ");
                    state = NEW_LINE;
                    continue;
                } else if (Character.isDefined(curr)) {
                    buf.appendCodePoint(curr);
                    continue;
                }
                break;
            case SECTION_BEGIN:
                if (style.isSectionCharacter(curr)) {
                    buf.appendCodePoint(curr);
                    continue;
                } else if (curr == ']') {
                    handler.section(buf.toString());
                    state = SECTION_END;
                    continue;
                }
                break;
            case SECTION_END:
                if (style.isLineEnd(curr)) {
                    state = NEW_LINE;
                    continue;
                } else if (style.isWhiteSpace(curr)) {
                    continue;
                }
                break;
            case KEY_BEGIN:
                if (style.isKeyCharacter(curr)) {
                    buf.appendCodePoint(curr);
                    continue;
                } else if (style.isWhiteSpace(curr) || curr == '=') {
                    handler.key(buf.toString());
                    if (curr == '=') state = VALUE;
                    else state = KEY_END;

                    buf.delete(0, buf.length());
                    continue;
                }
                break;
            case KEY_END:
                if (curr == '=') {
                    state = VALUE;
                    continue;
                } else if (style.isWhiteSpace(curr)) {
                    continue;
                }
                break;
            case VALUE:
                if (style.isLineEnd(curr)) {
                    handler.value(buf.toString());
                    state = NEW_LINE;
                    continue;
                } else if (style.isWhiteSpace(curr)) {
                    if (buf.length() != 0) buf.appendCodePoint(curr);
                    continue;
                } else if (Character.isDefined(curr)) {
                    buf.appendCodePoint(curr);
                    continue;
                }
                break;
            default:
                throw new RuntimeException("undefined case");
            }

            handler.error(state, curr, line, col);
            state = NEW_LINE;
        }

        reader.close();
    }
}
