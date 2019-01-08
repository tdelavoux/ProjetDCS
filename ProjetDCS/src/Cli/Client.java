//ByteArrayOutputStream
//ByteArrayInputStream
package Cli;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeSet;

import CommServCli.*;
import Serv.ThreadServer;

public class Client {

	public static void main(String[] args) {
		if(args.length != 3)
		{
			System.out.println("Error, wrong argument's number");
			System.out.println("Usage : java Client ipServ NumPort pathFile");
			System.exit(1);
		}

		int portServ = 0;
		try { 
			portServ = Integer.parseInt(args[1]);
		}
		catch (NumberFormatException e) {
			System.out.println("Invalid port Number");
			System.exit(1);
		}
 		if ( portServ < 1024 || portServ > 65535 ){
			System.out.println("Forbidden port Number");
			System.exit(1);
		}
 		
 		String pathFile = args[2].replace('\\', '/');

		Socket comm = null;
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;
		BufferedReader br = null;
		Address myAddress = null;
		ThreadClient tc = null;
		
		ServerSocket connClient = null;
		
		
		File directory = new File(pathFile);
		
		if(!(directory.exists() && directory.isDirectory())){
			System.out.println("Error, the Directory do not exist or is not a Directory ");
		}

		try{
			
			String ipServ = args[0];
			comm = new Socket(ipServ,portServ);
			
			connClient = new ServerSocket(0);
			tc = new ThreadClient(connClient,pathFile);
			tc.start();

			oos = new ObjectOutputStream(comm.getOutputStream());
			ois = new ObjectInputStream(comm.getInputStream());
			
			br = new BufferedReader(new InputStreamReader(System.in));
			
			File [] listFile = directory.listFiles();
			ArrayList<P2PFile> list = new ArrayList<P2PFile>();
			LinkedHashMap<P2PFile,TreeSet<Address>> currentSearch = null;
			LinkedHashMap<P2PFile,TreeSet<Address>> currentGet = null;
			
			for(File file : listFile){
				if(file.isFile())
					list.add(new P2PFile(file.length(),file.getName().replaceAll(pathFile + "/","")));
			}
			
			
			oos.writeObject(list);
			
			myAddress = new Address(comm.getLocalAddress().toString().replaceAll("/", ""), connClient.getLocalPort());
			oos.writeObject(myAddress);
			oos.flush();

			String s = "";
			System.out.println("Connexion etablished with Server with adress :  " + comm.getInetAddress().getHostAddress() + " " + comm.getPort() +"\n");
			
			
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
						r = new Requete(s);
						
						if(r.getCommand().equals("quit")){
							break;
						}
						else if(r.getCommand().equals("list")) {
							getCurrentList(currentSearch, myAddress);
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
						
						if(r.getCommand().equals("search")) {
							currentSearch = (LinkedHashMap<P2PFile,TreeSet<Address>>)ois.readObject();
							getCurrentList(currentSearch, myAddress);
						}
						
						else if(r.getCommand().equals("get")) {
							
							try {
								if(Integer.parseInt(r.getArgument()) > currentSearch.size()) {
									System.out.println("Error, the number is out of range");
									continue;
								}
							}catch(NumberFormatException e) {
								System.out.println("Error, argument is not a number ! ");
								continue;
							}
							
							oos.writeObject(currentSearch);
							oos.flush();
							
							currentGet = (LinkedHashMap<P2PFile,TreeSet<Address>>)ois.readObject();
							
							if(currentGet == null) {
								System.out.println("Error as occured, list is empty");
								continue;
							}
							
							if(!verifyAlreadyGet(currentGet, myAddress)) {
								getCurrentList(currentGet, myAddress);
								f(currentGet, comm.getLocalAddress().toString(),pathFile);
							}
							else
								System.out.println("You already Own this file\n\n");
						}
						
					}
					catch(RequestFormationException e)
					{
						System.out.println("Error, Invalid argument");
						continue;
					}	
 				}

			}while(!s.equals(""));
		
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		
		}finally {			
			
			// ----- Close flux -------------------------------------------------------------- //
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
			
			tc.exit();
		}
	}
	
	private static boolean verifyAlreadyGet(LinkedHashMap<P2PFile,TreeSet<Address>> currentSearch, Address myAddress){
		for(Map.Entry<P2PFile, TreeSet<Address>> entry : currentSearch.entrySet()) {
	
			for(Address add : entry.getValue()) {
				if(add.equals(myAddress))
					return true;
			}
		}
		return false;
	}
	
	private static void getCurrentList(LinkedHashMap<P2PFile,TreeSet<Address>> currentSearch, Address myAddress) {
		System.out.println(" -- Current List  --");
		
		if(currentSearch == null) {
			System.out.println("No current list. Use search to get list of file");
		}
		else {
			int i = 0;
			Address test = null;
			for(Map.Entry<P2PFile, TreeSet<Address>> entry : currentSearch.entrySet()) {
				System.out.println(i + ". "  + entry.getKey() + " : ");
				
				for(Address add : entry.getValue()) {
					System.out.println("\t" + add);
					test = add;
				}
				++i;
			}
			
		}
	}
	
	private static void f(LinkedHashMap<P2PFile,TreeSet<Address>> currentGet, String ip, String pathFile)
	{
		P2PFile file = null;
		TreeSet<Address> cli = null;
		ip = ip.replaceAll("/", "");
		
		for(Map.Entry<P2PFile, TreeSet<Address>> entry : currentGet.entrySet()) {
			file = entry.getKey();
			cli = entry.getValue();
		}
		
		try
		{
			int numPack = (int)Math.ceil(((double)file.getSize())/1024);
			int numPackCli = numPack/cli.size();
			int resteDiv = numPack%cli.size();
			int min = 0, max = (cli.size() == 1)? numPackCli+1 : numPackCli;
			int i = 0;
			
			for(Address add : cli) {
				Socket s = new Socket(add.getAddressIp(), add.getPort());
				ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
				
				DatagramSocket ds = new DatagramSocket();
				
				String request = ip + "/" + ds.getLocalPort() + "/" + file.getName() + "/" + file.getSize() + "/" + min + "/" + max;
				
				oos.writeObject(request);
				oos.flush();
				
				ThreadReceiver tr = new ThreadReceiver(ds,file.getName(),pathFile);
				tr.start();
				
				
				min += numPackCli;
				i++;
				
				if(i == cli.size()-1)
					max += numPackCli + resteDiv + 1;
				else
					max += numPackCli;
			}
		}
		catch(IOException e)
		{
			
		}
	}
	
}
