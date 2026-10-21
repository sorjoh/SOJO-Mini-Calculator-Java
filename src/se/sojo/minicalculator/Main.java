package se.sojo.minicalculator;

import javax.swing.*;
import java.text.DecimalFormatSymbols;

public class Main extends JFrame {
    private static final Boolean DEBUG = true;
    private static final Integer MAX_INPUT_LENGTH = 15;

    public static void main(String[] args) {
        new Main();
    }

    private Main() {
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        char DECIMAL_SEPARATOR = dfs.getDecimalSeparator();

        Calculator calc;

        try {
            calc = new Calculator();
            calc.resetValues();

            UI uiCal = new UI(calc, DECIMAL_SEPARATOR, MAX_INPUT_LENGTH, DEBUG);
            uiCal.init();
        } catch (Exception e) {
            if (DEBUG)
                System.out.println(e.getMessage());
        }
    }
}


