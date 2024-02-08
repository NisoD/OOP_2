package bricker.gameobjects;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.Layer;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * the second paddle class, responsible for the behavior of the second paddle
 */
public class SecondPaddle extends Paddle{
    private Counter ballCoillisionNumber;
    private BrickerGameManager brickerGameManager;

    /**
     * Construct a new GameObject instance, makes a new collision counter
     * @param topLeftCorner    Position of the object, in window coordinates (pixels).
     *                         Note that (0,0) is the top-left corner of the window.
     * @param dimensions       Width and height in window coordinates.
     * @param renderable       The renderable representing the object. Can be null, in which case
     *                         the GameObject will not be rendered.
     * @param inputListener the input listener
     * @param windowDimensions the window dimensions of the game
     */
    public SecondPaddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                        UserInputListener inputListener, Vector2 windowDimensions,
                        BrickerGameManager brickerGameManager) {
        super(topLeftCorner, dimensions, renderable, inputListener, windowDimensions);
        ballCoillisionNumber = new Counter(0);
        this.brickerGameManager = brickerGameManager;
        this.setTag("SecondPaddle");
    }

    /**
     * getter for number of balls that collided with the paddle
     * @return number of balls that collided with the paddle
     */
    public int getBallCoillisionNumber() { return ballCoillisionNumber.value(); }

    /**
     * is the collider was a ball, increments the number of ball collision
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other.getTag().equals("Ball") || other.getTag().equals("Puck")){
            ballCoillisionNumber.increment();
            if (ballCoillisionNumber.value() >= 4){
                brickerGameManager.RemoveItemFromGame(this, Layer.DEFAULT);
            }
        }
    }
}
