package udp_projet;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class Session extends Thread {
    public DatagramSocket datagramSocket;
    public InetAddress inetAddress;
    public int port;
    Scanner scanner = new Scanner(System.in);
    public static String client_receiver;



    public Session(DatagramSocket datagramSocket, InetAddress inetAddress, int port) {
        System.out.print("chose to client : ");
        client_receiver = scanner.nextLine();
        System.out.println("conversation  start with  : " + client_receiver);
        System.out.println("-----------------");
        SendMessage sendMessage = new SendMessage(datagramSocket, inetAddress, port);
        sendMessage.start();
        ReceiveMessage receiveMessage = new ReceiveMessage(datagramSocket);
        receiveMessage.start();

        try {
            sendMessage.join();
            receiveMessage.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
