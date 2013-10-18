package utils.ini;

/**
 * Файл <code>*.ini</code> является текстовым файлом с кодировкой по умолчанию
 * utf-8. Логическая структура состоит из ключей, секций и комментариев.
 * <dl>
 * <dt>Секция
 * <dd>Секция начинается со строки, начинающейся с символа <code>[</code>.
 * Последующие символы до символа <code>]</code> трактуются как
 * <code>имя секции</code>. <code>Имя секции</code> может содержать только
 * символы из предопределённого набора.
 * </dl>
 * 
 * @author Николай
 * 
 */
public class INIFile extends INIElement
{
    protected INIFile() {
        super(null, null);
    }

    @Override
    public int getType() {
        return INI_ROOT;
    }
}
