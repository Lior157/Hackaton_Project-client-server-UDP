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
        int start = convertStringToInt(startRange);
        int end = convertStringToInt(endRange);
        int length = startRange.length();
        for(int i = start; i <= end; i++){
            String currentString = converxtIntToString(i, length);
            String hash = hash(currentString);
            if(originalHash.equals(hash)){
                return currentString;
            }
        }
        return null;
    }

    private static int convertStringToInt(String toConvert) {
        char[] charArray = toConvert.toCharArray();
        int num = 0;
        for(char c : charArray){
            if(c < 'a' || c > 'z'){
                throw new RuntimeException();
            }
            num *= 26;
            num += c - 'a';
        }
        return num;
    }


    private  static String converxtIntToString(int toConvert, int length) {
        StringBuilder s = new StringBuilder(length);
        while (toConvert > 0 ){
            int c = toConvert % 26;
            s.insert(0, (char) (c + 'a'));
            toConvert /= 26;
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

        int total = convertStringToInt(last.toString());
        int perServer = (int) Math.floor (((double)total) /  ((double)numOfServers));

        domains[0] = first.toString(); //aaa
        domains[domains.length -1 ] = last.toString(); //zzz
        int summer = 0;

        for(int i = 1; i <= domains.length -2; i += 2){
            summer += perServer;
            domains[i] = converxtIntToString(summer, stringLength); //end domain of server
            summer++;
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
