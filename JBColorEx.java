import com.intellij.ui.JBColor;

import java.awt.*;

public class JBColorEx extends JBColor {
    public JBColorEx(int rgb, int darkRGB) {
        super(rgb, darkRGB);
    }

    public static final Color green_light = new Color(218, 255, 198);
    public static final Color GREEN_LIGHT = green_light;

    public static final Color red_light = new Color(255, 203, 179);
    public static final Color RED_LIGHT = red_light;

    public static final Color blue_light = new Color(214, 224, 233);
    public static final Color BLUE_LIGHT = blue_light;
}
