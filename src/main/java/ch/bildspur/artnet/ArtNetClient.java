package ch.bildspur.artnet;

import ch.bildspur.artnet.events.ArtNetServerListener;
import ch.bildspur.artnet.packets.ArtDmxPacket;
import ch.bildspur.artnet.packets.ArtNetPacket;

import java.net.InetAddress;
import java.net.SocketException;

import static ch.bildspur.artnet.packets.PacketType.ART_OUTPUT;

public class ArtNetClient {
    private ArtNet artnet;

    private int sequenceId = 0;
    private boolean isRunning = false;

    public ArtNetClient() {
        artnet = new ArtNet();
    }

    public void start()
    {
        // use default network interface
        this.start(null);
    }

    public void start(InetAddress networkInterface) {
        if (isRunning)
            return;

        try {
            artnet.init();
            artnet.addServerListener(new ArtNetServerListener() {
                @Override
                public void artNetPacketReceived(final ArtNetPacket artNetPacket) {
                    packetReceived(artNetPacket);
                }

                @Override
                public void artNetServerStopped(final ArtNetServer artNetServer) {
                }

                @Override
                public void artNetServerStarted(final ArtNetServer artNetServer) {
                }

                @Override
                public void artNetPacketUnicasted(final ArtNetPacket artNetPacket) {
                }

                @Override
                public void artNetPacketBroadcasted(final ArtNetPacket artNetPacket) {
                }
            });

            artnet.setBroadCastAddress(ArtNetServer.DEFAULT_BROADCAST_IP);
            artnet.start(networkInterface);

            isRunning = true;
        } catch (SocketException | ArtNetException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (!isRunning)
            return;

        artnet.stop();

        isRunning = false;
    }

    public void broadcastDmx(int subnet, int universe, byte[] dmxData)
    {
        artnet.broadcastPacket(createDmxPacket(subnet, universe, dmxData));
    }

    public void unicastDmx(ArtNetNode node, int subnet, int universe, byte[] dmxData)
    {
        artnet.unicastPacket(createDmxPacket(subnet, universe, dmxData), node);
    }

    private ArtDmxPacket createDmxPacket(int subnet, int universe, byte[] dmxData)
    {
        ArtDmxPacket dmx = new ArtDmxPacket();

        dmx.setUniverse(subnet, universe);
        dmx.setSequenceID(++sequenceId);
        dmx.setDMX(dmxData, dmxData.length);

        sequenceId %= 256;

        return dmx;
    }

    private void packetReceived(final ArtNetPacket packet) {
        if (packet.getType() != ART_OUTPUT)
            return;

        ArtDmxPacket dmxPacket = (ArtDmxPacket) packet;
        int subnet = dmxPacket.getSubnetID();
        int universe = dmxPacket.getUniverseID();

        // todo: copy data to buffer
        System.out.println("data received");
    }

    public ArtNet getArtnet() {
        return artnet;
    }

    public boolean isRunning() {
        return isRunning;
    }
}
