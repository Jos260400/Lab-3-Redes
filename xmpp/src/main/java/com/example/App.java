package com.example;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;



import com.example.Node;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.RosterListener;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

//import java.io.File;  // Import the File class

import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
//import org.jivesoftware.smackx.pubsub.Node;
//import org.jivesoftware.smackx.si.packet.StreamInitiation.File;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Resourcepart;
import org.jxmpp.stringprep.XmppStringprepException;
import org.w3c.dom.NodeList;


public final class App {

    public static String options = null;

    public static void Menu(){
        System.out.println("""
            --------Enter one of the options in the menu--------
            1. Login
            2. Log Out
            0. Show menu
            ----------------------------------------------------
            """);
            
            // Scanner scan = new Scanner(System.in); // Capturing the input
            // options = scan.nextLine();
    }

    public static void Menu2(){
        System.out.println("""
            --------Enter one of the options in the menu--------
            1. Flooding Routing Algorithm
            2. DVR Algorithm
            3. Link State Algorithm
            4. Exit
            ----------------------------------------------------
            """);
    }
    

    public static int index(int arr[], int t){
        if (arr == null){
            return -1;
        }

        int length = arr.length;
        int i = 0;

        while (i < length){
            if (arr[i] == t){
                return i;
            }

            else{
                i = i +1;
            }

        }
        return -1;

    }

    public static void showNetworkMatrix(int m[][],int n){
        System.out.println("\n\nNetwork Matrix 1st row and colomn showing nodes(or hops) id\n\n");
        for(int i=0;i<=n;i++){
            for(int j=0;j<=n;j++){
                if(i==0&&j==0)
                    System.out.print("nodes-   ");
                else if(j==0)System.out.print(m[i][j]+"        ");
                else System.out.print(m[i][j]+"    ");
            }
            System.out.println();
        }
    }

    public static void Flooding(Node nodes[], int source, int destination, int n, String msg, EntityBareJid jid, ChatManager chatManager) throws XmppStringprepException, NotConnectedException, InterruptedException{
        System.out.println("Source:" + nodes[source - 1].getNode());

        String nodesListed = "";
        String messageToBeSent = "Message: " + msg;
        Boolean msgReceived = false;

        int[] Buffer = new int[n-1];
        int control, i, j, k;

        for (i = 0; i <= (n-1); i++){
            if(nodes[i].getNodeID() == source){
                System.out.println("Node: \n" + nodes[i] + nodes[i].getHopsMade() + "\n");

                int pathSize = nodes[i].getPath().length;
                
                for(j = 0; j<=pathSize; j++){
                    String nodeMessage;

                    Buffer[j] = nodes[i].getPath()[j];
                    //Verifying if the node recieved the message and the hops made to the node
                    nodes[Buffer[j] - 1].setMsgReceived(true);
                    nodes[Buffer[j] - 1].setHopsMade(nodes[i].getHopsMade());

                    //Printing the path that the message will follow to the destination
                    System.out.println("Node: " + nodes[i].getNode() + " -----> " + "Node: " + nodes[Buffer[j] - 1].getNode());

                    nodesListed += "Node: " + nodes[i].getNode() + " -----> " + "Node: " + nodes[Buffer[j] - 1].getNode() + "\n";

                    jid = JidCreate.entityBareFrom(nodes[Buffer[j] - 1].getUserNames());
                    Chat chat = chatManager.chatWith(jid);

                    
                    nodeMessage = nodes[Buffer[j] - 1].messagePath(nodes[i].getNode(), nodes[destination - 1].getNode()) + nodesListed + msg;
                    chat.send(nodeMessage);

                    if (nodes[Buffer[j] - 1].getNodeID() == destination){
                        msgReceived = true;
                        System.out.println("\n" + "Hops: " + nodes[Buffer[j] - 1].getHopsMade());
                        System.out.println("\n" + "To: " + nodes[Buffer[j] - 1].getNode() + "\n");
                        break;
                    }
                }
                break;

            }
        }

        int currentNode = 0;
        k = Buffer[currentNode];

        while(msgReceived == false){
            for (int l = 0; l<=nodes[k-1].getPath().length-1; l++){
                int index = nodes[k-1].getPath()[l];
                int nodeIndex = index(Buffer, 0);

                if (nodes[index-1].getNodeID() != source ){
                    if(nodes[index-1].getNodeID() != destination){
                        if (!nodes[index-1].getMsgReceived()){
                            String newMessage;
                            Buffer[nodeIndex] = nodes[index-1].getNodeID();
                            nodes[index - 1].setMsgReceived(true);
                            nodes[index - 1].setHopsMade(nodes[k-1].getHopsMade());

                            System.out.println("Node: " + nodes[k-1].getNode() + "----->" + "Node: " + nodes[index-1].getNode());
                            nodesListed += "Nodo: " + nodes[k-1].getNode() + " -----> " + "Nodo: " + nodes[index-1].getNode() + "\n";

                            //Mensaje enviado al nodo
                            jid = JidCreate.entityBareFrom(nodes[index-1].getUserNames());
                            Chat chat = chatManager.chatWith(jid);

                            //Variable que reune toda la info para enviar a los nodes
                            newMessage = nodes[index-1].messagePath(nodes[k-1].getNode(), nodes[destination - 1].getNode()) + nodesListed + msg;

                            chat.send(newMessage);
                        }
                    }else{
                        String newMessage;
                        Buffer[index] = nodes[index-1].getNodeID();
                        nodes[index - 1].setMsgReceived(true);
                        nodes[index - 1].setHopsMade(nodes[k-1].getHopsMade());
                        msgReceived = true;
                        System.out.println("Nodo: " + nodes[k-1].getNode() + " -----> " + "Nodo: " + nodes[index-1].getNode());
                        nodesListed += "Nodo: " + nodes[k-1].getNode() + " -----> " + "Nodo: " + nodes[index-1].getNode() + "\n";

                        //Mensaje enviado al nodo
                        jid = JidCreate.entityBareFrom(nodes[index-1].getUserNames());
                        Chat chat = chatManager.chatWith(jid);

                        //Variable que reune toda la info para enviar a los nodes
                        newMessage = nodes[index-1].messagePath(nodes[k-1].getNode(), nodes[destination - 1].getNode()) + nodesListed + msg;

                        chat.send(newMessage);

                        System.out.println("\n" + "Hops: " + nodes[index - 1].getHopsMade());
                        System.out.println("\n" + "To: " + nodes[index-1].getNode() + "\n");
                    }
                }
            }
            currentNode += 1;
            k = Buffer[currentNode];
        }
        System.out.println("Listed Nodes: " + Arrays.toString(Buffer));
    }

    public static List<Object> getGrafo(String user) throws FileNotFoundException, IOException, ParseException, org.json.simple.parser.ParseException{
        JSONParser parser = new JSONParser();

        //Se inicializa el json que contiene la topologia del grafo
        Object obj = parser.parse(new FileReader("./src/topologia.json"));
        JSONObject jsonObject = (JSONObject)obj;
        String type = (String)jsonObject.get("type");
        System.out.println("Se ha cargado el archivo tipo: " + type + "\n"); 
        JSONObject config = (JSONObject)jsonObject.get("config");
        
        //Se inicizliza el json que contiene los nombres de los grafos
        Object names = parser.parse(new FileReader("./src/names.json"));
        JSONObject namesObject = (JSONObject)names;
        String typeName = (String)namesObject.get("type");
        System.out.println("Se ha cargado el archivo tipo: " + typeName); 
        JSONObject nameConfig = (JSONObject)namesObject.get("config");

        //Se inicializa el array que contendra el grafo
        int n = config.size();
        int [][]network = new int[n + 1][n + 1];

        // Es para agregar a la matriz en numeor de columnas y filas
        for(int i=1;i<=n;i++){
            network[0][i]=i;
            network[i][0]=i;
        }

        Node[] nodos;
        nodos = new Node[n];

        final Integer[] innerK = new Integer[1];
        innerK[0] = 1;
        final Integer[] NodeUser = new Integer[1];
        NodeUser[0] = 0;
        config.keySet().forEach(keyStr ->{
            // Object keyvalue = config.get(keyStr);
            // System.out.println("key: " + keyStr);
            
            //Se convierte en Array al objeto json de la respectiva llave para luego iterarla
            JSONArray subjects = (JSONArray)config.get(keyStr);
            // VAriable que tiene los nombres de todos los nodos por key
            String name = (String)nameConfig.get(keyStr);
            
            
            Iterator iterator = subjects.iterator();
            int [] id = new int [subjects.size()];
            String [] idName = new String [subjects.size()];
            int k = 0;
            while (iterator.hasNext()) {
                String str = (String)iterator.next();
                
                id [k] = (int)str.charAt(0) - 64;
                idName [k] = str;

                
                //Se rellena la matrix netword con los datos del json
                network[innerK[0]][(int)str.charAt(0) - 64] = 1;
                k += 1;
            }
            //Para llenar de 0 los nodos que no estan conectados
            for (int i = 1; i < n; i++){
                if (network[innerK[0]][i] != 1) network[innerK[0]][i] = 0;     
            }
            
            String idNodo = (String)keyStr;
            //Obtiene el usuario del grafo con el que se hizo login
            if (user.equals(name))   NodeUser[0] = (int)idNodo.charAt(0) - 64;   
            

            //Iniciacion de nodo
            nodos[innerK[0] - 1] = new Node();
            nodos[innerK[0] - 1].data(id, idName, (String)keyStr, (int)idNodo.charAt(0) - 64,false, name);
            innerK[0] = innerK[0] + 1;
        });

        if (NodeUser[0] != 0){
            showNetworkMatrix(network,n);
            System.out.println("\n" + "------Todos los preparativos estan listos para Flooding------" + "\n");  
        }
        return Arrays.asList(nodos, n, NodeUser[0]); 
    }


    
    
    //private OutputStream outputStream;

    // private Jid initiator;

    // private Thread transferThread;

    public static void main(String[] args) {
        new Thread(){    
        public void run(){
            try{
                //Getting the user info to login later
                XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                .setXmppDomain("alumchat.fun")
                .setHost("alumchat.fun")
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                .setSendPresence(true)
                .build();

                AbstractXMPPConnection connection = new XMPPTCPConnection(config);
                connection.connect(); //Establishes a connection to the server
                System.out.println("Connected");
                
                

                //Intializing the variables of the menu and printing it
                Scanner menuScanner = new Scanner(System.in);
                int menu1;
                int menu2;
                Menu();
                String spacer = "\n";
                String loginUser = "Please enter the username:";
                String loginPassword = "Please enter the password: ";
                String userID, password;
                
                Scanner login = new Scanner(System.in);
                while (true){
                    menu1 = Integer.parseInt(menuScanner.nextLine());
                    System.out.println(spacer);

                    if (menu1 == 1){
                        System.out.println(spacer);

                        System.out.println(loginUser);
                        userID = login.nextLine();
                        
                        System.out.println(loginPassword);
                        password = login.nextLine();

                        connection.login(userID, password);
                        System.out.println("Your connected.....");

                        AccountManager accountManager = AccountManager.getInstance(connection);
                        EntityBareJid jid = null;
                        Presence presence;
                        
                        ChatManager chatManager = ChatManager.getInstanceFor(connection);
                        chatManager.addIncomingListener(new IncomingChatMessageListener() {
                            @Override
                            public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
                              System.out.println("New message from " + from + ": " + message.getBody());
                            }
                        });

                        Menu2();
                        String msg;
                        Node[] nodes = null;
                        int cNodes = 0;

                        int source = 0;
                        while(connection.isConnected()){
                            System.out.println(spacer);
                            menu2 = Integer.parseInt(menuScanner.nextLine());
                            System.out.println(spacer);

                            switch(menu2){
                                case 1:
                                    //Flooding algorithm implementation 
                                    if (nodes == null && cNodes == 0){
                                        String getUser = connection.getUser().toString();
                                        String[] arroUser = getUser.split("/");
                                        List<Object> objects = getGrafo(arroUser[0]);
                                        Object[] grafo = objects.toArray();
                                        nodes = (Node[])grafo[0];
                                        cNodes = (int)grafo[1];
                                        source = (int)grafo[2];
                                    }

                                    // if (source == 0) {
                                        // System.out.println("\n" + "!!!!Este usuario no es parte de la topologia!!!!" + "\n");
                                        // break;   
                                    // }

                                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));   
                                    int c; //Una variable solo para obtener los inputs  
                                    String mensaje; // Una variable para obtner el input del mensaje a enviar a los nodos
                                    System.out.println(" The source node  id  :  " + source + "\n");
                                    System.out.print(" Enter the  destination node id  :  ");
                                    c=Integer.valueOf(br.readLine());
                                    int d=c;//destination node id variable
                                    System.out.print(" Enter the message to pass  :  ");
                                    msg = String.valueOf(br.readLine());

                                    Flooding(nodes, source, d, cNodes, msg, jid, chatManager);
                                    break;


                                case 2:
                                    //DVR
                                    System.out.println("DVR");
                                    break;

                                case 3:
                                    //LS
                                    System.out.println("LS");
                                    break;

                                case 4:
                                    menu1 = 2;
                                    System.out.println("BYE");
                                    connection.disconnect();
                                    break;

                                default:
                                    System.out.println("Please enter a valid option");
                                    break;
                                
                            }

                        }

                        connection.connect();
                        Menu();


                    }

                    else if(menu1==2){
                        break;
                    }

                    else if(menu1 == 0){
                        Menu();
                    }

                    else{
                        System.out.println("""

                                Please enter a valid option
                                
                                """);
                    }
                }

                System.out.println("Disconnected");
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        }.start();
    }
}
