package udp_projet;

import javax.security.sasl.SaslClient;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class SendMessage extends Thread{
    private byte [] buffer=new byte[2048];
    private String messageToSend;
    private int port;
    private InetAddress inetAddress;
    private DatagramSocket datagramSocket;
    public Scanner scanner = new Scanner(System.in);
    Client client = new Client();

    public SendMessage(){

    }
    public SendMessage(DatagramSocket datagramSocket , InetAddress inetAddress,int port) {
        this.datagramSocket = datagramSocket;
        this.inetAddress = inetAddress;
        this.port = port;
    }

    public void run() {

        while(true) {
            try {
                String messageTosend = scanner.nextLine();
                if(messageTosend.equals("EXIT") || messageTosend .equals("exit")){
                    buffer=("2" + "exit").getBytes();
                    DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length, inetAddress, port);
                    datagramSocket.send(datagramPacket);
                    return;
                }
                else if(messageTosend.length()!=0){
                    buffer =("2" + messageTosend +" ," + Session.client_receiver).getBytes();

                    DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length, inetAddress, port);
                    datagramSocket.send(datagramPacket);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
