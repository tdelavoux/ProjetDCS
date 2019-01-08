//Delavoux Bleu

package Cli;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class ThreadSender extends Thread{
	private final int SIZE = 1024;
	private String[] tab;
	private String pathFile;
	
	public ThreadSender(String request, String pathFile)
	{
		tab = request.split("/");
		this.pathFile = pathFile;

	}
	
	public static void serialize(Object o,DatagramPacket pack) throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);

		oos.writeObject(o);
		oos.flush();
		oos.close();
		oos = null;

		byte[] buf = baos.toByteArray();
		pack.setData(buf);

		baos.close();
	}
	
	public void run()
	{
		DatagramSocket ts = null;
		try {
			ts = new DatagramSocket();
			
			byte[] buf = new byte[SIZE];
			int i = 0, num = Integer.parseInt(tab[4]);
			
			DatagramPacket pack = new DatagramPacket(new byte[2000],2000,InetAddress.getByName(tab[0]),Integer.parseInt(tab[1]));
			
			RandomAccessFile stream = new RandomAccessFile(pathFile+"/"+tab[2],"r");
			stream.seek(Integer.parseInt(tab[4])*1024);
			
			while((stream.read(buf) != -1)&&(i < Integer.parseInt(tab[5])))
			{
				Pack p = new Pack(num*1024,buf);
				serialize(p,pack);
				ts.send(pack);
				num++;
			}
			stream.close();
			ts.close();
			this.interrupt();
			
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			ts.close();
		}
	}
}
