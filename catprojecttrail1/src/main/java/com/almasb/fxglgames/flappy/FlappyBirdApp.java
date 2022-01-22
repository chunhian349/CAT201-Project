package com.almasb.fxglgames.flappy;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.*;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.texture.Texture;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;

import static com.almasb.fxglgames.flappy.EntityType.*;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class FlappyBirdApp extends GameApplication {

    private PlayerComponent playerComponent;
    private boolean requestNewGame = false;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1980);
        settings.setHeight(1080);
        settings.setTitle("Flappy Bird Clone");
        settings.setVersion("1.0");
        settings.setMainMenuEnabled(true);
    }

    @Override
    protected void initInput() {

        UserAction jump = new UserAction("Jump") {
            @Override
            protected void onAction() {
                playerComponent.jump();
            }       // onActionBegin() to onAction() to hold to go up
        };

        //Click or hold left mouse click to fly
        getInput().addAction(jump, MouseButton.PRIMARY);

    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("stageColor", Color.BLACK);
        vars.put("score", 0);
    }

    //@Override
    //protected void onPreInit() {
    //    loopBGM("bgm.mp3");
    //}

    @Override
    protected void initGame() {
        initBackground();
        initPlayer();
        getGameTimer().runAtInterval(() -> {            // count the score by 1
            inc("score", +1);
        }, Duration.seconds(1));
    }

    @Override
    protected void initPhysics() {
        onCollisionBegin(PLAYER, MOUNTAIN, (player, mountain) -> {
            showGameOver();
        });
        onCollisionBegin(PLAYER, BIRD, (player, bird) -> {
            showGameOver();
        });
    }

    @Override
    protected void initUI() {
        Text uiScore = new Text("");
        uiScore.setFont(Font.font(72));
        uiScore.setTranslateX(getAppWidth() - 200);
        uiScore.setTranslateY(50);
        uiScore.fillProperty().bind(getop("stageColor"));
        uiScore.textProperty().bind(getip("score").asString());

        addUINode(uiScore);
    }

    @Override
    protected void onUpdate(double tpf) {
        if (geti("score") == 3000) {
            showGameOver();
        }

        if (requestNewGame) {
            requestNewGame = false;
            getGameController().startNewGame();
        }
    }

    private void initBackground() {

        Texture background = texture("sky_photo.jpg");
            background.setFitWidth(getAppWidth());
            background.setFitHeight(getAppHeight());

            Entity bg = entityBuilder()
                    .at(0,0)
                    .type(EntityType.CLOUD)
                    .view(background)
                    .buildAndAttach();

            bg.xProperty().bind(getGameScene().getViewport().xProperty());
            bg.yProperty().bind(getGameScene().getViewport().yProperty());
    }

    private void initPlayer() {
        playerComponent = new PlayerComponent();
        Texture plane = texture("plane.png");
        plane.setPreserveRatio(true);
        plane.setFitHeight(50);

        Entity player = entityBuilder()
                .at(100, 500)
                .type(PLAYER)
                .bbox(new HitBox(BoundingShape.box(74, 50)))
                .view(plane)
                .collidable()
                .with(playerComponent, new WallBuildingComponent())
                .build();

        getGameScene().getViewport().setBounds(0, 0, Integer.MAX_VALUE, getAppHeight());
        getGameScene().getViewport().bindToEntity(player, getAppWidth() / 3, getAppHeight() / 2);

        spawnWithScale(player, Duration.seconds(0.86), Interpolators.BOUNCE.EASE_OUT());
    }

    public void showGameOver() {
        StringBuilder builder = new StringBuilder();
        builder.append("Game Over!\n\n");
        builder.append("Final score: ")
                .append(geti("score"))
                .append("\n\n");
        builder.append("Would you like to restart?");
        getDialogService().showConfirmationBox(builder.toString(), yes ->
        {
            if(yes)
            {
                getGameController().startNewGame();
            }
            else
            {
                getGameController().gotoMainMenu();
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}

