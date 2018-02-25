package ch.bildspur.artnet.test;

import ch.bildspur.artnet.processing.ArtNetSender;
import processing.core.PApplet;


public class SendArtNetSketch extends PApplet {
    public static void main(String... args) {
        SendArtNetSketch sketch = new SendArtNetSketch();
        sketch.run();
    }

    public void run()
    {
        runSketch();
    }

    ArtNetSender artnet = new ArtNetSender();
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

        artnet.open();
        artnet.setReceiver("127.0.0.1");
    }

    @Override
    public void draw() {
        int c = color(frameCount % 360, 50, 50);

        background(c);
        setRGB(0, c);

        // send dmx
        artnet.send(0, dmxData);
    }

    @Override
    public void stop() {
        artnet.close();
    }

    void setRGB(int address, int color)
    {
        dmxData[address] = (byte) red(color);
        dmxData[address + 1] = (byte) green(color);
        dmxData[address + 2] = (byte) blue(color);
    }
}
