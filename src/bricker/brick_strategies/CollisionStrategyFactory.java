package bricker.brick_strategies;

import java.util.Arrays;
import java.util.Random;
import bricker.main.BrickerGameManager;

/**
 * the factory that makes all the strategies
 */
public class CollisionStrategyFactory {
    private final BrickerGameManager brickerGameManager;
    private static final int MAX_STRATEGIES = 3;
    private final int DOUBLE_INDEX = 4;
    private final int RESTRICTION = 10;
    private final int BOUND_OF_DOUBLE = 5;
    private final int FIRST_NUMBER_OF_STARTEGIES = 2;

    /**
     * constructor for the factory
     * @param brickerGameManager the game manager
     */
    public CollisionStrategyFactory(BrickerGameManager brickerGameManager){
        this.brickerGameManager = brickerGameManager;
    }

    /**
     * makes the strategy according to the number that was given
     * @param numberOfStrategy the number that was guven
     * @return the strategy
     */
    public CollisionStrategy makeStrategy(int numberOfStrategy){
        CollisionStrategy collisionStrategy;
        switch (numberOfStrategy){
            case 0:
                collisionStrategy = new IncrementLifeStrategy(brickerGameManager);
                break;
            case 1:
                collisionStrategy = new PuckCollisionStrategy(brickerGameManager);
                break;
            case 2:
                collisionStrategy = new AnotherPaddleStrategy(brickerGameManager);
                break;
            case 3:
                collisionStrategy = new CameraStrategy(brickerGameManager);
                break;
            case 4:
                CollisionStrategy[] strategies = DoubleStrategyHandler();
                collisionStrategy = new DoubleBehaviourStrategy(brickerGameManager, strategies);
                break;
            default:
                collisionStrategy = new BasicCollisionStrategy(brickerGameManager);
                break;
        }
        return collisionStrategy;
    }

    /**
     * the method that makes the array of strategies for the double strategy
     * @return the array of strategies
     */
    private CollisionStrategy[] DoubleStrategyHandler(){
        CollisionStrategy[] strategies = new CollisionStrategy[MAX_STRATEGIES];
        int[] integerArray = fillArray();

        for (int i = 0; i < MAX_STRATEGIES; i++) {
            if (integerArray[i] != -1){
                strategies[i] = makeStrategy(integerArray[i]);
            }
        }

        return strategies;
    }

    /**
     * fills an array with integers, so that the handler can fill it with strategies accordingly
     * @return the array of integers
     */
    private int[] fillArray() {
        Random random = new Random();
        int[] lst = new int[MAX_STRATEGIES];
        Arrays.fill(lst, -1);
        int numbersInList = FIRST_NUMBER_OF_STARTEGIES;
        int index = FIRST_NUMBER_OF_STARTEGIES;
        lst[0] = random.nextInt(BOUND_OF_DOUBLE);
        lst[1] = random.nextInt(BOUND_OF_DOUBLE);

        int doubleIndex = isThereADoubleIndex(lst);
//        int iterations = 0; // To limit maximum iterations   && iterations < MAX_STRATEGIES * RESTRICTION
        while (doubleIndex >= 0 && index <= MAX_STRATEGIES) {
            if (numbersInList < MAX_STRATEGIES) {
                numbersInList++;
            }
            lst[doubleIndex] = random.nextInt(BOUND_OF_DOUBLE);
            if (index == MAX_STRATEGIES) {
                index++;
                continue;
            }
            lst[index] = random.nextInt(BOUND_OF_DOUBLE);
            index++;

            doubleIndex = isThereADoubleIndex(lst);
        }
        return lst;
    }

    /**
     * checks if there is an integer that represents the double strategy index
     * @param lst the list to check
     * @return the index if true, -1 if there isn't any
     */
    private int isThereADoubleIndex(int[] lst){
        for (int i = 0; i < lst.length; i++) {
            if (lst[i] == DOUBLE_INDEX){
                return i;
            }
        }
        return -1;
    }
}

