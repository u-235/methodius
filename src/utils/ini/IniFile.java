
package utils.ini;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import utils.SettingsFile;

public class IniFile implements SettingsFile {
    Parser parser;
    String    address;
    
    public IniFile(String addr){
        address=addr;
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

            System.out.println("error at "+line+" line in "+ col+" column, character code point = "+ ch);
        }

        @Override
        public void comment(String com) {
            System.out.println("comments : "+com);
            // TODO Auto-generated method stub
        }

        @Override
        public void section(String sec) {
            System.out.println("section  : "+sec);
            work = work.node(sec);
        }

        @Override
        public void key(String k) {
            System.out.println("key      : "+k);
            key = k;
        }

        @Override
        public void value(String value) {
            System.out.println("value    : "+value);
            work.put(key, value);
        }

    }

    @Override
    public void load(Preferences owner){
        if (parser == null) parser = new Parser();
        
        parser.setHandler(new IniHandler(owner));
    
        try {
            parser.parse(new FileInputStream(new File(address)));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void beginFlush(String path) throws BackingStoreException {
        // TODO Auto-generated method stub

    }

    @Override
    public void endFlush(String path) throws BackingStoreException {
        // TODO Auto-generated method stub

    }

    @Override
    public void flushKey(String key, String value) throws BackingStoreException {
        // TODO Auto-generated method stub

    }

    @Override
    public void beginSync(String path) throws BackingStoreException {
        // TODO Auto-generated method stub

    }

    @Override
    public void syncKey(String key, String value) throws BackingStoreException {
        // TODO Auto-generated method stub
    }
}
