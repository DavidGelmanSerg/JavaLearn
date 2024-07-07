package expression;

import java.util.Set;

public class Operations {
    public static final String SUM = "+";
    public static final String SUBTRACTION = "-";
    public static final String MULTIPLICATION = "*";
    public static final String DIVISION = "/";

    public static Set<String> asSet() {
        return Set.of(SUM, SUBTRACTION, MULTIPLICATION, DIVISION);
    }
}
