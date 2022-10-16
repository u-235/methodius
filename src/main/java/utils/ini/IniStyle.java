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

import java.nio.charset.Charset;

public class IniStyle {
    protected String  commentStart;
    protected String  lineEnd;
    protected String  keyChars = "_";
    protected String  secChars = "/\\";
    protected Charset charset;

    public static IniStyle flexible() {
        IniStyle ret = new IniStyle();
        ret.charset = Charset.forName("utf-8");
        ret.commentStart = ";#";
        ret.lineEnd = System.getProperty("line.separator");
        return ret;
    }

    public boolean isLineEnd(int ch) {
        return ch == '\n' || ch == '\r';
    }

    public boolean isWhiteSpace(int ch) {
        return !isLineEnd(ch) && Character.isWhitespace(ch);
    }

    public boolean isCommentMark(int ch) {
        return commentStart.indexOf(ch) >= 0;
    }

    public boolean isKeyCharacter(int ch) {
        if (ch >= 'a' && ch <= 'z') return true;
        if (ch >= 'A' && ch <= 'Z') return true;
        if (ch >= '0' && ch <= '9') return true;
        return keyChars.indexOf(ch) >= 0;
    }

    public boolean isSectionCharacter(int ch) {
        if (ch >= 'a' && ch <= 'z') return true;
        if (ch >= 'A' && ch <= 'Z') return true;
        if (ch >= '0' && ch <= '9') return true;
        return secChars.indexOf(ch) >= 0;
    }

    public Charset charset() {
        // TODO Auto-generated method stub
        return charset;
    }
}
