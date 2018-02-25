package ch.bildspur.artnet;

import ch.bildspur.artnet.events.ArtNetServerEventAdapter;
import ch.bildspur.artnet.packets.ArtDmxPacket;
import ch.bildspur.artnet.packets.ArtNetPacket;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import static ch.bildspur.artnet.packets.PacketType.ART_OUTPUT;

public class ArtNetClient {
    private ArtNetServer server;

    private int sequenceId = 0;
    private boolean isRunning = false;

    public ArtNetClient() {
        server = new ArtNetServer();
    }

    public void start() {
        // use default network interface
        this.start(null);
    }

    public void start(InetAddress networkInterface) {
        if (isRunning)
            return;

        try {
            server.addListener(
                    new ArtNetServerEventAdapter() {
                        @Override
                        public void artNetPacketReceived(ArtNetPacket packet) {
                            onPacketReceived(packet);
                        }
                    });

            server.setBroadcastAddress(ArtNetServer.DEFAULT_BROADCAST_IP);
            server.start(networkInterface);

            isRunning = true;
        } catch (SocketException | ArtNetException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (!isRunning)
            return;

        server.stop();

        isRunning = false;
    }

    public void broadcastDmx(int subnet, int universe, byte[] dmxData) {
        server.broadcastPacket(createDmxPacket(subnet, universe, dmxData));
    }

    public void unicastDmx(String address, int subnet, int universe, byte[] dmxData) {
        try {
            this.unicastDmx(InetAddress.getByName(address), subnet, universe, dmxData);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void unicastDmx(ArtNetNode node, int subnet, int universe, byte[] dmxData) {
        server.unicastPacket(createDmxPacket(subnet, universe, dmxData), node.getIPAddress());
    }

    public void unicastDmx(InetAddress address, int subnet, int universe, byte[] dmxData) {
        server.unicastPacket(createDmxPacket(subnet, universe, dmxData), address);
    }

    private ArtDmxPacket createDmxPacket(int subnet, int universe, byte[] dmxData) {
        ArtDmxPacket dmx = new ArtDmxPacket();

        dmx.setUniverse(subnet, universe);
        dmx.setSequenceID(++sequenceId);
        dmx.setDMX(dmxData, dmxData.length);

        sequenceId %= 256;

        return dmx;
    }

    private void onPacketReceived(final ArtNetPacket packet) {
        if (packet.getType() != ART_OUTPUT)
            return;

        ArtDmxPacket dmxPacket = (ArtDmxPacket) packet;
        int subnet = dmxPacket.getSubnetID();
        int universe = dmxPacket.getUniverseID();

        // todo: copy data to buffer
        System.out.println("data received");
    }

    public ArtNetServer getArtNetServer() {
        return server;
    }

    public boolean isRunning() {
        return isRunning;
    }
}
