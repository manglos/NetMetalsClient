/* This is a client template for the Android side of You Cant Hide
 * The class provides methods for sending and recieving via Sockets
 */
import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;
 
public class MyClient {
	static int PORT = 2689; // You will need to change this port to match your server port
	static Socket smtpSocket = null;  
	static DataOutputStream dos = null;
	static DataInputStream dis = null;
	static String hostname = "rho.oswego.edu";
	
	//in order to send and recieve objects over the socket
	//here we need an ObjectOutputSteam... i believe.
	static DataInputStream is;
	static PrintWriter out;
	static Socket clientSocket = null;
	static BufferedReader in=null;
	static ServerSocket echoServer = null;
	static int numRequest=0;
	static Header myHeader;
static Packet myPacket;
	public static ForkJoinPool myPool;
	public static Long S, T;
	public static double C[];
	public static int M_HEIGHT = 4;
	public static int M_WIDTH = 16;
	public static Long MAX_TEMP=(long)(10000 * 100);
	public static boolean debug=true;
	public static boolean headerRecieved=false;
	public static TempPoint leftNeighbors[][], rightNeighbors[][];
	public static TempPoint myQuad[][];
	
	public static void setValues(Long sVal, Long tVal, double cVal[], int hVal, int wVal, Long mVal){
		S=sVal;T=tVal;C=cVal;M_HEIGHT=hVal;M_WIDTH=wVal;MAX_TEMP=mVal;
	} 

	public static void setLeftNeighbors(TempPoint[][] ln){
		leftNeighbors=ln;
	}
	public static void setRightNeighbors(TempPoint[][] rn){
		rightNeighbors=rn;
	}
	public static void setMyQuad(TempPoint[][] mq){
		myQuad=mq;
	}
	public static void main(String[] args) {

		smtpSocket=null;
		dos=null;
		dis=null;
		is=null;
		out=null;
		clientSocket=null;
		in=null;
		echoServer=null;
		myHeader=null;
		myPacket=null;
		oos=null;
		ois=null;
		
		myPool = new ForkJoinPool();
		if(openSocket())
			queryServer();
	
    }
    
    static boolean openSocket(){
	

		try {
			echoServer = new ServerSocket(PORT);
			if(debug)System.out.println("I am listening...");
			clientSocket = echoServer.accept();

			oos = new ObjectOutputStream(clientSocket.getOutputStream());
			ois = new ObjectInputStream(clientSocket.getInputStream());

			if(debug)System.out.println("Connection Established.");
			return true;
		}
		catch (IOException e) {
			System.out.println("Hold on IO Exception creating socket \n" + e);
		} 
		
		return false;
		
	}          

	
    static void queryServer(){
			
		Scanner scan = new Scanner(System.in);
		String input=null;
		String s;
	

		try {

			
			if(!headerRecieved){
				Object result = ois.readObject();
				myHeader = (Header)result;
				setValues(myHeader.S, myHeader.T, myHeader.C, myHeader.M_HEIGHT, myHeader.M_WIDTH, myHeader.MAX_TEMP);
				headerRecieved=true;
			}

			Object result = ois.readObject();
			myPacket = (Packet)result;			

			setLeftNeighbors(myPacket.leftNeighbors);
			setRightNeighbors(myPacket.rightNeighbors);
			setMyQuad(myPacket.rQuad);


			QuadrantWorker q = new QuadrantWorker(myQuad, null);
			if(debug)System.out.println("Recieved Header - Calculating...");

			myPool.invoke(q);
		
			System.out.println("Done. Sending...");
			oos.writeObject(q.getWriteQuad());
			if(debug)System.out.println("Sent");

			oos.close();
			ois.close();
			clientSocket.close();
			echoServer.close();
			
			
							 
		} catch (UnknownHostException e) {
			System.err.println("Trying to connect to unknown host: " + e);
		} catch (IOException e) {
			System.err.println("IOException:  " + e);
		} catch (ClassNotFoundException cnfe){
			System.err.println("ClassNotFoundException: "+ cnfe);
		}

		
		if(openSocket())
			queryServer();
	
    }
   
}

