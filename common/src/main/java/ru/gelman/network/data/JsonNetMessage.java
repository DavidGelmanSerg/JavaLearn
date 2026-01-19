package ru.gelman.network.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
public class JsonNetMessage implements NetMessage {
    private static final ObjectMapper mapper = new ObjectMapper();
    private final ObjectNode header;
    private final ObjectNode body;

    public JsonNetMessage(String value) throws JsonProcessingException {
        JsonNode root = mapper.readTree(value);
        this.header = (ObjectNode) root.get("header");
        this.body = (ObjectNode) root.get("body");
    }

    public JsonNetMessage() {
        this.header = mapper.createObjectNode();
        this.body = mapper.createObjectNode();
    }

    @Override
    public <T> T getHeader(String name, Class<T> type) {
        return mapper.convertValue(header.get(name), type);
    }

    @Override
    public String getHeader(String name) {
        return getHeader(name, String.class);
    }

    @Override
    public void setHeader(String name, Object value) {
        header.putPOJO(name, value);
    }

    @Override
    public <T> T getBodyValue(String name, Class<T> type) {
        return mapper.convertValue(body.get(name), type);
    }

    @Override
    public String getBodyValue(String name) {
        return getBodyValue(name, String.class);
    }

    @Override
    public void setBodyValue(String name, Object value) {
        body.putPOJO(name, value);
    }

    @Override
    public <T> List<T> getListFromBody(String name, Class<T> type) {
        try {
            return mapper.readerForListOf(type).readValue(body.get(name));
        } catch (IOException e) {
            throw new RuntimeException("cannot convert list");
        }
    }

    @Override
    public <T> void setListToBody(String name, List<T> list) {
        ArrayNode array = body.putArray(name);
        list.forEach(el -> array.add(mapper.convertValue(el, JsonNode.class)));
    }

    @Override
    public Map<String, Object> getHeaders() {
        return mapper.convertValue(header, new TypeReference<>() {
        });
    }

    @Override
    public void setHeaders(Map<String, Object> headers) {
        headers.forEach(this::setHeader);
    }

    @Override
    public Map<String, Object> getBody() {
        return mapper.convertValue(body, new TypeReference<>() {
        });
    }

    @Override
    public String asStringValue() {
        try {
            ObjectNode tree = mapper.createObjectNode();
            tree.putPOJO("header", header);
            tree.putPOJO("body", body);
            return mapper.writeValueAsString(tree);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] asBytes() {
        try {
            ObjectNode tree = mapper.createObjectNode();
            tree.putPOJO("header", header);
            tree.putPOJO("body", body);
            return mapper.writeValueAsBytes(tree);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
