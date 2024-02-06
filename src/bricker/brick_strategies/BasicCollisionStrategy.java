package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;

/**
 * the BasicCollisionStrategy class, responsible on the behavior of the basic collision
 */
public class BasicCollisionStrategy implements CollisionStrategy{
    private BrickerGameManager brickerGameManager;

    /**
     * the constructor of the BasicCollisionStrategy
     * @param brickerGameManager the game manager
     */
    public BasicCollisionStrategy(BrickerGameManager brickerGameManager) {

        this.brickerGameManager = brickerGameManager;
    }

    /**
     * the implementation of on collision: only makes the brick disappear
     * @param collider the brick that the other object collided with
     * @param other the other object that collided with the brick
     */
    @Override
    public void onCollision(GameObject collider ,GameObject other) {
        if (other.getTag().equals("Ball") || other.getTag().equals("Puck")){
            brickerGameManager.RemoveBrickFromGame(collider);
        }
    }
}
