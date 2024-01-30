package bricker.main;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import gameobjects.Ball;

import java.awt.*;

public class BrickerGameManager extends GameManager{
    /**
     * constructor for BrickerGameManager, makes a window in game manager
     * @param windowTitle the title of the window
     * @param windowDimensions the dimensions of the window
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions){
        super(windowTitle, windowDimensions);
    }

    /**
     *
     * @param imageReader Contains a single method: readImage, which reads an image from disk.
     *                 See its documentation for help.
     * @param soundReader Contains a single method: readSound, which reads a wav file from
     *                    disk. See its documentation for help.
     * @param inputListener Contains a single method: isKeyPressed, which returns whether
     *                      a given key is currently pressed by the user or not. See its
     *                      documentation.
     * @param windowController Contains an array of helpful, self explanatory methods
     *                         concerning the window.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        Vector2 windowDimensions = windowController.getWindowDimensions();

        // create ball
        Ball ball = MakeBall(windowDimensions, imageReader);
        gameObjects().addGameObject(ball);

        // create paddles
        int[] paddleHeights = {(int) windowDimensions.y() - 30, 30};
        Renderable paddleImage =
                imageReader.readImage("assets/paddle.png", false);

        for (int i = 0; i < paddleHeights.length; i++) {
            GameObject paddle = new GameObject(Vector2.ZERO, new Vector2(200, 20), paddleImage);
            paddle.setCenter(new Vector2(windowDimensions.x()/2, paddleHeights[i]));
            gameObjects().addGameObject(paddle);
        }

        // create walls
        GameObject[] walls = MakeWalls(windowDimensions);
        for (GameObject wall: walls){
            gameObjects().addGameObject(wall);
        }

        GameObject background = MakeBackground(windowDimensions, imageReader); // make the background
        gameObjects().addGameObject(background, Layer.BACKGROUND);
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
    }

    /**
     * makes the ball
     * @param windowDimensions the dimensions of the window
     * @param imageReader the image reader
     * @return the ball of the game
     */
    private Ball MakeBall(Vector2 windowDimensions, ImageReader imageReader){
        Renderable ballImage = imageReader.readImage("assets/ball.png", true);
        Ball ball = new Ball(Vector2.ZERO, new Vector2(50, 50), ballImage);
        ball.setVelocity(new Vector2(0, 200));

        ball.setCenter(windowDimensions.mult(0.5f));
        return ball;
    }

    /**
     * make the background of the game
     * @param windowDimensions the dimensions of the window
     * @param imageReader the image reader
     * @return the background
     */
    private GameObject MakeBackground(Vector2 windowDimensions, ImageReader imageReader){
        Renderable backImage = imageReader.readImage("assets/DARK_BG2_small.jpeg",
                false);
        GameObject background = new GameObject(Vector2.ZERO, new Vector2(windowDimensions.x(),
                windowDimensions.y()),backImage);
        return background;
    }

    /**
     * makes the walls of the game
     * @param windowDimensions the dimensions of the window
     * @return returns an array with the walls
     */
    private GameObject[] MakeWalls(Vector2 windowDimensions){
        GameObject upWall = new GameObject(Vector2.ZERO, new Vector2(windowDimensions.x(), 10),
                null);
        GameObject leftWall = new GameObject(Vector2.ZERO, new Vector2(10, windowDimensions.y()),
                null);
        GameObject rightWall = new GameObject(new Vector2(windowDimensions.x()-10, 0),
                new Vector2(10, windowDimensions.y()), null);
        GameObject[] wallArr = {upWall, leftWall, rightWall};
        return wallArr;
    }

    /**
     * the main function
     * @param args command line arguments
     */
    public static void main(String[] args) {
        BrickerGameManager gameManager = new BrickerGameManager("Bouncing Ball",
                new Vector2(700, 500));
        gameManager.run();
    }
}
