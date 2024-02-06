package bricker.brick_strategies;

import java.util.Arrays;
import java.util.Random;
import bricker.main.BrickerGameManager;

public class StrategyFactory {
    private final BrickerGameManager brickerGameManager;
    private static final int MAX_STRATEGIES = 3;
    private final int DOUBLE_INDEX = 4;

    StrategyFactory(BrickerGameManager brickerGameManager){
        this.brickerGameManager = brickerGameManager;
    }
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
    private int[] fillArray() {
        Random random = new Random();
        int[] lst = new int[MAX_STRATEGIES];
        Arrays.fill(lst, -1);
        int numbersInList = 2;
        int index = 2;
        lst[0] = 4;
        lst[1] = 4;

        int doubleIndex = isThereADoubleIndex(lst);
        while (doubleIndex >= 0 && index <= MAX_STRATEGIES) {
            if (numbersInList < MAX_STRATEGIES) {
                numbersInList++;
            }
            lst[doubleIndex] = random.nextInt(5);
            if (index == MAX_STRATEGIES) {
                continue;
            }
            lst[index] = random.nextInt(5);
            index++;

            doubleIndex = isThereADoubleIndex(lst);
        }
        return lst;
    }

    private int isThereADoubleIndex(int[] lst){
        for (int i = 0; i < lst.length; i++) {
            if (lst[i] == DOUBLE_INDEX){
                return i;
            }
        }
        return -1;
    }
}

