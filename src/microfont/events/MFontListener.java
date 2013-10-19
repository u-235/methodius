package microfont.events;

import utils.event.DataEventListener;

public interface MFontListener extends DataEventListener
{
    public void mFontEvent(MFontEvent ev);
}
