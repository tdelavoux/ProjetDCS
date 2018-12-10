package Cli;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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
			System.out.println("Num�ro de port non valide !");
			System.exit(1);
		}
 		if ( portServ < 1024 || portServ > 65535 ){
			System.out.println("Num�ro de port non autoris� ou non valide !");
			System.exit(1);
		}

		Socket comm = null;
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;
		BufferedReader br = null;
		
		File directory = new File(args[2]);
		
		if(!(directory.exists() && directory.isDirectory()))
		{
			System.out.println("Erreur");
		}

		try
		{
			String ipServ = args[0];
			comm = new Socket(ipServ,portServ);

			oos = new ObjectOutputStream(comm.getOutputStream());
			ois = new ObjectInputStream(comm.getInputStream());
			
			br = new BufferedReader(new InputStreamReader(System.in));
			
			File [] listFile = directory.listFiles();
			ArrayList<P2PFile> list = new ArrayList<P2PFile>();
			
			for(File file : listFile)
			{
				list.add(new P2PFile(file.length(),file.getName().replaceAll(args[2] + "/","")));
			}
			
			/*for(P2PFile p : list)
			{
				System.out.println(p);
			}*/
			
			oos.writeObject(list);
			oos.flush();

			String s = "";

			System.out.println("Debug, connexion reussie, adresse locale = " + comm.getLocalAddress() + ":" + comm.getLocalPort());

			do
			{
				System.out.print("Saisir votre requete : ");
				s = br.readLine();

				if(!s.equals(""))
				{
					Requete r = null;
					try
					{
						r = new Requete(s);
						oos.writeObject(r);
						oos.flush();
						
						LinkedHashMap<P2PFile,TreeSet<Address>> t = (LinkedHashMap<P2PFile,TreeSet<Address>>)ois.readObject();
						
						if(t == null)
							System.out.println("null");
						else
							System.out.println("Pas null");
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
		}
		try
		{
			if(oos != null)
			{
				oos.close();
			}

			if(ois != null)
			{
				ois.close();
			}

			if(br != null)
			{
				br.close();
			}

			comm.close();
			comm = null;
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
