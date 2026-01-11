package ru.gelman.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class CommandOptions {
    private static final Pattern KEY_PATTERN = Pattern.compile("-[a-zA-Z]+");
    private final Map<String, String> options;

    public CommandOptions(Map<String, String> options) {
        this.options = options;
    }

    public static CommandOptions parse(String command) {
        String[] args = command.split(" ");
        Map<String, String> options = new HashMap<>();
        for (int i = 0; i < args.length; i += 2) {
            boolean isCorrectOption = KEY_PATTERN.matcher(args[i]).matches() && !KEY_PATTERN.matcher(args[i + 1]).matches();

            if (!isCorrectOption) {
                throw new IllegalArgumentException(args[i]);
            }

            options.put(args[i], args[i + 1]);
        }
        return new CommandOptions(options);
    }

    public boolean hasOption(String key) {
        return options.containsKey(key);
    }

    public boolean hasOptions(List<String> keys) {
        return !keys.stream().allMatch(this::hasOption);
    }

    public String getOption(String key) {
        return options.get(key);
    }
}
