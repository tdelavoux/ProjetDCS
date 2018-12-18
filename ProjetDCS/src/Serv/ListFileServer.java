package Serv;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeSet;

import CommServCli.Address;
import CommServCli.P2PFile;

public class ListFileServer {

		private LinkedHashMap<P2PFile, TreeSet<Address>> list;

		public ListFileServer() {
			this.list = new LinkedHashMap<>();
		}
		
		/**
		 * Insertion d'une file de Fichier à la connexion d'un client
		 * @param toInsert Liste des fichiers ïà insérer
		 * @param clientAddress Adresse du client
		 */
		public synchronized void insert(ArrayList<P2PFile> toInsert, Address clientAddress) {
			for(P2PFile file : toInsert) {
				if(this.list.containsKey(file)) 
					this.list.get(file).add(clientAddress);
				else {
					TreeSet<Address> temp = new TreeSet<>();
					temp .add(clientAddress);
					this.list.put(file, temp);
				}
			}
		}
		
		
		
		/**
		 * Suppression de la liste des fichiers d'un client ï¿½ sa dï¿½connexion
		 * @param address Adresse du client se dï¿½connectant
		 */
		public synchronized void remove(Address address) {
			ArrayList<P2PFile> tab = new ArrayList<>();
			
			for(Map.Entry<P2PFile, TreeSet<Address>> entry : list.entrySet()) {
				
				TreeSet<Address> temp = entry.getValue();
				if(temp.contains(address)) {
					temp.remove(address);
					if(temp.size() == 0) {
						tab.add(entry.getKey());
					}
				}
			}
			
			for(P2PFile f : tab)
			{
				list.remove(f);
			}
		}
		
		
		/**
		 * Obtention de la liste des adresses et des fichiers pour une partie de nom de P2PFile recherche.
		 * @param String Partie d'un nom ï¿½ recherche
		 * @return P2Pfile et TreeSet<Address> (liste des adresses qui ont un fichier dont le nom contient toSearch), null si aucune adresse existante
		 */
		public synchronized LinkedHashMap<P2PFile, TreeSet<Address>> getListAddressSearch (String toSearch){
			LinkedHashMap<P2PFile, TreeSet<Address>> res = new LinkedHashMap<>();
			
			System.out.println("Search in progress ... ");
			
			for(Map.Entry<P2PFile, TreeSet<Address>> entry : list.entrySet())
			{
				//System.out.println(entry.getKey().getName().toLowerCase() + "  " + toSearch.toLowerCase());
				if(entry.getKey().getName().toLowerCase().contains(toSearch.toLowerCase()))
				{
					res.put(entry.getKey(), entry.getValue());
				}
			}
			
			for(Map.Entry<P2PFile, TreeSet<Address>> entry : res.entrySet())
			{
				System.out.println(entry.getKey());
				for(Address a : entry.getValue())
				{
					System.out.println("\t" + a);
				}
			}
			
			
			return res;
		}
		
		
		/**
		 * Obtention de la liste des adresses pour un P2PFile recherche.
		 * @param searched P2PFile 	Fichier a telecharger
		 * @return P2Pfile et TreeSet<Address>  Liste des adresses qui ont le fichier, null si aucune adresse existante
		 */
		public synchronized LinkedHashMap<P2PFile, TreeSet<Address>> getListAddressDownload (P2PFile searched){
			LinkedHashMap<P2PFile, TreeSet<Address>> res = new LinkedHashMap<>();
			if(list.containsKey(searched)) {
				System.out.println(searched);
				res.put(searched, this.list.get(searched));
				return res;
			}
			
			return null;
		}
		
		/**
		 * @return the list
		 */
		public LinkedHashMap<P2PFile, TreeSet<Address>> getList() {
			return list;
		}
		
		public void printList()
		{
			for(Map.Entry<P2PFile, TreeSet<Address>> entry : list.entrySet())
			{
				System.out.println(entry.getKey());
				for(Address a : entry.getValue())
				{
					System.out.println("\t" + a);
				}
			}
		}
}
