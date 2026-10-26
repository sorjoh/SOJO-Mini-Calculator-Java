package se.sojo.minicalculator;

import javax.swing.*;
import java.text.DecimalFormatSymbols;

public class Main extends JFrame {
    // Use debugging if needed
    private static final Boolean DEBUG = true;

    // Max length of the display numbers
    private static final Integer MAX_INPUT_LENGTH = 15;

    public static void main(String[] args) {
        new Main();
    }

    private Main() {
        // Get decimal separator
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        char DECIMAL_SEPARATOR = dfs.getDecimalSeparator();

        // Variable for Calculator methods and variables
        Calculator calc;

        try {
            // Initialize Calculator
            calc = new Calculator();
            calc.resetValues();

            // Initialize UI
            UI uiCal = new UI(calc, DECIMAL_SEPARATOR, MAX_INPUT_LENGTH, DEBUG);
            uiCal.init();
        } catch (Exception e) {
            if (DEBUG)
                System.out.println(e.getMessage());
        }
    }
}


