package ch.bildspur.artnet.test;

import ch.bildspur.artnet.ArtNetClient;
import ch.bildspur.artnet.ArtNetNode;
import processing.core.PApplet;


public class ReceiveArtNetSketch extends PApplet {
    public static void main(String... args) {
        ReceiveArtNetSketch sketch = new ReceiveArtNetSketch();
        sketch.run();
    }

    public void run()
    {
        runSketch();
    }

    ArtNetClient artnet = new ArtNetClient();

    @Override
    public void settings()
    {
        size(500, 500, FX2D);
    }

    @Override
    public void setup()
    {
        artnet.start();
    }

    @Override
    public void draw() {
       byte[] data = artnet.readDmxData(0, 0);
        background(color(data[0], data[1], data[2]));
    }

    @Override
    public void stop() {
        artnet.stop();
    }


    int toRGB(byte red, byte green, byte blue)
    {
        return color(red, green, blue);
    }
}
