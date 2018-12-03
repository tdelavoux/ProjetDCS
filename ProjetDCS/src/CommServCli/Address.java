package servcli;

public class Address implements Comparable<Address>{
	private String addressIp;
	private int port;
	
	public Address(String addressIp, int port)
	{
		this.addressIp = addressIp;
		this.port = port;
	}
	
	public String getAddressIp() { return addressIp; }
	public int getPort() { return port; }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((addressIp == null) ? 0 : addressIp.hashCode());
		result = prime * result + port;
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
		Address other = (Address) obj;
		if (addressIp == null) {
			if (other.addressIp != null)
				return false;
		} else if (!addressIp.equals(other.addressIp))
			return false;
		if (port != other.port)
			return false;
		return true;
	}

	public int compareTo(Address a) {
		if(a.addressIp.equals(this.addressIp))
		{
			if(a.port < this.port)
			{
				return 1;
			}
			if(a.port > this.port)
			{
				return -1;
			}
		}
		else
		{
			return 1;
		}	
		return 0;
	}
	
	
}
