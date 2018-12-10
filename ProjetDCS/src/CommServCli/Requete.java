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
	 * V�rification du contenue de la requ�te, taillet et contenu
	 * @param requestTab  Tableau de requ�te
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
	 * V�rification de la taille du tableau de requ�te, rep�re les erreurs de nombre de param�tres
	 * @param 	String[] 	requete		Requ�te � analyser   
	 * @return Vrai si la taille est ok, faux sinon
	 */
	private static boolean sizeVerify(String[] requete) {
		 int taille = requete.length;
		 if( (taille < 1 || taille > 2)
		   || (taille != 2 && (requete[0].equals("search") || requete[0].equals("get")))
		   || (taille != 1 && (!requete[0].equals("search") && !requete[0].equals("get")))) {
			 
				System.out.println("Reponse : Erreur, nombre de param�tres incoh�rent");
				return false;
		 }
		 return true;
	 }
	 
	/**
	 * V�rification de la coh�rence de la requ�te en v�rifiant que les commandes soient valides
	 * @param 	String[] 	requete		Requ�te � analyser   
	 * @return Vrai si la requ�te est ok, faux sinon
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
	  * Fonction de v�rification de l'existence des param�tres. V�rifieque le parametre de la requete existe dans un tableau de possibilit�s.
	  * @param T 	Tableau des termes possible pour le param�tre
	  * @param t 	Param�tre � v�rifier
	  * @return     vrai si le param�tre existe, faux sinon
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
