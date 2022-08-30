package com.example;


//import java.io.File;  // Import the File class

import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;
import org.jivesoftware.smackx.filetransfer.FileTransfer.Status;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.muc.RoomInfo;
import org.jivesoftware.smackx.si.packet.StreamInitiation.File;
//import org.jivesoftware.smackx.si.packet.StreamInitiation.File;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.EntityFullJid;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Localpart;
import org.jxmpp.jid.parts.Resourcepart;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import java.util.Collection;
import java.util.Scanner;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.RosterListener;


public final class App {

    public static String options = null;

    public static void Menu(){
        System.out.println("""
            --------Enter one of the options in the menu--------

            1. Login
            2. Create account
            3. DM's
            4. Create roomChat
            5. List of roomChats
            6. Send Files
            7. List all the contacts
            8. Add a new contact
            9. Delete user's account
           10. Log Out
            0. Exit
            ----------------------------------------------------
            """);
            
            Scanner scan = new Scanner(System.in); // Capturing the input
            options = scan.nextLine();
    }
    
    //private OutputStream outputStream;

    private Jid initiator;

    private Thread transferThread;

    private App() {
    }

    /**
     * Says hello to the world.
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        new Thread(){    
        public void run(){
            try{

                XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                .setUsernameAndPassword("test","test")
                .setXmppDomain("alumchat.fun")
                .setHost("alumchat.fun")
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                .setSendPresence(true)
                .build();

                AbstractXMPPConnection connection = new XMPPTCPConnection(config);
                connection.connect(); //Establishes a connection to the server
                System.out.println("Connected");
                //Scanner user = new Scanner(System.in);
                connection.login(); //Logs in
                //EntityBareJid jid = JidCreate.entityBareFrom(user.nextLine() + "@" + connection.getHost());

                //#region adding friends
                // Scanner user = new Scanner(System.in);
                // EntityBareJid jid = JidCreate.entityBareFrom(user.nextLine() + "@alumchat.fun" );

                // try {
                //     Roster.setDefaultSubscriptionMode(Roster.SubscriptionMode.manual);
                //     Roster roster = Roster.getInstanceFor(connection);
    
                //     if (!roster.contains(jid)) {
                //         roster.createItemAndRequestSubscription(jid, user.nextLine(), null);
                //     } else {
                //         System.out.println("A friend has been added");
                //     }
    
                // } catch (SmackException.NotLoggedInException e) {
                //     e.printStackTrace();
                // } catch (SmackException.NoResponseException e) {
                //     e.printStackTrace();
                // } catch (SmackException.NotConnectedException e) {
                //     e.printStackTrace();
                // } catch (XMPPException.XMPPErrorException e) {
                //     e.printStackTrace();
                // } catch (Exception e) {
                //     e.printStackTrace();
                // }
                //#endregion


                //#region Chat 1v1
                Scanner user = new Scanner(System.in);
                //EntityBareJid jidGroup = JidCreate.entityBareFrom("are@conference." + connection.getHost());
                EntityBareJid jid = JidCreate.entityBareFrom("test@alumchat.fun" );
                ChatManager chatManager = ChatManager.getInstanceFor(connection);
                //Chat chat = chatManager.chatWith(jidGroup);
                Chat chat = chatManager.chatWith(jid);
                
                chatManager.addIncomingListener(new IncomingChatMessageListener() {
                    @Override
                    public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
                      System.out.println("New message from " + from + ": " + message.getBody());
                    }
                });

                Scanner messege = new Scanner(System.in);
                while(connection.isConnected()){
                    chat.send(messege.nextLine());
                }
                //#endregion
                
                //#region Chat Grupal
                // MultiUserChatManager manager = MultiUserChatManager.getInstanceFor(connection);
                // MultiUserChat muc = manager.getMultiUserChat(jid);
                // Resourcepart room = Resourcepart.from("arem");
                // if (!muc.isJoined())
                //     muc.join(room);

                // muc.addMessageListener(new MessageListener() {
                //     @Override
                //     public void processMessage(Message message){
                //         System.out.println((message != null ? message.getBody() : "NULL") + "  , Message sender :" + message.getFrom());
                //     }
                // });
                
                // Scanner conteiner = new Scanner(System.in);
                // String messegeString = "";
                // System.out.println("Para ver la opciones en el menu precione 1: ");
                // while(!messegeString.contains("~")){
                //     messegeString = conteiner.nextLine();
                //         if (messegeString.contains("1"))
                //             System.out.println("Presione ~ para salir");
                //         else
                //             muc.sendMessage(messegeString);
                //     }
                    
                //     muc.leave();
                //#endregion
                    
                System.out.println("Disconnected");
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        }.start();
    }
}