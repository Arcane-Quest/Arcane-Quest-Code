package org.example;


import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


public class Main {
    public static void main(String[] args) {
        var gamePanel = new Gamepanel();

        try {
            // checkForUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        var window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Arcane Quest");
        window.add(gamePanel);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gamePanel.setupGame();
        gamePanel.startGameThread();


    }

    public static void checkForUpdate() throws URISyntaxException, IOException {
        if(true) { //FIXME: actually see version etc.
            if(JOptionPane.showConfirmDialog(null, "Redirect to update instructions?",
                    "Update Availible!", JOptionPane.YES_NO_OPTION) == 0) {
                // they answered yes (0 = yes)

                if(Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    Desktop.getDesktop().browse(new URI("https://github.com/robot-penguin34/Untitled-mage-game/blob/master/HOWTOUPDATE.md"));
                    System.exit(0);
                }
            }
        }
    }
}