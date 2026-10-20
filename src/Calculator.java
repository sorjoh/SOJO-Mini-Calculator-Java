import javax.swing.*;
import java.awt.*;

public class Calculator extends JFrame {
    int width = 375;
    int height = 300;

    public static void main(String[] args)  {
        Calculator calculator = new Calculator();
    }

    private Calculator() {
        setSize(width, height);
        center();
        setBackground(Color.lightGray);

        GridBagLayout layout = new GridBagLayout();
        getContentPane().setLayout(layout);

        Constraints constraints = new Constraints();
    }

    void center() {
        Toolkit toolKit = this.getToolkit();
        Dimension screenSize = toolKit.getScreenSize();
        setLocation((screenSize.width - width) / 3, (screenSize.height - height) / 3);
    }
}


