package udp_projet;

import udp_test.LoginForm;
import udp_test.User;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Client extends Thread{
    public final int  port=1111;
    public DatagramSocket datagramSocket;
    public DatagramPacket datagramPacket;
    public  InetAddress inetAddress;
    private byte [] buffer;
    public User user;
    static Scanner scanner = new Scanner(System.in);
    public boolean isConnected;
    public static String client_receiver;

    public Client(){

    }
    public Client(DatagramSocket datagramSocket, InetAddress inetAddress) {
        this.datagramSocket = datagramSocket;
        this.inetAddress = inetAddress;
        this.isConnected = false;
    }

    //methode 1
    public void Login(){
        LoginForm loginForm = new LoginForm(null);
        user = loginForm.user;
        //if the user is login is success
        if(user!=null){
            isConnected=true;
            String username = user.username;
            if(username.length()!=0){
                try {
                    buffer = ("1"+ username).getBytes();
                    datagramPacket = new DatagramPacket(buffer,buffer.length,inetAddress,port);
                    datagramSocket.send(datagramPacket);

                    byte []  data = new byte[2048];
                    datagramPacket = new DatagramPacket(data,data.length);
                    datagramSocket.receive(datagramPacket);
                    String messageFromServer = new String (datagramPacket.getData(),0,datagramPacket.getLength());
                    System.out.println(messageFromServer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else{
            System.out.println("authentication faild");
        }
    }

    //methode 2
    public void StartConversation() throws IOException, InterruptedException {
        //start the converstain
        Session session = new Session(datagramSocket, inetAddress, port);
        session.start();
        session.join();
    }

    //methode 3
    public void sendMessageToShowClientConnected() throws IOException {
        //this methode show to user the user connected
        buffer = ("3").getBytes();
        datagramPacket = new DatagramPacket(buffer, buffer.length, inetAddress, port);
        datagramSocket.send(datagramPacket);


        byte[] data = new byte[1024];
        datagramPacket = new DatagramPacket(data, data.length);
        datagramSocket.receive(datagramPacket);
        String messageReceived = new String(datagramPacket.getData(), 0, datagramPacket.getLength()).trim();
        String[] usersConnected = messageReceived.split(",");
        if (messageReceived.length() != 0) {
            int numberClientConnected = 1;
            for (String i : usersConnected) {
                System.out.println(numberClientConnected++ + " - " + i);
            }
        } else {
            System.out.println("NO one is connected");
        }
    }


    //methode 4
    public void sendRequestToSeDeConnect() throws IOException {
        //se
        buffer = ("4" + "i want to se doconnect").getBytes();
        datagramPacket = new DatagramPacket(buffer,buffer.length,inetAddress,port);
        datagramSocket.send(datagramPacket);


        byte[] data = new byte[1024];
        datagramPacket = new DatagramPacket(data, data.length);
        datagramSocket.receive(datagramPacket);
        String messageReceived = new String(datagramPacket.getData(), 0, datagramPacket.getLength()).trim();
        System.out.println(messageReceived);

    }

    //methode 5
    public ArrayList sendRequestToShowListFriend() throws IOException {
        buffer = ("5"+ user.username).getBytes();
        datagramPacket = new DatagramPacket(buffer,buffer.length,inetAddress,port);
        datagramSocket.send(datagramPacket);

        byte[] data = new byte[1024];
        datagramPacket = new DatagramPacket(data, data.length);
        datagramSocket.receive(datagramPacket);
        String messageReceived = new String(datagramPacket.getData(), 0, datagramPacket.getLength()).trim();
        ArrayList<String> userFriend = new ArrayList<>(Arrays.asList(messageReceived.split(","))) ;
        if(messageReceived.length()!=0) {
            int numberFriend = 1;
            for (String i : userFriend) {
                System.out.println(numberFriend++ + " - " + i);
            }
        }else{
            System.out.println("add a new friend");
        }
        return userFriend;
    }


    //methode 6
    public void sendRequestToShowuUserNotFriend() throws IOException {
        buffer = ("6"+ user.username).getBytes();
        datagramPacket = new DatagramPacket(buffer,buffer.length,inetAddress,port);
        datagramSocket.send(datagramPacket);
        byte[] data = new byte[1024];
        datagramPacket = new DatagramPacket(data, data.length);
        datagramSocket.receive(datagramPacket);

        //recieve client not friend
        String messageReceived = new String(datagramPacket.getData(), 0, datagramPacket.getLength()).trim();
        ArrayList<String> usersNotFriend = new ArrayList<>(Arrays.asList(messageReceived.split(","))) ;
        int numberClientNotFriend = 1;
        if(messageReceived.length()!=0) {
            for (String i : usersNotFriend) {
                System.out.println(numberClientNotFriend++ + " - " + i);
                }
                addingAfriend(usersNotFriend);
        }else{
            System.out.println("there is not friend to add ");
        }
    }

    //methode 7
    public void addingAfriend(ArrayList<String> usersFriend) throws IOException {
            while(true) {
                System.out.print("choose a client to add a liste friend: ");
                String newClient = scanner.nextLine();
                if (usersFriend.contains(newClient)) {

                    buffer = ("7" + newClient).getBytes();
                    datagramPacket = new DatagramPacket(buffer, buffer.length, inetAddress, port);
                    datagramSocket.send(datagramPacket);

                    byte[] data = new byte[1024];
                    datagramPacket = new DatagramPacket(data, data.length);
                    datagramSocket.receive(datagramPacket);
                    String messageReceived = new String(datagramPacket.getData(), 0, datagramPacket.getLength()).trim();
                    System.out.println(messageReceived);
                    return;
                }
            }
    }

    //methode 8
    public void sendRequestToRemoveFriend() throws IOException {
        ArrayList<String> userFriend = sendRequestToShowListFriend();
        String input;
        if(userFriend.size()!=0){
            do {
                System.out.print("chose to Friend from to list : ");
                input = scanner.nextLine();
                if (userFriend.contains(input)) {
                    buffer = ("8" + input).getBytes();
                    DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length, inetAddress, port);
                    datagramSocket.send(datagramPacket);

                    byte[] data = new byte[1024];
                    DatagramPacket datagramPacket5 = new DatagramPacket(data, data.length);
                    datagramSocket.receive(datagramPacket5);
                    String messageReceived = new String(datagramPacket5.getData(), 0, datagramPacket5.getLength()).trim();
                    System.out.println(messageReceived);
                    return;

                }
            }while (input.toLowerCase().equals("exit") );
        }
    }


    //methode 9
    public  void run(){
        try {
            if (!isConnected) {
                Login();
            }
            if(isConnected){
                do {
                    System.out.println("\n::::::::::::::: MENU :::::::::::::::::::: ");
                    System.out.println("1-  Envoi de Message");
                    System.out.println("2-  liste Client Connecte ");
                    System.out.println("3 - Se Deconnecter :");
                    System.out.println("4 - liste friend");
                    System.out.println("5 - add new Friend");
                    System.out.println("6 - remove friend");
                    Scanner scanner = new Scanner(System.in);
                    System.out.print("chose somthing to do :");
                    int choose = scanner.nextInt();
                    scanner.nextLine();
                    switch (choose) {
                        case 1:
                            StartConversation();
                            break;
                        case 2:
                            sendMessageToShowClientConnected();
                            break;
                        case 3:
                            sendRequestToSeDeConnect();
                            System.exit(-100);
                            break;
                        case 4 :
                            sendRequestToShowListFriend();
                            break;
                        case 5:
                            sendRequestToShowuUserNotFriend();//ligne 127
                            break;
                        case 6:
                            sendRequestToRemoveFriend();
                            break;
                        default:
                            System.out.println("please choose 1 to comple your request");
                            break;
                    }
                } while (true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }





    public static void main(String[] args) throws IOException, InterruptedException {
        DatagramSocket datagramSocket = null;
        datagramSocket = new DatagramSocket();
        InetAddress inetAddress = InetAddress.getByName("localhost");

        Client client = new Client(datagramSocket, inetAddress);
        client.start();

    }
}
