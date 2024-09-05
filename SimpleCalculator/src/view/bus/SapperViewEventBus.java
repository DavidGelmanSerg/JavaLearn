package view.bus;

import view.bus.event.SapperViewEvent;
import view.bus.event.SapperViewEventType;
import view.bus.subscriber.SapperViewSubscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SapperViewEventBus {
    private static SapperViewEventBus globalBus;
    private final Map<SapperViewEventType, List<SapperViewSubscriber<?>>> subscribers = new HashMap<>();

    public static SapperViewEventBus getInstance() {
        if (globalBus == null) {
            globalBus = new SapperViewEventBus();
        }
        return globalBus;
    }

    public void register(SapperViewEventType eventType, SapperViewSubscriber<?> subscriber) {
        subscribers.computeIfAbsent(eventType, sList -> new ArrayList<>()).add(subscriber);
    }

    public void unregister(SapperViewEventType eventType, SapperViewSubscriber<?> subscriber) {
        List<SapperViewSubscriber<?>> subscribersByType = subscribers.get(eventType);
        if (subscribersByType != null) {
            subscribersByType.remove(subscriber);
        }
    }

    public <T extends SapperViewEvent<?>> void publish(SapperViewEventType eventType, T event) {
        List<SapperViewSubscriber<?>> subscribersByType = subscribers.get(eventType);
        if (subscribersByType != null) {
            for (SapperViewSubscriber<?> s : subscribersByType) {
                @SuppressWarnings("unchecked")
                SapperViewSubscriber<T> typedSubscriber = (SapperViewSubscriber<T>) s;
                typedSubscriber.handle(event);
            }
        }
    }
}
