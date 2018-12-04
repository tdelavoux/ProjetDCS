package CommServCli;

public class P2PFile {
	private long taille;
	private String name;
	
	public P2PFile(long taille, String name)
	{
		this.taille = taille;
		this.name = name;
	}	
	
	public long getTaille() { return taille; }
	public String getName() { return name; }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + (int) (taille ^ (taille >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		P2PFile other = (P2PFile) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (taille != other.taille)
			return false;
		return true;
	}
	
	
}
