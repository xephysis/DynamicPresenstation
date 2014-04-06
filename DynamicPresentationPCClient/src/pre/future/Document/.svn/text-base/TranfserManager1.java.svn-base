package pre.future.Document;

import java.awt.Desktop;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

public class TranfserManager1 extends Thread{
	ServerSocket serverSocket = null;
	Socket socket = null;

	public TranfserManager1()
	{
		this.receiveDocument();
	}
	public void receiveDocument()
	{
		try
		{
			serverSocket = new ServerSocket(10002);
			System.out.println("Initialized ...");
			FileReceiver1 fr;

			while((socket = serverSocket.accept())!=null)
			{
				System.out.println("Connected ...");

				fr = new FileReceiver1(socket);
				fr.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

class FileReceiver1 extends Thread {
	DataInputStream dis;
	FileOutputStream fos;
	BufferedOutputStream bos;

	Properties proper;
	Socket socket;

	public FileReceiver1(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			proper=System.getProperties();
			System.out.println("Start file trasfer ...");
			dis = new DataInputStream(socket.getInputStream());

			String fName = dis.readUTF();

			File f1=new File(fName);
			String temp=f1.getName();
			
			new File("C:\\DynamicPresentation").mkdir();
			
			File f=new File("C:\\DynamicPresentation\\",temp);
			
			
			fos = new FileOutputStream(f);
			bos = new BufferedOutputStream(fos);
			System.out.println("C:\\DynamicPresentation\\"+temp + " is created ...");

			int len;
			int size = 4096;
			byte[] data = new byte[size];
			while ((len = dis.read(data)) != -1) {
				bos.write(data, 0, len);
			}

			bos.flush();
			bos.close();
			fos.close();
			dis.close();
			System.out.println("Success!!!");
			System.out.println("Size of file : " + f.length());

			openDirectory();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//How I execute a received file?
	public void openDirectory()
	{
		proper=System.getProperties();
		try {
			//Desktop.getDesktop().open(new File("C:\\Users\\"+proper.getProperty("user.name")+"\\Desktop"));
			Desktop.getDesktop().open(new File("C:\\DynamicPresentation\\"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}