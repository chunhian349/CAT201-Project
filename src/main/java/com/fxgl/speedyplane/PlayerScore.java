package com.fxgl.speedyplane;

import com.almasb.fxgl.core.EngineService;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getDialogService;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getUIFactoryService;

public class PlayerScore extends EngineService {

    private final int MAX = 10;
    private String[] scorename = new String[MAX];

    // to get the score and name from user at mainMenuTest
    private String score = null, name = null;

    public void setScore(String sc){
        score = sc;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getScore(){return score;}
    public String getName(){return name;}

    public PlayerScore() {
        readScore();
    }

    public void setScorename(String sn){
        if(scorename[MAX-1] == null || scorename[MAX - 1].equals("")){
            for(int i=0; i<MAX; ++i){
                if(scorename[i] == null || scorename[i].equals(""))
                    scorename[i] = sn;
                break;
            }
        }else{                                              // if array is full
            int[] splitScore = split();                     // find the lowest value and return the index
            int index = findLowestIndex(splitScore);
            scorename[index] = sn;                          // reallocate the index and assign new value to it
        }
        storeScore();
    }

    public String[] getScoreName() {
        return scorename;
    }

    public String getScoreNameInd(int index){
        return scorename[index];
    }

    public int getHighestScore(){
        int highestscore = 0;
        int[] sc = split();
        highestscore = findHighest(sc);
        return highestscore;
    }

    private int findLowest(int[] scores){
        int lowest = scores[0];

        for(int i=1; i<MAX; ++i){
            if(scores[i] < lowest)
                lowest = scores[i];
        }

        return lowest;
    }

    private int findHighest(int[] scores){
        int highest = scores[0];

        for(int i=1; i<MAX; ++i){
            if(scores[i] > highest)
                highest = scores[i];
        }

        return highest;
    }

    private int findLowestIndex(int[] scores){
        int lowest = scores[0];
        int index = 0;

        for(int i=1; i<MAX; ++i){
            if(scores[i] < lowest) {
                lowest = scores[i];
                index = i;
            }
        }

        return index;
    }

    private int[] split(){
        int[] tempSc = new int[MAX];

        for (int x=0; x<MAX; ++x){
            if(scorename[x] != null){
                String[] words = scorename[x].split("\\s");
                tempSc[x] = Integer.parseInt(words[1]);                  // convert String to integer
            }else{
                break;
            }
        }

        return tempSc;
    }

    public void showScores(){
        if (scorename[0]==null || scorename[0].equals("")){
            getDialogService().showMessageBox("There is no records");
        }else{
            int[] splitScore = split();
            Arrays.sort(splitScore);
            String temp = "";

            int u = 1;          // loop counter for numbering

            GridPane pane = new GridPane();
            pane.setEffect(new DropShadow(3, 2.5, 2.5, Color.YELLOW));
            pane.setHgap(15);
            pane.setVgap(15);
            pane.setAlignment(Pos.CENTER);
            pane.addRow(0, getUIFactoryService().newText("Rankings"));
            pane.addRow(0, getUIFactoryService().newText("Score"));
            pane.addRow(0, getUIFactoryService().newText("Player Name"));

            for(int i=MAX-1; i>=0; --i){
                temp = String.valueOf(splitScore[i]);           // convert highest score into string
                for(int x=0; x<MAX; ++x){
                    if(scorename[x].endsWith(" "+temp)){
                        String[] tempScoreName = scorename[x].split("\\s");
                        pane.addRow(u, getUIFactoryService().newText(u+")"));
                        pane.addRow(u, getUIFactoryService().newText(tempScoreName[1]));
                        pane.addRow(u, getUIFactoryService().newText(""+tempScoreName[0]));
                        break;
                    }
                }
                ++u;
            }
            getDialogService().showBox("Leaderboards", pane, getUIFactoryService().newButton("OK"));
        }
    }

    /**
     create file and store the score in it
    **/

    public void storeScore(){
        try {
            FileWriter scoreFile = new FileWriter("scoreFile.txt");
            for (int i=0; i<MAX; ++i){
                scoreFile.write(scorename[i]+"\n");
            }
            scoreFile.close();
        }catch(Exception e){
            System.out.println("Failed to write file");
        }
    }

    public void readScore(){
        try{
            FileReader readScore = new FileReader("scoreFile.txt");
            BufferedReader br = new BufferedReader(readScore);

            int i = 0;
            String temp = "";
            while((temp=br.readLine())!=null){
                scorename[i] = temp;
                ++i;
            }
            readScore.close();
            br.close();
        }catch(Exception e){
            System.out.println("Fail to read file");
        }
    }

}
