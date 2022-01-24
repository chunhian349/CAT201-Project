package com.fxgl.speedyplane;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;

import static com.almasb.fxgl.dsl.FXGL.getAppHeight;

/**
 * CAT201 PROJECT
 */
public class PlayerComponent extends Component {

    private Vec2 acceleration = new Vec2(6, 0);

    @Override
    public void onUpdate(double tpf) {
        acceleration.x += tpf * 0.2;
        acceleration.y += tpf * 10;

        if (acceleration.y < -5)
            acceleration.y = -5;

        if (acceleration.y > 5)
            acceleration.y = 5;

        entity.translate(acceleration.x, acceleration.y);

        if (entity.getBottomY() > getAppHeight() || entity.getBottomY() < 0) {
            FXGL.<SpeedyPlaneApp>getAppCast().showGameOver();
        }
    }

    public void jump() {
        acceleration.addLocal(0, -5);
    }
}