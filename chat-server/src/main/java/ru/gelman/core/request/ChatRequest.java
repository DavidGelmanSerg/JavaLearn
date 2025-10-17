package ru.gelman.core.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import ru.gelman.core.ChatAction;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
public class ChatRequest {
    private static final ObjectMapper mapper = new ObjectMapper();
    private final JsonNode header;
    private final JsonNode body;

    public ChatRequest(String request) throws JsonProcessingException {
        JsonNode root = mapper.readTree(request);
        this.header = root.get("header");
        this.body = root.get("body");
    }


    public String getHeader(String key) {
        return getHeaderAsObject(key, String.class);
    }


    public <T> T getHeaderAsObject(String key, Class<T> type) {
        return mapper.convertValue(header.get(key), type);
    }

    public ChatAction getAction() {
        String action = getHeader("action").toUpperCase();
        return ChatAction.valueOf(action);
    }

    public String getBodyValue(String key) {
        return getBodyValueAsObject(key, String.class);
    }

    public <T> T getBodyValueAsObject(String key, Class<T> type) {
        return mapper.convertValue(body.get(key), type);
    }

    public <T> List<T> getListFromBody(String key, Class<T> type) {
        try {
            return mapper.readerForListOf(type).readValue(body.get(key));
        } catch (IOException e) {
            throw new RuntimeException("cannot convert list");
        }
    }

    public Map<String, Object> getHeaders() {
        return mapper.convertValue(header, new TypeReference<>() {
        });
    }


    public String toString() {
        return mapper.valueToTree(this).asText();
    }
}
