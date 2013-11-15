
package microfont.events;

import java.util.EventListener;

public interface NotifyEventListener extends EventListener {
    public void notifyHappened(NotifyEvent event);
}
