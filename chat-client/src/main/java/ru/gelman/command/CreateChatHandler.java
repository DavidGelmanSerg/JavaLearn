package ru.gelman.command;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import ru.gelman.message.CommandExecutionResult;
import ru.gelman.message.consumer.CommandExecutionResultConsumer;
import ru.gelman.network.client.Client;
import ru.gelman.network.data.JsonNetMessage;
import ru.gelman.network.data.NetMessage;

import java.util.Arrays;
import java.util.List;

@Slf4j
class CreateChatHandler implements ChatCommand {
    private final Client client;
    private final CommandExecutionResultConsumer consumer;

    public CreateChatHandler(Client client, CommandExecutionResultConsumer consumer) {
        this.client = client;
        this.consumer = consumer;
    }

    @Override
    public void execute(CommandOptions options) {
        List<String> optionsKeys = List.of("-sid", "-n", "-ul", "-cid");

        if (options.hasOptions(optionsKeys)) {
            throw new IllegalArgumentException("data options not found");
        }

        log.info("building net message");
        NetMessage message = new JsonNetMessage();
        message.setHeader("action", ChatAction.CHAT_CREATE);

        message.setHeader("sessionId", options.getOption("-sid"));

        String name = options.getOption("-n");
        message.setBodyValue("name", name);

        List<Integer> userIds = Arrays
                .stream(options.getOption("-ul").split(","))
                .map(Integer::parseInt)
                .toList();
        message.setListToBody("users", userIds);
        message.setBodyValue("creatorId", options.getOption("-cid"));

        log.debug("built message: {}", message.asStringValue());
        log.info("requesting to service");
        NetMessage response = client.getResponse(message);

        log.debug("get response from service: {}", response.asStringValue());
        boolean success = response.getBodyValue("status", ObjectNode.class).get("success").asBoolean();
        String description = response.getBodyValue("status", ObjectNode.class).get("description").asText();
        if (success) {
            log.info("get success response from service");
            JsonNode chatInfo = response.getBodyValue("chat", ObjectNode.class).get("info");
            String messageForClient = String.format(
                    "Successful created chat. name: %s, id: %d",
                    chatInfo.get("name").asText(),
                    chatInfo.get("id").asInt()
            );
            consumer.accept(new CommandExecutionResult(true, messageForClient));
        } else {
            log.warn("get error from service");
            String messageForClient = String.format("Could not create user %s. reason: %s", name, description);
            consumer.accept(new CommandExecutionResult(false, messageForClient));
        }
    }
}
