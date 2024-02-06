package bricker.brick_strategies;

import bricker.gameobjects.Heart;
import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
public class IncrementLifeStrategy implements CollisionStrategy{

    private final ImageReader imageReader;
    private final int SPEED_OF_FALL = 100;
    private final BrickerGameManager brickerGameManager;


    public IncrementLifeStrategy( BrickerGameManager brickerGameManager){
        this.brickerGameManager = brickerGameManager;
        this.imageReader=brickerGameManager.getImageReader();
    }
    @Override
    public void onCollision(GameObject collider, GameObject other) {
        if (other.getTag().equals("Ball") || other.getTag().equals("Puck")){
            int lifeSize = brickerGameManager.getLifeCountSize();
            Renderable img = imageReader.readImage("assets/heart.png",true);

            Heart heart = new Heart(new Vector2(collider.getCenter()),new Vector2(lifeSize,lifeSize),img,
                    brickerGameManager);
            heart.setVelocity(new Vector2(0,SPEED_OF_FALL));
            brickerGameManager.AddGameObjectToGame(heart);

            brickerGameManager.RemoveBrickFromGame(collider);
        }
    }
}
