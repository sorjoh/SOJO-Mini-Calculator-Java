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

    public UI(Calculator calc, char decimalSeparator, Integer maxInputLength, Boolean debug) {
        this.calc = calc;

        DECIMAL_SEPARATOR = decimalSeparator;
        MAX_INPUT_LENGTH = maxInputLength;
        DEBUG = debug;

        frame = new JFrame("MiniCalc");
        frame.setSize(WIDTH, HEIGHT);

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panelRow1 = new JPanel(new FlowLayout());
        panelRow2 = new JPanel(new FlowLayout());
        panelRow3 = new JPanel(new FlowLayout());
        panelRow4 = new JPanel(new FlowLayout());
        panelRow5 = new JPanel(new FlowLayout());

        font = new Font("Consolas",Font.PLAIN, 18);
        textFont = new Font("Consolas",Font.BOLD, 24);

        display = new JTextArea(1, MAX_INPUT_LENGTH);

        buttonNumber = new JButton[10];
        for (int i = 0; i < 10; i++) {
            buttonNumber[i] = new JButton(String.valueOf(i));
        }
        buttonDecimal = new JButton(String.valueOf(DECIMAL_SEPARATOR));
        buttonPlus = new JButton(calc.getOperationSign(Calculator.Operator.PLUS));
        buttonMinus = new JButton(calc.getOperationSign(Calculator.Operator.MINUS));
        buttonMultiply = new JButton(calc.getOperationSign(Calculator.Operator.MULTIPLY));
        buttonDivide = new JButton(calc.getOperationSign(Calculator.Operator.DIVIDE));
        buttonEqual = new JButton("=");

        displayOutput(calc.outputValue);
    }

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

        panelRow1.add(display);
        panelRow1.add(Box.createVerticalStrut(30));
        panel.add(panelRow1);

        panelRow2.add(buttonNumber[7]);
        panelRow2.add(buttonNumber[8]);
        panelRow2.add(buttonNumber[9]);
        panelRow2.add(buttonMultiply);
        panel.add(panelRow2);

        panelRow3.add(buttonNumber[4]);
        panelRow3.add(buttonNumber[5]);
        panelRow3.add(buttonNumber[6]);
        panelRow3.add(buttonDivide);
        panel.add(panelRow3);

        panelRow4.add(buttonNumber[1]);
        panelRow4.add(buttonNumber[2]);
        panelRow4.add(buttonNumber[3]);
        panelRow4.add(buttonPlus);
        panel.add(panelRow4);

        panelRow5.add(buttonNumber[0]);
        panelRow5.add(buttonDecimal);
        panelRow5.add(buttonEqual);
        panelRow5.add(buttonMinus);
        panel.add(panelRow5);

        for (int i = 0; i < 10; i++) {
            buttonNumber[i].addActionListener(this);
        }

        buttonDecimal.addActionListener(this);
        buttonPlus.addActionListener(this);
        buttonMinus.addActionListener(this);
        buttonMultiply.addActionListener(this);
        buttonDivide.addActionListener(this);
        buttonEqual.addActionListener(this);

        frame.add(panel);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final Object source = e.getSource();

        for (int i = 0; i < 10; i++) {
            if (source == buttonNumber[i]) {
                numberInput(buttonNumberValue[i]);
                return;
            }
        }

        if (source == buttonDecimal) {
            decimalInput(String.valueOf(DECIMAL_SEPARATOR));
        }

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

        if (source == buttonEqual) {
            equal();
        }
    }

    // ========= NUMBER INPUT =========
    public void numberInput(String inputKey) {
        if (DEBUG)
            System.out.println("Number(before) : button=" + inputKey + ", inputValue=" + calc.inputValue + ", outputValue=" + calc.outputValue + ", Display=" + display.getText() + ", currentOperation=" + calc.getOperationSign(calc.currentOperator) + ", hasResult=" + calc.hasResult + ", hasOperator=" + calc.hasOperator);

        if (calc.hasResult) {
            calc.resetValues();
            calc.inputValue = inputKey;
            calc.outputValue = calc.inputValue;
        } else if (calc.hasOperator) {
            String currentResultValue;

            try {
                String[] splitOutputValue = calc.outputValue.trim().split("[" + calc.getOperationSign(calc.currentOperator) + "]");

                if (Objects.equals(calc.inputValue, "0"))
                    calc.inputValue = inputKey;
                else
                    calc.inputValue += inputKey;

                currentResultValue = splitOutputValue[0];

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

            calc.outputValue = currentResultValue + calc.getOperationSign(calc.currentOperator) + calc.inputValue;
        } else {
            calc.inputValue += inputKey;
            calc.outputValue = calc.inputValue;
        }

        calc.isEqual = false; //Bugfix for example 2.3 + 5 = .

        displayOutput(calc.outputValue);

        if (DEBUG)
            System.out.println("Number(after) : button=" + inputKey + ", inputValue=" + calc.inputValue + ", outputValue=" + calc.outputValue + ", Display=" + display.getText() + ", currentOperation=" + calc.getOperationSign(calc.currentOperator) + ", hasResult=" + calc.hasResult + ", hasOperator=" + calc.hasOperator);
    }

    // ========= DECIMAL INPUT =========
    private void decimalInput(String decimal)
    {
        if (DEBUG)
            System.out.println("Decimal(before) : inputValue=" + calc.inputValue + ", outputValue=" + calc.outputValue + ", Display=" + display.getText() + ", currentOperation=" + calc.getOperationSign(calc.currentOperator) + ", hasResult=" + calc.hasResult + ", hasOperator=" + calc.hasOperator);

        if (calc.hasResult) {
            calc.resetValues();
        } else {
            try {
                String[] splitOutputValue = calc.outputValue.split("[" + calc.getOperationSign(calc.currentOperator) + "]");

                if (calc.hasOperator) {
                    calc.outputValue = splitOutputValue[0] + calc.getOperationSign(calc.currentOperator);
                    calc.inputValue = splitOutputValue[1];
                } else {
                    calc.outputValue = "";

                    //Bugfix for example 2.3 + 5 = .
                    if (calc.isEqual)
                        calc.inputValue = "";
                    else
                        calc.inputValue = splitOutputValue[0];
                }
            } catch (Exception e) {
                if (DEBUG)
                    System.out.println(e.getMessage() + ", outputValue=" + calc.outputValue);

                // Error fix when splitting outputValue
                if (!calc.hasOperator) {
                    calc.outputValue = "";
                }
            }

            if (!calc.inputValue.contains(decimal)) {
                calc.inputValue = calc.inputValue + decimal;
            }
        }

        if (calc.inputValue.startsWith(decimal)) {
            calc.inputValue = "0" + calc.inputValue;
        }

        calc.outputValue = calc.outputValue + calc.inputValue;

        calc.isEqual = false; //Bugfix for example 2.3 + 5 = .

        displayOutput(calc.outputValue);

        if (DEBUG)
            System.out.println("Decimal(after) : inputValue=" + calc.inputValue + ", outputValue=" + calc.outputValue + ", Display=" + display.getText() + ", currentOperation=" + calc.getOperationSign(calc.currentOperator) + ", hasResult=" + calc.hasResult + ", hasOperator=" + calc.hasOperator);
    }

    // ========= OPERATOR INPUT =========
    private void operatorInput(Calculator.Operator operation)
    {
        if (DEBUG)
            System.out.println("Operator(before) : button=" + calc.getOperationSign(operation) + ", inputValue=" + calc.inputValue + ", outputValue=" + calc.outputValue + ", Display=" + display.getText() + ", currentOperation=" + calc.getOperationSign(calc.currentOperator) + ", hasResult=" + calc.hasResult + ", hasOperator=" + calc.hasOperator);

        Calculator.Operator previousOperator = calc.currentOperator;

        boolean doNotCalculate = false; // Fix for example 12 * %

        if (calc.hasOperator) {
            try {
                String[] splitOutputValue = calc.outputValue.split("[" + calc.getOperationSign(previousOperator) + "]");

                calc.resetValues();

                calc.outputValue = splitOutputValue[0];
                calc.inputValue = splitOutputValue[1];

                if (previousOperator == Calculator.Operator.MINUS && calc.outputValue.isEmpty()) {
                    calc.outputValue = calc.getOperationSign(Calculator.Operator.MINUS) + splitOutputValue[1];
                    calc.inputValue = splitOutputValue[2];
                }
            } catch (Exception e) {
                if (DEBUG)
                    System.out.println(e.getMessage() + ", outputValue=" + calc.outputValue);

                doNotCalculate = true; // Fix for example 12 * %

                calc.inputValue = "0";
                calc.resultValue = new BigDecimal(calc.outputValue.replace(DECIMAL_SEPARATOR, '.'));
            }

            calc.firstValue = new BigDecimal(calc.outputValue.replace(DECIMAL_SEPARATOR, '.'));
            calc.secondValue = new BigDecimal(calc.inputValue.replace(DECIMAL_SEPARATOR, '.'));

            // Fix for example 12 * %
            if (!doNotCalculate) {
                try {
                    calc.resultValue = calc.calculate(calc.firstValue, calc.secondValue, previousOperator);
                } catch (Exception e) {
                    System.out.println(e.getMessage());

                    calc.resultValue = new BigDecimal(calc.outputValue.replace(DECIMAL_SEPARATOR, '.'));
                }
            }

            calc.currentOperator = operation;

            calc.inputValue = "";
            calc.outputValue = String.valueOf(calc.resultValue).replace('.', DECIMAL_SEPARATOR) + calc.getOperationSign(calc.currentOperator);
        } else if (calc.outputValue.isEmpty()) {
            calc.resetValues();

            calc.currentOperator = operation;

            calc.inputValue = "0";
            calc.outputValue = calc.inputValue + calc.getOperationSign(calc.currentOperator);
        } else {
            calc.currentOperator = operation;

            calc.inputValue = "";
            calc.outputValue = calc.outputValue + calc.getOperationSign(calc.currentOperator);
        }

        calc.hasOperator = true;

        calc.isEqual = false; //Bugfix for example 2.3 + 5 = .

        displayOutput(calc.outputValue);

        if (DEBUG)
            System.out.println("Operator(after) : button=" + calc.getOperationSign(operation) + ", resultValue=" + calc.resultValue + ", firstValue=" + calc.firstValue + ", secondValue=" + calc.secondValue + ", inputValue=" + calc.inputValue + ", outputValue=" + calc.outputValue + ", Display=" + display.getText() + ", currentOperation=" + calc.getOperationSign(calc.currentOperator) + ", hasResult=" + calc.hasResult + ", hasOperator=" + calc.hasOperator);
    }

    // ========= EQUAL =========
    private void equal() {
        if (DEBUG)
            System.out.println("Equal(before) : inputValue=" + calc.inputValue + ", outputValue=" + calc.outputValue + ", Display=" + display.getText() + ", currentOperation=" + calc.getOperationSign(calc.currentOperator) + ", hasResult=" + calc.hasResult);

        try {
            String[] splitOutputValue = calc.outputValue.split("[" + calc.getOperationSign(calc.currentOperator) + "]");

            calc.outputValue = splitOutputValue[0];

            try {
                if (calc.currentOperator == Calculator.Operator.MINUS && calc.outputValue.isEmpty()) {
                    calc.outputValue = calc.getOperationSign(Calculator.Operator.MINUS) + splitOutputValue[1];
                    calc.inputValue = splitOutputValue[2];
                } else {
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
            calc.firstValue = new BigDecimal(calc.outputValue.replace(DECIMAL_SEPARATOR, '.'));
            calc.secondValue = new BigDecimal(calc.inputValue.replace(DECIMAL_SEPARATOR, '.'));

            calc.resultValue = calc.calculate(calc.firstValue, calc.secondValue, calc.currentOperator);

            calc.inputValue = "";
            calc.outputValue = String.valueOf(calc.resultValue).replace('.', DECIMAL_SEPARATOR);
        } catch (Exception e) {
            if (DEBUG)
                System.out.println(e.getMessage() + ", After Try: outputValue=" + calc.outputValue + ", inputValue=" + calc.inputValue);

            // Fix if operator is present with no second value
            String tmpOutputValue = calc.hasOperator ? calc.outputValue : "";

            calc.resetValues();

            calc.outputValue = tmpOutputValue;
        }

        calc.currentOperator = Calculator.Operator.NONE;
        calc.hasOperator = false;
        calc.hasResult = false;

        calc.isEqual = true; //Bugfix for example 2.3 + 5 = .

        displayOutput(calc.outputValue);

        if (DEBUG)
            System.out.println("Equal(before) : inputValue=" + calc.inputValue + ", outputValue=" + calc.outputValue + ", Display=" + display.getText() + ", currentOperation=" + calc.getOperationSign(calc.currentOperator) + ", hasResult=" + calc.hasResult);
    }

    // ========= DISPLAY OUTPUT =========
    private void displayOutput(String output) {
        StringBuilder space = new StringBuilder();

        String tmp = "";

        if (output.isEmpty())
            tmp = "0";
        else
            tmp = output;

        space.append(" ".repeat(Math.max(0, MAX_INPUT_LENGTH - tmp.length())));
        display.setText(space + tmp);
    }
}

