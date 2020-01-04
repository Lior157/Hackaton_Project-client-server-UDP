public class Main {
    //testing
    public static void main(String[] args) {

        Message msg = new Message();
        msg.setTeamName("DonaldCyber"); //size 32
        msg.setType((byte)3);
        msg.setHash("a346f3083515cbc8ca18aae24f331dee2d23454b");
        msg.setOriginalLength((byte)5);
        msg.setOriginalStrStart("vaaaa");
        msg.setOriginalStrEnd("vzzzz");

        byte[] bytes = msg.tobyteArray();
        System.out.println(MessageInterpreter.getType(bytes));
        System.out.println(MessageInterpreter.getOriginalStrStart(bytes));
        System.out.println(MessageInterpreter.getOriginalStrEnd(bytes));
        System.out.println(MessageInterpreter.getHash(bytes));
        System.out.println(MessageInterpreter.getTeamName(bytes));


    }
}
