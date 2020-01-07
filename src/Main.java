public class Main {
    //testing
    public static void main(String[] args) {
        String [] div = HelperFunctions.divideToDomains(230,4);
        for(String s : div)
            System.out.println(s);

       String hash = HelperFunctions.hash("somestringwithmanymanychars"); //len 27
       // System.out.println(hash); //4ca26abfe0aef43348ebfecfff73a27201653e36
        //System.out.println(HelperFunctions.tryDeHash("somestringwithmanymanyaaaaa", "spazzzzzzzzzzzzzzzzzzzzzzzz", hash));

        String longStr = "somestringwithmanymanycharssomestringwithmanymanycharssomestringwithmanymanycharssomestringwithmanymanycharssomestringwithmanymanycharssomestringwithmanymanycharssomestringwithmanymanycharssomestringwithmanymanychars";
        String hash2 =  HelperFunctions.hash(longStr); //len 216
        System.out.println(hash2);
        System.out.println(HelperFunctions.tryDeHash("somestringwithmanymanycharssomestringwithmanymanycharssomestringwithmanymanycharssomestringwithmanymanycharssomestringwithmanymanycharssomestringwithmanymanycharssomestringwithmanymanycharssomestringwithmanymanycaaaa",
                "somestringwithmanymanycharssomestringwithmanymanycharssomestringwithmanymanycharssomestringwithmanymanycharssomestringwithmanymanycharssomestringwithmanymanycharssomestringwithmanymanycharssomestringwithmanymanyzzzzzz",
                hash2));

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
