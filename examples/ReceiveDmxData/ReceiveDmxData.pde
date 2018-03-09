import ch.bildspur.artnet.*;

ArtNetClient artnet;
byte[] dmxData = new byte[512];

void setup()
{
  size(500, 250);
  textAlign(CENTER, CENTER);
  textSize(20);

  // create artnet client
  artnet = new ArtNetClient();
  artnet.start();
}

void draw()
{
  // read rgb color from the first 3 bytes
  byte[] data = artnet.readDmxData(0, 0);
  int c = color(data[0] & 0xFF, data[1] & 0xFF, data[2] & 0xFF);

  // set background
  background(c);

  // show values
  text("R: " + (int)red(c) + " Green: " + (int)green(c) + " Blue: " + (int)blue(c), width / 2, height / 2);
}