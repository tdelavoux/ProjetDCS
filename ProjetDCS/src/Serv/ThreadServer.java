package Serv;
import java.net.Socket;

import CommServCli.ListFileServer;

public class ThreadServer extends Thread{
	
	private Socket sockComm = null;  
	
	public ThreadServer(Socket sockComm, ListFileServer list) {
		this.sockComm = sockComm;  
	}
	
	public void run() {
		
	}

}
