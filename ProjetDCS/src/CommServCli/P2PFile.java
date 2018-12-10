package CommServCli;

public class P2PFile {
	private long size;
	private String name;
	
	public P2PFile(long size, String name)
	{
		this.size = size;
		this.name = name;
	}	
	
	public long getSize() { return size; }
	public String getName() { return name; }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + (int) (size ^ (size >>> 32));
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
		if (size != other.size)
			return false;
		return true;
	}
	
	public String toString()
	{
		return name + " (" + size + ")";
	}
}
