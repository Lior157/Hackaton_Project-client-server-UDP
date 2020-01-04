import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ServerMessageProc implements Runnable{
    private DatagramSocket serverSocket ;
    private volatile DatagramPacket receivePacket;

    public ServerMessageProc(DatagramSocket serverSocket, DatagramPacket receivePacket) {
        this.serverSocket = serverSocket;
        this.receivePacket = receivePacket;
    }

    private DatagramPacket DiscoverRecieved(){
        InetAddress IPAddress = receivePacket.getAddress();
        int port = receivePacket.getPort();

        Message msg = new Message();
        msg.setTeamName(MessageInterpreter.getTeamName(receivePacket.getData())); //size 32
        msg.setType((byte)2);

        byte[] data = msg.tobyteArray();
        DatagramPacket sendPacket =
                new DatagramPacket(data, data.length, IPAddress, port);
        return sendPacket;
    }
    private DatagramPacket RequestRecieved(){
        InetAddress IPAddress = receivePacket.getAddress();
        int port = receivePacket.getPort();

        byte[] dataRecieved = receivePacket.getData();
        String start =MessageInterpreter.getOriginalStrStart(dataRecieved);
        String end = MessageInterpreter.getOriginalStrEnd(dataRecieved);
        byte length = MessageInterpreter.getOriginalLength(dataRecieved);
        String message = MessageInterpreter.getHash(dataRecieved);
        String returnString = HelperFunctions.tryDeHash(start,end,message);

        Message msg = new Message();
        msg.setTeamName(MessageInterpreter.getTeamName(receivePacket.getData())); //size 32
        msg.setOriginalLength(length);
        if(returnString != null) {
            msg.setType((byte) 4);
            msg.setOriginalStrStart(returnString);
            msg.setOriginalStrEnd(returnString);
        }else{
            msg.setType((byte) 5);
            msg.setOriginalStrStart(start);
            msg.setOriginalStrEnd(end);
        }


        byte[] data = msg.tobyteArray();
        DatagramPacket sendPacket =
                new DatagramPacket(data, data.length, IPAddress, port);
        return sendPacket;
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
