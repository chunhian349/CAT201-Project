package com.almasb.fxglgames.flappy;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.ViewComponent;
import com.almasb.fxgl.texture.Texture;
import javafx.scene.shape.Rectangle;

import static com.almasb.fxgl.dsl.FXGL.*;

public class newBackground extends Component {
    private double ttlHeight = FXGL.getAppHeight();
    private double lastCloud = 3;

    @Override
    public void onUpdate(double tpf){
        if (lastCloud - entity.getX() < FXGL.getAppWidth()) {
            newBgk();
        }
    }

    public void newBgk() {
        Texture cloud = texture("cloud.png");
        cloud.setPreserveRatio(true);
        cloud.setFitHeight(150);

        //for (int i = 1; i <= 20; ++i) {
            entityBuilder()
                    .at(lastCloud, ttlHeight - (ttlHeight - 150))
                    .type(EntityType.WALL)
                    .view(cloud)
                    .buildAndAttach();
        //}
        lastCloud += 5 * 500;
    }
}

// if only want add a cloud , can use wallbuildingcompoennt.java method
