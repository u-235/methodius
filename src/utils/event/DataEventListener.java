package utils.event;

/**
 * Описание интерфейса получателя событий. Интерфейс должен содержать один
 * ничего не возвращающий метод с произвольным именем и принимающий в качестве
 * параметра объект с описанием события.
 * 
 * <pre>
 * public interface MyListener extends DataEventListener
 * {
 *     public void onMyEvent(MyEvent ev);
 * }
 * </pre>
 */
public interface DataEventListener
{
}
