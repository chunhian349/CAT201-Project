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

    private PlayerComponent playerComponent;        //Object for PlayerComponent

    /*
    initSettings calls the function from the prepared library to initialise all the settings of the game
    Also initialise the main menu made for the game from spMainMenu
    Initialise the Scoring system for the game
     */
    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1980);                         //Width of the app
        settings.setHeight(1080);                        //Height of the app
        settings.setTitle("Speedy PLane");               //Name of the app
        settings.setVersion("1.0");                      //Version of the app
        settings.setMainMenuEnabled(true);               //Enable & calls the main menu function from the library
        settings.addEngineService(PlayerScore.class);    //Provides linkage with playerScore class to retrieve highscore using getService
        //Adds the main menu scene
        settings.setSceneFactory(new SceneFactory(){
            @Override
            public FXGLMenu newMainMenu(){
                return new SpMainMenu();
            }
        });
    }

    /*
    initInput() is to get the input from user. Mouse button click to rise the plane
     */
    @Override
    protected void initInput() {

        UserAction fly = new UserAction("fly") {
            @Override
            protected void onAction() {
                playerComponent.fly();
            }
        };

        //Click or hold left mouse click to fly
        getInput().addAction(fly, MouseButton.PRIMARY);

    }


    /*
    initGameVars is used to initialize the variable used for to store the score for scoring system "score"
    and the colour of the UI for the display of the score "stageColor"
     */
    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("stageColor", Color.BLACK);
        vars.put("score", 0);
    }

    /*
    onPreInit() is a library preset function which will automatically be called when initialising the game
    This function is used to initialise the game background music using the function initMenuBGM()
     */
    @Override
    protected void onPreInit() {
        initMenuBGM();
    }

    /*
    initGame() is a library preset function which will automatically be called when starting the game
    This function is used to initialise the background and spawn the player entity.
    This is also used to increase the score based on time
     */
    @Override
    protected void initGame() {
        initBackground();
        initPlayer();
        getGameTimer().runAtInterval(() -> {            // count the score by 1
            inc("score", +1);
        }, Duration.millis(100));
    }

    /*
    initPhysics() is a library preset function which will automatically be called when starting the game
    This function is used to detect collision between entities in the game
    Collision between player and mountain or player and bird will result in game over.
    Collision between player and coin will result in increment of score by 25 and playing the sound of coin collection
     */
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

    /*
    initUI() is a library preset function which will automatically be called when starting the game
    Initialise UI for score so that the player can keep track of their score while playing
     */
    @Override
    protected void initUI() {
        Text uiScore = new Text("");
        uiScore.setFont(Font.font(72));                                      //Sets the font size
        uiScore.setTranslateX(getAppWidth() - 200);                          //Sets the x-position of the UI
        uiScore.setTranslateY(50);                                           //Sets the y-position of the UI
        uiScore.fillProperty().bind(getop("stageColor"));            //Retrieve the colour from the variable stageColor
        uiScore.textProperty().bind(getip("score").asString());      //Retrieve the int value from score

        addUINode(uiScore); //Adds the UI into the game
    }

    //initBackground() is a function used to initialise the background of the game
    private void initBackground() {

        Texture background = texture("sky_photo.jpg");
        //Sets the width and height of the background the same as the app
        background.setFitWidth(getAppWidth());
        background.setFitHeight(getAppHeight());

        //Create an entity for the background
        Entity bg = entityBuilder()
                .at(0,0) //Position of the entity
                .view(background) //Insert texture
                .buildAndAttach(); //Initialise

        //Fix the position of the background to the screen
        bg.xProperty().bind(getGameScene().getViewport().xProperty());
    }


    //Function used to initialise and spawn the player entity
    private void initPlayer() {
        playerComponent = new PlayerComponent();
        Texture plane = texture("plane.png");                       //Assign texture
        //Set width and height
        plane.setPreserveRatio(true);
        plane.setFitHeight(75);

        //Create entity
        Entity player = entityBuilder()
                .at(100, 500)                                           //Spawn position
                .type(PLAYER)                                                 //Entity type
                .bbox(new HitBox(BoundingShape.box(111, 75)))     //Collision hitbox
                .view(plane)                                                  //Insert texture
                .collidable()                                                 //Makes it collidable
                .with(playerComponent, new GameObjectComponent())           //collidable target objects
                .build();                                                     //Initialise

        //Fix the x-position of the player entity to the screen area so that is follows the screen instead of left out
        getGameScene().getViewport().setBounds(0, 0, Integer.MAX_VALUE, getAppHeight());
        getGameScene().getViewport().bindToEntity(player, getAppWidth() / 3, getAppHeight() / 2);

        //Spawn the entity
        spawnWithScale(player, Duration.seconds(0.01), Interpolators.BOUNCE.EASE_OUT());
    }

    //This function is used to play the bgm
    private void initMenuBGM(){
        getAudioPlayer().stopAllSoundsAndMusic();
        Music menuBGM = getAssetLoader().loadMusic("bgm1.mp3");
        getAudioPlayer().loopMusic(menuBGM);
    }

    //This function is used to play the sound for game over
    private void initDeathSF(){
        getAudioPlayer().stopAllSoundsAndMusic();
        Sound deathSF = getAssetLoader().loadSound("dying.mp3");
        getAudioPlayer().playSound(deathSF);
    }

    //Function to be called from initPhysics() when game over
    public void showGameOver() {
        initDeathSF();                                      //Stop bgm and plays the game over sound
        StringBuilder builder = new StringBuilder();        //Create string of message to be displayed after game over
        builder.append("Game Over!\n\n");
        builder.append("Final score: ")
                .append(geti("score"))
                .append("\n\n");
        builder.append("Would you like to restart?");

        getDialogService().showConfirmationBox(builder.toString(), yes -> //Adds the string of message into the dialog box with yes or no option
        {

            if (yes) {                                  //If player chose yes to restart the game
                initMenuBGM();                          //Stop game over sound and play bgm
                getGameController().startNewGame();     //Start new game
            } else {
                initMenuBGM();                          //Stop game over sound and play bgm
                getGameController().gotoMainMenu();     //Goes back to main menu
            }

        });
        enterScore();                                   //Calls the function enterScore()
    }

    //enterScore will be called if the player achieved higher score than the highest score
    public void enterScore(){
        if(geti("score") > FXGL.getService(PlayerScore.class).getHighestScore()) {

            StringBuilder highscore = new StringBuilder();      //Create a string of message when achived highscore
            highscore.append("New High score: ")
                    .append(geti("score"))
                    .append("\nEnter your name");

            //Inserts the string message into a dialog box with input box
            //Players can type in their name and the name will be saved and display on the leaderboard
            getDialogService().showInputBox(highscore.toString(),
                    s -> s.matches("[a-zA-Z]*"), name -> {

                        getService(PlayerScore.class).setScore(String.valueOf(geti("score")));
                        getService(PlayerScore.class).setName(name);
                        String scnm = getService(PlayerScore.class).getName() + " " + getService(PlayerScore.class).getScore();
                        getService(PlayerScore.class).setScorename(scnm);
                    });
        }
    }

    //Launch the app
    public static void main(String[] args) {
        launch(args);
    }
}

