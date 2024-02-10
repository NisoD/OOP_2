package bricker.main;

import bricker.brick_strategies.CollisionStrategyFactory;
import bricker.gameobjects.*;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.Camera;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * the game manager - responsible for the GameManager,
 * the game window, the graphics are the scores
 */
public class BrickerGameManager extends GameManager {
    private static final int WINDOW_WIDTH = 700;
    private static final int WINDOW_LENGTH = 500;
    private static final float BALL_SPEED = 200;
    private static final int WALLS_WIDTH = 10;
    private static final int BRICK_SPACES = 2;
    private static final int BRICK_HEIGHT = 15;
    private static final int DEFAULT_BRICK_ROW_NUMBER = 7;
    private static final int DEFAULT_BRICK_COL_NUMBER = 8;
    private static final int START_LIFE = 3;
    private static final int LIFE_COUNT_SIZE = 30;
    private static final int SPACE_TO_REMOVE = 30;
    private static final int PADDLE_WIDTH = 200;
    private static final int PADDLE_HEIGHT = 20;
    private static final float BALL_SIZE = 20;
    private static final int TIME_OF_BALL_COLLISION_CAMERA_MOVE = 4;
    private static final int MAX_LIFE_AMOUNT = 4;
    private boolean isThereASecondPaddle = false;
    private Ball ball;
    private int rowBrickNumber, colBrickNumber;
    private Vector2 windowDimensions, nextHeartPosition;
    private WindowController windowController;
    private Counter lifeCounter, brickCounter;
    private TextRenderable lifeText;
    private GameObject[] hearts;
    private UserInputListener inputListener;
    private ImageReader imageReader;
    private SoundReader soundReader;
    private int timesOfBallCollision;

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
     * getter for the isThereASecondPaddle
     * @return Second paddle exist?
     */
    public boolean getIsThereSecondPaddle(){return isThereASecondPaddle;}

    /**
     * setter for the isThereASecondPaddle
     * @param isThereASecondPaddle the value to set
     */
    public void setIsThereSecondPaddle(boolean isThereASecondPaddle){
        this.isThereASecondPaddle = isThereASecondPaddle;
    }

    /**
     * getter for the imageReader
     * @return the imageReader
     */
    public ImageReader getImageReader(){return imageReader;}

    /**
     * getter for soundReader
     * @return soundReader
     */
    public SoundReader getSoundReader(){return soundReader;}

    /**
     * getter for the window controller
     * @return windowController
     */
    public WindowController getWindowController(){return windowController;}

    /**
     * @return heart image size
     */
    public  int getLifeCountSize() {
        return LIFE_COUNT_SIZE;
    }

    /**
     * removes the brick that the ball hit from the game
     * @param collider the brick that the ball hit
     */
    public void RemoveBrickFromGame(GameObject collider){
        brickCounter.decrement();
        gameObjects().removeGameObject(collider, Layer.DEFAULT);
    }

    /**
     * removes any item from the game
     * @param object the object to remove
     * @param layer the layer to remove the object from
     */
    public void RemoveItemFromGame(GameObject object, int layer){
        gameObjects().removeGameObject(object, layer);

        if (object.getTag().equals("SecondPaddle")){
            isThereASecondPaddle = false;
        }
    }

    /**
     * increments the brick counter by 1
     */
    public void IncrementBrickCounter(){
        brickCounter.increment();
    }

    /**
     * decrements the brick counter by 1
     */
    public void DecrementBrickCounter(){
        brickCounter.decrement();
    }

    /**
     * getter for ballSize
     * @return returns the ball size in game
     */
    public float getBallSize(){return BALL_SIZE;}

    /**
     * getter for windowDimensions
     * @return window dimension for other game components
     */
    public Vector2 getWindowDimensions(){return windowDimensions;}

    /**
     * getter for the size of the paddle
     * @return  paddle sizes for another game components to use
     */
    public Vector2 getPaddleSize(){return new Vector2(PADDLE_WIDTH,PADDLE_HEIGHT);}

    /**
     * getter for the inputListener
     * @return inputListener for another game components to use
     */
    public UserInputListener getInputListener(){return inputListener;}

    /**
     * sets the camera to the given camera
     * @param camera the given camera
     */
    @Override
    public void setCamera(Camera camera) {
        if (ball != null)
            this.timesOfBallCollision = ball.getCollisionCounter();

        super.setCamera(camera);
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
        this.hearts = new GameObject[MAX_LIFE_AMOUNT];
        this.inputListener = inputListener;
        this.imageReader = imageReader;
        this.soundReader = soundReader;

        MakeBall(); // create ball
        MakePaddle(); // make paddles
        MakeWalls(); // create walls

        GameObject background = MakeBackground(); // make the background
        gameObjects().addGameObject(background, Layer.BACKGROUND);
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);

        MakeBricks(); //make the bricks
        MakeLifeCount(); // make the life counter
        MakeHeartLife(); // make the life graphics
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

        if (camera() != null && ball.getCollisionCounter() > timesOfBallCollision +
                TIME_OF_BALL_COLLISION_CAMERA_MOVE){
            setCamera(null);
        }

        String prompt = "";
        if (brickCounter.value() == 0 || inputListener.isKeyPressed(KeyEvent.VK_W)){
            prompt = "You win!";
        }
        else if (ballHeight > windowDimensions.y()) {
            lifeCounter.decrement();
            if (lifeCounter.value() > 0){
                UpdateLifeCounterGraphic();
                DecrementHeartsLife();
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

    /**
     * adds a given gameObject to the game
     * @param object object to add to the game
     */
    public void AddGameObjectToGame(GameObject object){
        gameObjects().addGameObject(object);
    }

    /**
     * make the paddle of the player
     */
    private void MakePaddle(){
        // create user paddle
        Renderable paddleImage =
                imageReader.readImage("assets/paddle.png", false);
        Paddle userPaddle = new Paddle(Vector2.ZERO, new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT),
                paddleImage, inputListener, windowDimensions);
        userPaddle.setCenter(new Vector2(windowDimensions.x()/2, (int) windowDimensions.y() -
                SPACE_TO_REMOVE));
        gameObjects().addGameObject(userPaddle);
    }

    /**
     * makes the graphics life representation
     */
    private void MakeHeartLife(){
        Renderable heartImage = imageReader.readImage("assets/heart.png", true);
        for (int i = 0; i < lifeCounter.value(); i++) {
            GameObject heart = new GameObject(nextHeartPosition, new Vector2(LIFE_COUNT_SIZE,
                    LIFE_COUNT_SIZE), heartImage);
            hearts[i] = heart;
            gameObjects().addGameObject(heart, Layer.UI);
            nextHeartPosition = new Vector2(WALLS_WIDTH+(i+2)*(LIFE_COUNT_SIZE + BRICK_SPACES),
                    windowDimensions.y() - WALLS_WIDTH - LIFE_COUNT_SIZE);
        }
    }

    /**
     * decrements the hearts graphics
     */
    private void DecrementHeartsLife(){
        gameObjects().removeGameObject(hearts[lifeCounter.value()], Layer.UI);
        hearts[lifeCounter.value()] = null;
        Vector2 newPosition = new Vector2(nextHeartPosition.x() - (LIFE_COUNT_SIZE + BRICK_SPACES),
                nextHeartPosition.y());
        nextHeartPosition = newPosition;
    }

    /**
     * increments the hearts graphics,
     * counts on the fact that the lifeCounter has already been incremented
     */
    public void IncrementHeartsLife(){
        if(lifeCounter.value()>=MAX_LIFE_AMOUNT) return; // checks for exceeded life
        Renderable heartImage = imageReader.readImage("assets/heart.png", true);
        lifeCounter.increment();
        UpdateLifeCounterGraphic();
        for (int i = 0; i < lifeCounter.value(); i++) {
            if (hearts[i] == null){
                GameObject newHeart = new GameObject(nextHeartPosition, new Vector2(LIFE_COUNT_SIZE,
                        LIFE_COUNT_SIZE), heartImage);
                gameObjects().addGameObject(newHeart, Layer.UI);
                hearts[i] = newHeart;
                Vector2 newPosition = new Vector2(nextHeartPosition.x() + (LIFE_COUNT_SIZE +
                        BRICK_SPACES), nextHeartPosition.y());
                nextHeartPosition = newPosition;
                break;
            }
        }
    }

    /**
     * make the numeric life representation
     */
    private void MakeLifeCount(){
        lifeText = new TextRenderable(" ");
        GameObject lifeCounterText = new GameObject(new Vector2(WALLS_WIDTH,
                windowDimensions.y() - WALLS_WIDTH - LIFE_COUNT_SIZE),
                new Vector2(LIFE_COUNT_SIZE, LIFE_COUNT_SIZE), lifeText);
        UpdateLifeCounterGraphic();
        nextHeartPosition = new Vector2(WALLS_WIDTH + LIFE_COUNT_SIZE,
                windowDimensions.y() - WALLS_WIDTH - LIFE_COUNT_SIZE);
        gameObjects().addGameObject(lifeCounterText, Layer.UI);
    }

    /**
     * updates the numeric life representation, each time there is a change
     */
    private void UpdateLifeCounterGraphic(){
        int currentLife = lifeCounter.value();
        Color currentColor;
        lifeText.setString(Integer.toString(currentLife));
        switch (currentLife) {
            case 2:
                currentColor = Color.YELLOW;
                break;
            case 1:
                currentColor = Color.RED;
                break;
            default:
                currentColor = Color.GREEN;
        };
        lifeText.setColor(currentColor);
    }

    /**
     * makes the bricks of the game, adds them to the board
     */
    private void MakeBricks(){
        Renderable brickImage = imageReader.readImage("assets/brick.png", false);
        int brickWidth =
                (int) (windowDimensions.x() - (2*2*WALLS_WIDTH) - (BRICK_SPACES * colBrickNumber)) /
                        rowBrickNumber;
        brickCounter = new Counter(colBrickNumber*rowBrickNumber);
        CollisionStrategyFactory factory = new CollisionStrategyFactory(this);
        Random random = new Random();

        for (int i = 0; i < rowBrickNumber; i++) {
            for (int j = 0; j < colBrickNumber; j++) {
                int randomNum = random.nextInt(10);
                Brick brick = new Brick(
                    new Vector2(2*WALLS_WIDTH + BRICK_SPACES + ((brickWidth + BRICK_SPACES)* i),
                                2*WALLS_WIDTH + BRICK_SPACES + ((BRICK_HEIGHT + BRICK_SPACES)* j)),
                    new Vector2(brickWidth, BRICK_HEIGHT), brickImage,
                    factory.makeStrategy(randomNum));
                gameObjects().addGameObject(brick);
            }
        }
     }

    /**
     * makes the ball
     */
    private void MakeBall(){
        Renderable ballImage = imageReader.readImage("assets/ball.png", true);
        Sound collisionSound = soundReader.readSound("assets/blop_cut_silenced.wav");
        ball = new Ball(Vector2.ZERO, new Vector2(BALL_SIZE, BALL_SIZE), ballImage, collisionSound);
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
     * @return the background
     */
    private GameObject MakeBackground(){
        Renderable backImage = imageReader.readImage("assets/DARK_BG2_small.jpeg",
                false);
        GameObject background = new GameObject(Vector2.ZERO, new Vector2(windowDimensions.x(),
                windowDimensions.y()),backImage);
        return background;
    }

    /**
     * makes the walls of the game
     */
    private void MakeWalls(){
        GameObject upWall = new GameObject(Vector2.ZERO, new Vector2(windowDimensions.x(), WALLS_WIDTH),
                null);
        GameObject leftWall = new GameObject(Vector2.ZERO, new Vector2(WALLS_WIDTH, windowDimensions.y()),
                null);
        GameObject rightWall = new GameObject(new Vector2(windowDimensions.x()-WALLS_WIDTH, 0),
                new Vector2(WALLS_WIDTH, windowDimensions.y()), null);
        GameObject[] wallArr = {upWall, leftWall, rightWall};

        for (GameObject wall: wallArr){
            gameObjects().addGameObject(wall);
        }
    }

    /**
     * the main function
     * @param args command line arguments
     */
    public static void main(String[] args) {
        BrickerGameManager gameManager;
        if (args.length == 2){
            gameManager = new BrickerGameManager("Bouncing Ball",
                    new Vector2(WINDOW_WIDTH, WINDOW_LENGTH), Integer.parseInt(args[0]),
                    Integer.parseInt(args[1]));
        }
        else {
            gameManager = new BrickerGameManager("Bouncing Ball",
                    new Vector2(WINDOW_WIDTH, WINDOW_LENGTH));
        }
        gameManager.run();
    }
}
