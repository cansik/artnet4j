package ch.bildspur.artnet4j;

import processing.core.PApplet;


public class ProcessingSketch extends PApplet {
    public static void main(String... args) {
        ProcessingSketch sketch = new ProcessingSketch();
        sketch.run();
    }

    public void run()
    {
        runSketch();
    }

    @Override
    public void settings()
    {
        size(500, 500, FX2D);
    }

    @Override
    public void setup()
    {
    }

    @Override
    public void draw() {
        background(100, 200, 50);
    }

    @Override
    public void stop() {
    }
}
