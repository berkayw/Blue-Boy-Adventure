package main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class Config {
    GamePanel gp;
    public Config(GamePanel gp)
    {
        this.gp = gp;
    }

    public void saveConfig()
    {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("config.txt"));

            // Full Screen
            if(gp.fullScreenOn == true)
            {
                bw.write("On");
            }
            if(gp.fullScreenOn == false)
            {
                bw.write("Off");
            }
            bw.newLine();

            //Music Volume
            bw.write(String.valueOf(gp.music.volumeScale));
            bw.newLine();

            //SE Volume
            bw.write(String.valueOf(gp.se.volumeScale));
            bw.newLine();

            bw.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadConfig()
    {
        try {
            BufferedReader br = new BufferedReader(new FileReader("config.txt"));

            String s = br.readLine();

            //Full Screen
            if(s.equals("On"))
            {
                gp.fullScreenOn = true;
            }
            if(s.equals("Off"))
            {
                gp.fullScreenOn = false;
            }

            //Music Volume
            s = br.readLine();
            gp.music.volumeScale = Integer.parseInt(s);

            //SE Volume
            s = br.readLine();
            gp.se.volumeScale = Integer.parseInt(s);

            br.close();


        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
