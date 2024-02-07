package bricker.brick_strategies;

import bricker.gameobjects.Ball;
import bricker.gameobjects.Paddle;
import bricker.gameobjects.Puck;
import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * the class that's responsible for the puck balls strategy
 */
public class PuckCollisionStrategy implements CollisionStrategy{
    private BrickerGameManager brickerGameManager;
    private final ImageReader imageReader;
    private final SoundReader soundReader;
    private final Vector2 windowDimensions;

    /**
     * the constructor of the BasicCollisionStrategy
     * @param brickerGameManager the game manager
     */
    public PuckCollisionStrategy(BrickerGameManager brickerGameManager) {
        this.brickerGameManager = brickerGameManager;
        this.imageReader = brickerGameManager.getImageReader();
        this.soundReader = brickerGameManager.getSoundReader();
        this.windowDimensions = brickerGameManager.getWindowDimensions();
    }

    /**
     * what happens on collision: make two puck balls, then remove the brick
     * @param collider the brick that the other object collided with
     * @param other the other object that collided with the brick
     */
    @Override
    public void onCollision(GameObject collider, GameObject other) {
        if (other.getTag().equals("Ball") || other.getTag().equals("Puck")){
            Vector2 brickPosition = collider.getCenter();
            float ballSize = brickerGameManager.getBallSize();
            Sound collisionSound = soundReader.readSound("assets/blop_cut_silenced.wav");
            Renderable puckImage = imageReader.readImage("assets/mockBall.png",
                    true);
            for (int i = 0; i < 2; i++) {
                Puck puckBall = new Puck(Vector2.ZERO,
                        new Vector2(0.75f * ballSize, 0.75f * ballSize), puckImage, collisionSound,
                        brickPosition,brickerGameManager, windowDimensions);
                brickerGameManager.AddGameObjectToGame(puckBall);
            }
            brickerGameManager.RemoveBrickFromGame(collider);
        }
    }
}
