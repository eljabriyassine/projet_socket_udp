package udp_projet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class   ReceiveMessage extends Thread{
    public DatagramSocket datagramSocket;
    public ReceiveMessage(DatagramSocket datagramSocket) {
        this.datagramSocket = datagramSocket;
    }

    public void run() {
        while (true) {
            try {


                byte[] data = new byte[2048];
                DatagramPacket datagramPacket = new DatagramPacket(data, data.length);
                datagramSocket.receive(datagramPacket);
                String messageReceived = new String(datagramPacket.getData(), 0, datagramPacket.getLength()).trim();
                String[] tab = messageReceived.split(":");

                if (messageReceived.equals("EXIT") || messageReceived.equals("exit")) {
                    break;
                }
                //for prevent the user to recive a messge for a user doesn't choose him to talk with(
                // e.g if yassine want to receive a message from karim .yassine must choose karim like  user_reciever
                if ((Session.client_receiver).trim().equals(tab[0].trim())) {
                    System.out.println(messageReceived);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
