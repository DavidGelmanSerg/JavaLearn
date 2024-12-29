package ru.gelman.common;

import java.util.Objects;

public record Time(long minutes, long seconds) implements Comparable<Time> {
    public static Time of(String time) {
        if (!isValidFormat(time)) {
            throw new IllegalArgumentException("Incorrect time format was found: " + time + ". correct time format: MM:SS");
        }
        String[] timeComponents = time.split(":");
        long minutes = Long.parseLong(timeComponents[0]);
        long seconds = Long.parseLong(timeComponents[1]);
        return new Time(minutes, seconds);
    }

    public static boolean isValidFormat(String time) {
        String pattern = "\\d{2}:\\d{2}";
        return time.matches(pattern);
    }

    public static Time of(Time time) {
        return new Time(time.minutes, time.seconds);
    }

    @Override
    public String toString() {
        String minutesString = minutes < 10 ? "0" + minutes : String.valueOf(minutes);
        String secondStr = seconds < 10 ? "0" + seconds : String.valueOf(seconds);
        return minutesString + ":" + secondStr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Time time = (Time) o;
        return minutes == time.minutes && seconds == time.seconds;
    }

    @Override
    public int hashCode() {
        return Objects.hash(minutes, seconds);
    }

    @Override
    public int compareTo(Time o) {
        long minusMinutes = minutes - o.minutes;
        long minusSeconds = seconds - o.seconds;

        if (minusMinutes == 0 && minusSeconds == 0) {
            return 0;
        } else if (minusMinutes < 0 || (minusMinutes == 0 && minusSeconds < 0)) {
            return -1;
        } else {
            return 1;
        }
    }
}
