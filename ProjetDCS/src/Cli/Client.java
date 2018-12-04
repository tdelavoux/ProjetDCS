package Cli;

import java.io.*;
import java.net.*;
import java.util.TreeSet;

import CommServCli.*;

public class Client {

	public static void main(String[] args) {
		if(args.length != 3)
		{
			System.exit(1);
		}

		int portServ = 0;
		try { 
			portServ = Integer.parseInt(args[1]);
		}
		catch (NumberFormatException e) {
			System.out.println("Numéro de port non valide !");
			System.exit(1);
		}
 		if ( portServ < 1024 || portServ > 65535 ){
			System.out.println("Numéro de port non autorisé ou non valide !");
			System.exit(1);
		}

		Socket comm = null;
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;

		BufferedReader br = null;

		try
		{
			String ipServ = args[0];
			comm = new Socket(ipServ,portServ);

			oos = new ObjectOutputStream(comm.getOutputStream());
			ois = new ObjectInputStream(comm.getInputStream());
			
			br = new BufferedReader(new InputStreamReader(System.in));

			String s = "";

			System.out.println("Debug, connexion reussie, adresse locale = " + comm.getLocalAddress() + ":" + comm.getLocalPort());

			do
			{
				System.out.print("Saisir votre requete : ");
				s = br.readLine();

				if(!s.equals(""))
				{
					oos.writeObject(new Requete(s));
					oos.flush();
					
 				}

			}while(!s.equals(""));
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		catch(RequestFormationException e)
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
