package ru.gelman.common.observer;

import ru.gelman.common.observer.events.SapperEvent;

public interface SapperListener {
    void update(SapperEvent event);
}
