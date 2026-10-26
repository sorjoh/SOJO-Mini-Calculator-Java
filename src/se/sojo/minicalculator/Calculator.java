package se.sojo.minicalculator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Calculator {
    // Status indicator if equal button or another calculation has presented a total result
    public Boolean hasResult;

    // Variables holding current and previous operations
    public Calculator.Operator currentOperator = Calculator.Operator.NONE;

    public Boolean hasOperator;

    // Variables holding the result and input values
    public BigDecimal resultValue;
    public BigDecimal firstValue;
    public BigDecimal secondValue;

    // Variables holding the input and output text
    public String inputValue;
    public String outputValue;

    public Boolean isEqual; //Fix for example 2.3 + 5 = .

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

    public BigDecimal calculate(BigDecimal firstValue, BigDecimal secondValue, Operator operation) {
        MathContext mc = new MathContext(10, RoundingMode.DOWN);

        return switch(operation) {
            case Operator.PLUS -> firstValue. add(secondValue);
            case Operator.MINUS -> firstValue.subtract(secondValue);
            case Operator.MULTIPLY -> firstValue.multiply(secondValue);
            case Operator.DIVIDE -> firstValue.divide(secondValue, mc);
            default -> firstValue;
        };
    }

    void resetValues() {
        hasResult = false;

        currentOperator = Operator.NONE;

        hasOperator = false;

        resultValue = new BigDecimal(0);
        firstValue =  new BigDecimal(0);
        secondValue =  new BigDecimal(0);

        inputValue = "";
        outputValue = "";

        isEqual = false; //Bugfix for 2.3 + 5 = .
    }
}
