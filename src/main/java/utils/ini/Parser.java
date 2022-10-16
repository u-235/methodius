/*
 * Copyright 2013-2022 © Nick Egorrov, nicegorov@yandex.ru.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package utils.ini;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Parser implements Closeable {
    public final static int NEW_LINE      = 0;
    public final static int BEGIN_LINE    = 1;
    public final static int COMMENT       = 2;
    public final static int SECTION_BEGIN = 3;
    public final static int SECTION_END   = 4;
    public final static int KEY_BEGIN     = 5;
    public final static int KEY_END       = 6;
    public final static int VALUE         = 7;
    public final static int ESCAPE        = 8;
    private BufferedReader  reader;
    IniStyle                style;

    public Parser(InputStream in, IniStyle style) {
        if (style == null) this.style = IniStyle.flexible();
        else this.style = style;

        reader = new BufferedReader(new InputStreamReader(in,
                        this.style.charset));
    }

    public Parser(InputStream in) {
        this(in, null);
    }

    @Override
    public void close() throws IOException {
        if (reader == null) return;
        reader.close();
        reader = null;
    }

    public void parse(Handler handler) throws IOException, InterruptedException {
        if (handler == null) return;

        int line = 0, col = 0;
        int ch;
        int prev, curr = 0;
        int state = NEW_LINE;
        StringBuffer buf = new StringBuffer(256);

        while ((ch = reader.read()) != -1) {
            prev = curr;
            curr = ch;

            if (Thread.interrupted()) {
                Thread.currentThread().interrupt();
                throw new InterruptedException();
            }

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
                } else if (curr == '\\') {
                    state = ESCAPE;
                    continue;
                } else if (style.isWhiteSpace(curr)) {
                    if (buf.length() != 0) buf.appendCodePoint(curr);
                    continue;
                } else if (Character.isDefined(curr)) {
                    buf.appendCodePoint(curr);
                    continue;
                }
                break;
            case ESCAPE:
                state = VALUE;
                if (curr == '\\') {
                    buf.append('\\');
                    continue;
                } else if (curr == 's') {
                    buf.append(' ');
                    continue;
                } else if (curr == 't') {
                    buf.append('\t');
                    continue;
                } else if (curr == 'r') {
                    buf.append('\r');
                    continue;
                } else if (curr == 'n') {
                    buf.append('\n');
                    continue;
                }
                break;
            default:
                throw new RuntimeException("undefined case");
            }

            handler.error(state, curr, line, col);
            state = NEW_LINE;
        }
    }
}
