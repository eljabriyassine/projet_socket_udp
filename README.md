# projet_socket_udp
the user this applicatioon run the server first : when  is run he will make all clients disconnected : and second run the main in the class client then you can see the form of login 

the application is chat which you can you can the class server which contains a methodes that allow the client
the respont for her request 

    we have 5 class :
        server : constains  methodes 
            -idPacket():the first methode excecuted  when the packet received  from client and split the see a id_packet and based on this packet we executed an others methode  to repsond  the client
                receiveAndSendMessageAndSendToclient():accept request contains message and client receiver which we wanna send message and if you want to exist chat you can type exit||EXIT
                receiveRequestAndSendClientConnected():the resoond client of the client connected 
                ClientSeDeconnet():if you client want to disconnect 
                and this is also other methodes that allow the client to see friend and add one on remved

                finnaly we have methode run (): which contains all methode above that gonna executed base of id_client 

                class main: we run the server with severt.start();

        client : constains  methodes : each one add a id_packet in the first before send it to server that help the server the respond the client
            Login(): lance the first allow the client to login or make him enligne
            StartConversation(): we run a class session that allow  two client talking each others
            sendMessageToShowClientConnected(): send request to see client connected 
            and this is also others methode like sendRequestToSeDeConnect(),sendRequestToShowListFriend(),sendRequestToShowuUserNotFriend(),addingAfriend()

            finnaly we have methode run the conatains all methode 

        Session : the run two Thread sendMessage and receiveMessage

        sendMessage:methode run that send the messagee and 
        receiveMessage : methode run the receive the message from the client and if message receive contains exit the client will leave the conversation 

        DATA BASE:
        conatins two table
        user : three attribute : username and password and isConnected 
        some exeple
        
        table username:some Exemple
        ---------------username --------------passowrd ----------------------isConnected-
                       achraf                   123                             NO
                       yassine                  123                             NO
                       kaim                     123                             NO
                       haimd                    123                             NO
        ----------------------------------------------------------------------------------


        table friends   :Some Exemples
        -----------------------user-------------user_friend------------------
                                yassine         karim   
                                karim           hamid   
                                walid           hamid
                                hamid           achraf
                                yassine         hamid
                                