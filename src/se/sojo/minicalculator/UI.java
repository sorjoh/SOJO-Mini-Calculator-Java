package se.sojo.minicalculator;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;
import java.util.Objects;

import javax.swing.*;
import javax.swing.text.*;

public class UI implements ActionListener {
    private final JFrame frame;

    private final JPanel panel;
    private final JPanel panelRow1;
    private final JPanel panelRow2;
    private final JPanel panelRow3;
    private final JPanel panelRow4;
    private final JPanel panelRow5;

    private final JTextArea display;

    private final JButton[] buttonNumber;
    private final JButton buttonDecimal;
    private final JButton buttonPlus;
    private final JButton buttonMinus;
    private final JButton buttonMultiply;
    private final JButton buttonDivide;
    private final JButton buttonEqual;

    private final String[] buttonNumberValue = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };

    private final Font font;
    private final Font textFont;

    public final Calculator calc;

    public boolean DEBUG;
    public int MAX_INPUT_LENGTH;
    public char DECIMAL_SEPARATOR;

    int WIDTH = 230;
    int HEIGHT = 260;
    int DISPLAY_MAX_WIDTH = WIDTH - 20;

    /*
        UI Calculator
     */
    public UI(Calculator calc, char decimalSeparator, Integer maxInputLength, Boolean debug) {
        // Initialize Calculator
        this.calc = calc;

        // Save the included parameters
        DECIMAL_SEPARATOR = decimalSeparator;
        MAX_INPUT_LENGTH = maxInputLength;
        DEBUG = debug;

        // Set the frame
        frame = new JFrame("MiniCalc");
        frame.setSize(WIDTH, HEIGHT);

        // Set the panels
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panelRow1 = new JPanel(new FlowLayout());
        panelRow2 = new JPanel(new FlowLayout());
        panelRow3 = new JPanel(new FlowLayout());
        panelRow4 = new JPanel(new FlowLayout());
        panelRow5 = new JPanel(new FlowLayout());

        font = new Font("Consolas",Font.PLAIN, 18);
        textFont = new Font("Consolas",Font.BOLD, 24);

        // Add the display
        display = new JTextArea(1, MAX_INPUT_LENGTH);

        // Add the numbers
        buttonNumber = new JButton[10];
        for (int i = 0; i < 10; i++) {
            buttonNumber[i] = new JButton(String.valueOf(i));
        }

        // Add operators, decimal and equal
        buttonDecimal = new JButton(String.valueOf(DECIMAL_SEPARATOR));
        buttonPlus = new JButton(calc.getOperationSign(Calculator.Operator.PLUS));
        buttonMinus = new JButton(calc.getOperationSign(Calculator.Operator.MINUS));
        buttonMultiply = new JButton(calc.getOperationSign(Calculator.Operator.MULTIPLY));
        buttonDivide = new JButton(calc.getOperationSign(Calculator.Operator.DIVIDE));
        buttonEqual = new JButton("=");

        displayOutput(calc.outputValue);
    }

    // ========= INIT =========
    public void init() {
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        display.setFont(textFont);
        display.setEditable(false);

        display.setSize(DISPLAY_MAX_WIDTH, Integer.MAX_VALUE);

        // Remove Caret
        Caret caret = new DefaultCaret() {
            public void focusGained(FocusEvent e) {
                setVisible(false);
                setSelectionVisible(false);
            }
        };
        display.setCaret(caret);

        for (int i = 0; i < 10; i++) {
            buttonNumber[i].setFont(font);
        }

        buttonDecimal.setFont(font);
        buttonPlus.setFont(font);
        buttonMinus.setFont(font);
        buttonMultiply.setFont(font);
        buttonDivide.setFont(font);
        buttonEqual.setFont(font);

        // Display
        panelRow1.add(display);
        panelRow1.add(Box.createVerticalStrut(30));
        panel.add(panelRow1);

        // 7, 8, 9, *
        panelRow2.add(buttonNumber[7]);
        panelRow2.add(buttonNumber[8]);
        panelRow2.add(buttonNumber[9]);
        panelRow2.add(buttonMultiply);
        panel.add(panelRow2);

        // 4, 5, 6, /
        panelRow3.add(buttonNumber[4]);
        panelRow3.add(buttonNumber[5]);
        panelRow3.add(buttonNumber[6]);
        panelRow3.add(buttonDivide);
        panel.add(panelRow3);

        // 1, 2, 3, +
        panelRow4.add(buttonNumber[1]);
        panelRow4.add(buttonNumber[2]);
        panelRow4.add(buttonNumber[3]);
        panelRow4.add(buttonPlus);
        panel.add(panelRow4);

        // 0, ., =, -
        panelRow5.add(buttonNumber[0]);
        panelRow5.add(buttonDecimal);
        panelRow5.add(buttonEqual);
        panelRow5.add(buttonMinus);
        panel.add(panelRow5);

        // Add Action listener for the numbers
        for (int i = 0; i < 10; i++) {
            buttonNumber[i].addActionListener(this);
        }

        // Add action listener for operations, decimal and equal
        buttonDecimal.addActionListener(this);
        buttonPlus.addActionListener(this);
        buttonMinus.addActionListener(this);
        buttonMultiply.addActionListener(this);
        buttonDivide.addActionListener(this);
        buttonEqual.addActionListener(this);

        // Show the calculator
        frame.add(panel);
        frame.setVisible(true);
    }

    // ========= ACTION PERFORMED =========
    @Override
    public void actionPerformed(ActionEvent e) {
        final Object source = e.getSource();

        // Add action for numbers
        for (int i = 0; i < 10; i++) {
            if (source == buttonNumber[i]) {
                numberInput(buttonNumberValue[i]);
                return;
            }
        }

        // Add action for decimal
        if (source == buttonDecimal) {
            decimalInput(String.valueOf(DECIMAL_SEPARATOR));
        }

        // Add action for operators
        if (source == buttonPlus) {
            operatorInput(calc.getOperation(buttonPlus.getText()));
        }
        if (source == buttonMinus) {
            operatorInput(calc.getOperation(buttonMinus.getText()));
        }
        if (source == buttonMultiply) {
            operatorInput(calc.getOperation(buttonMultiply.getText()));
        }
        if (source == buttonDivide) {
            operatorInput(calc.getOperation(buttonDivide.getText()));
        }

        // Add action for equal
        if (source == buttonEqual) {
            equal();
        }
    }

    // ========= NUMBER INPUT =========
    public void numberInput(String inputKey) {
        if (DEBUG)
            System.out.println("Number(before) : button=" + inputKey + ", inputValue=" + calc.inputValue + ", outputValue=" + calc.outputValue + ", Display=" + display.getText() + ", currentOperation=" + calc.getOperationSign(calc.currentOperator) + ", hasResult=" + calc.hasResult + ", hasOperator=" + calc.hasOperator);

        if (calc.hasResult) { // Calculation has result
            // Reset all values
            calc.resetValues();
            // Add the first number to inputValue
            calc.inputValue = inputKey;
            // Replace outputValue with the newly and initialized inputValue
            calc.outputValue = calc.inputValue;
        } else if (calc.hasOperator) { // Calculation has operator
            String currentResultValue;

            try {
                // Get first and second value from the output
                String[] splitOutputValue = calc.outputValue.trim().split("[" + calc.getOperationSign(calc.currentOperator) + "]");

                // If inputValue is zero then replace inputValue with the number, otherwise add the number
                calc.inputValue = Objects.equals(calc.inputValue, "0") ? inputKey : calc.inputValue + inputKey;

                // Set current result initially as the first split value from the outputValue
                currentResultValue = splitOutputValue[0];

                // Add minus sign in the beginning if current result value is empty and operator is minus
                if (calc.currentOperator == Calculator.Operator.MINUS && currentResultValue.isEmpty()) {
                    currentResultValue = calc.getOperationSign(Calculator.Operator.MINUS) + splitOutputValue[1];
                }
            } catch (Exception e) {
                if (DEBUG)
                    System.out.println(e.getMessage() + ", outputValue=" + calc.outputValue);

                // Error fix when splitting outputValue
                calc.inputValue += inputKey;
                currentResultValue = calc.outputValue.trim();
            }

            // Replace output value with the temporary current result value together with current operator sign and input value
            calc.outputValue = currentResultValue + calc.getOperationSign(calc.currentOperator) + calc.inputValue;
        } else {
            // Otherwise add the number to input value
            calc.inputValue += inputKey;
            // Replace output value with the new input value
            calc.outputValue = calc.inputValue;
        }

        // Equal button has not been pressed
        calc.isEqual = false; //Bugfix for example 2.3 + 5 = .

        // Display the output
        displayOutput(calc.outputValue);

        if (DEBUG)
            System.out.println("Number(after) : button=" + inputKey + ", inputValue=" + calc.inputValue + ", outputValue=" + calc.outputValue + ", Display=" + display.getText() + ", currentOperation=" + calc.getOperationSign(calc.currentOperator) + ", hasResult=" + calc.hasResult + ", hasOperator=" + calc.hasOperator);
    }

    // ========= DECIMAL INPUT =========
    private void decimalInput(String decimal)
    {
        if (DEBUG)
            System.out.println("Decimal(before) : inputValue=" + calc.inputValue + ", outputValue=" + calc.outputValue + ", Display=" + display.getText() + ", currentOperation=" + calc.getOperationSign(calc.currentOperator) + ", hasResult=" + calc.hasResult + ", hasOperator=" + calc.hasOperator);

        if (calc.hasResult) { // Calculation has result
            // Reset all values
            calc.resetValues();
        } else { // Calculation has no result
            try {
                // Get first and second value from the output
                String[] splitOutputValue = calc.outputValue.split("[" + calc.getOperationSign(calc.currentOperator) + "]");

                if (calc.hasOperator) { // Calculation has operator
                    // Set output value to the first split output value plus the current operator sign
                    calc.outputValue = splitOutputValue[0] + calc.getOperationSign(calc.currentOperator);
                    // Set input value to the second split output value
                    calc.inputValue = splitOutputValue[1];
                } else { // Otherwise if calculation has no operator
                    // Set output value to an empty string
                    calc.outputValue = "";

                    // If equal has been pressed before this button then replace inputValue with an empty string, otherwise add the first split value
                    calc.inputValue = calc.isEqual ? "" : splitOutputValue[0];
                }
            } catch (Exception e) {
                if (DEBUG)
                    System.out.println(e.getMessage() + ", outputValue=" + calc.outputValue);

                // Error fix when splitting outputValue
                if (!calc.hasOperator) {
                    calc.outputValue = "";
                }
            }

            // Check if input value already contains a decimal otherwise add the decimal separator
            if (!calc.inputValue.contains(decimal)) {
                calc.inputValue = calc.inputValue + decimal;
            }
        }

        // Add a zero before the decimal separator if the input value begins with a decimal
        if (calc.inputValue.startsWith(decimal)) {
            calc.inputValue = "0" + calc.inputValue;
        }

        // Add the decimal and the result of input value to the output value
        calc.outputValue = calc.outputValue + calc.inputValue;

        // Equal button has not been pressed
        calc.isEqual = false;

        // Display the output
        displayOutput(calc.outputValue);

        if (DEBUG)
            System.out.println("Decimal(after) : inputValue=" + calc.inputValue + ", outputValue=" + calc.outputValue + ", Display=" + display.getText() + ", currentOperation=" + calc.getOperationSign(calc.currentOperator) + ", hasResult=" + calc.hasResult + ", hasOperator=" + calc.hasOperator);
    }

    // ========= OPERATOR INPUT =========
    private void operatorInput(Calculator.Operator operation)
    {
        if (DEBUG)
            System.out.println("Operator(before) : button=" + calc.getOperationSign(operation) + ", inputValue=" + calc.inputValue + ", outputValue=" + calc.outputValue + ", Display=" + display.getText() + ", currentOperation=" + calc.getOperationSign(calc.currentOperator) + ", hasResult=" + calc.hasResult + ", hasOperator=" + calc.hasOperator);

        // Save current operator as previous operator
        Calculator.Operator previousOperator = calc.currentOperator;

        // Variable to flag when no calculation shall be done when error happens, for example 12 * /
        boolean doNotCalculate = false;

        if (calc.hasOperator) { // Calculation has operator
            try {
                // Get first and second value from the output
                String[] splitOutputValue = calc.outputValue.split("[" + calc.getOperationSign(previousOperator) + "]");

                // Reset values
                calc.resetValues();

                // Set output value to the first split output value
                calc.outputValue = splitOutputValue[0];
                // Set input value to the second split output value
                calc.inputValue = splitOutputValue[1];

                // Add minus sign in the beginning if current output value is empty and previous operator is minus
                if (previousOperator == Calculator.Operator.MINUS && calc.outputValue.isEmpty()) {
                    // Set output value to the second split output value
                    calc.outputValue = calc.getOperationSign(Calculator.Operator.MINUS) + splitOutputValue[1];
                    // Set input value to the third split output value
                    calc.inputValue = splitOutputValue[2];
                }
            } catch (Exception e) {
                if (DEBUG)
                    System.out.println(e.getMessage() + ", outputValue=" + calc.outputValue);

                // Set flag to not do any calculation when the error happens, for example 12 * /
                doNotCalculate = true;

                // Set input value to zero
                calc.inputValue = "0";

                // Replace the decimal from localities with a dot for the calculation
                calc.resultValue = new BigDecimal(calc.outputValue.replace(DECIMAL_SEPARATOR, '.'));
            }

            // Convert and replace the decimal from localities with a dot for the calculation
            calc.firstValue = new BigDecimal(calc.outputValue.replace(DECIMAL_SEPARATOR, '.'));
            calc.secondValue = new BigDecimal(calc.inputValue.replace(DECIMAL_SEPARATOR, '.'));

            // Fix for example 12 * %
            if (!doNotCalculate) {
                try {
                    // Calculate the result
                    calc.resultValue = calc.calculate(calc.firstValue, calc.secondValue, previousOperator);
                } catch (Exception e) {
                    if (DEBUG)
                        System.out.println(e.getMessage());

                    // Convert and replace the decimal from localities with a dot for the calculation
                    calc.resultValue = new BigDecimal(calc.outputValue.replace(DECIMAL_SEPARATOR, '.'));
                }
            }

            // Set current operator
            calc.currentOperator = operation;

            // Set input value to an empty string
            calc.inputValue = "";

            // Replace output value with the result and local decimal separator, plus the current operator sign
            calc.outputValue = String.valueOf(calc.resultValue).replace('.', DECIMAL_SEPARATOR) + calc.getOperationSign(calc.currentOperator);
        } else if (calc.outputValue.isEmpty()) { // Output value is empty
            // Reset values
            calc.resetValues();

            // Set current operator
            calc.currentOperator = operation;

            // Set input value to zero
            calc.inputValue = "0";
            // Replace output value with the input value plus the current operator sign
            calc.outputValue = calc.inputValue + calc.getOperationSign(calc.currentOperator);
        } else { // Calculation has no operator and output value is not empty
            // Set current operator
            calc.currentOperator = operation;

            // Set input value to an empty string
            calc.inputValue = "";

            // Add the current operator sign to output value
            calc.outputValue = calc.outputValue + calc.getOperationSign(calc.currentOperator);
        }

        // Tell the app that an operator is present
        calc.hasOperator = true;

        // Equal button has not been pressed
        calc.isEqual = false;

        // Display the output
        displayOutput(calc.outputValue);

        if (DEBUG)
            System.out.println("Operator(after) : button=" + calc.getOperationSign(operation) + ", resultValue=" + calc.resultValue + ", firstValue=" + calc.firstValue + ", secondValue=" + calc.secondValue + ", inputValue=" + calc.inputValue + ", outputValue=" + calc.outputValue + ", Display=" + display.getText() + ", currentOperation=" + calc.getOperationSign(calc.currentOperator) + ", hasResult=" + calc.hasResult + ", hasOperator=" + calc.hasOperator);
    }

    // ========= EQUAL =========
    private void equal() {
        if (DEBUG)
            System.out.println("Equal(before) : inputValue=" + calc.inputValue + ", outputValue=" + calc.outputValue + ", Display=" + display.getText() + ", currentOperation=" + calc.getOperationSign(calc.currentOperator) + ", hasResult=" + calc.hasResult);

        try {
            // Get first and second value from the output
            String[] splitOutputValue = calc.outputValue.split("[" + calc.getOperationSign(calc.currentOperator) + "]");

            // Set output value to the first split output value
            calc.outputValue = splitOutputValue[0];

            try {
                // Add minus sign in the beginning if current output value is empty and current operator is minus
                if (calc.currentOperator == Calculator.Operator.MINUS && calc.outputValue.isEmpty()) {
                    // Set output value to the second split output value
                    calc.outputValue = calc.getOperationSign(Calculator.Operator.MINUS) + splitOutputValue[1];
                    // Set input value to the third split output value
                    calc.inputValue = splitOutputValue[2];
                } else { // Otherwise set input value to the second split output value
                    calc.inputValue = splitOutputValue[1];
                }
            } catch (Exception e) {
                if (DEBUG)
                    System.out.println(e.getMessage() + ", Inner Try: outputValue=" + calc.outputValue + ", inputValue=" + calc.inputValue);
            }
        } catch (Exception e) {
            if (DEBUG)
                System.out.println(e.getMessage() + ", Outer Try: outputValue=" + calc.outputValue);
        }

        try {
            // Convert and replace the decimal from localities with a dot for the calculation
            calc.firstValue = new BigDecimal(calc.outputValue.replace(DECIMAL_SEPARATOR, '.'));
            calc.secondValue = new BigDecimal(calc.inputValue.replace(DECIMAL_SEPARATOR, '.'));

            // Calculate the result
            calc.resultValue = calc.calculate(calc.firstValue, calc.secondValue, calc.currentOperator);

            // Set input value to an empty string
            calc.inputValue = "";

            // Convert and replace the decimal from localities with a dot for the calculation
            calc.outputValue = String.valueOf(calc.resultValue).replace('.', DECIMAL_SEPARATOR);
        } catch (Exception e) {
            if (DEBUG)
                System.out.println(e.getMessage() + ", After Try: outputValue=" + calc.outputValue + ", inputValue=" + calc.inputValue);

            // Fix if operator is present with no second value and also reset all values if Equal been pressed twice,
            // otherwise reset all values except for the output value
            String tmpOutputValue = calc.hasOperator ? calc.outputValue : "";

            // Reset values
            calc.resetValues();

            // Restore the output value if needed
            calc.outputValue = tmpOutputValue;
        }

        // Set current operator to none
        calc.currentOperator = Calculator.Operator.NONE;

        // No operator is used
        calc.hasOperator = false;

        // Set a flag that no result has been calculated, even if it has been done
        calc.hasResult = false;

        // Tell the app that equal has been pressed
        calc.isEqual = true;

        // Display the output
        displayOutput(calc.outputValue);

        if (DEBUG)
            System.out.println("Equal(before) : inputValue=" + calc.inputValue + ", outputValue=" + calc.outputValue + ", Display=" + display.getText() + ", currentOperation=" + calc.getOperationSign(calc.currentOperator) + ", hasResult=" + calc.hasResult);
    }

    // ========= DISPLAY OUTPUT =========
    private void displayOutput(String output) {
        StringBuilder space = new StringBuilder();

        // Add a zero if output is empty
        String tmp = output.isEmpty() ? "0" : output;

        // Add space so the output is aligned at right
        space.append(" ".repeat(Math.max(0, MAX_INPUT_LENGTH - tmp.length())));

        // Display output with needed spaces
        display.setText(space + tmp);
    }
}

