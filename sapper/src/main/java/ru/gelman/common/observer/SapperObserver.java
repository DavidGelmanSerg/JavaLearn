package ru.gelman.common.observer;

import ru.gelman.common.observer.events.SapperEvent;

public interface SapperObserver {
    void addSapperListener(SapperListener listener);

    void removeSapperListener(SapperListener listener);

    void clearListenerList();

    void notifyListeners(SapperEvent event);
}
