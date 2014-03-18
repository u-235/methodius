
package utils.ini;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Parser {
    final static int BEGIN_LINE    = 0;
    final static int COMMENT       = 1;
    final static int SECTION_BEGIN = 2;
    final static int SECTION_END   = 3;
    final static int KEY_BEGIN     = 4;
    final static int KEY_END       = 5;
    final static int VALUE         = 6;
    IniStyle         style;
    Handler          handler;

    public void parse(InputStream input) {
        if (input == null || handler == null) return;
        if (style == null) style = IniStyle.flexible();

        InputStreamReader reader = new InputStreamReader(input, style.charset);
        int line = 1, col = 0;
        int ch;
        int state = BEGIN_LINE;
        StringBuffer buf = new StringBuffer(256);

        try {
            while ((ch = reader.read()) != -1) {
                col++;
                switch (state) {
                case COMMENT:
                    if (Character.isDefined(ch)) {
                        buf.appendCodePoint(ch);
                        continue;
                    }
                    break;
                case SECTION_BEGIN:
                    if (style.isSectionCharacter(ch)) {
                        buf.appendCodePoint(ch);
                        continue;
                    } else if (ch == ']') {
                        handler.section(buf.toString());
                        state = SECTION_END;
                        buf = new StringBuffer(256);
                        continue;
                    }
                    break;
                case SECTION_END:
                    continue;
                case KEY_BEGIN:
                    if (style.isKeyCharacter(ch)) {
                        buf.appendCodePoint(ch);
                        continue;
                    } else if (Character.isWhitespace(ch) || ch == '=') {
                        handler.key(buf.toString());
                        if (ch == '=') state = VALUE;
                        else state = KEY_END;
                        buf = new StringBuffer(256);
                        continue;
                    }
                    break;
                default:// i.e. == BEGIN_LINE
                    if (style.isKeyCharacter(ch)) {
                        state = KEY_BEGIN;
                        buf.appendCodePoint(ch);
                        continue;
                    } else if (ch == '[') {
                        state = SECTION_BEGIN;
                        continue;
                    } else if (style.isCommentMark(ch)) {
                        state = COMMENT;
                        continue;
                    } else if (Character.isWhitespace(ch)) {
                        continue;
                    }
                }
                handler.error(line, col, ch);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            reader.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
