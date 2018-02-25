package ch.bildspur.artnet.processing;

import ch.bildspur.artnet.ArtNet;
import ch.bildspur.artnet.ArtNetException;
import ch.bildspur.artnet.ArtNetNode;
import ch.bildspur.artnet.packets.ArtDmxPacket;

import java.net.InetAddress;
import java.net.SocketException;

public class ArtNetSender {

    private int sequenceId;
    private ArtNet artnet;
    private ArtNetNode receiver;

    public ArtNetSender()
    {
        artnet = new ArtNet();
    }

    public void open()
    {
        open(null, null);
    }

    public void open(InetAddress in, String address)
    {
        try
        {
            // sender
            artnet.start(in);
            setReceiver(address);
        }
        catch (SocketException e) {
            e.printStackTrace();
        }
        catch (ArtNetException e) {
            e.printStackTrace();
        }
    }

    public void setReceiver(String address)
    {
        if (null == address)
            receiver = null;

        try
        {
            receiver = new ArtNetNode();
            receiver.setIPAddress(InetAddress.getByName(address));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close()
    {
        artnet.stop();
    }

    public void send(int universe, byte[] data)
    {
        send(receiver, universe, data);
    }

    public void send(ArtNetNode node, int universe, byte[] data)
    {
        ArtDmxPacket dmx = new ArtDmxPacket();

        dmx.setUniverse(0, universe);
        dmx.setSequenceID(sequenceId % 256);
        dmx.setDMX(data, data.length);

        if (receiver != null)
            artnet.unicastPacket(dmx, node);
        else
            artnet.broadcastPacket(dmx);

        sequenceId++;
    }
}