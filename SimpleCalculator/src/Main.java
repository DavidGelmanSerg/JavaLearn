import expression.ExpressionCalculator;
import expression.ExpressionParser;
import expression.exceptions.EmptyExpressionException;
import expression.exceptions.UnexpectedTokenException;
import input.parser.InputParser;
import input.parser.InvalidArgumentException;
import output.printers.ConsolePrinter;
import output.printers.FilePrinter;
import output.printers.OutputPrinter;

import java.math.BigDecimal;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        ExpressionParser expressionParser;
        try {
            InputParser parser = new InputParser(args);
            String expression = parser.getOptionValue("-e");

            if (expression.isEmpty()) {
                ConsolePrinter cliPrinter = new ConsolePrinter();
                cliPrinter.print("Введите выражение: ");

                Scanner in = new Scanner(System.in);
                expression = in.nextLine();
            }

            expressionParser = new ExpressionParser(expression);
            BigDecimal result = ExpressionCalculator.calculate(expressionParser.getPostfixForm());

            OutputPrinter printer;

            /*
               Вот в этом моменте я тоже совсем не понял, как поступать правильно. Мне нужно в зависимости от ключа
               Выбирать определенный принтер. Но если ключей будет много, Main будет огромный с кучей условий.
               Как будто нужно это вынести в отдельный метод. Но большой вопрос в том, где его разместить.
               Классы моей программы не могут его содержать, потому что это не их ответственность - определять
               значений ключей.
               Как будто все ложится на плечи Main, потому что он один может определять семантику каждого ключа.
               но класть сюда кучу if'ов не хочется.
             */
            if (parser.hasOption("-f")) {
                printer = new FilePrinter(parser.getOptionValue("-f"));
            } else {
                printer = new ConsolePrinter();
            }

            printer.print("Ответ:" + result.toString());
        } catch (InvalidArgumentException e) {
            ConsolePrinter cliPrinter = new ConsolePrinter();
            cliPrinter.print("Аргумент [" + e.getMessage() + "] не имеет значения!");
        } catch (UnexpectedTokenException e) {
            ConsolePrinter cliPrinter = new ConsolePrinter();
            cliPrinter.print("Вы неверно ввели выражение!");
        } catch (EmptyExpressionException e) {
            ConsolePrinter cliPrinter = new ConsolePrinter();
            cliPrinter.print("Вы не ввели выражение!");
        }
    }
}
