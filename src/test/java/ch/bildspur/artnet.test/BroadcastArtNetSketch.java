package ch.bildspur.artnet.test;

import ch.bildspur.artnet.ArtNetClient;
import processing.core.PApplet;


public class BroadcastArtNetSketch extends PApplet {
    public static void main(String... args) {
        BroadcastArtNetSketch sketch = new BroadcastArtNetSketch();
        sketch.run();
    }

    public void run()
    {
        runSketch();
    }

    ArtNetClient artnet = new ArtNetClient(null);
    byte[] dmxData = new byte[512];

    @Override
    public void settings()
    {
        size(500, 500, FX2D);
    }

    @Override
    public void setup()
    {
        colorMode(HSB, 360, 100, 100);

        artnet.start();
    }

    @Override
    public void draw() {
        int c = color(frameCount % 360, 50, 50);

        background(c);
        setRGB(0, c);

        // send dmx
        artnet.broadcastDmx(0, 0, dmxData);
    }

    @Override
    public void stop() {
        artnet.stop();
    }

    void setRGB(int address, int color)
    {
        dmxData[address] = (byte) red(color);
        dmxData[address + 1] = (byte) green(color);
        dmxData[address + 2] = (byte) blue(color);
    }
}
