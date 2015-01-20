package com.org.source.event;


public interface ISystemEventHandler
{
    public enum EventType {UNKOWN, DESTORY};
    
    public class SystemEvent
    {
        public EventType mEventType = EventType.UNKOWN;

        public Object mObject;
    }

    public void onEvent(SystemEvent event);
}
