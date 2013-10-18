package microfont.events;

/**
 * Исключение, показывающее, что произошла попытка выполнить действие, которое
 * запрещено текущей конфигурацией.
 * <p>
 * Например, можно изменить любой размер символа, если он не принадлежит шрифту.
 * Однако, если символ принадлежит шрифту, то попытка изменить высоту символа
 * выбросит это исключение.
 */
public class DisallowOperationException extends Exception
{
    private static final long serialVersionUID = 5920575077510951839L;

    /**
     * Создаёт исключение с пустым сообщением. Метод {@link #getMessage()}
     * вернёт <code>null</code>.
     */
    public DisallowOperationException() {
        super();
    }

    /**
     * Создаёт исключение с описанием <code>message</code>.
     */
    public DisallowOperationException(String message) {
        super(message);
    }
}
