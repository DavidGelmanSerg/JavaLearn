package ru.gelman.dto;

import lombok.Getter;

@Getter
public class ResponseStatus {
    private final boolean success;
    private final String description;

    private ResponseStatus(boolean success, String description) {
        this.success = success;
        this.description = description;
    }

    public static ResponseStatus success() {
        return new ResponseStatus(true, "success");
    }

    public static ResponseStatus error(String description) {
        return new ResponseStatus(false, description);
    }
}
