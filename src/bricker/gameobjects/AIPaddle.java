package bricker.gameobjects;

import danogl.GameObject;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class AIPaddle extends GameObject {
    private static final float PADDLE_SPEED = 200;
    private GameObject objectToFlollow;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public AIPaddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                    GameObject objectToFlollow) {
        super(topLeftCorner, dimensions, renderable);
        this.objectToFlollow = objectToFlollow;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Vector2 movementDir = Vector2.ZERO;
        if (objectToFlollow.getCenter().x() < getCenter().x()){
            movementDir = Vector2.LEFT;
        }
        else if (objectToFlollow.getCenter().x() > getCenter().x()) {
            movementDir = Vector2.RIGHT;
        }
        setVelocity(movementDir.mult(PADDLE_SPEED));
    }
}
