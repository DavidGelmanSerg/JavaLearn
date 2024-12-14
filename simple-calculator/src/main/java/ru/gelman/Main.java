package ru.gelman;

import ru.gelman.exceptions.InvalidArgumentException;
import ru.gelman.exceptions.UnsupportedTargetException;
import ru.gelman.exceptions.expression.EmptyExpressionException;
import ru.gelman.exceptions.expression.InvalidExpressionException;
import ru.gelman.expression.ExpressionCalculator;
import ru.gelman.expression.ExpressionParser;
import ru.gelman.output.printers.OutputPrinter;
import ru.gelman.output.printers.PrinterFactory;
import ru.gelman.output.target.Target;
import ru.gelman.output.target.TargetFactory;
import ru.gelman.output.target.TargetType;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try {
            InputParser parser = new InputParser(args);
            String expression = parser.getOptionValue("-e");

            String[] keys = TargetFactory.getTypesAsStringArray();
            String foundedType = parser.getFirstOfKeys(keys);
            TargetType type = TargetFactory.getType(foundedType);
            Target target = TargetFactory.getTarget(type, parser.getOptionValue(foundedType));

            if (!expression.isEmpty()) {
                ExpressionParser expressionParser = new ExpressionParser(expression);
                expression = ExpressionCalculator.calculate(expressionParser.getPostfixForm()).toString();
            }
            target.setExpression(expression);

            target.addPropertyChangeListener("expression", evt -> {
                String newValue = evt.getNewValue().toString();
                String oldValue = evt.getOldValue().toString();
                if (newValue.equals(oldValue)) {
                    ExpressionParser expressionParser = new ExpressionParser(newValue);
                    newValue = ExpressionCalculator.calculate(expressionParser.getPostfixForm()).toString();
                }
                target.setExpression(newValue);
            });
        } catch (InvalidArgumentException | InvalidExpressionException e) {
            OutputPrinter printer = PrinterFactory.getDefaultPrinter();
            printer.print(e.getMessage());
        } catch (EmptyExpressionException ex) {
            Target t = ex.getSource();
            OutputPrinter printer = PrinterFactory.getDefaultPrinter();

            printer.print("Введите выражение: ");
            Scanner in = new Scanner(System.in);

            ExpressionParser parser = new ExpressionParser(in.nextLine());
            String result = ExpressionCalculator.calculate(parser.getPostfixForm()).toString();
            t.setExpression(result);
        } catch (UnsupportedTargetException ex) {
            OutputPrinter printer = PrinterFactory.getDefaultPrinter();

            printer.print("Введите выражение: ");
            Scanner in = new Scanner(System.in);

            ExpressionParser parser = new ExpressionParser(in.nextLine());
            String result = ExpressionCalculator.calculate(parser.getPostfixForm()).toString();
            printer.print(result);
        }
    }
}

