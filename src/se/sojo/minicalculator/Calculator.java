package se.sojo.minicalculator;

public class Calculator {
    // Status indicator if equal button or another calculation has presented a total result
    public Boolean hasResult;

    // Variables holding current and previous operations
    public Calculator.Operator currentOperator = Calculator.Operator.NONE;

    public Boolean hasOperator;

    // Variables holding the result and input values
    public double resultValue;
    public double firstValue;
    public double secondValue;

    // Variables holding the input and output text
    public String inputValue;
    public String outputValue;

    public enum Operator {
        PLUS,
        MINUS,
        MULTIPLY,
        DIVIDE,
        NONE
    }

    public String getOperationSign(Operator op) {
        return switch(op) {
            case Operator.PLUS -> "+";
            case Operator.MINUS -> "-";
            case Operator.MULTIPLY -> "×";
            case Operator.DIVIDE -> "÷";
            case Operator.NONE -> "";
        };
    }

    public Operator getOperation(String key) {
        return switch(key) {
            case "+" -> Operator.PLUS;
            case "−", "-" -> Operator.MINUS;
            case "×", "*", "x", "X" -> Operator.MULTIPLY;
            case "÷", "/" -> Operator.DIVIDE;
            default -> Operator.NONE;
        };
    }

    public double calculate(double firstValue, double secondValue, Operator operation) {
        return switch(operation) {
            case Operator.PLUS -> firstValue + secondValue;
            case Operator.MINUS -> firstValue - secondValue;
            case Operator.MULTIPLY -> firstValue * secondValue;
            case Operator.DIVIDE -> firstValue / secondValue;
            default -> firstValue;
        };
    }

    void resetValues() {
        hasResult = false;

        currentOperator = Operator.NONE;

        hasOperator = false;

        resultValue = 0.0d;
        firstValue = 0.0d;
        secondValue = 0.0d;

        inputValue = "";
        outputValue = "";
    }
}
