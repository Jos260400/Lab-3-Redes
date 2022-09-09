package com.example;
import java.util.*;

public class Node {
    public int[] graphPath;
    public String [] nodesPath;
    public int hops = 0;

    public String node;
    public int nodeID;
    
    public String users;

    public boolean messageReceived;
    public String msg;

    public void data (int [] id, String[] userID, String user, int nodeID, boolean msgReceived, String account){
        this.graphPath = Arrays.copyOf(id,id.length);
        this.nodesPath = Arrays.copyOf(userID, userID.length);
        this.nodeID = nodeID;
        this.node = user;
        
        
        this.messageReceived = msgReceived;
        
        this.users = account;

    }


    public String messagePath(String source, String destination){
        return this.msg = "----------------------------------------------------\nSource: " + source + "\nDestination:" + destination + "\nHops made: " + hops + "\n----------------------------------------------------";
    }

    public void setMsgReceived(boolean msgReceived){
        this.messageReceived = msgReceived;

    }

    public boolean getMsgReceived(){
        return messageReceived;

    }

    public void setHopsMade(int hopsCounter){
        this.hops = hopsCounter + 1;

    }

    public int getHopsMade(){
        return hops;
    }

    public int[] getPath(){
        return graphPath;
        
    }

    public String getNode(){
        return node;
        
    }

    public int getNodeID(){
        return nodeID;
        
    }

    public String[] getNodesPath(){
        return nodesPath;
        
    }

    public String getUserNames(){
        return users;
    
    }


}