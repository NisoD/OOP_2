package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;

public class DoubleBehaviourStrategy implements CollisionStrategy{
    private final BrickerGameManager brickerGameManager;
    private final CollisionStrategy[] collisionStrategies;
    private final CollisionStrategyFactory collisionStrategyFactory;


    DoubleBehaviourStrategy(BrickerGameManager brickerGameManager, CollisionStrategy[] strategies){
        this.collisionStrategies = strategies;
        this.brickerGameManager = brickerGameManager;
        this.collisionStrategyFactory = new CollisionStrategyFactory(brickerGameManager);

    }
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
