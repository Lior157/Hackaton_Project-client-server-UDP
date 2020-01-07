import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HelperFunctions {
    public static String hash(String toHash) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] messageDigest = md.digest(toHash.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            StringBuilder hashText = new StringBuilder(no.toString(16));
            while (hashText.length() < 32){
                hashText.insert(0, "0");
            }
            return hashText.toString();
        }
        catch (NoSuchAlgorithmException e){
            throw new RuntimeException(e);
        }
    }

    public static String tryDeHash(String startRange, String endRange, String originalHash){
        BigInteger start = convertStringToInt(startRange);
        BigInteger end = convertStringToInt(endRange);
        int length = startRange.length();
        for(BigInteger i = start; i.compareTo(end) <= 0; i = i.add(BigInteger.valueOf(1))){
            String currentString = converxtIntToString(i, length);
            String hash = hash(currentString);
            if(originalHash.equals(hash)){
                return currentString;
            }
        }
        return null;
    }

    private static BigInteger convertStringToInt(String toConvert) {
        char[] charArray = toConvert.toCharArray();
        BigInteger num = new BigInteger("0");
        for(char c : charArray){
            if(c < 'a' || c > 'z'){
                throw new RuntimeException();
            }
           num = num.multiply(BigInteger.valueOf(26));// *= 26;
           num = num.add(BigInteger.valueOf(c-'a'));// += c - 'a';
        }
        return num;
    }


    private  static String converxtIntToString(BigInteger toConvert, int length) {
        StringBuilder s = new StringBuilder(length);
        while (toConvert.compareTo(new BigInteger("0")) > 0 ){
            int c = toConvert.mod(BigInteger.valueOf(26)).intValue(); //% 26;
            s.insert(0, (char) (c + 'a'));
            toConvert = toConvert.divide(BigInteger.valueOf(26)); // /= 26;
            length --;
        }
        while (length > 0){
            s.insert(0, 'a');
            length--;
        }
        return s.toString();
    }

    public  static String [] divideToDomains (int stringLength, int numOfServers){
        String [] domains = new String[numOfServers * 2];

        StringBuilder first = new StringBuilder(); //aaa
        StringBuilder last = new StringBuilder(); //zzz

        for(int i = 0; i < stringLength; i++){
            first.append("a"); //aaa
            last.append("z"); //zzz
        }

       // int total = convertStringToInt(last.toString());
        BigInteger total = convertStringToInt(last.toString());
        //int perServer = (int) Math.floor (((double)total) /  ((double)numOfServers));
       // BigInteger perServer = new BigInteger(BigInteger.valueOf (Math.floor (((double)total) /  ((double)numOfServers))));
        BigInteger perServer = total.divide(BigInteger.valueOf(numOfServers));

        domains[0] = first.toString(); //aaa
        domains[domains.length -1 ] = last.toString(); //zzz
        BigInteger summer = new BigInteger("0");

        for(int i = 1; i <= domains.length -2; i += 2){
            summer =  summer.add(perServer);// += perServer;
            domains[i] = converxtIntToString(summer, stringLength); //end domain of server
            summer = summer.add(BigInteger.valueOf(1));//++;
            domains[i + 1] = converxtIntToString(summer, stringLength); //start domain of next server
        }

        return domains;
    }

    public static void main(String[] args) {
       String h = hash("viper");
        System.out.println(h);
       // System.out.println(tryDeHash("vaaaa", "vzzzz", h));

        String [] domains = divideToDomains(5, 2);
        for (String s : domains){
            System.out.println(s);
        }
    }
}
