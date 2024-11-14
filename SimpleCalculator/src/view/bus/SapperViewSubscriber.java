package view.bus;

import view.bus.event.SapperViewEvent;

public interface SapperViewSubscriber<E extends SapperViewEvent<?>> {
    void handle(E event);
}
