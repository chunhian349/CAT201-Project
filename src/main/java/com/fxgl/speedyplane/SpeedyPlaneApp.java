package com.fxgl.speedyplane;

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

import static com.fxgl.speedyplane.EntityType.*;

/**
 * CAT201 PROJECT
 */
public class SpeedyPlaneApp extends GameApplication {

    private PlayerComponent playerComponent;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1920);
        settings.setHeight(1080);
        settings.setTitle("Speedy Plane");
        settings.setVersion("1.0");
        settings.setAppIcon("plane.png");
        settings.setMainMenuEnabled(true);
        settings.addEngineService(PlayerScore.class);
        settings.setSceneFactory(new SceneFactory(){
            @Override
            public FXGLMenu newMainMenu(){
                return new SpMainMenu();
            }
        });
    }

    @Override
    protected void initInput() {
        //Click or hold left mouse click to fly
        getInput().addAction(new UserAction("Fly") {
            @Override
            protected void onAction() {
                playerComponent.fly();
            }       // onActionBegin() to onAction() to hold to go up
        }, MouseButton.PRIMARY);
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
                .with(playerComponent, new GameObjectComponent())
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

    private void initDeathSF(){
        getAudioPlayer().stopAllSoundsAndMusic();
        Sound deathSF = getAssetLoader().loadSound("dying.mp3");
        getAudioPlayer().playSound(deathSF);
    }

    public void showGameOver() {
        initDeathSF();
        String builder = "Game Over!\n\n" +
                "Final score: " +
                geti("score") +
                "\n\n" +
                "Would you like to restart?";
        getDialogService().showConfirmationBox(builder, yes ->
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
        if(geti("score") > FXGL.getService(PlayerScore.class).getHighestScore()) {
            String highscore = "New High score: " +
                    geti("score") +
                    "\nEnter your name";
            getDialogService().showInputBox(highscore,
                    s -> s.matches("[a-zA-Z]*"), name -> {

                        getService(PlayerScore.class).setScore(String.valueOf(geti("score")));
                        getService(PlayerScore.class).setName(name);
                        String scnm = getService(PlayerScore.class).getName() + " " + getService(PlayerScore.class).getScore();
                        getService(PlayerScore.class).setScorename(scnm);
                    });
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

