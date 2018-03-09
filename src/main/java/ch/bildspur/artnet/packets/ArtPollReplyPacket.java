/*
 * This file is part of artnet4j.
 * 
 * Copyright 2009 Karsten Schmidt (PostSpectacular Ltd.)
 * 
 * artnet4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * artnet4j is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with artnet4j. If not, see <http://www.gnu.org/licenses/>.
 */

package ch.bildspur.artnet.packets;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;

import ch.bildspur.artnet.ArtNetServer;
import ch.bildspur.artnet.NodeReportCode;
import ch.bildspur.artnet.NodeStyle;
import ch.bildspur.artnet.PortDescriptor;

public class ArtPollReplyPacket extends ArtNetPacket {

    private InetAddress ip;

    private int versionInfo;
    private int subSwitch;
    private int oemCode;
    private int nodeStatus;
    private int ubeaVersion;
    private int estaManufacturerCode;

    private String shortName;
    private String longName;

    private int numPorts;
    private PortDescriptor[] ports = new PortDescriptor[numPorts];

    private NodeStyle nodeStyle = NodeStyle.ST_NODE;
    private NodeReportCode reportCode = NodeReportCode.RcDefault;

    private byte[] dmxIns = new byte[4];
    private byte[] dmxOuts = new byte[4];

    public ArtPollReplyPacket() {
        super(PacketType.ART_POLL_REPLY);
    }

    public ArtPollReplyPacket(byte[] data) {
        super(PacketType.ART_POLL_REPLY);
        setData(data);
    }

    /**
     * @return the dmxIns
     */
    public byte[] getDmxIns() {
        return dmxIns;
    }

    /**
     * @return the dmxOuts
     */
    public byte[] getDmxOuts() {
        return dmxOuts;
    }

    /**
     * @return the ip
     */
    public InetAddress getIPAddress() {
        InetAddress ipClone = null;
        try {
            ipClone = InetAddress.getByAddress(ip.getAddress());
        } catch (UnknownHostException e) {
        }
        return ipClone;
    }

    public String getLongName() {
        return longName;
    }

    public int getNodeStatus() {
        return nodeStatus;
    }

    public NodeStyle getNodeStyle() {
        return nodeStyle;
    }

    public int getOEMCode() {
        return oemCode;
    }

    public PortDescriptor[] getPorts() {
        return ports;
    }

    /**
     * @return the reportCode
     */
    public NodeReportCode getReportCode() {
        return reportCode;
    }

    public String getShortName() {
        return shortName;
    }

    public int getSubSwitch() {
        return subSwitch;
    }

    @Override
    public boolean parse(byte[] raw) {
        setData(raw);

        setIPAddress(data.getByteChunk(null, 10, 4));
        versionInfo = data.getInt16(16);
        subSwitch = data.getInt16(18);
        oemCode = data.getInt16(20);
        ubeaVersion = data.getInt8(22);
        nodeStatus = data.getInt8(23);
        estaManufacturerCode = data.getInt16LE(24);
        shortName = new String(data.getByteChunk(null, 26, 17));
        longName = new String(data.getByteChunk(null, 44, 63));
        reportCode = NodeReportCode.getForID(new String(data.getByteChunk(null, 108, 5)));
        numPorts = data.getInt16(172);
        ports = new PortDescriptor[numPorts];
        for (int i = 0; i < numPorts; i++) {
            ports[i] = new PortDescriptor(data.getInt8(174 + i));
        }
        dmxIns = data.getByteChunk(null, 186, 4);
        dmxOuts = data.getByteChunk(null, 190, 4);
        for (int i = 0; i < 4; i++) {
            dmxIns[i] &= 0x0f;
            dmxOuts[i] &= 0x0f;
        }
        int styleID = data.getInt8(200);
        for (NodeStyle s : NodeStyle.values()) {
            if (styleID == s.getStyleID()) {
                nodeStyle = s;
            }
        }
        return true;
    }

    public void translateData()
    {
        data = new ByteUtils(new byte[256]);

        // header
        data.setByteChunk(HEADER, 0, HEADER.length);

        // opcode
        data.setInt16LE(PacketType.ART_POLL_REPLY.getOpCode(), 8);

        // ip address
        data.setByteChunk(ip.getAddress(), 10, ip.getAddress().length);

        // port
        data.setInt16LE(ArtNetServer.DEFAULT_PORT, 14);

        // versinfo
        data.setInt16(versionInfo, 16);

        // subSwitch
        data.setInt16(subSwitch, 18);

        // oem
        data.setInt16(oemCode, 20);

        // ubeaVersion
        data.setInt8(ubeaVersion, 22);

        // status1
        data.setInt8(nodeStatus, 23);

        // estaMan code
        data.setInt16LE(estaManufacturerCode, 24);

        // short name
        data.setByteChunk(shortName.getBytes(), 26, Math.min(17, shortName.getBytes().length));

        // long name
        data.setByteChunk(longName.getBytes(), 44, Math.min(63, longName.getBytes().length));

        // node report
        // id
        byte[] reportCodeData = reportCode.getID().getBytes();
        data.setByteChunk(reportCodeData, 108, reportCodeData.length);

        // description
        byte[] reportDescriptionData = reportCode.getDescription().getBytes();
        data.setByteChunk(reportDescriptionData, 113, Math.min(59, reportDescriptionData.length));

        // num ports
        data.setInt16(numPorts, 172);

        // ports
        for (int i = 0; i < numPorts; i++) {
            data.setInt8(ports[i].getData(), 174 + i);
        }

        // dmx ins
        data.setByteChunk(dmxIns, 186, dmxIns.length);

        // dmx outs
        data.setByteChunk(dmxIns, 190, dmxIns.length);

        // style
        data.setInt8(nodeStyle.getStyleID(), 200);
    }

    /**
     * @param dmxIns
     *            the dmxIns to set
     */
    public void setDmxIns(byte[] dmxIns) {
        this.dmxIns = dmxIns;
    }

    /**
     * @param dmxOuts
     *            the dmxOuts to set
     */
    public void setDmxOuts(byte[] dmxOuts) {
        this.dmxOuts = dmxOuts;
    }

    private void setIPAddress(byte[] address) {
        try {
            ip = InetAddress.getByAddress(address);
            logger.fine("setting ip address: " + ip);
        } catch (UnknownHostException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
    }

    /**
     * @param reportCode
     *            the reportCode to set
     */
    public void setReportCode(NodeReportCode reportCode) {
        this.reportCode = reportCode;
    }

    public InetAddress getIp() {
        return ip;
    }

    public void setIp(InetAddress ip) {
        this.ip = ip;
    }

    public int getVersionInfo() {
        return versionInfo;
    }

    public void setVersionInfo(int versionInfo) {
        this.versionInfo = versionInfo;
    }

    public void setSubSwitch(int subSwitch) {
        this.subSwitch = subSwitch;
    }

    public int getOemCode() {
        return oemCode;
    }

    public void setOemCode(int oemCode) {
        this.oemCode = oemCode;
    }

    public void setNodeStatus(int nodeStatus) {
        this.nodeStatus = nodeStatus;
    }

    public int getUbeaVersion() {
        return ubeaVersion;
    }

    public void setUbeaVersion(int ubeaVersion) {
        this.ubeaVersion = ubeaVersion;
    }

    public int getEstaManufacturerCode() {
        return estaManufacturerCode;
    }

    public void setEstaManufacturerCode(int estaManufacturerCode) {
        this.estaManufacturerCode = estaManufacturerCode;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public int getNumPorts() {
        return numPorts;
    }

    public void setNumPorts(int numPorts) {
        this.numPorts = numPorts;
    }

    public void setPorts(PortDescriptor[] ports) {
        this.ports = ports;
    }

    public void setNodeStyle(NodeStyle nodeStyle) {
        this.nodeStyle = nodeStyle;
    }
}
