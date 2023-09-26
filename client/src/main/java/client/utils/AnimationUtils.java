package client.utils;

import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.util.function.Supplier;

public class AnimationUtils {

    private Supplier<TranslateTransition> translationSupplier;

    /**
     * Constructs and injects new animation util
     * @param translationSupplier mainly a javafx translateTransition serice
     */
    public AnimationUtils (Supplier<TranslateTransition> translationSupplier){
        this.translationSupplier = translationSupplier;
    }

    /**
     * Moves the title of a text place by said offset
     *
     * @param title - the text node with the title
     * @param offset - the offset to move the title by
     */
    public void moveTextAnimation(Text title, int offset) {
        TranslateTransition tt = translationSupplier.get();
        tt.setNode(title);
        tt.setDuration(Duration.millis(200));
        tt.setByX(offset);
        tt.play();
    }

    /**
     * Removes animation offset
     *
     * @param node - the node to remove animation from
     */
    public void positionBackAnimation(Node node) {
        TranslateTransition tt = translationSupplier.get();
        tt.setNode(node);
        tt.setDuration(Duration.millis(200));
        tt.setToX(0);
        tt.play();
    }

    /**
     * Throws shadow animation
     *
     * @param node - the node to put shadow effect
     * @param radius - the radius to which the shadow is thrown
     */
    public void throwShadow(Node node, float radius) {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(radius);
        node.setEffect(dropShadow);
    }

    /**
     * Removes effect of nodes
     *
     * @param node - the node to remove the effect from
     */
    public void clearEffect(Node node) {
        node.setEffect(null);
    }
}
