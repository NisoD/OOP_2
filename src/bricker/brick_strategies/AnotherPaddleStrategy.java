package bricker.brick_strategies;

import bricker.gameobjects.Ball;
import bricker.gameobjects.SecondPaddle;
import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

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
        if (other.getTag().equals("Ball")){
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
