
ArtNetClient artnet;

int i = 0;

byte[] buffer = new byte[512];

void setup()
{
  size(500, 500);  
  artnet = new ArtNetClient();
  artnet.open("127.0.0.1");
}

void draw()
{
  background(i, 0, 0);

  textSize(100);
  textAlign(CENTER, BOTTOM);
  text(i, width / 2, height / 2);

  buffer[0] = (byte)i;
  artnet.send(2, buffer);

  i = (i + 1) % 256;
}

void stop()
{
  artnet.close();
}