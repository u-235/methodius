package utils.ini;

public abstract class INIElement
{
    public final static int INI_ROOT     = 1;
    public final static int INI_KEY      = 2;
    public final static int INI_SECTION  = 3;

    private String          value;
    private String          name;
    private String          comment;
    private INIElement      next         = null;
    private INIElement      prev         = null;
    private INIKey          firstKey     = null;
    private INISection      firstSection = null;

    protected INIElement(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public abstract int getType();

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getValue() {
        if (this.getType() == INI_ROOT || this.getType() == INI_SECTION)
            return "";
        return value;
    }

    public void setValue(String value) {
        if (this.getType() == INI_ROOT || this.getType() == INI_SECTION)
            return;
        this.value = value;
    }

    public String getName() {
        if (this.getType() == INI_ROOT) return "";
        return name;
    }

    public void setName(String name) {
        if (this.getType() == INI_ROOT) return;
        this.name = name;
    }

    public INIElement getNext() {
        return next;
    }

    public INIElement getPrevious() {
        return prev;
    }

    public INIKey getKey(String key) {
        INIKey ret = firstKey;

        if (this.getType() == INI_KEY) return null;

        if (key != null) {
            while (ret != null) {
                if (key.equals(ret.getName())) break;
                ret = (INIKey) ret.getNext();
            }
        }

        return ret;
    }

    public String getKeyValue(String key) {
        INIKey rKey = getKey(key);

        if (rKey == null) return null;

        return rKey.getValue();
    }

    public INISection getSection(String section) {
        INISection ret = firstSection;

        if (this.getType() == INI_KEY) return null;

        if (section != null) {
            while (ret != null) {
                if (section.equals(ret.getName())) break;
                ret = (INISection) ret.getNext();
            }
        }

        return ret;
    }
}
