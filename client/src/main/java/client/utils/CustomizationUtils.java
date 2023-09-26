package client.utils;

import java.util.Arrays;
import java.util.List;
import javafx.scene.paint.Color;
import lombok.Getter;

public class CustomizationUtils {

    @Getter
    private final String defaultBoardOuterColor = "#A69180";

    @Getter
    private final String defaultBoardInnerColor = "#F2E5D5";

    @Getter
    private final String defaultCardColor = "#FFFFFF";

    @Getter
    private final String defaultCardListColor = "#F2E5D5";

    @Getter
    private final String defaultCardListBorderColor = "#B5C7A5";

    @Getter
    private final List<String> fonts = Arrays.asList(
        "Arial", "Calibri", "Gabriola", "Garamond", "Georgia", "Verdana"
    );


    /**
     * @param color to find the contrast of
     * @return the contrasting color
     */
    public Color getContrastingColor(String color) {
        if(color == null)
            return Color.BLACK;

        int red = Integer.parseInt(color.substring(1, 3), 16);
        int green = Integer.parseInt(color.substring(3, 5), 16);
        int blue = Integer.parseInt(color.substring(5, 7), 16);

        double luminance = (0.289 * red + 0.587 * green + 0.114 * blue) / 255;

        return luminance > 0.5 ? Color.BLACK : Color.WHITE;
    }

    /**
     * Convert color to string
     *
     * @param color color
     * @return hex color string
     */
    public String toString(Color color) {
        return String.format(
            "#%02X%02X%02X",
            (int) (color.getRed() * 255),
            (int) (color.getGreen() * 255),
            (int) (color.getBlue() * 255)
        );
    }


}
