package bricker.main;

import danogl.GameManager;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Vector2;

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
