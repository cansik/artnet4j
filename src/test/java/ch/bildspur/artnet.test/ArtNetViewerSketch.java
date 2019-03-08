package ch.bildspur.artnet.test;

import ch.bildspur.artnet.ArtNetBuffer;
import ch.bildspur.artnet.ArtNetClient;
import processing.core.PApplet;
import processing.core.PVector;


public class ArtNetViewerSketch extends PApplet {
    public static void main(String... args) {
        ArtNetViewerSketch sketch = new ArtNetViewerSketch();
        sketch.run();
    }

    public void run()
    {
        runSketch();
    }

    ArtNetClient artnet = new ArtNetClient(new ArtNetBuffer(), 8000, 8000);

    int subnet = 0;
    int universe = 0;

    int columns = 32;
    int rows = 512 / columns;

    int boxWidth = 30;
    int boxHeight = 30;

    @Override
    public void settings()
    {
        size(1024, 600, FX2D);
    }

    @Override
    public void setup()
    {
        surface.setTitle("ArtNet Viewer");
        artnet.start();
    }

    @Override
    public void draw() {
       byte[] data = artnet.readDmxData(subnet, universe);
       background(color(data[0] & 0xFF, data[1] & 0xFF, data[2] & 0xFF));

        background(55);

        textSize(14);
        textAlign(CENTER, CENTER);

        translate(10, 10);

       // render data
       for(int r = 0; r < rows; r++)
       {
           for(int c = 0; c < columns; c++)
           {
               PVector pos = new PVector(c * boxWidth, r * boxHeight);

               // draw box
               noFill();
               stroke(255);
               rect(pos.x, pos.y, boxWidth, boxHeight);

               // draw text
               fill(255);
               int value = data[r * columns + c] & 0xFF;
               text(value, pos.x + (boxWidth / 2f), pos.y + (boxHeight / 2f));
           }
       }
    }

    @Override
    public void stop() {
        artnet.stop();
    }

    @Override
    public void keyPressed()
    {
        artnet.stop();
    }

    int toRGB(byte red, byte green, byte blue)
    {
        return color(red, green, blue);
    }
}
