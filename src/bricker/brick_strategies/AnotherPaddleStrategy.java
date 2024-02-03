package bricker.brick_strategies;

import bricker.gameobjects.Ball;
import bricker.main.BrickerGameManager;
import danogl.GameObject;

public class AnotherPaddleStrategy implements CollisionStrategy {
    private BrickerGameManager brickerGameManager;

    /**
     * the constructor of the AnotherPaddleStrategy
     * @param brickerGameManager the game manager
     */
    public AnotherPaddleStrategy(BrickerGameManager brickerGameManager) {
        this.brickerGameManager = brickerGameManager;
    }
    @Override
    public void onCollision(GameObject collider, GameObject other) {
        if (other instanceof Ball){
            brickerGameManager.AddSecondPaddle();
            brickerGameManager.RemoveBrickFromGame(collider);
        }
    }
}
