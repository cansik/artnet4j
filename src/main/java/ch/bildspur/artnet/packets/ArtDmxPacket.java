package ch.bildspur.artnet.packets;

import java.util.Arrays;

public class ArtDmxPacket extends ArtNetPacket {

    private int numChannels;
    private int sequenceID;
    private int subnetID;
    private int universeID;
    private byte[] dmxData;

    public ArtDmxPacket() {
        super(PacketType.ART_OUTPUT);
        setData(new byte[530]);
        setHeader();
        setProtocol();
        data.setInt8(0x02, 13);
    }

    public byte[] getDmxData() {
        return dmxData;
    }

    /**
     * @return the actual packet size used. If an odd number DMX channels is
     *         used, the packet size is made even automatically.
     */
    @Override
    public int getLength() {
        return 18 + (1 == numChannels % 2 ? numChannels + 1 : numChannels);
    }

    /**
     * @return the number of DMX channels
     */
    public int getNumChannels() {
        return numChannels;
    }

    /**
     * @return the sequenceID
     */
    public int getSequenceID() {
        return sequenceID;
    }

    /**
     * @return the subnetID
     */
    public int getSubnetID() {
        return subnetID;
    }

    /**
     * @return the universeID
     */
    public int getUniverseID() {
        return universeID;
    }

    @Override
    public boolean parse(byte[] raw) {
        setData(raw);
        sequenceID = data.getInt8(12);
        int subnetUniverse = data.getInt8(14);
        subnetID = subnetUniverse >> 4;
        universeID = subnetUniverse & 0x0f;
        numChannels = data.getInt16(16);
        dmxData = data.getByteChunk(dmxData, 18, numChannels);
        return true;
    }

    public void setDMX(byte[] dmxData, int numChannels) {
        logger.finer("setting DMX data for: " + numChannels + " channels");
        this.dmxData = Arrays.copyOf(dmxData, numChannels);
        this.numChannels = numChannels;
        data.setByteChunk(dmxData, 18, numChannels);
        data.setInt16((1 == numChannels % 2 ? numChannels + 1 : numChannels),
                16);
    }

    /**
     * @param numChannels
     *            the number of DMX channels to set
     */
    public void setNumChannels(int numChannels) {
        this.numChannels = numChannels > 512 ? 512 : numChannels;
    }

    public void setSequenceID(int id) {
        sequenceID = id % 0xff;
        data.setInt8(id, 12);
    }

    /**
     * @param subnetID
     *            the subnetID to set
     */
    public void setSubnetID(int subnetID) {
        this.subnetID = subnetID & 0x0f;
    }

    public void setUniverse(int subnetID, int universeID) {
        this.subnetID = subnetID & 0x0f;
        this.universeID = universeID & 0x0f;
        data.setInt16LE(subnetID << 4 | universeID, 14);
        logger.finer("universe ID set to: subnet: "
                + ByteUtils.hex(subnetID, 2) + "/"
                + ByteUtils.hex(universeID, 2));
    }

    /**
     * @param universeID
     *            the universeID to set
     */
    public void setUniverseID(int universeID) {
        this.universeID = universeID & 0x0f;
    }
}