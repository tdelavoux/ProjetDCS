package Cli;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ThreadClient extends Thread{
	Socket comm;
	ServerSocket commClient;
	
	public ThreadClient(ServerSocket commClient) {
		this.commClient = commClient;
		
	}
	
	public void run() {
		try {
			comm = commClient.accept();
			System.out.println("Waiting ... ");
			
			System.out.println("Connexion du client : " + comm.getInetAddress().getHostAddress() +  comm.getPort() );
			ObjectInputStream ois = new ObjectInputStream(comm.getInputStream());
			
			String s = (String)ois.readObject();
			
			System.out.println("recieved " +s); 
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
