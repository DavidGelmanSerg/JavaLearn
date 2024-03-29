import calculator.Calculator;
import calculator.exceptions.DivisionByZeroException;
import input.analyzer.exceptions.InvalidExpressionException;
import input.parser.InputParser;
import input.parser.exceptions.InvalidArgumentsException;
import output.Output;

public class Main {

    public static void main(String[] args) {
        try {
            InputParser input = new InputParser(args);
            Calculator calculator = new Calculator(input.getExpression());

            Output output = new Output(input.getOutputStream());
            output.print(calculator.calculate());
        } catch (InvalidArgumentsException invalidArg) {
            System.out.println("Отсутствует значение для аргумента: " + invalidArg.getMessage());
            System.exit(0);
        } catch (InvalidExpressionException invalidExpr) {
            System.out.println("Вы ввели неверное выражение!");
            System.exit(0);
        } catch (DivisionByZeroException ex) {
            System.out.println("Деление на ноль запрещено!");
            System.exit(0);
        }
    }
}