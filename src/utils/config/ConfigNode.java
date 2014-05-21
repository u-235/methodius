
package utils.config;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import utils.event.ListenerChain;

/**
 * Класс для представления настроек в виде дерева. В отличие от
 * {@code java.util.prefs.Preferences}, {@code ConfigNode} ориентирован на
 * хранение настроек в определённом файле и не позволяет сохранение не корневого
 * узла.
 * 
 * <p>
 * Узлы настроек именуются на манер папок иерархической файловой системы. Каждый
 * узел настроек имеет {@link #name() имя} (не обязательно уникальное),
 * уникальный {@link #absolutePath() абсолютный путь} и относительные пути к
 * {@link #childrenNames() к дочерним узлам}.
 * 
 * <p>
 * Имя {@linkplain #root() корневого узла} состоит из пустой строки (""). Любой
 * другой узел имеет произвольное имя, заданное в конструкторе. У имени узла
 * есть только одно ограничение: оно не должно содержать символ наклонной черты
 * ('/').
 * 
 * <p>
 * Корневой узел имеет абсолютный путь из наклонной черты ("/"). Абсолютный путь
 * дочерних узлов корневого узла имеют вид {@code "/" + <имя узла>}. У всех
 * остальных узлов абсолютный путь равен
 * {@code <абсолютный путь родителя> + "/" + <имя узла>}. <b>Любой абсолютный
 * путь начинается с наклонной черты</b>.
 * 
 * <p>
 * Путь узла <b>N</b> относительно предка <b>A</b> это просто строка, которая
 * должна быть добавлена к абсолютному пути <b>A</b> для получения абсолютного
 * пути <b>N</b>, у которой начальный символ наклонной черты (если есть) удалён.
 * Иными словами:
 * <ul>
 * <li>Относительный путь не может начинаться с наклонной черты.
 * <li>Для каждого узла путь относительно самого себя - это пустая строка.
 * <li>Для каждого узла путь относительно родителя - это имя этого узла (за
 * исключением корневого узла, который не имеет родителя).
 * <li>Для каждого узла путь относительно корневого узла - это абсолютный путь
 * без начального символа наклонной черты.
 * </ul>
 * <p>
 * И, наконец, заметьте, что:
 * <ul>
 * <li>Путь не может содержать несколько символов наклонной черты подряд.
 * <li>Путь не может заканчиваться на символ наклонной черты, за исключением
 * абсолютного пути корневого узла.
 * <li>Любая строка, которая удовлетворяет этим двум правилам, является
 * допустимым путём.
 * </ul>
 * 
 * @see <a href="http://commons.apache.org/proper/commons-configuration/">Apache
 *      Commons Configuration</a> - продвинутая система настроек.
 * @author Nickolay Egorov
 */
public class ConfigNode {
    protected final ConfigNode              parent;
    protected final ConfigNode              root;
    protected final String                  name;
    protected final String                  absolutePath;
    protected final Map<String, ConfigNode> childs;
    protected final Map<String, String>     records;
    protected final ListenerChain           listeners;
    private String                          nodeComment;
    protected final Map<String, String>     comments;
    private boolean                         removed;

    /**
     * Создание узла.
     * 
     * @param parent Предок узла или {@code null}, если нужно создать корневой
     *            узел.
     * @param name Имя узла, не должно содержать символ наклонной черты ("/").
     *            При создании корневого узла имя игнорируется.
     */
    protected ConfigNode(ConfigNode parent, String name) {
        if (parent == null) {
            this.parent = null;
            this.root = this;
            this.name = "";
            this.absolutePath = "/";
        } else {
            if (name == null || name.isEmpty())
                throw new IllegalArgumentException("Invalid node name =="
                                + name);
            this.parent = parent;
            this.root = parent.root;
            this.name = name;
            parent.childs.put(name, this);
            if (parent != root) absolutePath = parent.absolutePath + "/" + name;
            else absolutePath = "/" + name;
        }
        System.out.println(absolutePath);

        removed = false;
        childs = new HashMap<String, ConfigNode>();
        records = new HashMap<String, String>();
        comments = new HashMap<String, String>();
        listeners = new ListenerChain();
    }

    /**
     * Возвращает родителький узел.
     * 
     * @return Родителький узел или {@code null}, если текущий узел является
     *         {@link #root() корневым}.
     * @throws IllegalStateException Если текущий узел (или его предок) был
     *             удалён вызовом {@link #removeNode()}.
     */
    public ConfigNode parent() {
        checkRemoved();
        return parent;
    }

    /**
     * Возвращает корневой узел настроек.
     * 
     * @return Корневой узел.
     * @throws IllegalStateException Если текущий узел (или его предок) был
     *             удалён вызовом {@link #removeNode()}.
     * @see #parent()
     */
    public ConfigNode root() {
        checkRemoved();
        return root;
    }
    
    /**
     * Возвращает {@code true} если узел не содержит информации. Это значит, что
     * нет ни одной записи и комментарий узла пуст или равен {@code null}.
     * @return
     */
    public boolean isEmpty(){
        if (nodeComment != null && !nodeComment.isEmpty()) return false;
        if (records.isEmpty())  return true;
        return false;
    }

    /**
     * Возвращает имя узла.
     */
    public String name() {
        return name;
    }

    /**
     * Возвращает абсолютный путь узла.
     */
    public String absolutePath() {
        return absolutePath;
    }

    /**
     * Возвращает имена дочерних узлов. Возвращаемый массив может иметь нулевой
     * размер если дочерних узлов нет.
     * 
     * @return Имена дочерних узлов текущего узла.
     * @throws IllegalStateException Если текущий узел (или его предок) был
     *             удалён вызовом {@link #removeNode()}.
     */
    public String[] childrenNames() {
        checkRemoved();
        synchronized (root) {
            return childs.keySet().toArray(new String[childs.size()]);
        }
    }

    /**
     * Возвращает заданный узел. Если узел ещё не существует, то он и его предки
     * создаются. Допускается относительный или абсолютный путь. Относительный
     * путь (который не начинается с символа наклонной линии) прокладывается от
     * текущего узла.
     * 
     * @param path Путь к требуемому узлу.
     * @return Заданный узел.
     * @throws IllegalArgumentException Если {@code path} недопустимо (содержит
     *             несколько наклонных линий подряд или заканчивается наклонной
     *             линией и содержит более одного символа).
     * @throws NullPointerException Если {@code path} равен {@code null}.
     * @throws IllegalStateException Если текущий узел (или его предок) был
     *             удалён вызовом {@link #removeNode()}.
     */
    public ConfigNode node(String path) {
        checkRemoved();
        if (path == null) throw new NullPointerException("Path name is null.");

        if (path.equals("")) return this;
        if (path.endsWith("/")) {
            if (path.length() == 1) return root;
            throw new IllegalArgumentException("Path name ends with a slash.");
        }

        StringTokenizer tokens = new StringTokenizer(path, "/", true);
        ConfigNode ret;
        boolean slash;
        if (path.startsWith("/")) {
            ret = root;
            slash = true;
        } else {
            ret = this;
            slash = false;
        }

        String element;
        synchronized (root) {
            while (tokens.hasMoreTokens()) {
                if (slash) tokens.nextToken();
                element = tokens.nextToken();

                if (element.equals("/"))
                    throw new IllegalArgumentException(
                                    "Path name contains consecutive slash.");

                if (ret.childs.containsKey(element)) {
                    ret = ret.childs.get(element);
                } else {
                    ret = new ConfigNode(ret, element);
                    ret.parent.fireChildAddedEvent(ret);
                }

                slash = true;
            }
        }
        return ret;
    }

    /**
     * Проверяет наличие заданного узла. Допускается относительный или
     * абсолютный путь. Относительный путь (который не начинается с символа
     * наклонной линии) прокладывается от текущего узла.
     * 
     * @param path Путь к узлу.
     * @return {@code true} если узел существует.
     * @throws IllegalArgumentException Если {@code path} недопустимо (содержит
     *             несколько наклонных линий подряд или заканчивается наклонной
     *             линией и содержит более одного символа).
     * @throws NullPointerException Если {@code path} равен {@code null}.
     * @throws IllegalStateException Если текущий узел (или его предок) был
     *             удалён вызовом {@link #removeNode()}.
     */
    public boolean nodeExists(String path) {
        checkRemoved();
        if (path == null) throw new NullPointerException("Path name is null.");

        if (path.equals("")) return true;
        if (path.endsWith("/")) {
            if (path.length() == 1) return true;
            throw new IllegalArgumentException("Path name ends with a slash.");
        }

        StringTokenizer names = new StringTokenizer(path, "/", true);
        ConfigNode ret;
        boolean slash;
        if (path.startsWith("/")) {
            ret = root;
            slash = true;
        } else {
            ret = this;
            slash = false;
        }

        String nn;
        synchronized (root) {
            while (names.hasMoreTokens()) {
                if (slash) names.nextToken();
                nn = names.nextToken();

                if (nn.equals("/"))
                    throw new IllegalArgumentException(
                                    "Path name contains consecutive slash.");

                if (ret.childs.containsKey(nn)) {
                    ret = ret.childs.get(nn);
                } else return false;

                slash = true;
            }
        }
        return true;
    }

    /**
     * Удаляет узел и всех его потомков, делая недействительными любые
     * настройки, хранившиеся в этих узлах. После удаления вызов любых методов
     * узла кроме {@link #name()} или {@link #absolutePath()} приведёт к выбросу
     * исключения {@code IllegalStateException}.
     * 
     * @throws IllegalStateException Если текущий узел (или его предок) был
     *             удалён вызовом {@link #removeNode()}.
     * @throws UnsupportedOperationException Если текущий узел является
     *             корневым.
     */
    public void removeNode() {
        if (root == this)
            throw new UnsupportedOperationException("Can't remove root node");
        checkRemoved();

        synchronized (root) {
            removed = true;
            parent.childs.remove(name);
            parent.fireChildRemovedEvent(this);
            clear2();

            Iterator<ConfigNode> i = childs.values().iterator();
            while (i.hasNext()) {
                i.next().removeNode();
            }
        }
    }

    // =========================================================
    //
    // Работа с записями.
    //
    // =========================================================

    /**
     * Возвращает все ключи узла. Если узел не содержит записей, то возвращаемый
     * массив имеет нулевой размер.
     * 
     * @return Названия записей узла.
     * 
     * @throws IllegalStateException Если текущий узел (или его предок) был
     *             удалён вызовом {@link #removeNode()}.
     */
    public String[] keys() {
        checkRemoved();
        synchronized (root) {
            return records.keySet().toArray(new String[records.size()]);
        }
    }

    /**
     * Удаляет запись.
     * 
     * @param key Название удаляемой записи.
     * 
     * @throws NullPointerException Если {@code key} равен {@code null}.
     * @throws IllegalStateException Если текущий узел (или его предок) был
     *             удалён вызовом {@link #removeNode()}.
     */
    public void remove(String key) {
        checkRemoved();
        checkKey(key);
        synchronized (root) {
            if (records.containsKey(key)) remove2(key);
        }
    }

    protected final void remove2(String key) {
        records.remove(key);
        comments.remove(key);
        fireConfigChangeEvent(key, null);
    }

    /**
     * Удаляет все записи узла.
     * 
     * @throws NullPointerException Если {@code key} равен {@code null}.
     * @throws IllegalStateException Если текущий узел (или его предок) был
     *             удалён вызовом {@link #removeNode()}.
     */
    public void clear() {
        checkRemoved();
        synchronized (root) {
            clear2();
        }
    }

    protected final void clear2() {
        for (String k : childrenNames()) {
            remove2(k);
        }
    }

    /**
     * Изменение значения записи.
     * 
     * @param key Имя записи.
     * @param value Новое значение.
     * 
     * @throws NullPointerException Если {@code key} равен {@code null}.
     * @throws NullPointerException Если {@code value} равен {@code null}.
     * @throws IllegalStateException Если текущий узел (или его предок) был
     *             удалён вызовом {@link #removeNode()}.
     * @see #remove(String)
     */
    public void put(String key, String value) {
        checkRemoved();
        checkKey(key);
        if (value == null) throw new NullPointerException("Value is null.");
        synchronized (root) {
            records.put(key, value);
            fireConfigChangeEvent(key, value);
        }
    }

    /**
     * Получение значения записи.
     * 
     * @param key Имя записи.
     * @param def Значение по умолчанию, может быть {@code null}.
     * @return Значение записи или {@code def}, если узел не содержит запись с
     *         таким именем.
     * 
     * @throws NullPointerException Если {@code key} равен {@code null}.
     * @throws IllegalStateException Если текущий узел (или его предок) был
     *             удалён вызовом {@link #removeNode()}.
     */
    public String get(String key, String def) {
        checkRemoved();
        checkKey(key);
        synchronized (root) {
            String ret = records.get(key);
            return ret == null ? def : ret;
        }
    }

    public void putByte(String key, byte value) {
        put(key, Byte.toString(value));
    }

    public byte getByte(String key, byte def) {
        byte ret = def;
        try {
            String rec = get(key, null);
            if (rec != null) ret = Byte.parseByte(rec);
        } catch (NumberFormatException e) {
        }

        return ret;
    }

    public void putShort(String key, short value) {
        put(key, Short.toString(value));
    }

    public short getShort(String key, short def) {
        short ret = def;
        try {
            String rec = get(key, null);
            if (rec != null) ret = Short.parseShort(rec);
        } catch (NumberFormatException e) {
        }

        return ret;
    }

    public void putInt(String key, int value) {
        put(key, Integer.toString(value));
    }

    public int getInt(String key, int def) {
        int ret = def;
        try {
            String rec = get(key, null);
            if (rec != null) ret = Integer.parseInt(rec);
        } catch (NumberFormatException e) {
        }

        return ret;
    }

    public void putLong(String key, long value) {
        put(key, Long.toString(value));
    }

    public long getLong(String key, long def) {
        long ret = def;
        try {
            String rec = get(key, null);
            if (rec != null) ret = Long.parseLong(rec);
        } catch (NumberFormatException e) {
        }

        return ret;
    }

    public void putFloat(String key, float value) {
        put(key, Float.toString(value));
    }

    public float getFloat(String key, float def) {
        float ret = def;
        try {
            String rec = get(key, null);
            if (rec != null) ret = Float.parseFloat(rec);
        } catch (NumberFormatException e) {
        }

        return ret;
    }

    public void putDouble(String key, double value) {
        put(key, Double.toString(value));
    }

    public double getDouble(String key, double def) {
        double ret = def;
        try {
            String rec = get(key, null);
            if (rec != null) ret = Double.parseDouble(rec);
        } catch (NumberFormatException e) {
        }

        return ret;
    }

    public void putBoolean(String key, boolean value) {
        put(key, Boolean.toString(value));
    }

    public boolean getBoolean(String key, boolean def) {
        boolean ret = def;
        try {
            String rec = get(key, null);
            if (rec != null) ret = Boolean.parseBoolean(rec);
        } catch (NumberFormatException e) {
        }

        return ret;
    }

    public void putByteArray(String key, byte[] value) {
        StringBuilder sb = new StringBuilder();
        for (byte b : value) {
            sb.append(Byte.toString(b));
            sb.append(' ');
        }
        put(key, sb.toString());
    }

    public byte[] getByteArray(String key, byte[] def) {
        byte[] ret = def;
        try {
            String rec = get(key, null);
            if (rec != null) {
                StringTokenizer st = new StringTokenizer(rec, " ", false);
                ret = new byte[st.countTokens()];
                for (int i = 0; st.hasMoreTokens(); i++) {
                    ret[i] = Byte.parseByte(st.nextToken());
                }
            }
        } catch (NumberFormatException e) {
        }

        return ret;
    }

    /**
     * Проверка был ли удалён текущий узел.
     * 
     * @throws IllegalStateException Если текущий узел (или его предок) был
     *             удалён вызовом {@link #removeNode()}.
     */
    protected final void checkRemoved() {
        if (removed)
            throw new IllegalStateException("This node has been removed");
    }

    protected final static void checkKey(String key) {
        if (key == null) throw new NullPointerException("Key is null");
    }

    // ==========================================================
    //
    // Работа с комментариями.
    //
    // ==========================================================

    /**
     * Изменение комментария узла.
     * 
     * @param comm Новый комментарий, может быть {@code null}.
     * @throws IllegalStateException Если текущий узел (или его предок) был
     *             удалён вызовом {@link #removeNode()}.
     */
    public void putComment(String comm) {
        checkRemoved();
        synchronized (root) {
            nodeComment = comm;
        }
    }

    /**
     * Получение комментария узла.
     * 
     * @return Комментарий узла. Может быть {@code null}.
     * @throws IllegalStateException Если текущий узел (или его предок) был
     *             удалён вызовом {@link #removeNode()}.
     */
    public String getComment() {
        checkRemoved();
        synchronized (root) {
            return nodeComment;
        }
    }

    /**
     * Удаление комментария узла.
     * 
     * @throws IllegalStateException Если текущий узел (или его предок) был
     *             удалён вызовом {@link #removeNode()}.
     */
    public void removeComment() {
        checkRemoved();
        synchronized (root) {
            nodeComment = null;
        }
    }

    /**
     * Изменение комментария записи.
     * 
     * @param key Имя записи.
     * @param comm Комментарий записи. Может быть {@code null}.
     * @throws IllegalStateException Если текущий узел (или его предок) был
     *             удалён вызовом {@link #removeNode()}.
     */
    public void putComment(String key, String comm) {
        checkRemoved();
        checkKey(key);
        synchronized (root) {
            if (!records.containsKey(key)) return;
            comments.put(key, comm);
        }
    }

    /**
     * Получение комментария записи.
     * 
     * @param key Имя записи.
     * @return Комментарий записи. Может быть {@code null}.
     * @throws IllegalStateException Если текущий узел (или его предок) был
     *             удалён вызовом {@link #removeNode()}.
     */
    public String getComment(String key) {
        checkRemoved();
        checkKey(key);
        synchronized (root) {
            return comments.get(key);
        }
    }

    /**
     * 
     * @param key
     * @throws IllegalStateException Если текущий узел (или его предок) был
     *             удалён вызовом {@link #removeNode()}.
     */
    public void removeComment(String key) {
        checkRemoved();
        checkKey(key);
        synchronized (root) {
            comments.remove(key);
        }
    }

    // =========================================================
    //
    // Работа со получателями сообщений.
    //
    // =========================================================

    /**
     * Добавление получателя сообщений о изменении записей.
     * 
     * @param ccl Получатель сообщений о изменении записей узла конфигурации.
     */
    public void addConfigChangeListener(ConfigChangeListener ccl) {
        synchronized (listeners) {
            listeners.add(ConfigChangeListener.class, ccl);
        }
    }

    /**
     * Удаление получателя сообщений о изменении записей.
     * 
     * @param ccl Получатель сообщений о изменении записей узла конфигурации.
     */
    public void removeConfigChangeListener(ConfigChangeListener ccl) {
        synchronized (listeners) {
            listeners.remove(ConfigChangeListener.class, ccl);
        }
    }

    /**
     * Добавление получателя сообщений о изменениях узла конфигурации.
     * 
     * @param ncl Получатель сообщений о изменении узла конфигурации.
     */
    public void addNodeChangeListener(NodeChangeListener ncl) {
        synchronized (listeners) {
            listeners.add(NodeChangeListener.class, ncl);
        }
    }

    /**
     * Удаление получателя сообщений о изменениях узла конфигурации.
     * 
     * @param ncl Получатель сообщений о изменении узла конфигурации.
     */
    public void removeNodeChangeListener(NodeChangeListener ncl) {
        synchronized (listeners) {
            listeners.remove(NodeChangeListener.class, ncl);
        }
    }

    protected void fireConfigChangeEvent(String key, String value) {
        synchronized (listeners) {
            ConfigChangeEvent event = new ConfigChangeEvent(this, key, value);
            Object[] listenerArray = listeners.getListenerList();

            for (int i = 0; i < listenerArray.length; i += 2) {
                if (listenerArray[i] == ConfigChangeListener.class)
                    ((ConfigChangeListener) listenerArray[i + 1])
                                    .configChange(event);
            }
        }
    }

    protected void fireChildAddedEvent(ConfigNode child) {
        synchronized (listeners) {
            NodeChangeEvent event = new NodeChangeEvent(this, child);
            Object[] listenerArray = listeners.getListenerList();

            for (int i = 0; i < listenerArray.length; i += 2) {
                if (listenerArray[i] == NodeChangeListener.class)
                    ((NodeChangeListener) listenerArray[i + 1])
                                    .childAdded(event);
            }
        }
    }

    protected void fireChildRemovedEvent(ConfigNode child) {
        synchronized (listeners) {
            NodeChangeEvent event = new NodeChangeEvent(this, child);
            Object[] listenerArray = listeners.getListenerList();

            for (int i = 0; i < listenerArray.length; i += 2) {
                if (listenerArray[i] == NodeChangeListener.class)
                    ((NodeChangeListener) listenerArray[i + 1])
                                    .childRemoved(event);
            }
        }
    }
}
