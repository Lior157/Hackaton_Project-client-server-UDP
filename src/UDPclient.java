import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.LinkedList;

public class UDPclient {
    private static final String TeamName = "DonaldCyber";
    private static String hashInput ;
    private static byte lengthInput ;
    private static LinkedList<DatagramPacket> recievedPockets = new LinkedList<>();
    public static void main(String args[]) throws Exception
    {
        byte[] sendData = new byte[1024];
        byte[] receiveData = new byte[1024];

        DatagramSocket clientSocket = new DatagramSocket();
        hashInput = "a346f3083515cbc8ca18aae24f331dee2d23454b" ;
        lengthInput = (byte)5 ;
//        System.out.println("Welcome to "+TeamName+". Please enter the hash:");
//        BufferedReader hashFromUser =
//                new BufferedReader(new InputStreamReader(System.in));
//        hashInput = hashFromUser.readLine();
//
//        System.out.println("Please enter the input string length:");
//        BufferedReader lengthFromUser =
//                new BufferedReader(new InputStreamReader(System.in));
//        lengthInput = (byte) Integer.parseInt(lengthFromUser.readLine());

      //  InetAddress IPAddress = InetAddress.getByName("localhost");
        InetAddress IPAddress = InetAddress.getByName("255.255.255.255");
        System.out.println(IPAddress);

        byte [] discoveredMessage = DiscoveredMessage().tobyteArray() ;
        DatagramPacket sendPacket = new DatagramPacket(discoveredMessage, discoveredMessage.length, IPAddress, 3117);
        clientSocket.send(sendPacket);

        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis()-startTime < 1000 || recievedPockets.size()==0) {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            try {
                clientSocket.setSoTimeout(30);
                clientSocket.receive(receivePacket);


                if((byte) 2  == MessageInterpreter.getType(receivePacket.getData())) {
                    System.out.println("offer recieved");
                    recievedPockets.add(receivePacket);
                }
            }catch (Exception e){}
        }
        clientSocket.setSoTimeout(70000);
        String [] domains = HelperFunctions.divideToDomains(lengthInput, recievedPockets.size());
        for (String s : domains){
            System.out.println(s);
        }

        int domain = 0 ;
        for (DatagramPacket dp:
             recievedPockets) {
            InetAddress IPAddressDp = dp.getAddress();
            System.out.println(IPAddressDp);
            int port = dp.getPort() ;
            System.out.println("port:"+port);
            System.out.println("end:"+domains[domain+1]);
            Message message = sendRequest(domains[domain],domains[domain+1]);
            byte[] messageBytes = message.tobyteArray();
            DatagramPacket sendPacketRequst = new DatagramPacket(messageBytes, messageBytes.length, IPAddressDp, port);
            clientSocket.send(sendPacketRequst);
            domain = domain + 2;
        }

        boolean recieved = false;
        while (!recieved){
            try {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                clientSocket.receive(receivePacket);
                System.out.println("recived");
                if ((byte) 4 == MessageInterpreter.getType(receivePacket.getData())) {
                    System.out.println(MessageInterpreter.getOriginalStrStart(receivePacket.getData()));
                    recieved = true;
                }
            }catch (Exception e){} ;
        }





     //   sendData = sentence.getBytes();
//        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
//        clientSocket.send(sendPacket);
//        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
//        clientSocket.receive(receivePacket);
//        String modifiedSentence = new String(receivePacket.getData());
//        System.out.println("FROM SERVER:" + modifiedSentence);
//        clientSocket.close();
    }
    public static Message DiscoveredMessage(){
        Message newMessage = new Message();
        newMessage.setTeamName(TeamName);
        newMessage.setType((byte) 1);
        newMessage.setOriginalLength(lengthInput);
        return  newMessage;
    }
    public static  Message sendRequest(String start , String end){
        Message newMessage = new Message();
        newMessage.setTeamName(TeamName);
        newMessage.setType((byte) 3);
        newMessage.setOriginalLength(lengthInput);
        newMessage.setHash(hashInput);
        newMessage.setOriginalStrStart(start);
        newMessage.setOriginalStrEnd(end);
        return  newMessage;
    }
}
