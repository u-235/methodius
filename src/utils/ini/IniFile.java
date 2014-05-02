
package utils.ini;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class IniFile {
    Parser         parser;
    String         address;
    BufferedWriter out;
    int flushCount;
    
    public IniFile(String addr) {
        address = addr;
    }

    class IniHandler implements Handler {
        Preferences work;
        String      key;

        IniHandler(Preferences root) {
            work = root;
        }

        @Override
        public void error(int state, int ch, int line, int col) {
            // TODO Auto-generated method stub

            System.out.println("error at " + line + " line in " + col
                            + " column, character code point = " + ch);
        }

        @Override
        public void comment(String com) {
            System.out.println("comments : " + com);
            // TODO Auto-generated method stub
        }

        @Override
        public void section(String sec) {
            System.out.println("section  : " + sec);
            work = work.node(sec);
        }

        @Override
        public void key(String k) {
            System.out.println("key      : " + k);
            key = k;
        }

        @Override
        public void value(String value) {
            System.out.println("value    : " + value);
            work.put(key, value);
        }

    }
}
