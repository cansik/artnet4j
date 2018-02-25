package ch.bildspur.artnet;

import java.util.HashMap;
import java.util.Map;

public class ArtNetBuffer {
    private static final int DEFAULT_CHANNEL_SIZE = 512;

    private final int channelSize;
    private Map<Integer, byte[]> data;

    public ArtNetBuffer()
    {
        this(DEFAULT_CHANNEL_SIZE);
    }

    public ArtNetBuffer(int channelSize)
    {
        this.channelSize = channelSize;
        data = new HashMap<>();
    }

    public byte[] getDmxData(short subnet, short universe){
        int key = hashKeyFromPair(subnet, universe);

        if(!data.containsKey(key))
            data.put(key, new byte[channelSize]);

        return data.get(key);
    }

    public void setDmxData(short subnet, short universe, final byte[] dmxData)
    {
        data.put(hashKeyFromPair(subnet, universe), dmxData);
    }

    public void clear()
    {
        data.clear();
    }

    private static int hashKeyFromPair(short a, short b) {
        assert a >= 0;
        assert b >= 0;
        long sum = (long) a + (long) b;
        return (int) (sum * (sum + 1) / 2) + a;
    }
}
