package client.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CustomizationUtilsTest {

    private CustomizationUtils colorUtils;

    @BeforeEach
    void setup() {
        colorUtils = new CustomizationUtils();
    }

    @Test
    public void whiteTest() {
        assertEquals(Color.BLACK, colorUtils.getContrastingColor("#FFFFFF"));
    }

    @Test
    public void blackTest() {
        assertEquals(Color.WHITE, colorUtils.getContrastingColor("#000000"));
    }

    @Test
    public void lightBlue() {
        assertEquals(Color.BLACK, colorUtils.getContrastingColor("#ADD8E6"));

    }

    @Test
    public void deepBlue() {
        assertEquals(Color.WHITE, colorUtils.getContrastingColor("#00008b"));
    }

    @Test
    public void nullColor(){
        assertEquals(Color.BLACK, colorUtils.getContrastingColor(null));
    }

    @Test
    public void testToString() {
        Color color = Color.color(1, 1, 0); // Yellow
        String hexString = colorUtils.toString(color);
        assertEquals("#FFFF00", hexString);
    }
}
