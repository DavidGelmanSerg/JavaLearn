package ru.gelman;

import lombok.extern.slf4j.Slf4j;
import ru.gelman.command.ChatAction;
import ru.gelman.command.ChatCommand;
import ru.gelman.command.ChatCommandFactory;
import ru.gelman.command.CommandOptions;
import ru.gelman.message.consumer.CommandExecutionResultConsumer;
import ru.gelman.network.client.Client;
import ru.gelman.network.client.TcpClient;
import ru.gelman.printer.ConsolePrinter;
import ru.gelman.printer.Printer;

import java.io.IOException;
import java.net.Socket;
import java.util.Properties;
import java.util.Scanner;

@Slf4j
public class ChatClientApplication {
    public static void main(String[] args) {
        log.info("starting console chat app");
        log.info("loading config from /config.properties");
        Properties config = PropertyLoader.load("/config.properties");
        String host = config.getProperty("host");
        int port = Integer.parseInt(config.getProperty("port"));
        int timeout = Integer.parseInt(config.getProperty("timeout"));
        int connAttempts = Integer.parseInt(config.getProperty("connection-attempts"));

        while (connAttempts > 0) {
            log.info("startup tcp client to host {}:{} with request timeout {}", host, port, timeout);
            try (Client client = new TcpClient(new Socket(host, port), timeout)) {
                Printer printer = new ConsolePrinter();
                CommandExecutionResultConsumer consumer = new CommandExecutionResultConsumer(printer);
                log.info("initializing command handlers");
                ChatCommandFactory factory = new ChatCommandFactory(client, consumer);
                Scanner in = new Scanner(System.in);

                log.info("starting app main loop");
                while (true) {
                    try {
                        printer.print("your command: ");
                        String input = in.nextLine();
                        log.info("user input: {}", input);
                        CommandOptions options = CommandOptions.parse(input);
                        ChatAction commandType = ChatAction.valueOf(options.getOption("-c").toUpperCase());

                        log.info("found command type: {}", commandType);
                        ChatCommand command = factory.getCommand(commandType);

                        log.info("run command handler for action: {}", commandType);
                        command.execute(options);
                    } catch (IllegalArgumentException e) {
                        log.warn(e.getMessage());
                    }

                }
            } catch (IOException e) {
                log.error("Server is not available by reason {}", e.getMessage());
                connAttempts--;
            }
        }
    }
}
