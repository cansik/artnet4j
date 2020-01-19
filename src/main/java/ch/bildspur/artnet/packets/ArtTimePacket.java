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
    
    public long encoded;
    
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
        encoded = encode(hours, minutes, seconds, frames, type);
        return true;
    }
    
    /**
     * Increment the timecode by 1.
     */
    public void increment() {
        encoded++;
        int[] val = decode(encoded, type);
        frames = val[3];
        seconds = val[2];
        minutes = val[1];
        hours = val[0];
        updateData();
    }
    
    /**
     * Decrement the timecode by 1;
     */
    public void decrement() {
        encoded--;
        int[] val = decode(encoded, type);
        frames = val[3];
        seconds = val[2];
        minutes = val[1];
        hours = val[0];
        updateData();
    }
    
    /**
     * Convert the separate values into one long value, for easy increment/decrement
     * 
     * @param hour number of hours
     * @param min number of minutes
     * @param sec number of seconds
     * @param frame number of frames
     * @param frameType the type of the timecode
     * @return the encoded time value
     */
    public long encode(int hour, int min, int sec, int frame, int frameType) {
        int framerate = 30;
        switch (frameType) {
            case 0:
            	//film
                framerate = 24;
                break;
            case 1:
            	//ebu
                framerate = 25;
                break;
            case 2:
            	//df
                throw new IllegalArgumentException("DF type not implemented! Do you wanna implement it yourself?");
            case 3:
            	//smtpe
                framerate = 30;
                break;
            default:
                framerate = 30;
                break;
        }
        
        int hour_fr = hour * 60 * 60 * framerate;
        int min_fr = min * 60 * framerate;
        int sec_fr = sec * framerate;
        
        return hour_fr + min_fr + sec_fr + frame;
    }
    
    /**
     * Decodes the encoded timecode value.<br>
     * Elements of the returning int array:<br>
     * 0: hour<br>
     * 1: minute<br>
     * 2: second<br>
     * 3: frame<br>
     * 
     * @param frames the encoded time data
     * @param frameType the type of the timecode
     * @return the decoded time values
     */
    public int[] decode(long frames, int frameType) {
        int framerate = 30;
        switch (frameType) {
            case 0:
                framerate = 24;
                break;
            case 1:
                framerate = 25;
                break;
            case 2:
                throw new IllegalArgumentException("DF type not implemented! Do you wanna implement it yourself?");
            case 3:
                framerate = 30;
                break;
            default:
                framerate = 30;
                break;
        }
        
        int[] dec = new int[4];
        
        int hour = ((int) frames / 60 / 60 / framerate);
        frames = frames - (hour * 60 * 60 * framerate);
        dec[0] = hour;
        
        int min = ((int) frames / 60 / framerate);
        frames = frames - (min * 60 * framerate);
        dec[1] = min;
        
        int sec = ((int) frames / framerate);
        frames = frames - (sec * framerate);
        dec[2] = sec;
        
        int frame = (int) frames;
        dec[3] = frame;
        
        return dec;
    }
    
    public void setTime(int hour, int min, int sec, int frame) {
    	this.hours = hour;
    	this.minutes = min;
    	this.seconds = sec;
    	this.frames = frame;
    	this.encoded = encode(hours, minutes, seconds, frames, type);
    	updateData();
    }

    /**
     * @return the number of frames
     */
    public int getFrames() {
        return frames;
    }

    
    public void setFrames(int frames) {
        this.frames = frames & 0x0f;
        this.encoded = encode(hours, minutes, seconds, this.frames, type);
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
        this.encoded = encode(hours, minutes, this.seconds, frames, type);
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
        this.encoded = encode(hours, this.minutes, seconds, frames, type);
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
        this.encoded = encode(this.hours, minutes, seconds, frames, type);
        updateData();
    }

    /**
     * <table><caption>Formats</caption><tr><th>Type</th><th> Type value</th><th>Frame rate</th><th>Frames in second</th></tr><tr><td>Film</td><td>0</td><td>24</td><td>0-23</td></tr><tr><td>EBU</td><td>1</td><td>25</td><td>0-24</td></tr><tr><td>DF</td><td>2</td><td>29.97</td><td>0-29</td></tr><tr><td>SMTPE</td><td>3</td><td>30</td><td>0-30</td></tr></table>
     * @return the frame type
     */
    public int getFrameType() {
        return type;
    }

    /**
     * <table><caption>Formats</caption><tr><th>Type</th><th> Type value</th><th>Frame rate</th><th>Frames in second</th></tr><tr><td>Film</td><td>0</td><td>24</td><td>0-23</td></tr><tr><td>EBU</td><td>1</td><td>25</td><td>0-24</td></tr><tr><td>DF</td><td>2</td><td>29.97</td><td>0-29</td></tr><tr><td>SMTPE</td><td>3</td><td>30</td><td>0-30</td></tr></table>
     * @param type the frame type
     */
    public void setFrameType(int type) {
        this.type = type;
        this.encoded = encode(hours, minutes, seconds, frames, this.type);
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
