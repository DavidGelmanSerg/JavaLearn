package ru.gelman.core.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ru.gelman.core.ChatAction;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ChatResponse {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final ObjectNode header;
    private final ObjectNode body;

    public ChatResponse() {
        MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        this.header = MAPPER.createObjectNode();
        this.body = MAPPER.createObjectNode();
    }

    public ChatResponse(Map<String, Object> header) {
        MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        this.header = MAPPER.valueToTree(header);
        this.body = MAPPER.createObjectNode();
    }

    public static ChatResponse error(Map<String, Object> header, String error, String description) {
        ChatResponse response = new ChatResponse(header);
        ObjectNode status = MAPPER.createObjectNode();
        status.put("success", false);
        status.put("error", error);
        status.put("description", description);
        response.setBodyValue("status", status);
        return response;
    }

    public static ChatResponse success(Map<String, Object> header) {
        ChatResponse response = new ChatResponse(header);
        ObjectNode status = MAPPER.createObjectNode();
        status.put("success", true);
        status.put("description", "success");
        response.setBodyValue("status", status);
        return response;
    }

    public boolean success() {
        return false;
    }

    public void setBodyValue(String key, Object value) {
        body.putPOJO(key, value);
    }

    public <T> void setListToBody(String key, List<T> list) {
        ArrayNode array = body.putArray(key);
        list.forEach(el -> array.add(MAPPER.convertValue(el, JsonNode.class)));
    }

    public String getHeader(String key) {
        return getHeaderAsObject(key, String.class);
    }


    public <T> T getHeaderAsObject(String key, Class<T> type) {
        return MAPPER.convertValue(header.get(key), type);
    }

    public ChatAction getAction() {
        String action = getHeader("action").toUpperCase();
        return ChatAction.valueOf(action);
    }

    public String getBodyValue(String key) {
        return getBodyValueAsObject(key, String.class);
    }

    public <T> T getBodyValueAsObject(String key, Class<T> type) {
        return MAPPER.convertValue(body.get(key), type);
    }

    public <T> List<T> getListFromBody(String key, Class<T> type) {
        try {

            return MAPPER.readerForListOf(type).readValue(body.get(key));
        } catch (IOException e) {
            throw new RuntimeException("cannot convert list");
        }
    }


    @Override
    public String toString() {
        try {
            ObjectNode tree = MAPPER.createObjectNode();
            tree.putPOJO("header", header);
            tree.putPOJO("body", body);
            return MAPPER.writeValueAsString(tree);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
