package Cli;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.RandomAccessFile;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ThreadReceiver extends Thread{
	private DatagramSocket ds;
	private final int SIZE = 1024;
	private String fileName, pathFile;
	
	public ThreadReceiver(DatagramSocket ds, String fileName, String pathFile)
	{
		this.ds = ds;
		this.fileName = fileName;
		this.pathFile = pathFile;
	
	}
	
	public static Object deserialize(DatagramPacket pack) throws IOException
	{
		byte[] buf = pack.getData();
		ByteArrayInputStream bais = new ByteArrayInputStream(buf);
		ObjectInputStream ois = new ObjectInputStream(bais);
		Object o = null;

		try
		{
			o = ois.readObject();
		}
		catch(ClassNotFoundException e)
		{

		}

		return o;
	}
	
	public void run()
	{

		byte[] buf = new byte[SIZE];
		DatagramPacket pack = new DatagramPacket(buf,0,SIZE);
		RandomAccessFile stream = null;
		int cmp = 0;
		
		try
		{
			
			stream = new RandomAccessFile(pathFile+"/"+fileName,"rw");
			while(true)
			{
				ds.receive(pack);
				Pack p = (Pack)deserialize(pack);
				stream.seek(p.getOffset());
				
				stream.write(p.getBuf());
				cmp++;
			}
		}
		catch(IOException e)
		{
			System.out.println("END " + cmp);
			try {
				stream.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}	
	}
	
	public void exit() {
		System.exit(0);
	}
}
