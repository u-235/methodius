
package utils.ini;

import java.nio.charset.Charset;

public class IniStyle {
    protected String commentStart;
    protected String delimeter;
    protected String lineEnd;
    public Charset charset;

    public static IniStyle flexible() {
        IniStyle ret = new IniStyle();
        ret.charset= Charset.forName("utf-8");
        ret.commentStart = "#;";
        ret.lineEnd = System.getProperty("line.separator");
        return ret;
    }

    public boolean isCommentMark(int ch) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isKeyCharacter(int ch) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isSectionCharacter(int ch) {
        // TODO Auto-generated method stub
        return false;
    }
}
