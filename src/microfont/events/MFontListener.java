package microfont.events;

import java.util.EventListener;

public interface MFontListener extends EventListener
{
    public void mFontEvent(MFontEvent ev);
}
