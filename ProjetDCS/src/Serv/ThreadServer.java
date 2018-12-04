package Serv;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

import CommServCli.ListFileServer;
import CommServCli.Requete;

public class ThreadServer extends Thread{
	
	private Socket sockComm = null;  
	private ListFileServer list;
	
	public ThreadServer(Socket sockComm, ListFileServer list) {
		this.sockComm = sockComm;  
	}
	
	public void run() {
		
		InetAddress sockInet = null;
		ObjectInputStream ois = null;   
		ObjectOutputStream oos = null;  
		
		try {
			
			oos = new ObjectOutputStream(sockComm.getOutputStream());    
			ois =  new ObjectInputStream(sockComm.getInputStream());
			
			 /*  ---------   Init Connection-------------------   */
			System.out.println(" \n=====  Connection �tablie avec  =====\n");
			
			sockInet = sockComm.getInetAddress();
			System.out.println("Adresse de socket = "+ sockInet.getHostAddress() + 
					 			"\nport : " + sockComm.getPort());
			System.out.println("\n");
			
			// Boucle de communication avec un client
			while(true){
				
				// R�ception d'un objet Requete, traitement de cette derniere et renvoie de la reponse
				Requete request = (Requete)ois.readObject();
				System.out.println("R�ception d'une requete :");
				
			}
			
		
		 }catch(SocketException e) {
			 System.out.println("D�connexion pr�matur�e de la socket");
		 }catch (ClassNotFoundException e) {
			 System.out.println("Erreur classe non trouv�e !");
		 }catch(IOException e) {    
			 System.out.println("Pb de communication " + e.toString());   
		 }finally{
			 
		 }
		
	}

}
