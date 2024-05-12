package expression;

import java.util.HashSet;

public class Operations {
    public static final String SUM = "+";
    public static final String SUBTRACTION = "-";
    public static final String MULTIPLICATION = "*";
    public static final String DIVISION = "/";

    public static HashSet<String> asSet() {
        HashSet<String> operationSet = new HashSet<>();
        operationSet.add(SUM);
        operationSet.add(SUBTRACTION);
        operationSet.add(MULTIPLICATION);
        operationSet.add(DIVISION);
        return operationSet;
    }
}
