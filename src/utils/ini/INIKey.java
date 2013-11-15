
package utils.ini;

public class INIKey extends INIElement {
    protected INIKey(String name, String value) {
        super(name, value);
    }

    @Override
    public int getType() {
        return INI_KEY;
    }
}
