package Cli;

import java.io.Serializable;

public class Pack implements Serializable{
	private long offset;
	private byte[] buf;
	
	public Pack(long offset, byte[] buf)
	{
		this.offset = offset;
		this.buf = buf;
	}
	
	public long getOffset() { return offset; }
	public byte[] getBuf() { return buf; }
}
