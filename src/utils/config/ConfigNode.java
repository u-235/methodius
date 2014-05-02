
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
 * @see <a href="http://commons.apache.org/proper/commons-configuration/"
 *      >Apache Commons Configuration</a> - продвинутая система настроек.
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
    private boolean                         removed;

    /**
     * Создание узла.
     * 
     * @param parent Предок узла или {@code null}, если нужно создать корневой
     *            узел.
     * @param name Имя узла, не должно содержать символ наклонной черты ("/").
     *            При создании корневого узла имя игнорируется.
     * @see #isValidName(String)
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
            this.absolutePath = parent.absolutePath + "/" + name;
        }

        removed = false;
        childs = new HashMap<String, ConfigNode>();
        records = new HashMap<String, String>();
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
        synchronized (root) {
            checkRemoved();
            return parent;
        }
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
        synchronized (root) {
            checkRemoved();
            return root;
        }
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
        synchronized (root) {
            checkRemoved();
            return (String[]) childs.keySet().toArray();
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
        synchronized (root) {
            checkRemoved();
            if (path == null)
                throw new NullPointerException("Path name is null.");

            if (path.equals("")) return this;
            if (path.endsWith("/")) {
                if (path.length() == 1) return root;
                throw new IllegalArgumentException(
                                "Path name ends with a slash.");
            }

            StringTokenizer names = new StringTokenizer(path, "/", true);
            ConfigNode ret;
            if (path.startsWith("/")) {
                ret = root;
                names.nextToken();
            } else {
                ret = this;
            }

            String nn;
            while (names.hasMoreTokens()) {
                nn = names.nextToken();
                if (nn.equals("/"))
                    throw new IllegalArgumentException(
                                    "Path name contains consecutive slash.");

                if (ret.childs.containsKey(nn)) {
                    ret = ret.childs.get(nn);
                } else {
                    ret = new ConfigNode(ret, name);
                    ret.parent.fireChildAddedEvent(ret);
                }

                names.nextToken();
            }

            return ret;
        }
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
        synchronized (root) {
            checkRemoved();
            if (path == null)
                throw new NullPointerException("Path name is null.");

            if (path.equals("")) return true;
            if (path.endsWith("/")) {
                if (path.length() == 1) return true;
                throw new IllegalArgumentException(
                                "Path name ends with a slash.");
            }

            StringTokenizer names = new StringTokenizer(path, "/", true);
            ConfigNode ret;
            if (path.startsWith("/")) {
                ret = root;
                names.nextToken();
            } else {
                ret = this;
            }

            String nn;
            while (names.hasMoreTokens()) {
                nn = names.nextToken();
                if (nn.equals("/"))
                    throw new IllegalArgumentException(
                                    "Path name contains consecutive slash.");

                if (ret.childs.containsKey(nn)) {
                    ret = ret.childs.get(nn);
                } else return false;

                names.nextToken();
            }

            return true;
        }
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
            parent.childs.remove(this);
            parent.fireChildRemovedEvent(this);
            clear2();

            Iterator<ConfigNode> i = childs.values().iterator();
            while (i.hasNext()) {
                i.next().removeNode();
            }
        }
    }

    // ========================================================
    //
    // Работа с записями.
    //
    // ========================================================

    public String[] keys() {
        synchronized (root) {
            checkRemoved();
            return (String[]) records.keySet().toArray();
        }
    }

    public void remove(String key) {
        synchronized (root) {
            checkRemoved();
            checkKey(key);
            remove2(key);
        }
    }

    protected final void remove2(String key) {
        records.remove(key);
        fireConfigChangeEvent(key, null);
    }

    public void clear() {
        synchronized (root) {
            checkRemoved();
            clear2();
        }
    }

    protected final void clear2() {
        records.clear();
        for (String k : childrenNames()) {
            remove2(k);
        }
    }

    public void put(String key, String value) {
        synchronized (root) {
            checkRemoved();
            checkKey(key);
            records.put(key, value);
            fireConfigChangeEvent(key, value);
        }
    }

    public String get(String key, String def) {
        synchronized (root) {
            checkRemoved();
            checkKey(key);
            String ret = records.get(key);
            return ret == null ? def : ret;
        }
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

    // ========================================================
    //
    // Работа со получателями сообщений.
    //
    // ========================================================

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
     * @param ccl Получатель сообщений о изменении узла конфигурации.
     */
    public void addNodeChangeListener(NodeChangeListener ncl) {
        synchronized (listeners) {
            listeners.add(NodeChangeListener.class, ncl);
        }
    }

    /**
     * Удаление получателя сообщений о изменениях узла конфигурации.
     * 
     * @param ccl Получатель сообщений о изменении узла конфигурации.
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
