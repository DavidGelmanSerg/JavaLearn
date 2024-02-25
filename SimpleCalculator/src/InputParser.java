public class InputParser {
    private final String[] Options;

    public InputParser(String[] options) {
        Options = options;
    }

    public String getOptionValue(String key) {
        for (int i = 0; i < Options.length - 1; i++) {
            if (Options[i].equals(key) && isValue(i + 1)) {
                return Options[i + 1];
            }
        }
        return "";
    }

    private boolean isValue(int keyIndex) {
        return (Options.length > keyIndex);
    }

}
