package client.scenes;

import client.utils.AnimationUtils;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;

public interface ButtonCtrl {

    AnimationUtils animationsUtil = new AnimationUtils(TranslateTransition::new);

    /**
     * Sets the hover effects
     */
    default void setHoverEffects() {}

    /**
     * Sets the hover effects for a button
     * @param button the button to set the hover effects
     */
    default void setHoverEffects(Node button) {
        button.setOnMouseEntered((event) -> this.onMouseEnter(button));
        button.setOnMouseExited((event -> this.onMouseExit(button)));
    }

    /**
     * Hover effects
     *
     * @param node the node to set the effects on
     */
    @SuppressWarnings("unused")
    default void onMouseEnter(Node node) {
        animationsUtil.throwShadow(node, 3.5f);
    }

    /**
     * Hover effects
     *
     * @param node the node to set the effects on
     */
    @SuppressWarnings("unused")
    default void onMouseExit(Node node) {
        animationsUtil.clearEffect(node);
    }
}
