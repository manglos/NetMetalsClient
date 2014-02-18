/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
/**
 *
 * @author uman
 */
public class ClientWorker implements Runnable {
    private Socket server;
    private String line,input;
    int myClientNumber;
    ArrayList<String> output;    
    static String coopString = "";
    static int coopInt = 0;

    //DataInputStream in = null;// new DataInputStream (server.getInputStream());
    PrintWriter os = null;// new PrintStream(server.getOutputStream());
    BufferedReader b = null;// new BufferedReader(new InputStreamReader(server.getInputStream()));

    
    Socket smtpSocket = null;  
    DataOutputStream dos = null;
    DataInputStream dis = null;
    String hostname = "localhost";

    public ClientWorker(Socket server, int n) {
      this.server=server;
      myClientNumber=n;
    }

    @Override
    public void run () {

        input="";

        try {
          
            os = new PrintWriter(server.getOutputStream(), true);
            b = new BufferedReader(new InputStreamReader(server.getInputStream()));

            output = new ArrayList<String>();
            output.add("********************** Welcome To Newtopia **********************");
            output.add("*                                                               *");
            output.add("*            Created By: Monty Newman as a tool for             *");
            output.add("*                      control the world.                       *");
            output.add("*               You are client number "+myClientNumber+"                       *");
            output.add("*****************************************************************");
            output.add("");
            output.add("Enter a command, 'help' or 'h' to access help menu.");
            sendMessage(output);
            // As long as we receive data, echo that data back to the client.
            line = b.readLine();
            while (line!=null && !line.equals("system:exit")) {
        	System.out.println("I Got: " + line);
            	output = new ArrayList<String>();
		
		if (line.startsWith("+")){
			try{
				coopInt += Integer.parseInt(line.substring(1));
				output.add("Co-Op Int Project is: " + coopInt);
			}catch(NumberFormatException nfe){
				coopString += line.substring(1);
				output.add("Co-Op String Project is: " + coopString);
			}
		}
		else if(line.startsWith("-")){
			try{
				coopInt -= Integer.parseInt(line.substring(1));
				output.add("Co-Op Int Project is: " + coopInt);
			}catch(NumberFormatException nfe){
				//if (coopString.contains(line.substring(1))){
				//	System.out.println("in the string");
					coopString = coopString.replaceAll(line.substring(1), "");
				//}
				output.add("Co-Op String Project is: " + coopString);
			}	
		}
		else
            		output.add("Server Answers: " + calculate(line));
 
            	sendMessage(output);
		line = b.readLine();

	    }
	    b.close();


	} catch (IOException ioe) {
          System.err.println("IOException on socket listen: " + ioe);
          ioe.printStackTrace();
        }
    }
    
    
    void sendToClient(String s){
        
        if(smtpSocket==null && dos==null && dis==null){
            try {
                smtpSocket = new Socket(hostname, 4891);
                dos = new DataOutputStream(smtpSocket.getOutputStream());
                dis = new DataInputStream(smtpSocket.getInputStream());
            } catch (UnknownHostException e) {
                System.err.println("Don't know about host: " + hostname);
            } catch (IOException e) {
                System.err.println("Couldn't get I/O for the connection to: " + hostname);
            } 
        }
        
        if (smtpSocket != null && dos != null && dis != null) {
            try {
                dos.writeBytes(s + "\n");
            } catch (UnknownHostException e) {
                System.err.println("Trying to connect to unknown host: " + e);
            } catch (IOException e) {
                System.err.println("IOException:  " + e);
            }
        }
        
        run();
        
    }

    String calculate(String s){
	return s;
    }

    void sendMessage(ArrayList<String> message){

	os.println(message.size()+"");
	for(String s : message)
		os.println(s);
    }              
}

