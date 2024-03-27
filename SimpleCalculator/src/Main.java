import exceptions.ExpressionNotValidException;

import java.io.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        InputParser input = new InputParser(args);
        Calculator calculator = new Calculator();
        try {
            if (!input.getOptionValue("-e").isEmpty()) {
                calculator.setExpression(input.getOptionValue("-e"));
            } else {
                System.out.print("Введите выражение: ");
                Scanner in = new Scanner(System.in);
                calculator.setExpression(in.nextLine().replace(" ", ""));
            }

        } catch (ExpressionNotValidException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
        try (PrintWriter out = new PrintWriter(input.getOptionValue("-f"))) {
            out.print(calculator.calculate().toString());
            System.out.println("Выражение записано");
        } catch (FileNotFoundException e) {
            System.out.println(calculator.calculate().toString());
        }
    }
}