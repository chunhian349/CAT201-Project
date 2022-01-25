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

import static com.almasb.fxgl.dsl.FXGL.*;

/**
 * CAT201 PROJECT
 */

public class GameObjectComponent extends Component {

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

        entityBuilder()
                .at(lastBird, (int)(Math.random()*200))
                .type(EntityType.BIRD)
                .bbox(new HitBox(BoundingShape.box(390,230)))
                .view(birdTxt)
                .with(new CollidableComponent(true))
                .buildAndAttach();

        lastBird += 2500;
        lastMount += 2500;
    }

    private void mountainSpawn() {
        Texture wallTxt = texture("mountain.png");
        wallTxt.setPreserveRatio(true);
        wallTxt.setFitHeight(600);

        entityBuilder()
                .at(lastMount, height - 600)
                .type(EntityType.MOUNTAIN)
                .bbox(new HitBox(BoundingShape.box(670,600)))
                .view(wallTxt)
                .with(new CollidableComponent(true))
                .buildAndAttach();

        lastBird += 2500;
        lastMount += 2500;
    }

    private void coinSpawn(double x, double y){
        entityBuilder()
                .at(x, y)
                .type(EntityType.COIN)
                .viewWithBBox(new Circle(25, Color.YELLOW))
                .with(new CollidableComponent(true))
                .buildAndAttach();
    }
}