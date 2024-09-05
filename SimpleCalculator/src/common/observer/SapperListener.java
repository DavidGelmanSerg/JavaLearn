package common.observer;

import common.observer.events.SapperEvent;

public interface SapperListener {
    void update(SapperEvent event);
}
