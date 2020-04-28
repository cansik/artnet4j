package ch.bildspur.artnet.packets;

/**
 * For further information about the ArtNet timecode packet, read <a href="https://art-net.org.uk/structure/time-keeping-triggering/arttimecode/">this</a>.
 * 
 * @author <a href="https://mrexplode.github.io">MrExplode</a>
 *
 */
public class ArtTimePacket extends ArtNetPacket {
    
    private int frames;
    private int seconds;
    private int minutes;
    private int hours;
    private int type;
    
    public ArtTimePacket() {
        super(PacketType.ART_TIMECODE);
        setData(new byte[19]);
        setHeader();
        setProtocol();
    }


    @Override
    public boolean parse(byte[] raw) {
        setData(raw);
        frames = data.getInt8(14);
        seconds = data.getInt8(15);
        minutes = data.getInt8(16);
        hours = data.getInt8(17);
        type = data.getInt8(18);
        return true;
    }
    
    /**
     * Set the time in one method.
     * @param hour hours
     * @param min minutes
     * @param sec seconds
     * @param frame frames
     */
    public void setTime(int hour, int min, int sec, int frame) {
    	this.hours = hour;
    	this.minutes = min;
    	this.seconds = sec;
    	this.frames = frame;
    	updateData();
    }

    /**
     * @return the number of frames
     */
    public int getFrames() {
        return frames;
    }

    
    public void setFrames(int frames) {
        this.frames = frames;
        updateData();
    }

    /**
     * @return the number of seconds
     */
    public int getSeconds() {
        return seconds;
    }

    
    public void setSeconds(int seconds) {
        this.seconds = seconds;
        updateData();
    }

    /**
     * @return the number of minutes
     */
    public int getMinutes() {
        return minutes;
    }

    
    public void setMinutes(int minutes) {
        this.minutes = minutes;
        updateData();
    }

    /**
     * @return the number of hours
     */
    public int getHours() {
        return hours;
    }

    
    public void setHours(int hours) {
        this.hours = hours;
        updateData();
    }

    /**
     * <table style="undefined;table-layout: fixed; width: 486px"><colgroup><col style="width: 120px"><col style="width: 107px"><col style="width: 101px"><col style="width: 158px"></colgroup><tr><th>Type</th><th> Type value</th><th>Frame rate</th><th>Frames in second</th></tr><tr><td>Film</td><td>0</td><td>24</td><td>0-23</td></tr><tr><td>EBU</td><td>1</td><td>25</td><td>0-24</td></tr><tr><td>DF</td><td>2</td><td>29.97</td><td>0-29</td></tr><tr><td>SMTPE</td><td>3</td><td>30</td><td>0-30</td></tr></table>
     * @return the frame type
     */
    public int getFrameType() {
        return type;
    }

    /**
     * <table style="undefined;table-layout: fixed; width: 486px"><colgroup><col style="width: 120px"><col style="width: 107px"><col style="width: 101px"><col style="width: 158px"></colgroup><tr><th>Type</th><th> Type value</th><th>Frame rate</th><th>Frames in second</th></tr><tr><td>Film</td><td>0</td><td>24</td><td>0-23</td></tr><tr><td>EBU</td><td>1</td><td>25</td><td>0-24</td></tr><tr><td>DF</td><td>2</td><td>29.97</td><td>0-29</td></tr><tr><td>SMTPE</td><td>3</td><td>30</td><td>0-30</td></tr></table>
     * @param type the frame type
     */
    public void setFrameType(int type) {
        this.type = type;
        updateData();
    }
    
    private void updateData() {
        data.setInt8(frames, 14);
        data.setInt8(seconds, 15);
        data.setInt8(minutes, 16);
        data.setInt8(hours, 17);
        data.setInt8(type, 18);
    }

}
