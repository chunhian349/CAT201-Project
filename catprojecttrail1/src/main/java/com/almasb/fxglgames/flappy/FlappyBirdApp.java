package com.almasb.fxglgames.flappy;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.audio.*;
import com.almasb.fxgl.dsl.FXGL;
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
        settings.addEngineService(playerScore.class);
        settings.setSceneFactory(new SceneFactory(){
            @Override
            public FXGLMenu newMainMenu(){
                return new spMainMenu();
            }
        });
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
        }, Duration.millis(100));
    }

    @Override
    protected void initPhysics() {
        onCollisionBegin(PLAYER, MOUNTAIN, (player, mountain) -> showGameOver());
        onCollisionBegin(PLAYER, BIRD, (player, bird) -> showGameOver());
        onCollisionBegin(PLAYER, COIN, (player, coin) -> {
            getAudioPlayer().playSound(getAssetLoader().loadSound("coin_sound.mp3"));
            coin.removeFromWorld();
            inc("score", +25);
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
        plane.setFitHeight(75);

        Entity player = entityBuilder()
                .at(100, 500)
                .type(PLAYER)
                .bbox(new HitBox(BoundingShape.box(111, 75)))
                .view(plane)
                .collidable()
                .with(playerComponent, new WallBuildingComponent())
                .build();

        getGameScene().getViewport().setBounds(0, 0, Integer.MAX_VALUE, getAppHeight());
        getGameScene().getViewport().bindToEntity(player, getAppWidth() / 3, getAppHeight() / 2);

        spawnWithScale(player, Duration.seconds(0.01), Interpolators.BOUNCE.EASE_OUT());
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
            if (yes) {
                initMenuBGM();
                getGameController().startNewGame();
            } else {
                initMenuBGM();
                getGameController().gotoMainMenu();
            }

        });
        enterScore();

    }

    public void enterScore(){
        if(geti("score") > FXGL.getService(playerScore.class).getHighestScore()) {
        StringBuilder highscore = new StringBuilder();
        highscore.append("New High score: ")
                .append(geti("score"))
                .append("\nEnter your name");
            getDialogService().showInputBox(highscore.toString(),
                    s -> s.matches("[a-zA-Z]*"), name -> {

                        getService(playerScore.class).setScore(String.valueOf(geti("score")));
                        getService(playerScore.class).setName(name);
                        String scnm = getService(playerScore.class).getName() + " " + getService(playerScore.class).getScore();
                        getService(playerScore.class).setScorename(scnm);
                    });
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

