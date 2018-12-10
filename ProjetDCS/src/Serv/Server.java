package Serv;

//Usage java Serveur portServeur

import java.net.*; 
import java.io.*; 
import Serv.ListFileServer;

public class Server { 

	public static void main(String[] args) {   
		
		// Vérification des arguments et création de la socket de communication TCP
		// Le port de la socket est le numero du port serveur
		ServerSocket conn = null;    
		 if (args.length != 1)
			{
				System.out.println(" Usage :  java Serveur portServeur");
				System.exit(1);
			}
			int portServ = 0;
	     try{
				portServ = Integer.parseInt(args[0]);
			}
			catch(NumberFormatException e){
				System.out.println("Numéro de port non valide.");
				System.exit(1);
			}
			if ( portServ < 1024 || portServ > 65535 ){
				System.out.println("Numéro de port non autorisé ou non valide.");
				System.exit(2);
			}
		
		ListFileServer list = new ListFileServer();	
		
		try {    
			conn = new ServerSocket(portServ);
			while(true) {     
				Socket comm = conn.accept();     
				ThreadServer t = new ThreadServer (comm, list);     
				t.start();    
			}   
		}catch(IOException e) {    
			System.out.println("Problème de connexion client : "+e.toString());   
		}finally{
			if (conn != null){
				try {
					conn.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
         }
		}

	}
}