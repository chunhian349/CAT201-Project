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

    private final int MAX = 10;                         //Create constant int 10
    private String[] scorename = new String[MAX];       //Set array of length 10

    private String score=null, name=null;               //Initialise score and name var

    public void setScore(String sc){
        score = sc;
    }           //setter function for score
    public void setName(String name) {
        this.name = name;
    }    //setter function for name
    public String getScore(){return score;}                   //getter function for score
    public String getName(){return name;}                     //getter function for name

    public PlayerScore() {           //Constructor
        readScore();
    }

    public void setScorename(String sn){

        //Insert name and score in every row if the data file is empty
        if(scorename[MAX-1] == null || scorename[MAX-1] == ""){
            for(int i=0; i<MAX; ++i){
                if(scorename[i] == null || scorename[i] == "")
                    scorename[i] = sn;
                else
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

    //Returns the highest score
    public int getHighestScore(){
        int highestscore=0;
        int[] sc = split();
        highestscore = findHighest(sc);
        return highestscore;
    }

    //Returns the lowest integer from the array
    private int findLowest(int scores[]){
        int lowest = scores[0];
        for(int i=1; i<MAX; ++i){
            if(scores[i] < lowest)
                lowest = scores[i];
        }
        return lowest;
    }

    //Returns the highest integer from the array
    private int findHighest(int scores[]){
        int highest = scores[0];
        for(int i=1; i<MAX; ++i){
            if(scores[i] > highest)
                highest = scores[i];
        }
        return highest;
    }

    //Returns the index position of the lowest value in the array
    private int findLowestIndex(int scores[]){
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

    //Returns the address of the array containing the name and score
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

    //Shows the Leaderboard from highest score to lowest score
    public void showScores(){
        if (scorename[0]==null || scorename[0]==""){                            //Runs if scorename is null
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
                        pane.addRow(0+u,getUIFactoryService().newText(u+")"));
                        pane.addRow(0+u,getUIFactoryService().newText(tempScoreName[1]));
                        pane.addRow(0+u, getUIFactoryService().newText(""+tempScoreName[0]));
                        break;
                    }
                }
                ++u;
            }
            getDialogService().showBox("Leaderboards", pane, getUIFactoryService().newButton("OK"));
        }
    }

    //Creates a file and store the highscore data in the file
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

    //Reads the data from file and store the data in scorename[]
    public void readScore(){
        try{
            FileReader readScore = new FileReader("scoreFile.txt");
            BufferedReader br = new BufferedReader(readScore);

            int i=0;
            String temp="";
            while((temp=br.readLine())!=null){
                scorename[i]=temp;
                ++i;
            }
            readScore.close();
            br.close();
        }catch(Exception e){
            System.out.println("Fail to read file");
        }
    }

}
