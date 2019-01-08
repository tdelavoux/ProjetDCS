package Cli;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.RandomAccessFile;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ThreadReceiver extends Thread{
	private DatagramSocket ds;
	private final int SIZE = 2000;
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
		byte[] buf = new byte[2000];
		DatagramPacket pack = new DatagramPacket(buf,0,2000);
		RandomAccessFile stream = null;
				
		try
		{
			
			stream = new RandomAccessFile(pathFile+"/"+fileName,"rw");
			while(true)
			{
				pack.setData(buf);
				ds.receive(pack);
				Pack p = (Pack)deserialize(pack);
				stream.seek(p.getOffset());
				
				stream.write(p.getBuf());
			}
		}
		catch(IOException e)
		{
			System.out.println("END ");
			try {
				stream.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			this.interrupt();
		}	
	}
	
	public void exit() {
		System.exit(0);
	}
}
