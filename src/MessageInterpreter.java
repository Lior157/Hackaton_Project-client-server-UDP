public class MessageInterpreter {
    /**
     * get info from msg represented by byte array
     */

    //message fields: name, type, hash, len, start, end.
    //positions in the byte array:
    //0-31: name
    //32: type
    //33-72: hash
    //73: len
    //start and end sizes can be between 1-256.
    //so how do we know their indexes?
    //we know that they size may vary between 1-256, but it is also equal.
    //(for example: start = "aaaa" end = "cccc" so the size is 4 for both)
    //so the amount of cells of each of them is:
    //(msg.length - (32+1+40+1)) / 2
    //I am a genius.

    public static String getTeamName(byte [] msg) {
        byte [] teamName = new byte[32];
        for(int i = 0; i < teamName.length; i++){
            teamName[i] = msg[i];
        }
        return new String (teamName);
    }

    public static byte getType(byte [] msg) {
        return msg[32];
    }

    public static String getHash(byte [] msg) {
        byte [] hash = new byte[40];
        for(int i = 0; i < hash.length; i++){
            hash[i] = msg[i+33];
        }
        return new String (hash);
    }

    public static byte getOriginalLength(byte [] msg) {
        return msg[73];
    }

    public static String getOriginalStrStart(byte [] msg) {
        byte [] originalStrStart = new byte[getOriginalLength(msg)];
        int startingIndex = 74;
        for(int i = 0; i < originalStrStart.length; i++){
            originalStrStart[i] = msg[i+startingIndex];
        }

        return new String (originalStrStart);

      /*  int startingIndex = 74;
        int endingIndex = 74 + ((msg.length - (32+1+40+1)) / 2) -1;

        byte [] originalStrStart = new byte[endingIndex - startingIndex +1];
        for(int i = 0; i < originalStrStart.length; i++){
            originalStrStart[i] = msg[i+startingIndex];
        }

        return new String (originalStrStart);*/
    }

    public static String getOriginalStrEnd(byte [] msg) {
        byte [] originalStrEnd = new byte[getOriginalLength(msg)];
      //  int startingIndex = 74 + getOriginalLength(msg);
        int startingIndex = 74 + 256;
      //  int endingIndex = 74 + 2*getOriginalLength(msg) - 1;
        int endingIndex = 74 +256 + getOriginalLength(msg) - 1;

        for(int i = 0; i < originalStrEnd.length; i++){
            originalStrEnd[i] = msg[i+startingIndex];
        }

        return new String (originalStrEnd);

      /*  int startingIndex = 74 + ((msg.length - (32+1+40+1)) / 2);
        int endingIndex = msg.length - 1;

        byte [] originalStrEnd = new byte[endingIndex - startingIndex +1];
        for(int i = 0; i < originalStrEnd.length; i++){
            originalStrEnd[i] = msg[i+startingIndex];
        }

        return new String (originalStrEnd);*/
    }
}
