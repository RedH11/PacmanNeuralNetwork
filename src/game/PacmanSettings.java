package game;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.*;
import java.util.*;

import static javafx.scene.input.KeyCode.F;
import static javax.sound.sampled.AudioSystem.getAudioInputStream;

public class PacmanSettings extends Pane {

    final int MAXMOVES = 200;

    int[][] pacCoords = new int[MAXMOVES][2];
    int[][] gCoords = new int[MAXMOVES][2];
    int[][] g2Coords = new int[MAXMOVES][2];
    int[] pDirs = new int[MAXMOVES];
    int[] pFits = new int[MAXMOVES];
    boolean[] pPowered = new boolean[MAXMOVES];

    private Alert invalAlert = new Alert(Alert.AlertType.INFORMATION, null);
    String PacmanDataPath;
    VisualGame vg = null;

    String[] audioFiles = new String[] {"src/game/assets/8 bit rocky.wav", "src/game/assets/8 bit all star.wav"};
    AudioInputStream audioInputStream;
    Clip clip;

    public PacmanSettings(Stage stage, String PacmanDataPath, GraphicsContext gameGC) {
        this.PacmanDataPath = PacmanDataPath;

        VBox viewer = new VBox();
        viewer.setPadding(new Insets(5, 10, 5, 10));
        viewer.setSpacing(10);
        viewer.setPrefWidth(300);

        // VIEWER
        Label viewLabel = new Label("Evolution Settings");

        Label possibleGames = new Label("Options:\n");
        // Gets amount of files in the folder already
        File folder = new File(PacmanDataPath + "/Game" + 100 + "/Gens");
        File[] listOfFiles = folder.listFiles();
        ArrayList<File> fileList = new ArrayList<>();

        // Starts at one to skip DS.Store file
        for (int i = 0; i < listOfFiles.length; i++) {
            fileList.add(listOfFiles[i]);
        }

        /*Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File s1, File s2) {
                int letter = Integer.parseInt(s1.getName().substring(4)) - Integer.parseInt(s2.getName().substring(4));
                if (letter != 0) return letter;
                else return 0;
            }
        });*/

        for (int i = 1; i < fileList.size() - 1; i++) {
            possibleGames.setText(possibleGames.getText() + fileList.get(i).getName() + ", ");
        }

        if (fileList.size() > 0) {
            possibleGames.setText(possibleGames.getText() + fileList.get(fileList.size() - 1).getName());
            possibleGames.setWrapText(true);
            possibleGames.setPrefWidth(175);
        }

        TextField genNum = new TextField();
        genNum.setPromptText("Insert Generation Number");

        Label gameType = new Label("^ Insert Generation");

        Rectangle progressRect = new Rectangle(10, 10, Color.BLUE);

        Button showGame = new Button("Show Game");
        Label trainingProgress = new Label("TRAININGGGGGG PROGRESSSSSSS!!!!!");

        showGame.setOnAction(ev -> {
            Thread progress = new Thread(() -> {
                startAudio();
                Random random = new Random();
                try {
                    for (int i = 0; i < 400; i += 10) {
                        if (i < 50) progressRect.setWidth(i);
                        else if (i < 100) progressRect.setWidth(progressRect.getWidth() - 0.5);
                        else if (i < 150) progressRect.setWidth(progressRect.getWidth() + 1);
                        else if (i < 200) progressRect.setHeight(progressRect.getHeight() + 10);
                        else if (i < 210) progressRect.setHeight(10);
                        else if (i < 340) {
                            progressRect.setWidth(progressRect.getHeight() + 10);
                            progressRect.setHeight(progressRect.getHeight() + 10);
                        }
                        else if (i < 350) {
                            progressRect.setWidth(10);
                            progressRect.setHeight(10);
                        } else if (i < 390) {
                            progressRect.setFill(Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
                        }
                        else progressRect.setWidth(3000);
                        Thread.sleep(250);
                    }
                } catch (Exception ex) {}
            });

            Thread showing = new Thread(() -> {
                try {
                    Thread.sleep(10000);
                    String gameFile = PacmanDataPath + "/Game" + 100 + "/Gens/PacGen_" + Integer.parseInt(genNum.getText());
                    parseFile(gameFile);
                    vg = new VisualGame(200, pacCoords, pDirs, pFits, pPowered, gameGC, Integer.parseInt(genNum.getText()), 8);
                    clip.stop();
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            });

            progress.start();
            showing.start();
        });


        BorderPane root = new BorderPane();

        viewer.getChildren().addAll(viewLabel, possibleGames, genNum, gameType, showGame, progressRect, trainingProgress);
        root.setCenter(viewer);

        getChildren().add(root);
    }

    public void parseFile(String fileName) throws FileNotFoundException {
        String str = "";

        int coordsCollected = 0;

        File file = new File(fileName);
        Scanner sc = new Scanner(file);

        while (sc.hasNextLine()) {
            str = sc.nextLine();
            if (str.length() > 0) {
                // Trim off P:_
                str = str.substring(str.indexOf("P") + 3);

                // X coord
                int x = Integer.parseInt(str.substring(0, str.indexOf(" ")));
                pacCoords[coordsCollected][0] = x;
                // Trim off space between
                str = str.substring(str.indexOf(" ") + 1);
                // Y coord
                int y = Integer.parseInt(str.substring(0, str.indexOf(" ")));
                pacCoords[coordsCollected][1] = y;
                str = str.substring(str.indexOf("I1:") + 4);
                // Trim off space
                str = str.substring(str.indexOf(" ") + 1);
                // Pacman direction int
                pDirs[coordsCollected] = Integer.parseInt(str.substring(0, str.indexOf(" ")));
                // Trim off Pfit:_
                str = str.substring(str.indexOf(":") + 2);
                pFits[coordsCollected] = Integer.parseInt(str.substring(0, str.indexOf(" ")));
                // Trim off Ppowered:
                str = str.substring(str.indexOf(":") + 2);
                pPowered[coordsCollected] = Boolean.valueOf(str);
                coordsCollected++;
            }
        }

    }

    private void startAudio() {
        try {
            audioPlayer();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }


    public void audioPlayer() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        // create AudioInputStream object
        audioInputStream =
                getAudioInputStream(new File(audioFiles[0]));

        // create clip reference
        clip = AudioSystem.getClip();

        // open audioInputStream to the clip
        clip.open(audioInputStream);
        clip.start();
    }
}