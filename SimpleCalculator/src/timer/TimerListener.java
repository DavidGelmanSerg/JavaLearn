package timer;

import common.Time;

public interface TimerListener {
    void onTimerTick(Time time);
}
