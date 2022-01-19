package com.almasb.fxglgames.flappy;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.texture.Texture;

import java.util.Random;

import static com.almasb.fxgl.dsl.FXGL.*;

/**
 * CAT201 PROJECT
 */

public class WallBuildingComponent extends Component {

    private double lastBird = 5;
    private double lastMount = 5;
    double height = FXGL.getAppHeight();

    @Override
    public void onUpdate(double tpf) {
        Random rd = new Random();

        if ((lastBird - entity.getX() < FXGL.getAppWidth()) && (lastMount - entity.getX() < FXGL.getAppWidth())){
            if(rd.nextBoolean()) {
                birdSpawn();
            }
            else{
                mountainSpawn();
            }
        }
    }

    private void birdSpawn() {
        Texture birdTxt = texture("bird.png");
        birdTxt.setPreserveRatio(true);
        birdTxt.setFitHeight(200);

        //for (int i = 1; i <= 20; i++) {

        entityBuilder()
                .at(lastBird, height - (height - 150))
                .type(EntityType.BIRD)
                .bbox(new HitBox(BoundingShape.box(300,200)))
                .view(birdTxt)
                .with(new CollidableComponent(true))
                .buildAndAttach();

        //}
        lastBird += 10 * 500;
        lastMount += 10 * 500;
    }
    private void mountainSpawn() {
        Texture wallTxt = texture("blue_mountain.png");
        wallTxt.setPreserveRatio(true);
        wallTxt.setFitHeight(300);
        //for (int i = 1; i <= 20; i++) {

        entityBuilder()
                .at(lastMount, height - 200)
                .type(EntityType.MOUNTAIN)
                .bbox(new HitBox(BoundingShape.box(600,300)))
                .view(wallTxt)
                .with(new CollidableComponent(true))
                .buildAndAttach();
        //}
        lastBird += 10 * 500;

        lastMount += 10 * 500;
        lastBird += 10 * 500;
    }

}

  /*
  private void buildWalls(){
       // buildCeiling();
        birdMountains();
    }

  private Rectangle wallView(double width, double height) {
        Rectangle wall = new Rectangle(width, height);
        wall.setArcWidth(25);
        wall.setArcHeight(25);
        wall.fillProperty().bind(FXGL.getWorldProperties().objectProperty("stageColor"));
        return wall;
    }*/

   /* private void buildCeiling(){
        double gameWidth = FXGL.getAppWidth();
        for (int i = 1; i<=20; ++i){
            entityBuilder()
                    .at((i-1)*gameWidth, 0)
                    .type(EntityType.WALL)
                    .viewWithBBox(wallView(gameWidth, 5))
                    .with(new CollidableComponent(true))
                    .buildAndAttach();
        }
    }
*/



// inside buildwall() function originally
// upper row wall from original
            /*entityBuilder()
                    .at(lastWall + i * 500, 0 - 25)
                    .type(EntityType.WALL)
                    .viewWithBBox(texture("mountains.png"))
                    .viewWithBBox(wallView(50, topHeight))
                    .with(new CollidableComponent(true))
                    .buildAndAttach();*/
//double wallHeight = height - distance - topHeight;
/*
double topHeight = Math.random() * (height - distance);
            entityBuilder()
                    .at(lastWall + i * 500, 0 + topHeight + distance + 25)
                    .type(EntityType.WALL)
                    .viewWithBBox(wallView(50, wallHeight))
                    .with(new CollidableComponent(true))
                    .buildAndAttach();*/
