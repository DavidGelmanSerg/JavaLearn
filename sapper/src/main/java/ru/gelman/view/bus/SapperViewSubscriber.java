package ru.gelman.view.bus;

import ru.gelman.view.bus.event.SapperViewEvent;

public interface SapperViewSubscriber<E extends SapperViewEvent<?>> {
    void handle(E event);
}
