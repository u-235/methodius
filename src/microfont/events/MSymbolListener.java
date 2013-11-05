package microfont.events;

import java.util.EventListener;

/**
 * Интерфейс получателя события при изменении символа.
 */
public interface MSymbolListener extends EventListener
{
    /**
     * Метод вызывается после изменений {@link microfont.MSymbol символа}.
     * 
     * @param change Информация об произошедших изменениях.
     */
    public void mSymbolEvent(MSymbolEvent change);
}
