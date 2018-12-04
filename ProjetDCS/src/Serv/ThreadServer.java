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
			System.out.println(" \n=====  Connection établie avec  =====\n");
			
			sockInet = sockComm.getInetAddress();
			System.out.println("Adresse de socket = "+ sockInet.getHostAddress() + 
					 			"\nport : " + sockComm.getPort());
			System.out.println("\n");
			
			// Boucle de communication avec un client
			Requete ligne = null;
			while((ligne = (Requete) ois.readObject()) != null) {
				
				//Traitement de la requête reçue
				System.out.println("Requête reçue !");

			}
			
			
		
		 }catch(SocketException e) {
			 System.out.println("Déconnexion prématurée de la socket");
		 }catch (ClassNotFoundException e) {
			 System.out.println("Erreur classe non trouvée !");
		 }catch(IOException e) {    
			 System.out.println("Pb de communication " + e.toString());   
		 }finally{
			 System.out.println("\n == Fin de connexion avec le client : " + sockInet.getHostAddress() + "   port : " + sockComm.getPort() + " ==");
		 }
		
	}

}
