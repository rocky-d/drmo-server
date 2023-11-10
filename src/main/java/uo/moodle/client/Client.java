package uo.moodle.client;

public class Client {

    private String keystore;
    private String serverAdress = "";
    private String messageContext ="coordinates";
    private String registerContext ="registration";


    public Client(String keystore2, String address) {
        this.keystore = keystore2;

        if (!address.endsWith("/")) {
			serverAdress = address;
            serverAdress +=  "/";
		}
        else{
            serverAdress = address;
        }
    }

    public Client(String address) {

        if (!address.endsWith("/")) {
			serverAdress = address;
            serverAdress +=  "/";
		}
        else{
            serverAdress = address;
        }

    }


    public String getKeystore() {
        System.out.println(this.keystore);
        return this.keystore;
    }

    public String getServerAddress(){
        return this.serverAdress;
    }

    public String getMessageContext(){
        return this.messageContext;
    }

    public String getRegisterContext(){
        return this.registerContext;
    }

    
}
