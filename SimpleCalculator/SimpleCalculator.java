import java.util.Scanner;

public class SimpleCalculator {
    public static void main(String[] args) {
        //Приветствие
        System.out.print("""
                Здравствуйте.для расчета значений введите данные в формате:
                Попробуйте еще раз. Введите данные в формате [операнд1] [оператор] [операнд2].
                Операнд может быть: +, -, *, : или /В качестве операндов принимаются любые числа""");

        //Переменные
        double firstOperand;
        double secondOperand;
        double operationResult = 0;
        String operator;

        //Объект для чтения данных со стандартным системным потоком
        Scanner input = new Scanner(System.in);

        System.out.print("\nВаш ввод: ");
        try {
            //Чтение данных
            firstOperand = input.nextDouble();
            operator = input.next();
            secondOperand = input.nextDouble();

            switch (operator) {
                case "+":
                    operationResult = firstOperand + secondOperand;
                    break;
                case "-":
                    operationResult = firstOperand - secondOperand;
                    break;
                case "*":
                    operationResult = firstOperand * secondOperand;
                    break;
                case ":":
                case "/":
                    if (secondOperand != 0) {
                        operationResult = firstOperand / secondOperand;
                        break;
                    } else {
                        System.out.print("Division by zero is not permitted! The second operand is 0");
                        System.exit(0);
                    }
                default:
                    System.out.print("""
                            Ввод данных не по формату.
                            Вводите данные в формате [операнд1] [оператор] [операнд2].
                            Операнд может быть: +, -, *, : или /. В качестве операндов принимаются любые числа
                            Обращайте внимание на наличие пробелов между операторами и операндами""");
            }
            System.out.printf("Результат операции: %.2f", operationResult);
        } catch (Exception e) {
            System.out.print("""
                    Ввод данных не по формату.
                    Вводите данные в формате [операнд1] [оператор] [операнд2].
                    Операнд может быть: +, -, *, : или /. В качестве операндов принимаются любые числа
                    Обращайте внимание на наличие пробелов между операторами и операндами""");
        }
    }
}