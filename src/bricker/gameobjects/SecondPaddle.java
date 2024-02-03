package bricker.gameobjects;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.Layer;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

public class SecondPaddle extends Paddle{
    private Counter ballCoillisionNumber;
    private BrickerGameManager brickerGameManager;
    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner    Position of the object, in window coordinates (pixels).
     *                         Note that (0,0) is the top-left corner of the window.
     * @param dimensions       Width and height in window coordinates.
     * @param renderable       The renderable representing the object. Can be null, in which case
     *                         the GameObject will not be rendered.
     * @param inputListener
     * @param windowDimensions
     */
    public SecondPaddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                        UserInputListener inputListener, Vector2 windowDimensions,
                        BrickerGameManager brickerGameManager) {
        super(topLeftCorner, dimensions, renderable, inputListener, windowDimensions);
        ballCoillisionNumber = new Counter(0);
        this.brickerGameManager = brickerGameManager;
    }

    public int getBallCoillisionNumber() {
        return ballCoillisionNumber.value();
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other instanceof Ball){
            ballCoillisionNumber.increment();
            if (ballCoillisionNumber.value() >= 4){
                brickerGameManager.RemoveItemFromGame(this, Layer.DEFAULT);
            }
        }
    }
}
