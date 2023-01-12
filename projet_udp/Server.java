package udp_projet;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;



public class Server extends Thread {
    private final int tailleMax = 2024;
    private final int port = 1111;
    byte[] tampon = new byte[tailleMax];
    public DatagramSocket datagramSocket;
    public static DatagramPacket datagramPacket;
    private static ArrayList<InetAddress> users_addresses;
    private static ArrayList<Integer> users_port;
    private static ArrayList<String> users_username;
    ConnectToDatabase connectToDatabase = new ConnectToDatabase();

    public static int port_client;
    public static String ContetOfPacket;


    public Server() throws SocketException {
        connectToDatabase.setAllUseerDisconnected();
        users_addresses = new ArrayList();
        users_port = new ArrayList<Integer>();
        users_username = new ArrayList();
        datagramSocket = new DatagramSocket(port);
        System.out.println("Server is running and is listening on port :" + port);
    }


    //methode 1
    public int idPacket() throws IOException {
        datagramPacket = new DatagramPacket(tampon, tampon.length);
        datagramSocket.receive(datagramPacket);
        String ip_packet = new String(datagramPacket.getData(), 0, 1);
        ContetOfPacket = new String(datagramPacket.getData(), 1, datagramPacket.getLength() - 1);
        port_client = datagramPacket.getPort();

        return Integer.parseInt(ip_packet);
    }

    //methode 2
    public void getAuthentifer() throws IOException {

        InetAddress inetAddressClient = datagramPacket.getAddress();
        System.out.println("new user :" + ContetOfPacket + ", port : " + port_client);


        //fillig information about the new user
        users_addresses.add(inetAddressClient);
        users_username.add((ContetOfPacket));
        users_port.add(port_client);


        connectToDatabase.setUserConnected(ContetOfPacket);
        byte[] data = ("Successful Authentication of: " + ContetOfPacket).getBytes();
        datagramPacket= new DatagramPacket(data, data.length, inetAddressClient, port_client);
        datagramSocket.send(datagramPacket);
    }


    // methode 3
    public void receiveAndSendMessageAndSendToclient() throws IOException {

        InetAddress inetAddressClient = datagramPacket.getAddress();

        //get the  name of the user_sender
        String user_sender = null;
        for (int i = 0; i < users_port.size(); i++) {
            if (users_port.get(i) == port_client) {
                user_sender = users_username.get(i);
            }
        }

        //the packet contains [message,client_receiver]
        String[] args = ContetOfPacket.split(",");

        //if the user don't enter exit
        if (!(ContetOfPacket.equals("exit"))) {
            ContetOfPacket = args[0];
            String user_receiver = args[1];

            System.out.println(user_sender + " send " + " : " + ContetOfPacket + " to : " +user_receiver );


            int user_receiver_port = 0;
            for (int i = 0; i < users_port.size(); i++) {
                if (users_username.get(i).equals(user_receiver)) {
                    user_receiver_port = users_port.get(i);
                }
            }

            byte[] data = (user_sender + ":" + ContetOfPacket).getBytes();
            datagramPacket = new DatagramPacket(data, data.length, inetAddressClient, user_receiver_port);
            datagramSocket.send(datagramPacket);

        } else {

            byte[] data = ContetOfPacket.getBytes();//messageFromClient ="exit"
            datagramPacket = new DatagramPacket(data, data.length, inetAddressClient, port_client);
            datagramSocket.send(datagramPacket);

        }

    }



    //methode 4
    public void receiveRequestAndSendClientConnected() throws IOException {
        InetAddress inetAddressClient = datagramPacket.getAddress();///127.0.0.1
        connectToDatabase.getUsersConnected();

        ArrayList usersConnected = connectToDatabase.getUsersConnected();

        String users ="";

        for (int i=0 ; i<usersConnected.size();i++){
            if (users_port.get(i) != port_client) {
                users = users + users_username.get(i) + ",";
            }
        }

        //send client connected with from String
        byte  [] data = users.getBytes();
        datagramPacket = new DatagramPacket(data, data.length, inetAddressClient, port_client);
        datagramSocket.send(datagramPacket);
    }


    //methode 5
    public void ClientSeDeconnet() throws IOException {
        InetAddress inetAddressClient = datagramPacket.getAddress();


        //remove data about user from arraylist
        String user_sender=null;
        for(int i =0 ; i< users_port.size(); i++) {
            if (users_port.get(i) == port_client) {
                user_sender = users_username.get(i);
                users_addresses.remove(i);
                users_username.remove((i));
                users_port.remove(i);
            }
        }

        System.out.println(user_sender + " : has disconnected");
        connectToDatabase.setUserNotConnected(user_sender);
        byte [] data = ("you are Successful deconnected ").getBytes();
         datagramPacket = new DatagramPacket(data, data.length, inetAddressClient, port_client);
        datagramSocket.send(datagramPacket);
    }

    //methode 6
    public void  receiveRequestAndSendListFriend() throws IOException{
        InetAddress inetAddressClient = datagramPacket.getAddress();
        String user_sender=ContetOfPacket;

        ArrayList userFriend = connectToDatabase.getUsersFriends(user_sender);
        String users ="";

        //convert table to string
        for (int i=0 ; i<userFriend.size();i++){
            users = users + userFriend.get(i) + ",";
        }

        byte  [] data = users.getBytes();
        datagramPacket = new DatagramPacket(data, data.length, inetAddressClient, port_client);
        datagramSocket.send(datagramPacket);
    }


    //methode 7:we use it to display list of user not friend fo call methode 8 the added a use choose it
    public void receiveRequestAndSendListNotFriend() throws IOException{
        InetAddress inetAddressClient = datagramPacket.getAddress();
        String user_sender=ContetOfPacket;

        ArrayList userNotFriend = connectToDatabase.getListeUsersNotFriend(user_sender);
        String users ="";

        for (int i=0 ; i<userNotFriend.size();i++){
            users = users + userNotFriend.get(i) + ",";
        }

        byte  [] data = users.getBytes();
        datagramPacket = new DatagramPacket(data, data.length, inetAddressClient, port_client);
        datagramSocket.send(datagramPacket);
    }

    //methode 8
    public void receiveRequestToAddClient() throws IOException {
        InetAddress inetAddressClient = datagramPacket.getAddress();
        String newFirend=ContetOfPacket;

        String user_sender=null;
        for(int i =0 ; i< users_port.size(); i++) {
            if (users_port.get(i) == port_client) {
                user_sender = users_username.get(i);
            }
        }

        connectToDatabase.addNewFriend(user_sender,newFirend);

        byte [] data = ("you are Successful added : " + newFirend).getBytes();
        DatagramPacket packet = new DatagramPacket(data, data.length, inetAddressClient, port_client);
        datagramSocket.send(packet);
    }


    //methode 9
    public void receiveRequestToRemoveClient() throws IOException {
        InetAddress inetAddressClient = datagramPacket.getAddress();
        String removedUser=ContetOfPacket;

        String user_sender=null;
        for(int i =0 ; i< users_port.size(); i++) {
            if (users_port.get(i) == port_client) {
                user_sender = users_username.get(i);
            }
        }
        connectToDatabase.deleteFriend(user_sender,removedUser);


        byte  [] data = (removedUser +" removed succeffully").getBytes();
        DatagramPacket datagramPacket = new DatagramPacket(data, data.length, inetAddressClient, port_client);
        datagramSocket.send(datagramPacket);
    }


    //methode 10
    public void run(){
        while (true) {
            int id_packet = 0;
            try {
                id_packet = idPacket();
                switch (id_packet) {
                    case 1:
                        getAuthentifer();
                        break;
                    case 2:
                        receiveAndSendMessageAndSendToclient();
                        break;
                    case 3:
                        receiveRequestAndSendClientConnected();
                        break;
                    case 4:
                        ClientSeDeconnet();
                        break;
                    case 5:
                        receiveRequestAndSendListFriend();
                        break;
                    case 6:
                        receiveRequestAndSendListNotFriend();
                        break;
                    case 7:
                        receiveRequestToAddClient();
                        break;
                    case 8:
                        receiveRequestToRemoveClient();
                        break;
                    default:
                        System.out.println("i don't now your request");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    public static void main(String[] args) throws IOException, InterruptedException {


        Server server = new Server();
        server.start();


    }
}
