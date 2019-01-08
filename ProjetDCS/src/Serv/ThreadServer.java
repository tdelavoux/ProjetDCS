package Serv;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeSet;

import CommServCli.Address;
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
		Address clientAddress = null;
		
		System.out.println("Server is Avaiable with address : ");
		System.out.println(new Address(sockComm.getLocalAddress().toString().replaceAll("/", ""), sockComm.getLocalPort()));
		
		try {
			
			oos = new ObjectOutputStream(sockComm.getOutputStream());    
			ois =  new ObjectInputStream(sockComm.getInputStream());
			
			 /*  ---------   Init Connection-------------------   */
			System.out.println(" \n=====  Connexion etablished with  =====\n");
			
			sockInet = sockComm.getInetAddress();
			System.out.println("Socket Address = "+ sockInet.getHostAddress() + 
					 			"\nport : " + sockComm.getPort());
			System.out.println("\n");
			
			
			// --- Réception de la liste des fichiers stocké en local par le client et mise a jour de la ListFileServer ---//
			@SuppressWarnings("unchecked")
			ArrayList<P2PFile> listFileClient = (ArrayList<P2PFile>)ois.readObject();
			
			clientAddress = (Address)ois.readObject();
			list.insert(listFileClient, clientAddress);

			
			// -- Boucle de communication avec un client -- //
			Requete request = null;
			while((request = (Requete) ois.readObject()) != null) {
				
				//Traitement de la requête reçue
				System.out.println("Request Received !");
				requestProcessing(request, oos, list, currentList);
			}

		 }catch(SocketException e) {
			 System.out.println("Socket has disconnected");
		 }catch (ClassNotFoundException e) {
			 System.out.println("Error, Class not found!");
		 }catch (EOFException e) {
			 System.out.println("Client has disconnected prematurly");
		 }catch(IOException e) {    
			 System.out.println("Critical Error " + e.toString());   
		 }finally{
			 System.out.println("\n == Client disconnected : " + sockInet.getHostAddress() + "   port : " + sockComm.getPort() + " ==");
			 list.remove(clientAddress);
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
				int i = 0;
				for(Map.Entry<P2PFile, TreeSet<Address>> entry : list.getList().entrySet()) {
					
					try {
						if (i == Integer.parseInt(request.getArgument())) {
								
								oos.writeObject(list.getListAddressDownload(entry.getKey()));
								oos.flush();
								break;
						}
						i++;
					}catch(NumberFormatException e) {
						System.out.println("Error not a number");
						oos.writeObject(null);
						oos.flush();
						break;
					}
				}
				
			}
			else {
				System.out.println("Error, Request unable to be processed by Server");
				oos.writeObject(null);
			}
		
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				oos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
