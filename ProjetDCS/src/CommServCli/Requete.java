package CommServCli;

import java.io.Serializable;

public class Requete implements Serializable {
	private String command;
	private String argument;
	
	public Requete(String line) throws RequestFormationException{
		String[] requestTab = line.split(" ");
		requestTab[0] = requestTab[0].toLowerCase();
		
		if(!verifyContent(requestTab)) 
			throw new RequestFormationException("Nombre d'arguments invalides");
		
		this.command  = requestTab[0];
		
		if(requestTab.length == 2) {
			requestTab[1].replace(" " ,"\"");
			this.argument = requestTab[1];
		}
	}
	
	/**
	 * Vérification du contenue de la requête, taillet et contenu
	 * @param requestTab  Tableau de requête
	 * @return Retourne vrai si la requete est correcte, fausse sinon
	 */
	private boolean verifyContent(String[] requestTab) {
		
		if(!verifyCommand(requestTab))
			return false;
		
		if(!sizeVerify(requestTab))
			return false;
		
		return true;
	}
	
	/**
	 * Vérification de la taille du tableau de requête, repère les erreurs de nombre de paramètres
	 * @param 	String[] 	requete		Requête à analyser   
	 * @return Vrai si la taille est ok, faux sinon
	 */
	private static boolean sizeVerify(String[] requete) {
		 int taille = requete.length;
		 if( (taille < 1 || taille > 2)
		   || (taille != 2 && (requete[0].equals("search") || requete[0].equals("get")))
		   || (taille != 1 && (!requete[0].equals("search") && !requete[0].equals("get")))) {
			 
				System.out.println("Reponse : Erreur, nombre de paramètres incohérent");
				return false;
		 }
		 return true;
	 }
	 
	/**
	 * Vérification de la cohérence de la requête en vérifiant que les commandes soient valides
	 * @param 	String[] 	requete		Requête à analyser   
	 * @return Vrai si la requête est ok, faux sinon
	 */
	 private static boolean verifyCommand(String[] requete){
		 
		 String[] T = {"search", "get", "list", "local-list", "quit"};

		 if(!compareStrings(T, requete[0])) {
			 System.out.println("Reponse : Erreur, Commande innexistante");
			 return false;
		 } 
		 return true;
	 }
	 
	 /**
	  * Fonction de vérification de l'existence des paramètres. Vérifieque le parametre de la requete existe dans un tableau de possibilités.
	  * @param T 	Tableau des termes possible pour le paramètre
	  * @param t 	Paramètre à vérifier
	  * @return     vrai si le paramètre existe, faux sinon
	  */
	 public static boolean compareStrings(String[] T, String t) {
		 for(String s : T) {
			 if(t.equals(s)) {
				 return true;
			 }
		 }
		 return false;
	 }

	
	 
	 /**
	 * @return the Command
	 */
	 public String getCommand() {
		return command;
	}

	/**
	* @return the argument
	*/
	public String getArgument() {
		return argument;
	}




}
