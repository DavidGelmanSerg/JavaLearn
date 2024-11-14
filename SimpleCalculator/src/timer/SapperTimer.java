package timer;

import common.Time;
import common.observer.SapperListener;
import common.observer.events.SapperEvent;
import common.observer.events.SapperEventType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SapperTimer implements SapperListener {
    private final List<TimerListener> listeners;
    private final Date interval;
    private final ExecutorService executor;
    private volatile boolean isStarted;

    public SapperTimer(TimeUnit unit, int amount) {
        interval = new Date(getIntervalByTimeUnit(unit, amount));
        listeners = new ArrayList<>();
        isStarted = false;
        executor = Executors.newSingleThreadExecutor();
    }

    public void addTimerListener(TimerListener listener) {
        listeners.add(listener);
    }

    public void start() {
        isStarted = true;
        tick();
    }

    private void tick() {
        Date startPoint = new Date();
        Date previousTickTime = new Date();
        while (isStarted) {
            Date currentTime = new Date();
            Date offset = new Date(previousTickTime.getTime() + interval.getTime());
            if (currentTime.after(offset)) {
                long timeInSeconds = TimeUnit.MILLISECONDS.toSeconds(currentTime.getTime() - startPoint.getTime());
                notifyListeners(new Time(timeInSeconds / 60, timeInSeconds % 60));
                previousTickTime = new Date(currentTime.getTime());
            }
        }
    }

    public void stop() {
        isStarted = false;
    }


    private long getIntervalByTimeUnit(TimeUnit unit, int amount) {
        return switch (unit) {
            case NANOSECONDS -> TimeUnit.NANOSECONDS.toMillis(amount);
            case MICROSECONDS -> TimeUnit.MICROSECONDS.toMillis(amount);
            case MILLISECONDS -> amount;
            case SECONDS -> TimeUnit.SECONDS.toMillis(amount);
            case MINUTES -> TimeUnit.MINUTES.toMillis(amount);
            case HOURS -> TimeUnit.HOURS.toMillis(amount);
            case DAYS -> TimeUnit.DAYS.toMillis(amount);
        };
    }

    private void notifyListeners(Time time) {
        for (TimerListener listener : listeners) {
            listener.onTimerTick(time);
        }
    }

    @Override
    public void update(SapperEvent event) {
        SapperEventType type = event.getType();
        switch (type) {
            case START -> executor.submit(this::start);
            case LOOSE, WIN, INIT -> stop();
        }
    }
}
