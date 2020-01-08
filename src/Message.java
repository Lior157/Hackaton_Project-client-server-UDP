import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * construct a message by calling the default constructor,
 * then feed the setters with info (pay attention to the parameters types).
 * then call for tobyteArray to get the message in byte array.
 * send the byte array to server/client.
 *
 * in order to understand a given msg (which is byte array),
 * call the getters from MessageInterpreter helper class.
 */
public class Message {
    private byte [] teamName;
    private byte type;
    private byte [] hash;
    private byte originalLength;
    private byte [] originalStrStart;
    private byte [] originalStrEnd;

    public Message() {
        teamName = new byte[32];
        hash = new byte[40];
        originalStrStart = new byte[256]; //can be 1-256
        originalStrEnd = new byte[256]; //can be 1-256
    }

    public void setTeamName(String name){
        //insert spaces to get total length 32
        StringBuilder tName = new StringBuilder(name);
        for(int i = name.length(); i < teamName.length; i++){
            tName.append(" ");
        }

        byte [] nameBytes = tName.toString().getBytes(StandardCharsets.UTF_8);
        for(int i = 0; i< nameBytes.length; i++){
            teamName[i] = nameBytes[i];
        }

    }


    public void setType(byte type) {
        this.type = type;
    }

    public void setHash(String hash) {
        this.hash = hash.getBytes(StandardCharsets.UTF_8);;
    }

    public void setOriginalLength(byte originalLength) {
        this.originalLength = originalLength;
    }

    public void setOriginalStrStart(String originalStrStart) {
        //DYNAIMC
        this.originalStrStart = originalStrStart.getBytes(StandardCharsets.UTF_8);

        //STATIC
      /*  byte [] bytes = originalStrStart.getBytes(StandardCharsets.UTF_8);
       // this.originalStrStart = originalStrStart.getBytes(StandardCharsets.UTF_8);
        for(int i = 0; i < bytes.length; i ++)
            this.originalStrStart[i] = bytes[i];*/
    }

    public void setOriginalStrEnd(String originalStrEnd) {
        //DYNAMIC
        this.originalStrEnd = originalStrEnd.getBytes(StandardCharsets.UTF_8);
        //STATIC
       /* byte [] bytes = originalStrEnd.getBytes(StandardCharsets.UTF_8);
       // this.originalStrEnd = originalStrEnd.getBytes(StandardCharsets.UTF_8);;
        for(int i = 0; i < bytes.length; i ++)
            this.originalStrEnd[i] = bytes[i];*/
    }



    public byte[] tobyteArray(){
        byte[] msg = new byte[32+1+40+1+originalStrStart.length+originalStrEnd.length];

        ByteBuffer buff = ByteBuffer.wrap(msg);
        buff.put(teamName);
        buff.put(type);
        buff.put(hash);
        buff.put(originalLength);
        buff.put(originalStrStart);
        buff.put(originalStrEnd);

        byte[] combined = buff.array();
        return combined;

    }


}
