package bricker.brick_strategies;

import bricker.gameobjects.Heart;
import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * the class that's responsible for the falling life strategy
 */
public class IncrementLifeStrategy implements CollisionStrategy{
    private final ImageReader imageReader;
    private final int SPEED_OF_FALL = 100;
    private final BrickerGameManager brickerGameManager;

    /**
     * constructor for the increment life strategy
     * @param brickerGameManager the game manager
     */
    public IncrementLifeStrategy( BrickerGameManager brickerGameManager){
        this.brickerGameManager = brickerGameManager;
        this.imageReader=brickerGameManager.getImageReader();
    }

    /**
     * on collision, checks if the collided was a kind od ball, if so,
     * makes another heart and adds it to the game. removes the brick from the game
     * @param collider the brick that the other object collided with
     * @param other the other object that collided with the brick
     */
    @Override
    public void onCollision(GameObject collider, GameObject other) {
        if (other.getTag().equals("Ball") || other.getTag().equals("Puck")){
            int lifeSize = brickerGameManager.getLifeCountSize();
            Renderable img = imageReader.readImage("assets/heart.png",true);

            Heart heart = new Heart(new Vector2(collider.getCenter()),new Vector2(lifeSize,lifeSize),img,
                    brickerGameManager);
            heart.setVelocity(new Vector2(0,SPEED_OF_FALL));
            brickerGameManager.AddGameObjectToGame(heart);

            brickerGameManager.RemoveBrickFromGame(collider);
        }
    }
}
