
package microfont.events;

import java.util.EventListener;

public interface PixselMapListener extends EventListener {
    public void pixselChanged(PixselMapEvent event);
}
