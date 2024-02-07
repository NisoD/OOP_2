package bricker.gameobjects;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * the Heart class, responsible on the behavior of the Heart
 */
public class Heart extends GameObject {
    private final BrickerGameManager brickerGameManager;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public Heart(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                 BrickerGameManager brickerGameManager) {
        super(topLeftCorner, dimensions, renderable);
        this.brickerGameManager = brickerGameManager;
    }

    /**
     * should not collide with the ball
     * @param other The other GameObject.
     * @return if should Collide With other
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return other.getTag().equals("Paddle");
    }

    /**
     * on collision, removes the brick from the game, adds a heart if the paddle is the
     * regular paddle
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other.getTag().equals("Paddle")) {
            brickerGameManager.RemoveItemFromGame(this, Layer.DEFAULT);
            brickerGameManager.IncrementHeartsLife();
        }
    }

    /**
     * checks if the heart is out of the window, if so- removes it
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
        float heartHeight = this.getCenter().y();

        if (heartHeight > brickerGameManager.getWindowDimensions().y()) {
            brickerGameManager.RemoveItemFromGame(this, Layer.DEFAULT);
        }
    }
}
