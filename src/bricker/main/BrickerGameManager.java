package bricker.main;

import bricker.brick_strategies.BasicCollisionStrategy;
import bricker.gameobjects.Brick;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import bricker.gameobjects.AIPaddle;
import bricker.gameobjects.Ball;
import bricker.gameobjects.UserPaddle;

import java.util.Random;

public class BrickerGameManager extends GameManager {
    private static final float BALL_SPEED = 200;
    private Ball ball;

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
        MakeBall(windowDimensions, imageReader, soundReader);

        // create user paddle
        Renderable paddleImage =
                imageReader.readImage("assets/paddle.png", false);
        UserPaddle userPaddle = new UserPaddle(Vector2.ZERO, new Vector2(200, 20), paddleImage,
                inputListener);
        userPaddle.setCenter(new Vector2(windowDimensions.x()/2, (int) windowDimensions.y() - 30));
        gameObjects().addGameObject(userPaddle);

        // create AI paddle
        AIPaddle aiPaddle = new AIPaddle(Vector2.ZERO, new Vector2(200, 20), paddleImage, ball);
        aiPaddle.setCenter(new Vector2(windowDimensions.x()/2, 30));
        gameObjects().addGameObject(aiPaddle);

        // create walls
        GameObject[] walls = MakeWalls(windowDimensions);
        for (GameObject wall: walls){
            gameObjects().addGameObject(wall);
        }

        GameObject background = MakeBackground(windowDimensions, imageReader); // make the background
        gameObjects().addGameObject(background, Layer.BACKGROUND);
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);

        Renderable brickImage = imageReader.readImage("assets/brick.png", false);
        Brick brick = new Brick(new Vector2(0, windowDimensions.y()*0.66f),
                new Vector2(windowDimensions.x(), 15), brickImage,
                new BasicCollisionStrategy(this));
        gameObjects().addGameObject(brick);
    }

    /**
     * removes the brick that the ball hit from the game
     * @param collider the brick that the ball hit
     */
    public void RemoveBrickFromGame(GameObject collider){
        gameObjects().removeGameObject(collider);
    }

    /**
     * makes the ball
     * @param windowDimensions the dimensions of the window
     * @param imageReader the image reader
     */
    private void MakeBall(Vector2 windowDimensions, ImageReader imageReader, SoundReader soundReader){
        Renderable ballImage = imageReader.readImage("assets/ball.png", true);
        Sound collisionSound = soundReader.readSound("assets/blop_cut_silenced.wav");
        ball = new Ball(Vector2.ZERO, new Vector2(40, 40), ballImage, collisionSound);
        float ballVelX = BALL_SPEED;
        float ballVelY = BALL_SPEED;
        Random rand = new Random();
        ball.setVelocity(new Vector2(ballVelX, ballVelY));

        if(rand.nextBoolean()){ballVelX*=-1;}
        if(rand.nextBoolean()){ballVelY*=-1;}

        ball.setVelocity(new Vector2(ballVelX,ballVelY));
        ball.setCenter(windowDimensions.mult(0.5f));
        gameObjects().addGameObject(ball);
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
