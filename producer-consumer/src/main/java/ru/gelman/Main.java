package ru.gelman;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gelman.consumer.ConsumeTask;
import ru.gelman.producer.MessageProducer;
import ru.gelman.producer.ProduceTask;
import ru.gelman.store.BlockingQueueMessageStore;
import ru.gelman.store.MessageStore;

import java.util.function.Consumer;

public class Main {
    public static final String CONSUMERS_AMOUNT_KEY = "-ca";
    public static final String PRODUCERS_AMOUNT_KEY = "-pa";
    public static final String CONSUME_INTERVAL_KEY = "-ci";
    public static final String PRODUCE_INTERVAL_KEY = "-pi";
    public static final String MESSAGE_STORE_CAPACITY_KEY = "-c";

    public static void main(String[] args) {
        InputParser argsParser = new InputParser(args); //Взял этот класс из самой первой задачи: калькулятора
        long consumersAmount = Long.parseLong(argsParser.getOptionValue(CONSUMERS_AMOUNT_KEY));
        long producersAmount = Long.parseLong(argsParser.getOptionValue(PRODUCERS_AMOUNT_KEY));
        long consumeInterval = Long.parseLong(argsParser.getOptionValue(CONSUME_INTERVAL_KEY));
        long produceInterval = Long.parseLong(argsParser.getOptionValue(PRODUCE_INTERVAL_KEY));
        int storeCapacity = Integer.parseInt(argsParser.getOptionValue(MESSAGE_STORE_CAPACITY_KEY));
        MessageStore store = new BlockingQueueMessageStore(storeCapacity);
        for (int i = 0; i < producersAmount; i++) {
            MessageProducer producer = new MessageProducer();
            ProduceTask produceTask = new ProduceTask(producer, store, produceInterval);
            new Thread(produceTask).start();
        }

        Logger logger = LoggerFactory.getLogger(ConsumeTask.class);
        for (int i = 0; i < consumersAmount; i++) {
            Consumer<Message> consumer = m -> logger.info("got " + m);
            ConsumeTask consumeTask = new ConsumeTask(consumer, store, consumeInterval);
            new Thread(consumeTask).start();
        }
    }
}