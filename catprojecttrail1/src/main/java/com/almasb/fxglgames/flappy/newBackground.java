package com.almasb.fxglgames.flappy;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.ViewComponent;
import com.almasb.fxgl.texture.Texture;
import javafx.scene.shape.Rectangle;

import static com.almasb.fxgl.dsl.FXGL.*;

public class newBackground extends Component {
    private double ttlHeight = 0;
    private double lastCloud = 0;
    
// Class may not needed

    @Override
    public void onUpdate(double tpf){
        if (lastCloud - entity.getX() < FXGL.getAppWidth()) {
            newBackground();
        }
    }

    /*public void newBgk() {
        Texture cloud = texture("cloud.png");
        cloud.setPreserveRatio(true);
        cloud.setFitHeight(150);

        //for (int i = 1; i <= 20; ++i) {
            entityBuilder()
                    .at(lastCloud, ttlHeight - (ttlHeight - 150))
                    .type(EntityType.CLOUD)
                    .view(cloud)
                    .buildAndAttach();
        //}
        lastCloud += 5 * 500;
    }*/

    public void newBackground() {
        Texture background = texture("sky_photo.jpg");
        background.setFitWidth(getAppWidth());
        background.setFitHeight(getAppHeight());

        //background.setFitHeight(0);
        entityBuilder()
                .at(lastCloud, ttlHeight) //- (ttlHeight - 150))
                .type(EntityType.CLOUD)
                .view(background)
                .buildAndAttach();
                lastCloud += getAppWidth();
    }
}

// if only want add a cloud , can use wallbuildingcompoennt.java method
