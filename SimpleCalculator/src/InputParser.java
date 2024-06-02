import exceptions.InvalidArgumentException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class InputParser {
    private final Map<String, String> options = new HashMap<String, String>();

    public InputParser(String[] args) {
        for (int i = 0; i < args.length; i += 2) {
            try {
                if (args[i].matches("-[a-zA-Z]") && !args[i + 1].matches("-[a-zA-Z]")) {
                    options.put(args[i], args[i + 1]);
                } else {
                    throw new InvalidArgumentException(args[i]);
                }
            } catch (NullPointerException e) {
                throw new InvalidArgumentException(args[i]);
            }
        }

    }

    public Set<String> getKeys() {
        return options.keySet();
    }

    public String getOptionValue(String key) {
        return options.getOrDefault(key, "");
    }

    public boolean hasOptions() {
        return !options.isEmpty();
    }

    public boolean hasOption(String key) {
        return options.containsKey(key);
    }

    public String getFirstOfKeys(String[] keys) {
        for (String key : keys) {
            if (hasOption(key)) {
                return key;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return options.toString();
    }

}
