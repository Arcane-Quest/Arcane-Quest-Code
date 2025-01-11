package org.example;

import ai.PathFinder;
import data.SaveLoad;
import entity.Entity;
import entity.Player;
import environment.EnvironmentManager;
import interactiveTile.InteractiveTile;
import tile.TileManager;

import javax.swing.JPanel;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

public class Gamepanel extends JPanel implements Runnable {
    // Settings section
    final int originalTileSize = 16; // 32x32 tiles (players, npcs etc)
    final int scale = 4; // 16x16 becomes 48x48

    public final int tilesize = originalTileSize * scale;
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tilesize * maxScreenCol;
    public final int screenHeight = tilesize * maxScreenRow;
    public final int maxMap = 100;
    public int currentMap = 0;

    //TODO: update all arrays to 1D
    public Entity obj[][] = new Entity[maxMap][10]; // you can only display 10 objects at the same time (increase the number for more)
    public Entity npc[][] = new Entity[maxMap][10];
    public Entity monster[][] = new Entity[maxMap][20];
    public InteractiveTile[][] interactiveTile = new InteractiveTile[maxMap][50];
    public Entity projectile[][] = new Entity[maxMap][20];
    public ArrayList<Entity> particleList = new ArrayList<>();
    ArrayList<Entity> entityList = new ArrayList<>();
    public boolean saveDataExists = false;
    public Sound music = new Sound();
    public Sound soundEffects = new Sound();

    // screen ratio 4:3
    //FPS
    int FPS = 60;

    // World map
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int worldWidth = tilesize * maxWorldCol;
    public final int worldHeight = tilesize * maxWorldRow;

    // Threads
    public EntityGenerator entityGenerator = new EntityGenerator(this);
    public Keyhandler keyH = new Keyhandler(this);
    public Thread gameThread;
    public CollisionChecker cChecker = new CollisionChecker(this);
    public Player player = new Player(this, keyH);
    public UI ui = new UI(this);
    public EventHandler eHandler = new EventHandler(this);
    public PathFinder pFinder = new PathFinder(this);
    public TileManager tileM = new TileManager(this);
    EnvironmentManager eManager = new EnvironmentManager(this);
    SaveLoad saveLoad = new SaveLoad(this);


    // set player default position
    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;

    // Game states
    public enum GameStates {
        PAUSED,
        GAMEOVER,
        TITLE,
        OPTIONS,
        ACTIVE,
        DIALOGUE,
        INVENTORY,
        TELEPORT,
        TRADING
    }
    public GameStates gameState = GameStates.TITLE;


    public Gamepanel() {
        setPreferredSize(new Dimension(screenWidth, screenHeight));
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
        addKeyListener(keyH);
        setFocusable(true);
    }

    public void setupGame() {
        File f = new File("saves/save_sigils.dat");
        if (f.exists() && !f.isDirectory()) {
            saveDataExists = true;
        }

        gameState = GameStates.TITLE;
        eManager.setup();
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        System.out.println("Game thread started");

        // find out how long each update should take to fit FPS frames in 1 second
        double drawInterval = 1_000_000_000/FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;

        while (gameThread != null) {
            update();

            // Draw screen with updated logic
            repaint(); // this is actually the paint component function, but it compiles to repaint

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime/1_000_000;

                if(remainingTime < 0){
                    remainingTime = 0;
                }

                Thread.sleep((long) remainingTime);

                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void update() {
        switch (gameState) {
            case ACTIVE:

                for (int i = 0; i < projectile[1].length; i++) {
                    if (projectile[currentMap][i] != null) {
                        if(!projectile[currentMap][i].alive) {
                            projectile[currentMap][i] = null;
                        } else if (projectile[currentMap][i].alive){
                            projectile[currentMap][i].update();
                        }
                    }
                }

                for (int i = 0; i < particleList.size(); i++) {
                    if (particleList.get(i) != null) {
                        if(!particleList.get(i).alive) {
                            particleList.remove(i);
                        } else if (particleList.get(i).alive){
                            particleList.get(i).update();
                        }
                    }
                }

                player.update();
                for (int i = 0; i < npc[1].length; i++) { // Entering the second dimension requires black magic
                    if (npc[currentMap][i] != null) {     // keep the npc[1] and other following
                        npc[currentMap][i].update();
                    }
                }

                for (int i = 0; i < monster[1].length; i++) {
                    if (monster[currentMap][i] != null) {
                        if(!monster[currentMap][i].alive) {
                            monster[currentMap][i].checkDrop();
                            monster[currentMap][i] = null;
                            continue;
                        }
                        if(!monster[currentMap][i].dyingState) {
                            monster[currentMap][i].update();
                        }
                    }
                    }

                for (int i = 0; i < interactiveTile[1].length; i++) {
                    if (interactiveTile[currentMap][i] != null) {
                        interactiveTile[currentMap][i].update();
                    }
                }

                eManager.update();

                break;
            case PAUSED:
                break;
            case DIALOGUE:
                break;
        }


    }
    public void paintComponent(Graphics graphics) {
        // call repaint() instead of paintComponent()
        // believe me
        super.paintComponent(graphics);

        Graphics2D graphics2D = (Graphics2D)graphics;

        if (Objects.requireNonNull(gameState) == GameStates.TITLE ||
                Objects.requireNonNull(gameState) == GameStates.OPTIONS) {
            ui.draw(graphics2D);
        } else {
            tileM.draw(graphics2D);

            for (int i = 0; i < interactiveTile[1].length; i++) {
                if (interactiveTile[currentMap][i] != null) {
                    interactiveTile[currentMap][i].draw(graphics2D);
                }
            }

            entityList.add(player);
            for(int i = 0; i < npc[1].length; i++) {
                if(npc[currentMap][i] != null) {
                    entityList.add(npc[currentMap][i]);
                }
            }

            for(int i = 0; i < obj[1].length; i++) {
                if(obj[currentMap][i] != null) {
                    entityList.add(obj[currentMap][i]);
                }
            }

            for(int i = 0; i < monster[1].length; i++) {
                if (monster[currentMap][i] != null) {
                    entityList.add(monster[currentMap][i]);
                }
            }

            for(int i = 0; i < projectile[1].length; i++) {
                if(projectile[currentMap][i] != null) {
                    entityList.add(projectile[currentMap][i]);
                }
            }

            for(int i = 0; i < particleList.size(); i++) {
                if(particleList.get(i) != null) {
                    entityList.add(particleList.get(i));
                }
            }

                if(keyH.debug) {
                    graphics2D.setFont(new Font("Arial", Font.PLAIN,20));
                    graphics2D.setColor(Color.MAGENTA);
                    int x = 10;
                    int y = 600;
                    int lineHeight = 20;

                    graphics2D.drawString("WorldX: " + player.worldX, x, y); y += lineHeight;
                    graphics2D.drawString("WorldY: " + player.worldY, x, y); y += lineHeight;
                    graphics2D.drawString("Col: " + (player.worldX + player.solidArea.x)/tilesize, x, y); y += lineHeight;
                    graphics2D.drawString("Row: " + (player.worldY + player.solidArea.y)/tilesize, x, y); y += lineHeight;
                }
            }

            // sort array list
            Collections.sort(entityList, new Comparator<Entity>() {
                @Override
                public int compare(Entity o1, Entity o2) {
                    int result = Integer.compare(o1.worldY, o2.worldY);
                    return result;
                }
            });

            for(int i = 0; i < entityList.size(); i++) {
                entityList.get(i).draw(graphics2D);
            }

            entityList.clear();

            eManager.draw(graphics2D);

            ui.draw(graphics2D);
        }

        public void resetGame(boolean deleteProgress) {
            player.retry();
            if(deleteProgress) {
                player.setDefaultValues();
                eManager.lighting.resetDay();
            }


        }

        public void playMusic(int i) {
            music.stop();
            music.setFile(i);
            music.play();
            music.loop();
        }

        public void stopMusic() {
            music.stop();
        }

        public void playSound(int i) {
            soundEffects.setFile(i);
            soundEffects.play();
        }
    }
