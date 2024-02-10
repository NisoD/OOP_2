package bricker.brick_strategies;

import bricker.gameobjects.Ball;
import bricker.gameobjects.Puck;
import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Vector2;

/**
 * the strategy that changes the main camera of the game, if it's not changed already
 */
public class CameraStrategy implements CollisionStrategy{
    private BrickerGameManager brickerGameManager;
    private final WindowController windowController;

    /**
     * the constructor of the CameraStrategy
     * @param brickerGameManager the game manager
     */
    public CameraStrategy(BrickerGameManager brickerGameManager) {
        this.brickerGameManager = brickerGameManager;
        this.windowController = brickerGameManager.getWindowController();
    }

    /**
     * on collision, checks if the collided was a regular ball and that the camera isn't
     * changed already, if so, changes the camera, removes the brick from the game
     * @param collider the brick that the other object collided with
     * @param other the other object that collided with the brick
     */
    @Override
    public void onCollision(GameObject collider, GameObject other) {
        if (other.getTag().equals("Ball") || other.getTag().equals("Puck")){
            if (!(other.getTag().equals("Puck")) && (brickerGameManager.camera() == null)){
                brickerGameManager.setCamera(new Camera(other, Vector2.ZERO,
                        windowController.getWindowDimensions().mult(1.2f),
                        windowController.getWindowDimensions()));
            }
            brickerGameManager.RemoveBrickFromGame(collider);
        }
    }
}
