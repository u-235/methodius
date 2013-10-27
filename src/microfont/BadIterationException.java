package microfont;

/**
 * Исключение при попытке получения (или изменения) следующего пикселя через
 * {@linkplain AbstractPixselMap.PixselIterator итератор}, который уже достиг
 * конца итераций.
 * 
 */
public class BadIterationException extends RuntimeException
{
    private static final long serialVersionUID = -254870756168468736L;

    /**
     * Создаёт исключение с пустым сообщением. Метод {@link #getMessage()}
     * вернёт <code>null</code>.
     */
    public BadIterationException() {
        super();
    }

    /**
     * Создаёт исключение с описанием <code>message</code>.
     */
    public BadIterationException(String message) {
        super(message);
    }
}
