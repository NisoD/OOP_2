package bricker.brick_strategies;

import bricker.gameobjects.Ball;
import bricker.main.BrickerGameManager;
import danogl.GameObject;

/**
 * the BasicCollisionStrategy class, responsible on the behavior of the basic collision
 */
public class BasicCollisionStrategy implements CollisionStrategy{
    private BrickerGameManager brickerGameManager;

    public BasicCollisionStrategy(BrickerGameManager brickerGameManager) {

        this.brickerGameManager = brickerGameManager;
    }

    @Override
    public void onCollision(GameObject collider ,GameObject other) {
        if (other instanceof Ball){
            brickerGameManager.RemoveBrickFromGame(collider);
        }
    }
}
