import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Enumeration;
import java.util.LinkedList;

public class UDPclient {
    private static final String TeamName = "DonaldCyber";
    private static String hashInput ;
    private static byte lengthInput ;
    private static LinkedList<DatagramPacket> recievedPockets = new LinkedList<>();
    private static final int  serverResponseTimeout = 15000;
    public static void main(String args[]) throws Exception
    {
        byte[] sendData = new byte[1024];
        byte[] receiveData = new byte[1024];

        DatagramSocket clientSocket = new DatagramSocket();
     //   hashInput = "a346f3083515cbc8ca18aae24f331dee2d23454b" ;//viper
     //   hashInput = "9017347a610d1436c1aaf52764e6578e8fc1a083" ; //cyber
        hashInput = "4ca26abfe0aef43348ebfecfff73a27201653e36"; //len 27
        lengthInput = (byte)27 ;
       // lengthInput = (byte)5 ;
 /*       System.out.println("Welcome to "+TeamName+". Please enter the hash:");
        BufferedReader hashFromUser =
                new BufferedReader(new InputStreamReader(System.in));
        hashInput = hashFromUser.readLine();

       System.out.println("Please enter the input string length:");
        BufferedReader lengthFromUser =
                new BufferedReader(new InputStreamReader(System.in));
        lengthInput = (byte) Integer.parseInt(lengthFromUser.readLine());*/


        byte [] discoveredMessage = DiscoveredMessage().tobyteArray() ;

     //   InetAddress IPAddressL = InetAddress.getByName("localhost");
       // DatagramPacket sendPacket2 = new DatagramPacket(discoveredMessage, discoveredMessage.length, IPAddressL, 3117);
       // clientSocket.setBroadcast(true);
        //clientSocket.send(sendPacket2);

        InetAddress IPAddress = InetAddress.getByName("255.255.255.255");
        //System.out.println(IPAddress);

     //   DatagramPacket sendPacket = new DatagramPacket(discoveredMessage, discoveredMessage.length, IPAddress, 3117);
        DatagramPacket sendPacket = new DatagramPacket(discoveredMessage, discoveredMessage.length, IPAddress, 3117);

     //   InetAddress ServerAddress = InetAddress.getByName("192.168.43.230");
      //  DatagramPacket sendPacket = new DatagramPacket(discoveredMessage, discoveredMessage.length, ServerAddress, 3117);

       clientSocket.setBroadcast(true);
        clientSocket.send(sendPacket);

     /*   Enumeration interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface networkInterface = (NetworkInterface)interfaces.nextElement();

            if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                continue; // Don't want to broadcast to the loopback interface
            }

            for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                InetAddress broadcast = interfaceAddress.getBroadcast();
                if (broadcast == null) {
                    continue;
                }

                // Send the broadcast package!
                try {
                   // DatagramPacket sendPacket1 = new DatagramPacket(sendData, sendData.length, broadcast, 8888);
                    clientSocket.send(sendPacket);
                } catch (Exception e) {
                }

            }
        }*/


        System.out.println("sending discover messages to find servers...");
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis()-startTime < 1000) {
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
        if(recievedPockets.size()==0){
            System.out.println("no offers from servers were received in 1 second. exiting program.");
            System.exit(0);
        }
        //clientSocket.setSoTimeout(10000);
        String [] domains = HelperFunctions.divideToDomains(lengthInput, recievedPockets.size());
        System.out.println("sending request messages to "+ recievedPockets.size() +" servers.");
        System.out.println("domains are: ");
        for (String s : domains){
            System.out.println(s);
        }

        int domain = 0 ;
        for (DatagramPacket dp:
             recievedPockets) {
            InetAddress IPAddressDp = dp.getAddress();
            //System.out.println(IPAddressDp);
            int port = dp.getPort() ;
           // System.out.println("port:"+port);
           // System.out.println("end:"+domains[domain+1]);
            Message message = sendRequest(domains[domain],domains[domain+1]);
            byte[] messageBytes = message.tobyteArray();
            DatagramPacket sendPacketRequst = new DatagramPacket(messageBytes, messageBytes.length, IPAddressDp, port);
            clientSocket.send(sendPacketRequst);
            domain = domain + 2;
        }

        int numOfServersConnected = recievedPockets.size();
        int counterNaks = 0;

        boolean recieved = false;
         startTime = System.currentTimeMillis();

        while (!recieved && System.currentTimeMillis()-startTime < serverResponseTimeout && counterNaks < numOfServersConnected){
            try {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                clientSocket.setSoTimeout(3000);
                clientSocket.receive(receivePacket);
               // System.out.println("recived message");
               // System.out.println("type of msg is "+MessageInterpreter.getType(receivePacket.getData()));

                if ((byte) 4 == MessageInterpreter.getType(receivePacket.getData())) {
                    System.out.println("The input string is "+MessageInterpreter.getOriginalStrStart(receivePacket.getData()));
                    recieved = true;
                }

                if ((byte) 5 == MessageInterpreter.getType(receivePacket.getData())) { //nak
                    counterNaks ++;
                  //  System.out.println("got nak");
                }
            }catch (Exception e){} ;
        }

        if(!recieved || counterNaks == numOfServersConnected){
            System.out.println("could not find the correct string for the hash.");
        }

     //   System.out.println("num of connected servers: "+numOfServersConnected);
   //     System.out.println("num of naks recieved: " + counterNaks);


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
