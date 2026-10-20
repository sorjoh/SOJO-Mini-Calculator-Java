import java.awt.*;

public class Constraints extends GridBagConstraints {

    public Constraints() {
        //GridBagConstraints constraints = new GridBagConstraints();
        weightx = weighty = 1.0;
        fill = BOTH;
        anchor = WEST;
        insets = new Insets(5, 5, 10, 10);
    }

    public void buildConstraints(GridBagConstraints gbc, int gx, int gy, int gw, int gh) {
        gbc.gridx = gx;
        gbc.gridy = gy;
        gbc.gridwidth = gw;
        gbc.gridheight = gh;
    }
}
