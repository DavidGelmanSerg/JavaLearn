package ru.gelman.producer;

import ru.gelman.Message;

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

public class MessageProducer implements Supplier<Message>{
    private static final AtomicLong nextId = new AtomicLong();
    @Override
    public Message get() {
        long id = nextId.incrementAndGet();
        return new Message(id);
    }
}
