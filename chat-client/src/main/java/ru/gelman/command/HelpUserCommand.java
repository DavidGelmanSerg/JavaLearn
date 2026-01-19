package ru.gelman.command;

import lombok.extern.slf4j.Slf4j;
import ru.gelman.message.CommandExecutionResult;
import ru.gelman.message.consumer.CommandExecutionResultConsumer;

import java.io.InputStream;
import java.util.Scanner;

@Slf4j
class HelpUserCommand implements ChatCommand {
    private static final String HELP_FILE_NAME = "/help.txt";
    private final CommandExecutionResultConsumer consumer;

    public HelpUserCommand(CommandExecutionResultConsumer consumer) {
        this.consumer = consumer;
    }

    @Override
    public void execute(CommandOptions options) {
        log.info("getting resource {}", HELP_FILE_NAME);
        InputStream helpFileInStream = this.getClass().getResourceAsStream(HELP_FILE_NAME);
        if (helpFileInStream != null) {
            log.info("{} found. reading file data", HELP_FILE_NAME);
            consumer.accept(new CommandExecutionResult(true, readHelpFile(helpFileInStream)));
        } else {
            log.info("{} not found. creating error message", HELP_FILE_NAME);
            consumer.accept(new CommandExecutionResult(false, "help file not found"));
        }
    }

    private String readHelpFile(InputStream in) {
        StringBuilder resultStringBuilder = new StringBuilder();
        Scanner scanner = new Scanner(in);
        while (scanner.hasNextLine()) {
            resultStringBuilder.append(scanner.nextLine()).append(System.lineSeparator());
        }
        return resultStringBuilder.toString();
    }
}
