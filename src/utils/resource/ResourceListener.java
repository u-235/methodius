
package utils.resource;

import java.util.EventListener;

public interface ResourceListener extends EventListener {
    public void onResourceEvent(ResourceEvent ev);
}
