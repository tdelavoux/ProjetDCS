package Serv;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeSet;

import CommServCli.Address;
import CommServCli.ListFileServer;
import CommServCli.P2PFile;
import CommServCli.Requete;

public class ThreadServer extends Thread{
	
	private Socket sockComm = null;  
	private ListFileServer list;
	private LinkedHashMap<P2PFile, TreeSet<Address>> currentList;
	
	public ThreadServer(Socket sockComm, ListFileServer list) {
		this.sockComm = sockComm;  
		this.list = list;
		this.currentList = new LinkedHashMap<>();
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
			Requete request = null;
			while((request = (Requete) ois.readObject()) != null) {
				
				//Traitement de la requête reçue
				System.out.println("Requête reçue !");
				requestProcessing(request, oos, list, currentList);
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
	
	/**
	 * Fonction de traitement de la requête envoyée par le client, 
	 * @param request
	 * @param oos
	 * @param list
	 */
	public static void requestProcessing(Requete request , ObjectOutputStream oos, ListFileServer list, LinkedHashMap<P2PFile, TreeSet<Address>> currentList) {
		
		try {
			if(request.getCommand().equals("search")) {
				currentList = list.getListAddressSearch(request.getArgument());
				oos.writeObject(currentList);
			}
			else if(request.getCommand().equals("get")) {
				int i = 1;
				for(Map.Entry<P2PFile, TreeSet<Address>> entry : list.getList().entrySet()) {
					if (i == Integer.parseInt(request.getArgument()))
							oos.writeObject(list.getListAddressDownload(entry.getKey()));
					i++;
				}
				
			}
			else {
				System.out.println("Erreur, commande impossible à traiter par le serveur");
			}
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
