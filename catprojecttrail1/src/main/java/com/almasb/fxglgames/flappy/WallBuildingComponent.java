package com.almasb.fxglgames.flappy;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.texture.Texture;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.Random;

import static com.almasb.fxgl.dsl.FXGL.*;

/**
 * CAT201 PROJECT
 */

public class WallBuildingComponent extends Component {

    private double lastBird = 1000;
    private double lastMount = 1000;
    double height = FXGL.getAppHeight();

    @Override
    public void onUpdate(double tpf) {
        Random rd = new Random();

        if ((lastBird - entity.getX() < FXGL.getAppWidth()) && (lastMount - entity.getX() < FXGL.getAppWidth())){
            if(rd.nextBoolean()) {
                coinSpawn(lastBird - 500, 600);
                birdSpawn();
                coinSpawn(lastBird + 1090, 600);
            }
            else{
                coinSpawn(lastMount - 400, 250);
                mountainSpawn();
                coinSpawn(lastMount + 1170, 250);
            }
        }
    }

    private void birdSpawn() {
        Texture birdTxt = texture("birds.png");
        birdTxt.setPreserveRatio(true);
        birdTxt.setFitHeight(250);
        System.out.println("width: " + birdTxt.getFitWidth());

        //for (int i = 1; i <= 20; i++) {

        entityBuilder()
                .at(lastBird, (int)(Math.random()*200))
                .type(EntityType.BIRD)
                .bbox(new HitBox(BoundingShape.box(390,240)))
                .view(birdTxt)
                .with(new CollidableComponent(true))
                .buildAndAttach();

        //}
        lastBird += 2500;
        lastMount += 2500;
    }
    private void mountainSpawn() {
        Texture wallTxt = texture("mountain.png");
        wallTxt.setPreserveRatio(true);
        wallTxt.setFitHeight(600);
        //for (int i = 1; i <= 20; i++) {

        entityBuilder()
                .at(lastMount, height - 600)
                .type(EntityType.MOUNTAIN)
                .bbox(new HitBox(BoundingShape.box(670,570)))
                .view(wallTxt)
                .with(new CollidableComponent(true))
                .buildAndAttach();
        //}
        lastBird += 2500;
        lastMount += 2500;

    }

    private void coinSpawn(double x, double y){
        entityBuilder()
                .at(x, y)
                .type(EntityType.COIN)
                .viewWithBBox(new Circle(15, Color.YELLOW))
                .with(new CollidableComponent(true))
                .buildAndAttach();
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
