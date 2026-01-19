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
class LoginUserCommand implements ChatCommand {
    private final Client client;
    private final CommandExecutionResultConsumer consumer;

    public LoginUserCommand(Client client, CommandExecutionResultConsumer consumer) {
        this.client = client;
        this.consumer = consumer;
    }

    @Override
    public void execute(CommandOptions options) {
        List<String> optionsKeys = List.of("-l", "-p");

        if (options.hasOptions(optionsKeys)) {
            throw new RuntimeException("data options not found");
        }

        log.info("building net message");
        NetMessage message = new JsonNetMessage();
        message.setHeader("action", ChatAction.USER_LOGIN);
        String login = options.getOption("-l");
        message.setBodyValue("name", options.getOption("-l"));
        message.setBodyValue("password", options.getOption("-p"));
        log.debug("built message: {}", message.asStringValue());

        log.info("requesting to service");
        NetMessage response = client.getResponse(message);

        log.debug("get response from service: {}", response.asStringValue());
        boolean success = response.getBodyValue("status", ObjectNode.class).get("success").asBoolean();
        String description = response.getBodyValue("status", ObjectNode.class).get("description").asText();
        CommandExecutionResult result;
        if (success) {
            log.info("get success response from service");
            String sessionId = response.getBodyValue("session", ObjectNode.class).get("sessionId").asText();
            String messageForClient = String.format("Successful logged in user %s with id %s", login, sessionId);
            result = new CommandExecutionResult(true, messageForClient);
            consumer.accept(result);
        } else {
            log.warn("get error from service");
            String messageForClient = String.format("Could not login user %s. reason: %s", login, description);
            result = new CommandExecutionResult(false, messageForClient);
            consumer.accept(result);
        }
    }
}
