import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UDPserver {
    public static void main(String args[]) throws Exception
    {
        DatagramSocket serverSocket = new DatagramSocket(9877);
        byte[] receiveData = new byte[1024];
        byte[] sendData = new byte[1024];
        ExecutorService tpex = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        while(true)
        {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
//            String sentence = new String( receivePacket.getData());
//            System.out.println("RECEIVED: " + sentence);
//            byte[] b = receivePacket.getData();
//            System.out.println(b[b.length-1] );
            ServerMessageProc serverMessageProc = new ServerMessageProc(serverSocket , receivePacket);
            tpex.execute(new Thread(serverMessageProc));

//            InetAddress IPAddress = receivePacket.getAddress();
//            int port = receivePacket.getPort();
//
//            String capitalizedSentence = sentence.toUpperCase();
//            sendData = capitalizedSentence.getBytes();
//            DatagramPacket sendPacket =
//                    new DatagramPacket(sendData, sendData.length, IPAddress, port);
//            serverSocket.send(sendPacket);

        }
    }
}
