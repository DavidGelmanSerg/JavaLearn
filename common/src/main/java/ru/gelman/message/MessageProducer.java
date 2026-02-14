package ru.gelman.message;

public interface MessageProducer<T> {
    T produce();
}
