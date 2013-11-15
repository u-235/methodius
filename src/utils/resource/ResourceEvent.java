
package utils.resource;

import utils.event.DataEvent;

@SuppressWarnings("serial")
public class ResourceEvent extends DataEvent {
    public static final int PREFIX = 1;

    public ResourceEvent(Object source, int reason, Object oldValue,
                    Object newValue) {
        super(source, reason, oldValue, newValue);
    }
}
