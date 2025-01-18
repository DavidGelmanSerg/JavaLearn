package ru.gelman;

import java.util.HashMap;
import java.util.Map;

public class InputParser {
    private final Map<String, String> options = new HashMap<>();

    public InputParser(String[] args) {
        for (int i = 0; i < args.length; i += 2) {
            try {
                if (args[i].matches("-[a-zA-Z]+") && !args[i + 1].matches("-[a-zA-Z]+")) {
                    options.put(args[i], args[i + 1]);
                } else {
                    throw new IllegalArgumentException(args[i]);
                }
            } catch (NullPointerException e) {
                throw new IllegalArgumentException(args[i]);
            }
        }

    }

    public String getOptionValue(String key) {
        return options.getOrDefault(key, "");
    }

    @Override
    public String toString() {
        return options.toString();
    }

}
