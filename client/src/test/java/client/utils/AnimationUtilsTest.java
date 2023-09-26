package client.utils;

import static org.mockito.Mockito.*;

import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.junit.jupiter.api.Test;
import java.util.function.Supplier;

public class AnimationUtilsTest {

    @Test
    public void testMoveTextAnimation() {
        Supplier<TranslateTransition> translationSupplier = mock(Supplier.class);
        TranslateTransition translateTransition = mock(TranslateTransition.class);
        when(translationSupplier.get()).thenReturn(translateTransition);
        Text text = mock(Text.class);
        AnimationUtils animationUtils = new AnimationUtils(translationSupplier);
        animationUtils.moveTextAnimation(text, 100);
        verify(translateTransition).setNode(text);
        verify(translateTransition).setDuration(Duration.millis(200));
        verify(translateTransition).setByX(100);
        verify(translateTransition).play();
    }

    @Test
    public void testPositionBackAnimation() {
        Supplier<TranslateTransition> translationSupplier = mock(Supplier.class);
        TranslateTransition translateTransition = mock(TranslateTransition.class);
        when(translationSupplier.get()).thenReturn(translateTransition);
        Node node = mock(Node.class);
        AnimationUtils animationUtils = new AnimationUtils(translationSupplier);
        animationUtils.positionBackAnimation(node);
        verify(translateTransition).setNode(node);
        verify(translateTransition).setDuration(Duration.millis(200));
        verify(translateTransition).setToX(0);
        verify(translateTransition).play();
    }

    @Test
    public void testThrowShadow() {
        Node node = mock(Node.class);
        AnimationUtils animationUtils = new AnimationUtils(null);
        animationUtils.throwShadow(node, 10.0f);
        verify(node).setEffect(any(DropShadow.class));
    }

    @Test
    public void testClearEffect() {
        Node node = mock(Node.class);
        AnimationUtils animationUtils = new AnimationUtils(null);
        animationUtils.clearEffect(node);
        verify(node).setEffect(null);
    }
}
