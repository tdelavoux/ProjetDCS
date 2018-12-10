package Cli;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeSet;

import CommServCli.*;

public class Client {

	public static void main(String[] args) {
		if(args.length != 3)
		{
			System.out.println("Erreur");
			System.exit(1);
		}

		int portServ = 0;
		try { 
			portServ = Integer.parseInt(args[1]);
		}
		catch (NumberFormatException e) {
			System.out.println("Numero de port non valide !");
			System.exit(1);
		}
 		if ( portServ < 1024 || portServ > 65535 ){
			System.out.println("Numero de port non autorise ou non valide !");
			System.exit(1);
		}

		Socket comm = null;
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;
		BufferedReader br = null;
		
		
		File directory = new File(args[2]);
		
		if(!(directory.exists() && directory.isDirectory())){
			System.out.println("Error, the Directory do not exist or is not a Directory ");
		}

		try{
			String ipServ = args[0];
			comm = new Socket(ipServ,portServ);

			oos = new ObjectOutputStream(comm.getOutputStream());
			ois = new ObjectInputStream(comm.getInputStream());
			
			br = new BufferedReader(new InputStreamReader(System.in));
			
			File [] listFile = directory.listFiles();
			ArrayList<P2PFile> list = new ArrayList<P2PFile>();
			LinkedHashMap<P2PFile,TreeSet<Address>> currentSearch = null;
			LinkedHashMap<P2PFile,TreeSet<Address>> currentGet = null;
			
			for(File file : listFile){
				if(file.isFile())
					list.add(new P2PFile(file.length(),file.getName().replaceAll(args[2] + "/","")));
			}
			
			
			oos.writeObject(list);
			oos.flush();

			String s = "";

			System.out.println("Connexion etablished with Server with adress :  " + comm.getLocalAddress() + ":" + comm.getLocalPort()+"\n");
			
			System.out.println("####### Welcome to P2P transfert software #######\n");
			System.out.println("Possible Request : ");
			System.out.println("  > search <pattern>");
			System.out.println("  > get <num of file in current file list>");
			System.out.println("  > list");
			System.out.println("  > local-list");
			System.out.println("  > quit\n\n");

			do
			{
				
				System.out.println(" --- Enter a request  ---\n");
				System.out.print(">>> ");

				s = br.readLine();

				if(!s.trim().equals(""))
				{
					Requete r = null;
					try
					{
						r = new Requete(s.toLowerCase());
						
						if(r.getCommand().equals("quit")){
							break;
						}
						else if(r.getCommand().equals("list")) {
							System.out.println(" -- Current List  --");
							
							if(currentSearch == null) {
								System.out.println("No current list. Use search to get list of file");
							}
							else {
								int i = 0;
								for(Map.Entry<P2PFile, TreeSet<Address>> entry : currentSearch.entrySet()) {
									System.out.println(i + ". "  + entry.getKey() + " -> " + entry.getValue());
								}
							}
							continue;
						}
						else if(r.getCommand().equals("local-list")) {
							System.out.println(" -- My Files  --");
							for(P2PFile p : list){
								System.out.println(p);
							}
							System.out.println("---------- \n");
							continue;
						}
						
						oos.writeObject(r);
						oos.flush();
						
						if(r.getCommand().equals("search"))
							currentSearch = (LinkedHashMap<P2PFile,TreeSet<Address>>)ois.readObject();
						else if(r.getCommand().equals("get"))
							currentGet = (LinkedHashMap<P2PFile,TreeSet<Address>>)ois.readObject();
						
//						if(currentSearch == null)
//							System.out.println("null");
//						else
//							System.out.println("Pas null");
					}
					catch(RequestFormationException e)
					{
						e.printStackTrace();
						continue;
					}	
 				}

			}while(!s.equals(""));
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		catch(ClassNotFoundException e) 
		{
			e.printStackTrace();
		}finally {
			try {
				if(oos != null) 
					oos.close();
				
				if(ois != null)
					ois.close();
	
				if(br != null)
					br.close();
	
				comm.close();
				comm = null;
			}
			catch(IOException e){
				e.printStackTrace();
			}
			System.out.println("\nThank's for using ! Come back Any Time");
			System.out.println(" ---- Client has closed with success ----");
		}
	}
}
