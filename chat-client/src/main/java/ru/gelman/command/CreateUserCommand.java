package ru.gelman.command;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import ru.gelman.message.CommandExecutionResult;
import ru.gelman.message.consumer.CommandExecutionResultConsumer;
import ru.gelman.network.client.Client;
import ru.gelman.network.data.JsonNetMessage;
import ru.gelman.network.data.NetMessage;

import java.util.List;

@Slf4j
class CreateUserCommand implements ChatCommand {
    private final Client client;
    private final CommandExecutionResultConsumer consumer;

    public CreateUserCommand(Client client, CommandExecutionResultConsumer consumer) {
        this.client = client;
        this.consumer = consumer;
    }

    @Override
    public void execute(CommandOptions options) {
        List<String> optionsKeys = List.of("-l", "-p");

        if (options.hasOptions(optionsKeys)) {
            throw new IllegalArgumentException("data options not found");
        }

        log.info("building net message");
        NetMessage message = new JsonNetMessage();
        message.setHeader("action", ChatAction.USER_CREATE);
        String login = options.getOption("-l");
        message.setBodyValue("name", options.getOption("-l"));
        message.setBodyValue("password", options.getOption("-p"));
        log.debug("built message: {}", message.asStringValue());

        log.info("requesting to service");
        NetMessage response = client.getResponse(message);

        log.debug("get response from service: {}", response.asStringValue());
        boolean success = response.getBodyValue("status", ObjectNode.class).get("success").asBoolean();
        String description = response.getBodyValue("status", ObjectNode.class).get("description").asText();
        if (success) {
            log.info("get success response from service");
            int userId = response.getBodyValue("user", ObjectNode.class).get("id").asInt();
            String messageForClient = String.format("Successful created user %s with id %d", login, userId);
            consumer.accept(new CommandExecutionResult(true, messageForClient));
        } else {
            log.warn("get error from service");
            String messageForClient = String.format("Could not create user %s. reason: %s", login, description);
            consumer.accept(new CommandExecutionResult(false, messageForClient));
        }
    }
}
