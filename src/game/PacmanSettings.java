package game;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;

/*
PacmanSettings allows us to save information from Pacman and Genetic Algorithim and display it in a simple UI.
 */
public class PacmanSettings extends Pane {

    private Alert invalAlert = new Alert(Alert.AlertType.INFORMATION, null);
    String PacmanDataPath;
    VisualGame vg = null;

    /**
     * Settings for the pacman game
     * @param stage viewing stage
     * @param PacmanDataPath path to PacmanData folder
     * @param gameGC
     */
    public PacmanSettings(Stage stage, String PacmanDataPath, GraphicsContext gameGC) {
        this.PacmanDataPath = PacmanDataPath;

        // Stores the pacman evolution settings
        VBox settings = new VBox();
        settings.setPadding(new Insets(5, 10, 5, 10));
        settings.setSpacing(10);
        settings.setPrefWidth(250);

        Label evolLabel = new Label("Evolution Settings");

        Label popLbl = new Label("Population");
        TextField popTF = new TextField("400");
        popTF.setPromptText("Population Size");

        Label genLbl = new Label("Generations");
        TextField gensTF = new TextField("100");
        gensTF.setPromptText("Total Generations");

        Label mutateLbl = new Label("Mutation");
        TextField mutateTF = new TextField(".65");
        mutateTF.setPromptText("Mutation Chance");

        Label topGhosts = new Label("Top Ghosts");
        TextField topGhostsTF = new TextField("0");
        topGhostsTF.setPromptText("How Many Top Ghosts to Keep");

        Label lowerGhosts = new Label("Lower Ghosts");
        TextField lowerGhostsTF = new TextField("0");
        lowerGhostsTF.setPromptText("How Many Worse Ghosts to Keep");

        Label lowerPac = new Label("Lower Pacman");
        TextField lowerPacmanTF = new TextField("6");
        lowerPacmanTF.setPromptText("How Many Worse Pacman to Keep");

        Label topPac = new Label ("Top Pacman");
        TextField topPacmanTF = new TextField("40");
        topPacmanTF.setPromptText("How Many Top Pacman to Keep");

        Button evolve = new Button("Evolve");
        evolve.setOnAction(ev -> {
            Thread tests = new Thread(() -> {
                int counter = 0;
                evolve.setDisable(true);
                    try {
                        GeneticAlgorithm ga = new GeneticAlgorithm(
                                PacmanDataPath, Integer.parseInt(popTF.getText()),
                                Integer.parseInt(gensTF.getText()), Double.parseDouble(mutateTF.getText()),
                                Integer.parseInt(lowerGhostsTF.getText()), Integer.parseInt(topGhostsTF.getText()),
                                Integer.parseInt(lowerPacmanTF.getText()), Integer.parseInt(topPacmanTF.getText()), evolve
                        );

                        ga.makeGenerations();

                    } catch (IOException ex) {
                        // Avoid throwing IllegalStateException by running from game non-JavaFX thread.
                        Platform.runLater(() -> {
                            evolve.setDisable(false);
                            invalAlert.setTitle("Error Evolving" + ex);
                            invalAlert.show();
                        });
                    }
                    counter++;
            });

            tests.start();
        });

        // Viewer Settings Creator
        VBox viewer = new VBox();
        viewer.setPadding(new Insets(5, 10, 5, 10));
        viewer.setSpacing(10);
        viewer.setPrefWidth(250);

        Label viewLabel = new Label("Viewer Settings");

        TextField gameNum = new TextField();
        gameNum.setPromptText("Insert Game Number");

        Label possibleGames = new Label("Options:\n");
        // Gets amount of files in the folder already
        File folder = new File(PacmanDataPath);
        File[] listOfFiles = folder.listFiles();
        ArrayList<File> fileList = new ArrayList<>();

        // Starts at one to skip DS.Store file
        for (int i = 1; i < listOfFiles.length; i++) {
            fileList.add(listOfFiles[i]);
        }
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

        Button gameType = new Button("Pacman");
        gameType.setOnAction(ev -> {
            if (gameType.getText().contains("Pacman")) gameType.setText("Inky");
            else gameType.setText("Pacman");
        });

        Button showGame = new Button("Show Game");
        showGame.setOnAction(ev -> {
            showGame.setDisable(true);
            Thread showing = new Thread(() -> {
                try {
                    if (vg != null) vg.stop();
                    boolean pacmanGame = gameType.getText().contains("Pacman");
                    InfoStorage is = parseFile(Integer.parseInt(gameNum.getText()), Integer.parseInt(genNum.getText()), pacmanGame);
                    vg = new VisualGame(gameGC, is, Integer.parseInt(genNum.getText()), showGame, 8);
                } catch (Exception ex) {
                    // Avoid throwing IllegalStateException by running from game non-JavaFX thread.
                    Platform.runLater(() -> {
                        showGame.setDisable(false);
                    });
                }
            });
            showing.start();
        });

        BorderPane root = new BorderPane();

        settings.getChildren().addAll(evolLabel, popLbl, popTF, genLbl, gensTF, mutateLbl, mutateTF, topGhosts, topGhostsTF, lowerGhosts, lowerGhostsTF, topPac, topPacmanTF, lowerPac, lowerPacmanTF, evolve);
        viewer.getChildren().addAll(viewLabel, possibleGames, gameNum, genNum, gameType, showGame);
        root.setLeft(settings);
        root.setRight(viewer);

        getChildren().add(root);
    }

    /**
     * Reads objects from infostorage file
     * @param filename the file path
     * @return objects in storage
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private static ArrayList<InfoStorage> readObjectsFromFile(String filename) throws IOException, ClassNotFoundException {
        ArrayList<InfoStorage> objects = new ArrayList<>();
        InputStream is = null;
        try {
            is = new FileInputStream(filename);
            ObjectInputStream ois = new ObjectInputStream(is);
            while (true) {
                try {
                    InfoStorage object = (InfoStorage) ois.readObject();
                    objects.add(object);
                } catch (EOFException ex) {
                    break;
                }
            }
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return objects;
    }

    /**
     *
     * @param gameNum
     * @param generationNum
     * @param pacman if there is pacman or not
     * @return the specified game
     */
    public InfoStorage parseFile(int gameNum, int generationNum, boolean pacman) {
        String gameFile = "";

        // Viewing Setup
        if (pacman) gameFile = PacmanDataPath + "/Game" +  gameNum + "/Gens/PacGens";
        else gameFile = PacmanDataPath + "/Game" +  gameNum + "/Gens/InkGens";

        try {
            ArrayList<InfoStorage> allGames = readObjectsFromFile(gameFile);

            return allGames.get(generationNum);

        } catch (Exception ex) {
            invalAlert.setContentText("File Not Found");
            invalAlert.show();
        }
        return null;
    }
}
