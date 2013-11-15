
package microfont.events;

import java.util.EventObject;

public class NotifyEvent extends EventObject {
    protected String notify;

    public NotifyEvent(Object source, String notify) {
        super(source);
        this.notify = notify;
    }

    public String getNotify() {
        return notify;
    }
}
