package ru.gelman;

import ru.gelman.consumer.MessageConsumer;
import ru.gelman.producer.MessageProducer;
import ru.gelman.message.MessageStore;
import ru.gelman.message.SynchronizedMessageStore;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        Properties properties = getStartConfig();
        long consumersAmount = Long.parseLong(properties.getProperty("consumer-amount"));
        long producersAmount = Long.parseLong(properties.getProperty("producer-amount"));
        long consumeInterval = Long.parseLong(properties.getProperty("consuming_delay"));
        long produceInterval = Long.parseLong(properties.getProperty("producing_delay"));
        int storeCapacity = Integer.parseInt(properties.getProperty("store-capacity"));
        MessageStore store = new SynchronizedMessageStore(storeCapacity);

        for (int i = 0; i < producersAmount; i++) {
            var producer = new MessageProducer(store, produceInterval);
            new Thread(producer).start();
        }

        for (int i = 0; i < consumersAmount; i++) {
            var consumer = new MessageConsumer(store, consumeInterval);
            new Thread(consumer).start();
        }
    }

    private static Properties getStartConfig() {
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("start-config.properties");
        Properties properties = new Properties();
        try {
            properties.load(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties;
    }
}