package expression;

import java.math.BigDecimal;

public class NumberUtils {
    private NumberUtils(){}
    public static boolean isNumber(String number) {
        for (int i = 0; i < number.length(); i++) {
            char ch = number.charAt(i);
            if (!Character.isDigit(ch) && (ch != '.' && ch !=',')) {
                return false;
            }
        }
        return true;
    }
}
