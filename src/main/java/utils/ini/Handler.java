
package utils.ini;

public interface Handler {
    public void error(int state, int ch, int line, int col);

    public void comment(String com);

    public void section(String sec);

    public void key(String key);

    public void value(String value);
}
