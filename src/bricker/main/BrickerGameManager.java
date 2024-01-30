package bricker.main;

import bricker.brick_strategies.BasicCollisionStrategy;
import bricker.gameobjects.*;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.*;
import java.util.Random;

public class BrickerGameManager extends GameManager {
    private static final float BALL_SPEED = 200;
    private static final int WALLS_WIDTH = 20;
    private static final int BRICK_SPACES = 2;
    private static final int BRICK_HEIGHT = 15;
    private static final int DEFAULT_BRICK_ROW_NUMBER = 7;
    private static final int DEFAULT_BRICK_COL_NUMBER = 8;
    private static final int START_LIFE = 3;
    private static final int LIFE_COUNT_SIZE = 30;
    private Ball ball;
    private int rowBrickNumber;
    private int colBrickNumber;
    private Vector2 windowDimensions;
    private WindowController windowController;
    private Counter lifeCounter;
    private TextRenderable lifeText;


    /**
     * default constructor for Bricker Game Manager, makes a window in game manager
     * @param windowTitle the title of the window
     * @param windowDimensions the dimensions of the window
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions){
        super(windowTitle, windowDimensions);
        this.rowBrickNumber = DEFAULT_BRICK_ROW_NUMBER;
        this.colBrickNumber = DEFAULT_BRICK_COL_NUMBER;
    }

    /**
     * constructor for Bricker Game Manager, makes a window in game manager
     * @param windowTitle the title of the window
     * @param windowDimensions the dimensions of the window
     * @param rowNum number of rows of bricks
     * @param colNum number of cols of bricks
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions, int rowNum, int colNum){
        super(windowTitle, windowDimensions);
        this.rowBrickNumber = rowNum;
        this.colBrickNumber = colNum;
    }

    /**
     * removes the brick that the ball hit from the game
     * @param collider the brick that the ball hit
     */
    public void RemoveBrickFromGame(GameObject collider){
        gameObjects().removeGameObject(collider, Layer.DEFAULT);
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
        windowDimensions = windowController.getWindowDimensions();
        this.windowController = windowController;
        this.lifeCounter = new Counter(START_LIFE);

        // create ball
        MakeBall(imageReader, soundReader);

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
        GameObject[] walls = MakeWalls();
        for (GameObject wall: walls){
            gameObjects().addGameObject(wall);
        }

        GameObject background = MakeBackground(imageReader); // make the background
        gameObjects().addGameObject(background, Layer.BACKGROUND);
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);

        MakeBricks(imageReader); //make the bricks
        MakeLifeCount();
    }

    /**
     * updates each frame
     * @param deltaTime The time, in seconds, that passed since the last invocation
     *                  of this method (i.e., since the last frame). This is useful
     *                  for either accumulating the total time that passed since some
     *                  event, or for physics integration (i.e., multiply this by
     *                  the acceleration to get an estimate of the added velocity or
     *                  by the velocity to get an estimate of the difference in position).
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float ballHeight = ball.getCenter().y();

        String prompt = "";
        if (ballHeight < 0){
            prompt = "You win!";
        }
        else if (ballHeight > windowDimensions.y()) {
            lifeCounter.decrement();
            if (lifeCounter.value() > 0){
                UpdateLifes();
                ball.setCenter(windowDimensions.mult(0.5f));
            }
            else {
                prompt = "You lose!";
            }
        }

        if (!prompt.isEmpty()){
            prompt += " Play again?";
            if (windowController.openYesNoDialog(prompt))
                windowController.resetGame();
            else
                windowController.closeWindow();
        }
    }

    private void MakeLifeCount(){
        lifeText = new TextRenderable(" ");
        GameObject lifeCounterText = new GameObject(new Vector2(WALLS_WIDTH,
                windowDimensions.y() - WALLS_WIDTH - LIFE_COUNT_SIZE),
                new Vector2(LIFE_COUNT_SIZE, LIFE_COUNT_SIZE), lifeText);
        UpdateLifes();
        gameObjects().addGameObject(lifeCounterText, Layer.UI);
    }

    private void UpdateLifes(){
        int currentLife = lifeCounter.value();
        Color currentColor;
        lifeText.setString(Integer.toString(currentLife));
        currentColor = switch (currentLife) {
            case 2 -> Color.YELLOW;
            case 1 -> Color.RED;
            default -> Color.GREEN;
        };
        lifeText.setColor(currentColor);
    }

    /**
     * makes the bricks of the game, adds them to the board
     * @param imageReader the image reader
     */
    private void MakeBricks(ImageReader imageReader){
        Renderable brickImage = imageReader.readImage("assets/brick.png", false);
        int brickWidth =
                (int) (windowDimensions.x() - (2 * WALLS_WIDTH) - (BRICK_SPACES * colBrickNumber)) /
                        rowBrickNumber;
        for (int i = 0; i < rowBrickNumber; i++) {
            for (int j = 0; j < colBrickNumber; j++) {
                Brick brick = new Brick(
                    new Vector2(WALLS_WIDTH + BRICK_SPACES + ((brickWidth + BRICK_SPACES)* i),
                                WALLS_WIDTH + BRICK_SPACES + ((BRICK_HEIGHT + BRICK_SPACES)* j)),
                    new Vector2(brickWidth, BRICK_HEIGHT), brickImage,
                    new BasicCollisionStrategy(this));
                gameObjects().addGameObject(brick);
            }
        }
     }

    /**
     * makes the ball
     * @param imageReader the image reader
     */
    private void MakeBall(ImageReader imageReader, SoundReader soundReader){
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
     * @param imageReader the image reader
     * @return the background
     */
    private GameObject MakeBackground(ImageReader imageReader){
        Renderable backImage = imageReader.readImage("assets/DARK_BG2_small.jpeg",
                false);
        GameObject background = new GameObject(Vector2.ZERO, new Vector2(windowDimensions.x(),
                windowDimensions.y()),backImage);
        return background;
    }

    /**
     * makes the walls of the game
     * @return returns an array with the walls
     */
    private GameObject[] MakeWalls(){
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
