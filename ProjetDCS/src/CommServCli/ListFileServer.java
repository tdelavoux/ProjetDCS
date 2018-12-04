package CommServCli;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeSet;

public class ListFileServer {

		LinkedHashMap<P2PFile, TreeSet<Address>> list;

		public ListFileServer() {
			this.list = new LinkedHashMap<>();
		}
		
		/**
		 * Insertion d'une file de Fichier � la connexion d'un client
		 * @param toInsert Liste des fichiers � ins�rer
		 * @param address Adresse du client
		 */
		public void insert(ArrayList<P2PFile> toInsert, Address address) {
			for(P2PFile file : toInsert) {
				if(this.list.containsKey(file)) 
					this.list.get(file).add(address);
				else {
					TreeSet<Address> temp = new TreeSet<>();
					temp .add(address);
					this.list.put(file, temp);
				}
			}
		}
		
		
		
		/**
		 * Suppression de la liste des fichiers d'un client � sa d�connexion
		 * @param address Adresse du client se d�connectant
		 */
		public void remove(Address address) {
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
		 * Obtention de la liste des adresses pour une partie de nom de P2PFile recherch�.
		 * @param String Partie d'un nom � recherch�
		 * @return TreeSet<Address> liste des adresses qui ont un fichier dont le nom contient toSearch, null si aucune adresse existante
		 */
		public TreeSet<Address> getListAddressSearch (String toSearch){
			TreeSet<Address> res = new TreeSet<>();
			
			for(Map.Entry<P2PFile, TreeSet<Address>> entry : list.entrySet())
			{
				if(entry.getKey().getName().contains(toSearch))
				{
					res.addAll(entry.getValue());
				}
			}
		
			return res;
		}
		
		
		/**
		 * Obtention de la liste des adresses pour un P2PFile recherch�.
		 * @param searched P2PFile 	Fichier � t�l�charger
		 * @return TreeSet<Address> Liste des adresses qui ont le fichier, null si aucune adresse existante
		 */
		public TreeSet<Address> getListAddressDownload (P2PFile searched){
		
			return this.list.get(searched);
		}
		
}
