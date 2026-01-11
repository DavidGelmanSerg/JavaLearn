package ru.gelman.message.consumer;

import lombok.extern.slf4j.Slf4j;
import ru.gelman.message.CommandExecutionResult;
import ru.gelman.message.MessageConsumer;
import ru.gelman.printer.Printer;

@Slf4j
public class CommandExecutionResultConsumer implements MessageConsumer<CommandExecutionResult> {
    private final Printer printer;

    public CommandExecutionResultConsumer(Printer printer) {
        this.printer = printer;
    }

    @Override
    public void accept(CommandExecutionResult message) {
        log.debug("accepting message: {}", message);
        String messageString = String.format(
                "success: %s. message: %s",
                message.success(), message.message()
        );
        log.info("printing data: {}", messageString);
        printer.printLine(messageString);
    }
}
