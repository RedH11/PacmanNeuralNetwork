package game;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PacmanUI {
    private JTextPane welcomeToPacmanTheTextPane;
    private JRadioButton gen20;
    private JRadioButton gen100;
    private JRadioButton gen200;
    private JButton playButton;
    private int genNumber;

    public PacmanUI() {
        //Run game with 20 generations ghost when clicked
        gen20.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                genNumber = 20;
            }
        });
        //Run game with 100 generations ghost when clicked
        gen100.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                genNumber = 100;
            }
        });
        //Run game with 200 generations ghost when clicked
        gen200.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                genNumber = 200;
            }
        });
        //Start game
        playButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                //TODO: run game with chosen number of generations
            }
        });
    }
}
