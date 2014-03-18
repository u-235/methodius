
package utils.ini;

public interface Handler {
    public void error(int line, int col, int ch);

    public void comment(String com);

    public void section(String sec);

    public void key(String key);

    public void value(String value);
}
