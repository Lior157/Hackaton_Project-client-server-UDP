import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ServerMessageProc implements Runnable{
    private DatagramSocket serverSocket ;
    private volatile DatagramPacket receivePacket;
    private static final int maxTimeToCalculate = 120000;

    public ServerMessageProc(DatagramSocket serverSocket, DatagramPacket receivePacket) {
        this.serverSocket = serverSocket;
        this.receivePacket = receivePacket;
    }

    private DatagramPacket DiscoverRecieved(){
        try {
            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();

            Message msg = new Message();
            msg.setTeamName(MessageInterpreter.getTeamName(receivePacket.getData())); //size 32
            msg.setType((byte) 2);

            byte[] data = msg.tobyteArray();
            DatagramPacket sendPacket =
                    new DatagramPacket(data, data.length, IPAddress, port);
            return sendPacket;
        }
        catch (Exception e){
            return null;
        }
    }
    private DatagramPacket RequestRecieved(){
        try {
            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();

            byte[] dataRecieved = receivePacket.getData();
            String start = MessageInterpreter.getOriginalStrStart(dataRecieved);
            String end = MessageInterpreter.getOriginalStrEnd(dataRecieved);
            byte length = MessageInterpreter.getOriginalLength(dataRecieved);
            String message = MessageInterpreter.getHash(dataRecieved);
            System.out.println("I got a request from team "+ MessageInterpreter.getTeamName(dataRecieved));
            System.out.println("I need to search from "+start+ " to "+end +" for input of len "+ length);

            System.out.println("startingFindString..");
            String returnString = HelperFunctions.tryDeHash(start, end, message, maxTimeToCalculate);
            System.out.println(returnString);
            if(returnString == null){
                System.out.println("I did not find the input, sending message to client...");
            }
            else{
                System.out.println("I found the input, sending message to client...");
            }

            Message msg = new Message();
            msg.setHash(message);
            msg.setOriginalLength(length);
            msg.setTeamName(MessageInterpreter.getTeamName(dataRecieved)); //size 32
            if (returnString != null) {
                msg.setType((byte) 4);
                msg.setOriginalStrStart(returnString);
                msg.setOriginalStrEnd(returnString);
            } else {
                msg.setType((byte) 5);
                msg.setOriginalStrStart(start);
                msg.setOriginalStrEnd(end);
            }


            byte[] data = msg.tobyteArray();
            DatagramPacket sendPacket =
                    new DatagramPacket(data, data.length, IPAddress, port);

            System.out.println("message sent");
            return sendPacket;
        }
        catch (Exception e){
            return null;
        }
    }
    @Override
    public void run() {
       byte Type = MessageInterpreter.getType(receivePacket.getData()) ;
       DatagramPacket sendPacket = null;
       switch ((int) Type){
           case 1:
               sendPacket = DiscoverRecieved();
               break;
           case 2:
               break;
           case 3:
               sendPacket = RequestRecieved();
               break;
           case 4:
               break;
           case 5:
               break;
       }
       if(sendPacket!=null) {
           try {
               serverSocket.send(sendPacket);
           }catch (Exception e){
               System.out.println(e.getStackTrace());
           }
       }
    }
}
