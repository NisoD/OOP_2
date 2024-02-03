package bricker.brick_strategies;

import bricker.gameobjects.Ball;
import bricker.gameobjects.Paddle;
import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.util.Vector2;

public class PuckCollisionStrategy implements CollisionStrategy{
    private BrickerGameManager brickerGameManager;

    /**
     * the constructor of the BasicCollisionStrategy
     * @param brickerGameManager the game manager
     */
    public PuckCollisionStrategy(BrickerGameManager brickerGameManager) {
        this.brickerGameManager = brickerGameManager;
    }

    /**
     * what happens on collision: make two puck balls, then remove the brick
     * @param collider the brick that the other object collided with
     * @param other the other object that collided with the brick
     */
    @Override
    public void onCollision(GameObject collider, GameObject other) {
        if (other instanceof Ball){
            brickerGameManager.MakeTwoPucks(collider.getCenter());
            brickerGameManager.RemoveBrickFromGame(collider);
        }
    }
}
