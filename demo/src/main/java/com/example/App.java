package com.example;


import java.io.*;
public class App{
    /*hopCount function will decide the life time for frame to live in network*/
    static int hopCount(int m[][],int n){
        int h=0;
        for(int i=1;i<=n;i++){
            for(int j=1;j<=n;j++){
                h+=m[i][j];
            }
        }
        h-=n;
        h=((h%2)==0)?(h/2):(h/2+1);
        //h=6;

        return h;
    }   


    /*this function show the network in matrix form which you  entered*/
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


    static void check(int m[][],int n,int ps,int s,int d,int h){//m-network matrix ; n-number of nodes  ;  ps - denoted previous node;  s-new source node ; d- destination node; h- time remain to live in network
        int i=s;
    
        if(h==0||s==d)return;
        for(int j=(i+1);j<=n;j++){
            if(m[i][j]==1&&j!=i&&j!=ps)  System.out.print(j+"    ");
        }

        System.out.println("\n\n");
        for(int j=1;j<=n;j++){
            if(m[i][j]==1&&j!=i&&j!=ps) {
                check(m,n,s,j,d,(h-1)); 
            }
            
        }

    }


    static void FloodAlgo(int m[][],int n,int s,int d,int h){
        System.out.println("\n\n"+s+"\n\n");
        check(m,n,0,s,d,h);//check function search the next node and repeat till it not got the destination address or hopcount will become zero;
    }


    public static void main(String args[])throws IOException{
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter Number of nodes in network:    ");
        int n=Integer.valueOf(br.readLine());
        //int n=6;
        /*1 stands for a connection and 0 stands for not connection between two nodes */
        System.out.println("Enter the network Matrix    with(0 and 1):");
        int [][]network=new int[n+1][n+1];
  
        /*first row and colmn contains nodes(hops) number starting from 1 to n */
        for(int i=1;i<=n;i++){
            network[0][i]=i;
            network[i][0]=i;
        }
        /*this is matrix containing the inmforation about connection between hops*/
        for(int i=1;i<=n;i++){
            System.out.println("Row no: "+(i));
            for(int j=1;j<=n;j++){
                System.out.print((j)+":   ");
                int c=Integer.valueOf(br.readLine());
                if(c==0||c==1)    network[i][j]=c;
                else {
                    System.out.println("You entered Other than 0 or 1.\n Enter again.");
                    j-=1;
                }
            }
        }

        showNetworkMatrix(network,n);
        int h=hopCount(network,n);//h used to count maximum time to live frame
        System.out.println("Maxium Life of frame :  "+h);
        int c;//just a variable used to determine error
        do{
            System.out.print(" Enter the source node  id  :  ");
            c=Integer.valueOf(br.readLine());
            if(c==0||c>n)
                System.out.print("Entered wrong id number not available in network.try again.\n Re");
       
        }
        
        while(c==0||c>n);
        int s=c;//source node id variable
        
        do{
            System.out.print(" Enter the  destination node id  :  ");
            c=Integer.valueOf(br.readLine());
            if(c==0||c>n)
                System.out.print("Entered wrong id number not available in network.try again.\n Re");
       
        }
        
        while(c==0||c>n);
        int d=c;//destination node id variable
        FloodAlgo(network,n,s,d,h);
    }
}