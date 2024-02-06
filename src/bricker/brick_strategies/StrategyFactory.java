package bricker.brick_strategies;

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
        Random random = new Random();
        int nextInt = random.nextInt(5);
        int strategiesThatEntered = 0;
        int doublesRandomized = 1;

        while (strategiesThatEntered < MAX_STRATEGIES ){
            if (nextInt == DOUBLE_INDEX && strategiesThatEntered < MAX_STRATEGIES){
                strategies[strategiesThatEntered] = makeStrategy(nextInt);
                strategiesThatEntered++;
            }
            else if (nextInt != DOUBLE_INDEX) {
                doublesRandomized++;
                nextInt = random.nextInt(5);
            }
        }


        return strategies;
    }
//    private int[] StrategyCollector(){
//        Random random = new Random();
//        int amount_Nums = 2;
//        int random1=0;
//        int random2=0;
//        boolean double_strategy = true;
//        int[] list = new int[MAX_STRATEGIES];
//        int index = 0;
//        while(double_strategy && index < MAX_STRATEGIES - 1){
//            random2=random.nextInt(5);
//            random1=random.nextInt(5);
//            if(random1 != 4 && random2 != 4){
//                list[index] = random1;
//                list[index + 1] = random2;
//                double_strategy = false;
//            }
//            else if(random1 == 4 && random2 != 4){
//                list[index] = random2;
//                index++;
//            }
//            else if(random1 != 4 && random2 == 4){
//                list[index] = random1;
//                index++;
//            }
//        }
//        return list;
//    }

    private void fillArray(){
        Random random = new Random();
        int random2=random.nextInt(5);
        int random1=random.nextInt(5);
        int[] list = new int[MAX_STRATEGIES];
        int numbersInList = 2;
        int index = 2;
        list[0] = random1;
        list[1] = random2;

        while ((isThereAFour(list) >= 0) && (index <= numbersInList)){
            if (numbersInList < MAX_STRATEGIES){
                numbersInList++;
            }
            list[isThereAFour(list)] = random.nextInt(5);
            list[index] = random.nextInt(5);
            index++;
        }
    }

    private int isThereAFour(int[] lst){
        for (int i = 0; i < lst.length; i++) {
            if (lst[i] == 4){
                return i;
            }
        }
        return -1;
    }
}

