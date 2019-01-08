//Delavoux Bleu

package Cli;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ThreadClient extends Thread{
	Socket comm;
	ServerSocket commClient;
	String pathFile;
	
	public ThreadClient(ServerSocket commClient,String pathFile) {
		this.commClient = commClient;
		this.pathFile = pathFile;
	}
	
	public void run() {
		try {
			while(true)
			{
				comm = commClient.accept();
				/*System.out.println("Waiting ... ");
				
				System.out.println("Connexion du client : " + comm.getInetAddress().getHostAddress() + ":" + comm.getPort() );*/
				ObjectInputStream ois = new ObjectInputStream(comm.getInputStream());
				
				String request = (String)ois.readObject();
				
				//System.out.println("recieved " + request);
				
				ThreadSender ts = new ThreadSender(request,pathFile);
				ts.start();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// Fermeture du Thread
	public void exit() {
		if(comm != null) {
			try {
				comm.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.exit(0);
	}
}
