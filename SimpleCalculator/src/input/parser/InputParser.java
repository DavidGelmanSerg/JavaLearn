package input.parser;

import calculator.operations.Operation;
import input.analyzer.Analyzer;
import input.analyzer.exceptions.InvalidExpressionException;
import input.parser.exceptions.InvalidArgumentsException;
import output.streams.ConsoleStream;
import output.streams.FileStream;
import output.streams.IStream;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class InputParser {
    private Map<String, String> options = new HashMap<String, String>();

    public InputParser(String[] args) throws InvalidArgumentsException {
        int i = 0;
        while (i < args.length) {
            try {
                if (args[i].matches("-[a-zA-Z]") && !args[i + 1].matches("-[a-zA-Z]")) {
                    options.put(args[i], args[i + 1]);
                    i += 2;
                } else {
                    throw new InvalidArgumentsException(args[i]);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new InvalidArgumentsException(args[i]);
            }
        }

    }

    public boolean isOptions() {
        return !options.isEmpty();
    }

    public String getOptionValue(String key) {
        String value = options.get(key);
        return (value == null) ? "" : value;
    }

    public boolean isOption(String key) {
        return options.containsKey(key);
    }

    public Operation getExpression() throws InvalidExpressionException {
        Analyzer analyzer;
        if (options.containsKey("-e")) {
            analyzer = new Analyzer(getOptionValue("-e"));
        } else {
            Scanner in = new Scanner(System.in);
            System.out.println("Введите выражение:");
            analyzer = new Analyzer(in.nextLine());
        }
        if (!analyzer.isValidExpression()) {
            throw new InvalidExpressionException();
        }
        return analyzer.getOperations();
    }

    public IStream getOutputStream() {
        for (String key : options.keySet()) {
            switch (key) {
                case "-f": {
                    return new FileStream(options.get("-f"));
                }
            }
        }
        return new ConsoleStream();
    }

    public void printOptions() {
        System.out.println(options);
    }
}
