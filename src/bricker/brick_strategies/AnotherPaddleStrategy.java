package bricker.brick_strategies;

import bricker.gameobjects.Ball;
import bricker.gameobjects.SecondPaddle;
import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * the class that makes the second paddle,
 * if there isn't already a paddle on the screen
 */
public class AnotherPaddleStrategy implements CollisionStrategy {
    private BrickerGameManager brickerGameManager;

    /**
     * the constructor of the AnotherPaddleStrategy
     * @param brickerGameManager the game manager
     */
    public AnotherPaddleStrategy(BrickerGameManager brickerGameManager) {
        this.brickerGameManager = brickerGameManager;
    }

    /**
     * on collision, checks if there is another paddle on screen,
     * if there isn't makes one and adds it. removes the brick from the game
     * @param collider the brick that the other object collided with
     * @param other the other object that collided with the brick
     */
    @Override
    public void onCollision(GameObject collider, GameObject other) {
        if (other.getTag().equals("Ball") || other.getTag().equals("Puck")){
            if (!brickerGameManager.getIsThereSecondPaddle()){
                Vector2 windowDimensions = brickerGameManager.getWindowDimensions();
                brickerGameManager.setIsThereSecondPaddle(true);
                Renderable paddleImage =
                        brickerGameManager.getImageReader().
                                readImage("assets/paddle.png", false);
                SecondPaddle secondPaddle = new SecondPaddle(Vector2.ZERO, // position
                        brickerGameManager.getPaddleSize(), paddleImage,
                        brickerGameManager.getInputListener(), windowDimensions ,
                        brickerGameManager);
                secondPaddle.setCenter(new Vector2(windowDimensions.x()/2, windowDimensions.y()/2));
                brickerGameManager.AddGameObjectToGame(secondPaddle);
            }
            brickerGameManager.RemoveBrickFromGame(collider); // removes the brick from the game
        }
    }
}
