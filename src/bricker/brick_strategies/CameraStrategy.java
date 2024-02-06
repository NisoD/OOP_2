package bricker.brick_strategies;

import bricker.gameobjects.Ball;
import bricker.gameobjects.Puck;
import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Vector2;

public class CameraStrategy implements CollisionStrategy{
    private BrickerGameManager brickerGameManager;
    private final WindowController windowController;
    private int timesOfBallCollision;

    /**
     * the constructor of the CameraStrategy
     * @param brickerGameManager the game manager
     */
    public CameraStrategy(BrickerGameManager brickerGameManager) {
        this.brickerGameManager = brickerGameManager;
        this.windowController = brickerGameManager.getWindowController();
    }
    @Override
    public void onCollision(GameObject collider, GameObject other) {
        if (other.getTag().equals("Ball") || other.getTag().equals("Puck")){
            brickerGameManager.RemoveBrickFromGame(collider);
            if (!(other.getTag().equals("Puck")) && (brickerGameManager.camera() == null)){
                timesOfBallCollision = ((Ball) other).getCollisionCounter();
                brickerGameManager.setCamera(new Camera(other, Vector2.ZERO,
                        windowController.getWindowDimensions().mult(1.2f),
                        windowController.getWindowDimensions()));
            }
        }
    }
}
