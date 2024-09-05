package view.bus.subscriber;

import view.bus.event.SapperViewEvent;

public interface SapperViewSubscriber<T extends SapperViewEvent<?>> {
    void handle(T event);
}
