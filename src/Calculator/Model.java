package Calculator;

public class Model {
    public double calculate(double number1, double number2, String operator) {
        switch (operator) {
            case "+":
                return number1 + number2;
            case "-":
                return number1 - number2;
            case "*":
                return number1 * number2;
            case "/":
                if (number2 == 0)
                    throw new ArithmeticException("Деление на ноль невозможно");
                return number1 / number2;
            case "%":
                return (number1*number2)/100;
        }

        System.out.println("Unknown operator 1 - " + operator);
        return 0;
    }

    public double calculate2(double number1, String operator) {
        switch (operator) {
            case "±":
                if(number1==0)
                    return 0;
                else
                    return number1*(-1);
            case "√":
                if(number1<0)
                    throw new ArithmeticException("Недопустимый ввод");
                return Math.sqrt(number1);
            case "1/x":
                if (number1 == 0)
                    throw new ArithmeticException("Деление на ноль невозможно");
                return 1 / number1;
        }

        System.out.println("Unknown operator 2 - " + operator);
        return 0;
    }
}