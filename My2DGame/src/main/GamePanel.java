package main;
import ai.PathFinder;
import data.SaveLoad;
import entity.Entity;
import entity.Player;
import environment.EnvironmentManager;
import tile.Map;
import tile.TileManager;
import tile_interactive.InteractiveTile;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class GamePanel extends JPanel implements Runnable{
    //SCREEN SETTINGS
    final int originalTileSize = 16; // 16*16  tile. default
    final int scale = 3; // 16*3 scale

    public final int tileSize = originalTileSize * scale; // 48*48 tile // public cuz we use it in Player Class
    public final int maxScreenCol = 20; // 4:3 window
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol;  //48*20 = 960 pixels
    public final int screenHeight = tileSize * maxScreenRow;  //48*12 = 576 pixels  // GAME SCREEN SIZE

    //WORLD SETTINGS
    public int maxWorldCol;
    public int maxWorldRow;
    public final int maxMap = 10;
    public int currentMap = 0;

    //FOR FULLSCREEN
    int screenWidth2 = screenWidth;
    int screenHeight2 = screenHeight;
    BufferedImage tempScreen;
    Graphics2D g2;
    public boolean fullScreenOn = false;


    //FPS
    int FPS = 60;

    //SYSTEM
    public TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(this);
    public EventHandler eHandler = new EventHandler(this);
    Sound music = new Sound(); // Created 2 different objects for Sound Effect and Music. If you use 1 object SE or Music stops sometimes.
    Sound se = new Sound();
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter  aSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    Config config = new Config(this);
    public PathFinder pFinder = new PathFinder(this);
    EnvironmentManager eManager = new EnvironmentManager(this);
    Map map = new Map(this);
    SaveLoad saveLoad = new SaveLoad(this);
    public EntityGenerator eGenerator = new EntityGenerator(this);
    public CutsceneManager csManager = new CutsceneManager(this);
    Thread gameThread;

    //ENTITY AND OBJECT
    public Player player = new Player(this,keyH);
    public Entity obj[][] = new Entity[maxMap][20]; // display 10 objects same time
    public Entity npc[][] = new Entity[maxMap][10];
    public Entity monster[][] = new Entity[maxMap][20];
    public InteractiveTile iTile[][] = new InteractiveTile[maxMap][50];
    public Entity projectile[][] = new Entity[maxMap][20]; // cut projectile
    //public ArrayList<Entity> projectileList = new ArrayList<>();
    public ArrayList<Entity> particleList = new ArrayList<>();
    ArrayList<Entity> entityList = new ArrayList<>();


    //GAME STATE
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;
    public final int characterState = 4;
    public final int optionsState = 5;
    public final int gameOverState = 6;
    public final int transitionState = 7;
    public final int tradeState = 8;
    public final int sleepState = 9;
    public final int mapState = 10;
    public final int cutsceneState = 11;

    //OTHERS
    public boolean bossBattleOn = false;

    //AREA
    public int currentArea;
    public int nextArea;
    public final int outside = 50;
    public final int indoor = 51;
    public final int dungeon = 52;


    public GamePanel() // constructor
    {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight)); // JPanel size
        this.setBackground(Color.black);
        this.setDoubleBuffered(true); // improve game's rendering performance
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }
    public void setupGame()
    {
        aSetter.setObject();
        aSetter.setNPC();
        aSetter.setMonster();
        aSetter.setInteractiveTile();
        eManager.setup();

        /*playMusic(0);   // 0 = BlueBoyAdventure.wav
        stopMusic();*/
        gameState = titleState;
        //FOR FULLSCREEN
        tempScreen = new BufferedImage(screenWidth,screenHeight,BufferedImage.TYPE_INT_ARGB); //blank screen
        g2 = (Graphics2D) tempScreen.getGraphics(); // g2 attached to this tempScreen. g2 will draw on this tempScreen buffered image.
        if(fullScreenOn == true)
        {
            setFullScreen();
        }
    }
    public void resetGame(boolean restart)
    {
        stopMusic();
        currentArea = outside;
        removeTempEntity();
        bossBattleOn = false;
        player.setDefaultPositions();
        player.restoreStatus();
        aSetter.setMonster();
        aSetter.setNPC();
        player.resetCounter();

        if(restart == true)
        {
            player.setDefaultValues();
            aSetter.setObject();
            aSetter.setInteractiveTile();
            eManager.lighting.resetDay();
        }

    }
    public void setFullScreen()
    {
        //GET LOCAL SCREEN DEVICE
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        gd.setFullScreenWindow(Main.window);

        //GET FULL SCREEN WIDTH AND HEIGHT
        screenWidth2 = Main.window.getWidth();
        screenHeight2 = Main.window.getHeight();
    }

    public void startGameThread()
    {
        gameThread = new Thread(this);
        gameThread.start(); // run'ı cagirir
    }

    @Override
    public void run()
    {
        double drawInterval = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        //long timer = 0;
        //int drawCount = 0;


        while(gameThread != null)
        {
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            //timer += currentTime - lastTime;
            lastTime = currentTime;
            if(delta >= 1)
            {
                update();
                /*repaint(); COMMENTED FOR FULL SCREEN*/
                drawToTempScreen(); //FOR FULL SCREEN - Draw everything to the buffered image
                drawToScreen();     //FOR FULL SCREEN - Draw the buffered image to the screen
                delta--;
                //drawCount++;
            }
            //SHOW FPS
            /*if(timer >= 1000000000)
            {
                System.out.println("FPS:" + drawCount);
                drawCount = 0;
                timer = 0;
            }*/
        }
    }

    public void update()
    {
        if(gameState == playState)
        {
            //PLAYER
            player.update();

            //NPC
            for(int i = 0; i < npc[1].length; i++) //[1] means second dimension's length!!!
            {
                if(npc[currentMap][i] != null)
                {
                    npc[currentMap][i].update();
                }
            }

            //MONSTER
            for(int i = 0; i < monster[1].length; i++)
            {
                if(monster[currentMap][i] != null)
                {
                    if(monster[currentMap][i].alive == true && monster[currentMap][i].dying == false)
                    {
                        monster[currentMap][i].update();
                    }
                    if(monster[currentMap][i].alive == false)
                    {
                        monster[currentMap][i].checkDrop(); //when monster dies, i check its drop
                        monster[currentMap][i] = null;
                    }
                }
            }

            //PROJECTILE
            for(int i = 0; i < projectile[1].length; i++)
            {
                if(projectile[currentMap][i] != null)
                {
                    if(projectile[currentMap][i].alive == true)
                    {
                        projectile[currentMap][i].update();
                    }
                    if(projectile[currentMap][i].alive == false)
                    {
                        projectile[currentMap][i] = null;
                    }
                }
            }

            //PARTICLE
            for(int i = 0; i < particleList.size(); i++)
            {
                if(particleList.get(i)!= null)
                {
                    if(particleList.get(i).alive == true)
                    {
                        particleList.get(i).update();
                    }
                    if(particleList.get(i).alive == false)
                    {
                        particleList.remove(i);
                    }
                }
            }

            //INTERACTIVE TILE
            for(int i = 0; i < iTile[1].length; i++)
            {
                if(iTile[currentMap][i] != null)
                {
                    iTile[currentMap][i].update();
                }
            }

            eManager.update();
        }

        if(gameState == pauseState)
        {
            //nothing, just pause screen
        }
    }

    //FOR FULL SCREEN (FIRST DRAW TO TEMP SCREEN INSTEAD OF JPANEL)
    public void drawToTempScreen()
    {
        //DEBUG
        long drawStart = 0;
        if(keyH.showDebugText == true)
        {
            drawStart = System.nanoTime();
        }

        //TITLE SCREEN
        if(gameState == titleState)
        {
            ui.draw(g2);
        }
        //MAP SCREEN
        else if(gameState == mapState)
        {
            map.drawFullMapScreen(g2);
        }
        //OTHERS
        else
        {
            //TILE
            tileM.draw(g2);

            //INTERACTIVE TILE
            for(int i = 0; i < iTile[1].length; i++)
            {
                if(iTile[currentMap][i] != null)
                {
                    iTile[currentMap][i].draw(g2);
                }
            }

            //ADD ENTITIES TO THE LIST
            //PLAYER
            entityList.add(player);

            //NPCs
            for(int i = 0; i < npc[1].length; i++)
            {
                if(npc[currentMap][i] != null)
                {
                    entityList.add(npc[currentMap][i]);
                }
            }

            //OBJECTS
            for(int i = 0; i < obj[1].length; i++)
            {
                if(obj[currentMap][i] != null)
                {
                    entityList.add(obj[currentMap][i]);
                }
            }

            //MONSTERS
            for(int i = 0; i < monster[1].length; i++)
            {
                if(monster[currentMap][i] != null)
                {
                    entityList.add(monster[currentMap][i]);
                }
            }

            //PROJECTILES
            for(int i = 0; i < projectile[1].length; i++)
            {
                if(projectile[currentMap][i] != null)
                {
                    entityList.add(projectile[currentMap][i]);
                }
            }

            //PARTICLES
            for(int i = 0; i < particleList.size(); i++)
            {
                if(particleList.get(i) != null)
                {
                    entityList.add(particleList.get(i));
                }
            }

            //SORT
            Collections.sort(entityList, new Comparator<Entity>() {
                @Override
                public int compare(Entity e1, Entity e2) {
                    int result = Integer.compare(e1.worldY, e2.worldY);   // result returns : (x=y : 0, x>y : >0, x<y : <0)
                    return result;
                }
            });

            //DRAW ENTITIES
            for(int i = 0; i < entityList.size(); i++)
            {
                entityList.get(i).draw(g2);
            }

            //EMPTY ENTITY LIST
            entityList.clear();

            //ENVIRONMENT
            eManager.draw(g2);

            //MINI MAP
            map.drawMiniMap(g2);

            //CUTSCENE
            csManager.draw(g2);

            //UI
            ui.draw(g2);

            //DEBUG

            if(keyH.showDebugText == true)
            {
                long drawEnd = System.nanoTime();
                long passed = drawEnd - drawStart;

                g2.setFont(new Font("Arial", Font.PLAIN,20));
                g2.setColor(Color.white);
                int x = 10;
                int y = 400;
                int lineHeight = 20;

                g2.drawString("WorldX " + player.worldX,x,y);
                y+= lineHeight;
                g2.drawString("WorldY " + player.worldY,x,y);
                y+= lineHeight;
                g2.drawString("Col " + (player.worldX + player.solidArea.x) / tileSize,x,y);
                y+= lineHeight;
                g2.drawString("Row " + (player.worldY + player.solidArea.y) / tileSize,x,y);
                y+= lineHeight;
                g2.drawString("Map " + currentMap,x,y);
                y+= lineHeight;
                g2.drawString("Draw time: " + passed,x,y);
                y+= lineHeight;
                g2.drawString("God Mode: " + keyH.godModeOn, x, y);

            }
        }
    }
    public void drawToScreen()
    {
        Graphics g = getGraphics();
        g.drawImage(tempScreen, 0, 0,screenWidth2,screenHeight2,null);
        g.dispose();
    }
    //COMMENTED FOR FULLSCREEN
    /*public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g; // Graphics2D extends Graphics class

        //DEBUG
        long drawStart = 0;
        if(keyH.showDebugText == true)
        {
            drawStart = System.nanoTime();
        }

        //TITLE SCREEN
        if(gameState == titleState)
        {
            ui.draw(g2);
        }
        //OTHERS
        else
        {
            //TILE
            tileM.draw(g2);

            //INTERACTIVE TILE
            for(int i = 0; i < iTile.length; i++)
            {
                if(iTile[i] != null)
                {
                    iTile[i].draw(g2);
                }
            }

            //ADD ENTITIES TO THE LIST
            //PLAYER
            entityList.add(player);

            //NPCs
            for(int i = 0; i < npc.length; i++)
            {
                if(npc[i] != null)
                {
                    entityList.add(npc[i]);
                }
            }

            //OBJECTS
            for(int i = 0; i < obj.length; i++)
            {
                if(obj[i] != null)
                {
                    entityList.add(obj[i]);
                }
            }

            //MONSTERS
            for(int i = 0; i < monster.length; i++)
            {
                if(monster[i] != null)
                {
                    entityList.add(monster[i]);
                }
            }

            //PROJECTILES
            for(int i = 0; i < projectileList.size(); i++)
            {
                if(projectileList.get(i) != null)
                {
                    entityList.add(projectileList.get(i));
                }
            }

            //PARTICLES
            for(int i = 0; i < particleList.size(); i++)
            {
                if(particleList.get(i) != null)
                {
                    entityList.add(particleList.get(i));
                }
            }

            //SORT
            Collections.sort(entityList, new Comparator<Entity>() {
                @Override
                public int compare(Entity e1, Entity e2) {
                    int result = Integer.compare(e1.worldY, e2.worldY);   // result returns : (x=y : 0, x>y : >0, x<y : <0)
                    return result;
                }
            });

            //DRAW ENTITIES
            for(int i = 0; i < entityList.size(); i++)
            {
                entityList.get(i).draw(g2);
            }

            //EMPTY ENTITY LIST
            entityList.clear();

            //UI
            ui.draw(g2);

            //DEBUG

            if(keyH.showDebugText == true)
            {
                long drawEnd = System.nanoTime();
                long passed = drawEnd - drawStart;

                g2.setFont(new Font("Arial", Font.PLAIN,20));
                g2.setColor(Color.white);
                int x = 10;
                int y = 400;
                int lineHeight = 20;

                g2.drawString("WorldX " + player.worldX,x,y);
                y+= lineHeight;
                g2.drawString("WorldY " + player.worldY,x,y);
                y+= lineHeight;
                g2.drawString("Col " + (player.worldX + player.solidArea.x) / tileSize,x,y);
                y+= lineHeight;
                g2.drawString("Row " + (player.worldY + player.solidArea.y) / tileSize,x,y);
                y+= lineHeight;

                g2.drawString("Draw time : " + passed,x,y);
            }
            g2.dispose();
        }
    }*/

    public void playMusic(int i)
    {
        music.setFile(i);
        music.play();
        music.loop();
    }
    public void stopMusic()
    {
        music.stop();
    }
    public void playSE(int i) // Sound effect, dont need loop
    {
        se.setFile(i);
        se.play();
    }
    public void changeArea()
    {
        if(nextArea != currentArea)
        {
            stopMusic();

            if(nextArea == outside)
            {
                playMusic(0);
            }
            if(nextArea == indoor)
            {
                playMusic(18);
            }
            if(nextArea == dungeon)
            {
                playMusic(19);
            }
            aSetter.setNPC(); //reset for at the dungeon puzzle's stuck rocks.
        }

        currentArea = nextArea;
        aSetter.setMonster();
    }
    public void removeTempEntity()
    {
        for(int mapNum = 0; mapNum < maxMap; mapNum++)
        {
            for(int i = 0; i < obj[1].length; i++)
            {
                if(obj[mapNum][i] != null && obj[mapNum][i].temp == true)
                {
                    obj[mapNum][i] = null;
                }
            }
        }
    }
}
