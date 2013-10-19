package microfont.events;

import utils.event.DataEventListener;

/**
 * Интерфейс получателя события при изменении символа.
 */
public interface MSymbolListener extends DataEventListener
{
    /**
     * Метод вызывается после изменений {@link microfont.MSymbol символа}.
     * 
     * @param change Информация об произошедших изменениях.
     */
    public void mSymbolEvent(MSymbolEvent change);
}
