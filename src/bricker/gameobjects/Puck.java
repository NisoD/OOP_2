package bricker.gameobjects;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.Layer;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.util.Random;

/**
 * class for the Puck ball, responsible for the behavior of the puck ball
 */
public class Puck extends Ball{
    private static final float SPEED = 200;
    private static final float DOWN_POSITION = 20;
    private Vector2 brickPosition;
    private BrickerGameManager brickerGameManager;
    private final Vector2 windowDimensions;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner  Position of the object, in window coordinates (pixels).
     *                       Note that (0,0) is the top-left corner of the window.
     * @param dimensions     Width and height in window coordinates.
     * @param renderable     The renderable representing the object. Can be null, in which case
     *                       the GameObject will not be rendered.
     * @param collisionSound the sound when collision happens
     * @param brickPosition the position of the brick that the puck is made from
     * @param brickerGameManager the game manager
     * @param windowDimensions the window dimensions
     */
    public Puck(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Sound collisionSound,
                Vector2 brickPosition, BrickerGameManager brickerGameManager,
                Vector2 windowDimensions) {
        super(topLeftCorner, dimensions, renderable, collisionSound);
        this.brickPosition = brickPosition;
        this.brickerGameManager = brickerGameManager;
        this.windowDimensions = windowDimensions;
        this.setTag("Puck");
        RandomDiagonal();
    }

    /**
     * calculates a random diagonal that is the velocity of the ball,
     * sets the center of the ball
     */
    private void RandomDiagonal(){
        float ballVelX,ballVelY;
        Random rand = new Random();
        double angle = rand.nextDouble() * Math.PI;
        ballVelX = (float)Math.cos(angle) * SPEED;
        ballVelY = (float)Math.sin(angle) * SPEED;
        this.setVelocity(new Vector2(ballVelX, ballVelY));

        this.setCenter(new Vector2(brickPosition.x(), brickPosition.y() + DOWN_POSITION));
    }

    /**
     * checks if the ball is out of the window, if so- removes it
     * @param deltaTime The time elapsed, in seconds, since the last frame. Can
     *                  be used to determine a new position/velocity by multiplying
     *                  this delta with the velocity/acceleration respectively
     *                  and adding to the position/velocity:
     *                  velocity += deltaTime*acceleration
     *                  pos += deltaTime*velocity
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float puckHeight = this.getCenter().y();

        if (puckHeight > windowDimensions.y()) {
            brickerGameManager.RemoveItemFromGame(this, Layer.DEFAULT);
        }
    }
}
