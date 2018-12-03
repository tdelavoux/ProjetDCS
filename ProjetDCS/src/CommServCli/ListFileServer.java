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
		 * Insertion d'une file de Fichier à la connexion d'un client
		 * @param toInsert Liste des fichiers à insérer
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
		 * Suppression de la liste des fichiers d'un client à sa déconnexion
		 * @param address Adresse du client se déconnectant
		 */
		public void remove(Address address) {
			
			for(Map.Entry<P2PFile, TreeSet<Address>> entry : list.entrySet()) {
				
				TreeSet<Address> temp = entry.getValue();
				if(temp.contains(address)) {
					temp.remove(address);
					if(temp.size() == 0) {
						list.remove(entry.getKey());
					}
				}
			}
		}
		
		
		
		public TreeSet<Address> getListAddressSearch (String toSearch){
			TreeSet<Address> res = new TreeSet<>();
		
			return null;
		}
		
		
		/**
		 * Obtention de la liste des adresses pour un P2PFile recherché.
		 * @param searched P2PFile 	Fichier à télécharger
		 * @return TreeSet<Address> Liste des adresses qui ont le fichier, null si aucune adresse existante
		 */
		public TreeSet<Address> getListAddressDownload (P2PFile searched){
		
			return this.list.get(searched);
		}
		
}
