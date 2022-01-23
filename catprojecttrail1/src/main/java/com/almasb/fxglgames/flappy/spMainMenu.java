package com.almasb.fxglgames.flappy;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.view.KeyView;
import com.almasb.fxgl.texture.Texture;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;
import static javafx.scene.input.KeyCode.SPACE;
import static javafx.scene.input.KeyCode.UP;


public class spMainMenu extends FXGLMenu{

    private VBox scoresRoot = new VBox(10);
    private Node highScore;

    public spMainMenu() {
        super(MenuType.MAIN_MENU);          // declare this as main menu

        getContentRoot().getChildren().setAll(new Rectangle(getAppWidth(), getAppHeight()));        // set background

        var title = getUIFactoryService().newText(getSettings().getTitle(), Color.WHITE, 46.0);         // set title
        title.setStroke(Color.WHITESMOKE);
        title.setStrokeWidth(1.5);

        var version = getUIFactoryService().newText(getSettings().getVersion(), Color.WHITE, 22.0);        // set version

        getContentRoot().getChildren().addAll(title, version);                                      // output title and version

        Texture bgPic = texture("mainMenuBgk.png");                                         // give background a shirt to wear
        getContentRoot().getChildren().addAll(bgPic);

        var menuBox = new VBox(             // declare the menu button
                4,
                new MenuButton("New Game", () -> fireNewGame()),
                new MenuButton("Leaderboards", () -> displayScores()),
                new MenuButton("Help", () -> giveHelp()),
                new MenuButton("Exit", () -> fireExit())
        );
        menuBox.setAlignment(Pos.TOP_CENTER);

        menuBox.setTranslateX(getAppWidth() / 2.0 - 125);
        menuBox.setTranslateY(getAppHeight() / 2.0 + 125);

        scoresRoot.setPadding(new Insets(10));
        scoresRoot.setAlignment(Pos.TOP_LEFT);

        StackPane hsRoot = new StackPane(new Rectangle(450, 250, Color.color(0, 0, 0.2, 0.8)), scoresRoot);
        hsRoot.setAlignment(Pos.TOP_CENTER);
        hsRoot.setCache(true);
        hsRoot.setCacheHint(CacheHint.SPEED);
        hsRoot.setTranslateX(getAppWidth());
        hsRoot.setTranslateY(menuBox.getTranslateY());

        highScore = hsRoot;

        getContentRoot().getChildren().addAll(menuBox);         // output the menu button
    }

    private void giveHelp(){
        GridPane pane = new GridPane();
        if (!FXGL.isMobile()) {
            pane.setEffect(new DropShadow(5, 3.5, 3.5, Color.BLUE));
        }
        pane.setHgap(25);
        pane.setVgap(10);
        pane.setAlignment(Pos.CENTER);
        pane.addRow(0, getUIFactoryService().newText("How To Fly?"));
        pane.addRow(1, getUIFactoryService().newText("Press the screen to fly"));
        pane.addRow(3, getUIFactoryService().newText("How To Win?"));
        pane.addRow(4, getUIFactoryService().newText("Avoid colliding the bird and big rock mountain."));
        pane.addRow(5, getUIFactoryService().newText("Continue your flight till the end."));

        getDialogService().showBox("HELP", pane, getUIFactoryService().newButton("OK"));
    }

    private void displayScores() {
        FXGL.getService(playerScore.class).showScores();
    }

    private static class MenuButton extends Parent {
        MenuButton(String name, Runnable action) {
            var text = getUIFactoryService().newText(name, Color.WHITE, 36.0);
            text.setStrokeWidth(1.5);
            text.strokeProperty().bind(text.fillProperty());

            text.fillProperty().bind(
                    Bindings.when(hoverProperty())
                            .then(Color.BLUE)
                            .otherwise(Color.WHITE)
            );

            setOnMouseClicked(e -> action.run());

            setPickOnBounds(true);

            getChildren().add(text);
        }
    }
}



