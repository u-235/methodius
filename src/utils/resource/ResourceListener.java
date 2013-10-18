package utils.resource;


import utils.event.DataEventListener;

public interface ResourceListener extends DataEventListener
{
    public void onResourceEvent(ResourceEvent ev);
}
