package com.almasb.fxglgames.flappy;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.audio.*;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.input.virtual.VirtualButton;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.texture.Texture;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;

import static com.almasb.fxglgames.flappy.EntityType.*;

/**
 * CAT201 PROJECT
 */
public class FlappyBirdApp extends GameApplication {

    private PlayerComponent playerComponent;

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
        getInput().addAction(new UserAction("Jump") {
            @Override
            protected void onAction() { playerComponent.jump();
            }       // onActionBegin() to onAction() to hold to go up
        }, KeyCode.SPACE, VirtualButton.UP);
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("stageColor", Color.BLACK);
        vars.put("score", 0);
    }

    @Override
    protected void onPreInit() {
        initMenuBGM();
    }

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
        onCollisionBegin(PLAYER, MOUNTAIN, (player, mountain) -> showGameOver());
        onCollisionBegin(PLAYER, BIRD, (player, bird) -> showGameOver());
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

        Group dpadView = getInput().createVirtualDpadView();

        addUINode(dpadView, 0, 625);
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

    private void initMenuBGM(){
        getAudioPlayer().stopAllSoundsAndMusic();
        Music menuBGM = getAssetLoader().loadMusic("bgm1.mp3");
        getAudioPlayer().loopMusic(menuBGM);
    }

    /*private void initGameBGM(){
        getAudioPlayer().stopAllSoundsAndMusic();
        Music menuBGM = getAssetLoader().loadMusic("bgm2.mp3");
        getAudioPlayer().loopMusic(menuBGM);
    }*/

    private void initDeathSF(){
        getAudioPlayer().stopAllSoundsAndMusic();
        Sound deathSF = getAssetLoader().loadSound("dying.mp3");
        getAudioPlayer().playSound(deathSF);
    }

    public void showGameOver() {
        initDeathSF();
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
                initMenuBGM();
                getGameController().startNewGame();
            }
            else
            {
                initMenuBGM();
                getGameController().gotoMainMenu();
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}

