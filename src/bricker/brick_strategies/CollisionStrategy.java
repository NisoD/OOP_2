package bricker.brick_strategies;

import danogl.GameObject;

/**
 * the interface of the brick collision strategy
 */
public interface CollisionStrategy {
    void onCollision(GameObject collider ,GameObject other);
}
