import java.net.NetworkInterface;
import java.util.Enumeration;

ArtNetClient artnet;

int i = 0;

byte[] buffer = new byte[512];

void setup()
{
  size(500, 500);  

  // get all avaialable network interfaces
  listNetworkInterfaces();

  try
  {
    NetworkInterface ni = NetworkInterface.getByName("lo0");
    artnet = new ArtNetClient();
    Enumeration<InetAddress> addresses = ni.getInetAddresses();
    addresses.nextElement();
    addresses.nextElement();
    artnet.open(addresses.nextElement(), null);
  }
  catch (SocketException e) {
    e.printStackTrace();
  }
}

void draw()
{
  background(i, 0, 0);

  textSize(100);
  textAlign(CENTER, BOTTOM);
  text(i, width / 2, height / 2);

  buffer[300] = (byte)i;
   // buffer[1] = (byte)i;
 // buffer[2] = (byte)i;

  artnet.send(0, buffer);

  i = (i + 1) % 256;
}

void stop()
{
  artnet.close();
}

void listNetworkInterfaces()
{
  try {
    Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
    while (networkInterfaces.hasMoreElements()) {
      NetworkInterface i = networkInterfaces.nextElement();
      print(i.getDisplayName() + ": ");

      Enumeration<InetAddress> addresses = i.getInetAddresses();
      while (addresses.hasMoreElements())
      {
        InetAddress address = addresses.nextElement(); 
        print(address.getHostAddress() + " / ");
      }

      println();
    }
  } 
  catch (SocketException e) {
    e.printStackTrace();
  }
} 