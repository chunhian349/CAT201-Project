package com.almasb.fxglgames.flappy;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.texture.Texture;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static com.almasb.fxgl.dsl.FXGL.*;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class WallBuildingComponent extends Component {

    private double lastWall = 1000;

    @Override
    public void onUpdate(double tpf) {
        if (lastWall - entity.getX() < FXGL.getAppWidth()) {
            birdMountains();
        }
    }

    private void birdMountains() {
        double height = FXGL.getAppHeight();
        Texture wallTxt = texture("blue_mountain.png");
            wallTxt.setPreserveRatio(true);
            wallTxt.setFitHeight(300);
        Texture birdTxt = texture("bird.png");
            birdTxt.setPreserveRatio(true);
            birdTxt.setFitHeight(200);

            entityBuilder()
                    .at(lastWall, height - (height - 150))
                    .type(EntityType.WALL)
                    .view(new Rectangle(325, 200))
                    .viewWithBBox(birdTxt)
                    .with(new CollidableComponent(true))
                    .buildAndAttach();

            entityBuilder()
                    .at(lastWall, height - 300)
                    .type(EntityType.WALL)
                    .view(new Rectangle(1000, 300))
                    .viewWithBBox(wallTxt)
                    .with(new CollidableComponent(true))
                    .buildAndAttach();

            final int MIN = 2;
            final int MAX = 10;
            lastWall += (Math.random() % MAX + MIN) * getAppWidth();
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
