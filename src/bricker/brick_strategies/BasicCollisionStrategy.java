package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;

public class BasicCollisionStrategy implements CollisionStrategy{
    private BrickerGameManager brickerGameManager;

    public BasicCollisionStrategy(BrickerGameManager brickerGameManager) {

        this.brickerGameManager = brickerGameManager;
    }

    @Override
    public void onCollision(GameObject collider ,GameObject other) {
        System.out.println("collision with brick detected");
        brickerGameManager.RemoveBrickFromGame(collider);
    }
}
