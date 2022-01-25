package com.fxgl.speedyplane;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;

import static com.almasb.fxgl.dsl.FXGL.getAppHeight;

/**
 * CAT201 PROJECT
 */

/**
 * Define player bahaviour (fly)
 */
public class PlayerComponent extends Component {

    private Vec2 acceleration = new Vec2(6, 0);     //Set initial vector of player

    @Override
    public void onUpdate(double tpf) {
        acceleration.x += tpf * 0.2;    //acceleration in x direction increase gradually
        acceleration.y += tpf * 10;     //falling acceleration increase gradually

        if (acceleration.y < -5)    //Limit max falling acceleration to prevent falling too fast
            acceleration.y = -5;

        if (acceleration.y > 5)     //Limit max flying acceleration to prevent flying up too fast
            acceleration.y = 5;

        entity.translate(acceleration.x, acceleration.y);   //update accelerations to player entity

        //Game over if player hit top or bottom border of game app
        if (entity.getBottomY() > getAppHeight() || entity.getBottomY() < 0) {
            FXGL.<SpeedyPlaneApp>getAppCast().showGameOver();
        }
    }

    //Increase height of the player plane
    public void fly() {
        acceleration.addLocal(0, -5);
    }
}