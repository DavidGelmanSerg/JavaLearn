package ru.gelman.timer;

import ru.gelman.common.Time;

public interface TimerListener {
    void onTimerTick(Time time);
}
