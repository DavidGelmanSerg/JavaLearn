package ru.gelman.network.data;

import java.util.List;
import java.util.Map;

public interface NetMessage {
    <T> T getHeader(String name, Class<T> type);

    String getHeader(String name);

    void setHeader(String name, Object value);
    void setHeaders(Map<String, Object> headers);

    <T> T getBodyValue(String name, Class<T> type);

    String getBodyValue(String name);

    void setBodyValue(String name, Object value);

    <T> List<T> getListFromBody(String name, Class<T> type);

    <T> void setListToBody(String name, List<T> list);

    Map<String, Object> getHeaders();

    Map<String, Object> getBody();

    String asStringValue();

    byte[] asBytes();
}
