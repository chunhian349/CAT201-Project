package com.fxgl.speedyplane;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.texture.Texture;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.Random;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static com.almasb.fxgl.dsl.FXGL.texture;

/**
 * CAT201 PROJECT
 */

/**
 * Define non-player game object component
 * and how they are spawned in the game world
 */
public class GameObjectComponent extends Component {

    private double spawnPosInX = 1000; //initial spawn position in x-coordinate of birds/mountain

    /**Called every frame
     * @param tpf: time per frame (not needed in our override function)
     * If distance between player x-coordinate and next spawn position is smaller than app width size
     * then spawn birds/mountain and coin
     */
    @Override
    public void onUpdate(double tpf) {
        Random rd = new Random();

        if ((spawnPosInX - entity.getX() < FXGL.getAppWidth())){
            if(rd.nextBoolean()) { //spawn rate of birds and mountain are 50/50
                coinSpawn(spawnPosInX - 500, 600);  //selected to spawn birds, coin spawn at lower part
                birdSpawn();
                coinSpawn(spawnPosInX + 1090, 600);
            }
            else{
                coinSpawn(spawnPosInX - 400, 250);  //spawn mountain, coin spawn at upper part
                mountainSpawn();
                coinSpawn(spawnPosInX + 1170, 250);
            }
        }
    }

    //Method to spawn birds
    private void birdSpawn() {
        Texture birdTxt = texture("birds.png");    //load birds texture
        birdTxt.setPreserveRatio(true);                     //preserve ratio when resize the texture
        birdTxt.setFitHeight(250);                          //resize the texture by set a height

        entityBuilder()     //build bird entity and attach to gameworld
                .at(spawnPosInX, (int)(Math.random()*200))
                .type(EntityType.BIRD)
                .bbox(new HitBox(BoundingShape.box(390,230)))   //set hitbox size
                .view(birdTxt)                                              //add view with bird image
                .with(new CollidableComponent(true))                        //set it a collidable component
                .buildAndAttach();                                          //build and attach to game world

        spawnPosInX += 2500; //set next x-coordinate to spawn game object
    }

    //Method to spawn mountain
    private void mountainSpawn() {
        Texture wallTxt = texture("mountain.png");
        wallTxt.setPreserveRatio(true);
        wallTxt.setFitHeight(600);

        entityBuilder()
                .at(spawnPosInX, FXGL.getAppHeight() - 600)
                .type(EntityType.MOUNTAIN)
                .bbox(new HitBox(BoundingShape.box(670,600)))
                .view(wallTxt)
                .with(new CollidableComponent(true))
                .buildAndAttach();

        spawnPosInX += 2500;
    }

    //Method to spawn coin
    private void coinSpawn(double x, double y){
        entityBuilder()
                .at(x, y)
                .type(EntityType.COIN)
                .viewWithBBox(new Circle(25, Color.YELLOW)) //set collision area same with area of view shape(circle)
                .with(new CollidableComponent(true))
                .buildAndAttach();
    }
}