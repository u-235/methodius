
package utils.ini;

public class INISection extends INIElement {
    protected INISection(String name) {
        super(name, null);
    }

    @Override
    public int getType() {
        return INI_SECTION;
    }
}
