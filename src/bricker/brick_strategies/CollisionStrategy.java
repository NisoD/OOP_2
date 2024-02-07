package bricker.brick_strategies;

import danogl.GameObject;

/**
 * the interface of the brick collision strategy
 */
public interface CollisionStrategy {
    /**
     * what happens on collision
     * @param collider the brick that the other object collided with
     * @param other the other object that collided with the brick
     */
    void onCollision(GameObject collider ,GameObject other);
}
