package ru.gelman.consumer;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gelman.Message;
import ru.gelman.message.MessageStore;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class MessageConsumer implements Consumer<Message>, Runnable {
    private static final Logger logger = LoggerFactory.getLogger(MessageConsumer.class);
    private final MessageStore store;
    private final long consumeDelay;

    @Override
    public void run() {
        try {
            while (true) {
                Message message = store.get();
                accept(message);
                Thread.sleep(consumeDelay);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void accept(Message message) {
        logger.info("got " + message);
    }
}
