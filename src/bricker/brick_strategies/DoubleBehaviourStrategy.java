package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;

/**
 * the class that's responsible for double behavior of the brick
 */
public class DoubleBehaviourStrategy implements CollisionStrategy{
    private final BrickerGameManager brickerGameManager;
    private final CollisionStrategy[] collisionStrategies;
    private final CollisionStrategyFactory collisionStrategyFactory;

    /**
     * constructor for the double behavior strategy
     * @param brickerGameManager the game manager
     * @param strategies the array of strategies that the factory made
     */
    DoubleBehaviourStrategy(BrickerGameManager brickerGameManager, CollisionStrategy[] strategies){
        this.collisionStrategies = strategies;
        this.brickerGameManager = brickerGameManager;
        this.collisionStrategyFactory = new CollisionStrategyFactory(brickerGameManager);

    }

    /**
     * on collision, goes over all the strategies of the brick, calls the onCollision
     * of each one of them. only decrement the counter of bricks after all the on collision
     * were called
     * @param collider the brick that the other object collided with
     * @param other the other object that collided with the brick
     */
    @Override
    public void onCollision(GameObject collider, GameObject other) {
        if (other.getTag().equals("Ball") || other.getTag().equals("Puck")){
            for (CollisionStrategy strategy: collisionStrategies){
                if (strategy != null){
                    strategy.onCollision(collider, other);
                    brickerGameManager.IncrementBrickCounter();
                }
            }
            brickerGameManager.DecrementBrickCounter();
        }
    }
}
