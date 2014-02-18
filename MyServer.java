/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author montynewman
 */
public class MyServer{ 
    static final int PORT = 2689;
    FileInputStream fis = null;
    ObjectInputStream ois = null;
    static ArrayList<String> output;
    static PrintStream os;
    static int clientNumber = 0;

    public static void main (String[] args){
	System.out.println("I Am Listening...");
	openSocket();

	} 

    private static void openSocket(){            
        ServerSocket echoServer = null;
        //String output="";
        String line;
        DataInputStream is;
        Socket clientSocket = null;
        BufferedReader b=null;
 
        // Try to open a server socket on port 9999
        try {
           echoServer = new ServerSocket(PORT);
        }
        catch (IOException e) {
           System.out.println(e);
        }   
        // Create a socket object from the ServerSocket to listen and accept 
        // connections.
        // Open input and output streams

            
        while(true){
            ClientWorker w;
            try{
              w = new ClientWorker(echoServer.accept(), clientNumber++);
              Thread t = new Thread(w);
              t.start();
            } catch (IOException e) {
              System.out.println("Accept failed: " + PORT);
              System.exit(-1);
            }
        }
            
    }

    static  String calculate(String s){
	String strings[] = s.split(" ");

	int x=-1;
	int y=-1;
	

	try{
		x = Integer.parseInt(strings[0]);	
	}catch (NumberFormatException fx){
		System.out.println("Got a bad input.");
		return "Bad 1st Number";
	}
	
	try{
		y = Integer.parseInt(strings[2]);	
	}catch (NumberFormatException fx){
		System.out.println("Got a bad input.");
		return "Bad 2nd Number";
	}

	if (strings[1].equals("-")){
		return (x - y)+"";
	}

	if (strings[1].equals("+")){
		return (x + y)+"";
	}

	if (strings[1].equals("*")){
		return (x * y)+"";
	}

	return "Bad Input";
    }

    static void sendMessage(ArrayList<String> message){

	os.println(message.size()+"");
	for(String s : message)
		os.println(s);

    }    
}

