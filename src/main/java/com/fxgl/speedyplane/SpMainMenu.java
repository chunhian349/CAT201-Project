package com.fxgl.speedyplane;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.Texture;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;

/**
 * Customize Speedy Plane Main Menu
 */
public class SpMainMenu extends FXGLMenu{

    public SpMainMenu() {
        super(MenuType.MAIN_MENU);          // declare this as main menu

        getContentRoot().getChildren().setAll(new Rectangle(getAppWidth(), getAppHeight()));        // set background

        var title = getUIFactoryService().newText(getSettings().getTitle(), Color.WHITE, 46.0);         // set title
        title.setStroke(Color.WHITESMOKE);
        title.setStrokeWidth(1.5);

        var version = getUIFactoryService().newText(getSettings().getVersion(), Color.WHITE, 22.0);        // set version

        getContentRoot().getChildren().addAll(title, version);                                      // add title and version

        Texture bgPic = texture("mainMenuBgk.png");                                         // add background image
        getContentRoot().getChildren().addAll(bgPic);

        // declare the menu button
        var menuBox = new VBox(4,
                new MenuButton("New Game", () -> fireNewGame()),
                new MenuButton("Leaderboards", () -> displayScores()),
                new MenuButton("Help", () -> giveHelp()),
                new MenuButton("Exit", () -> fireExit())
        );
        menuBox.setAlignment(Pos.TOP_CENTER);
        menuBox.setTranslateX(getAppWidth() / 2.0 - 125);
        menuBox.setTranslateY(getAppHeight() / 2.0 + 125);

        getContentRoot().getChildren().addAll(menuBox);         // add the menu button
    }

    //Give gameplay help message
    private void giveHelp(){
        GridPane pane = new GridPane();
        if (!FXGL.isMobile()) {
            pane.setEffect(new DropShadow(5, 3.5, 3.5, Color.BLUE));
        }
        pane.setHgap(25);
        pane.setVgap(10);
        pane.setAlignment(Pos.CENTER);
        pane.addRow(0, getUIFactoryService().newText("Control:"));
        pane.addRow(1, getUIFactoryService().newText("Click or press left mouse button to fly"));
        pane.addRow(3, getUIFactoryService().newText("Gameplay:"));
        pane.addRow(4, getUIFactoryService().newText("Avoid colliding the birds and big rock mountain."));
        pane.addRow(5, getUIFactoryService().newText("You can collect coin for extra score."));
        pane.addRow(6, getUIFactoryService().newText("Try to get higher score as possible, have fun!"));

        getDialogService().showBox("HELP", pane, getUIFactoryService().newButton("OK"));
    }

    //Display leaderboard
    private void displayScores() {
        FXGL.getService(PlayerScore.class).showScores();
    }

    /**
     * Inner class extends abstract class javafx.scene.Parent
     * Set text format for menu button
     */
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