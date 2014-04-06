package pre.future.mobile.file;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

import pre.future.connector.DeviceConnectionManager;

public class DocumentTransferController {
	public void sendFile(String filepath) {
		String serverIp = DeviceConnectionManager.getInstance().getPCAddress();
		Socket socket = null;

		try {
			// 서버 연결
			socket = new Socket(serverIp, 10002);
			FileSender fs = new FileSender(socket);
			fs.setFilePath(filepath);
			//fs.start();
			
			//스레드 대신 일반 함수로 바꿀게요 
			fs.run();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

class FileSender extends Thread {
	Socket socket;
	DataOutputStream dos;
	FileInputStream fis;
	BufferedInputStream bis;
	
	String filepath;

	public FileSender(Socket socket) {
		this.socket = socket;
		try {
			// 데이터 전송용 스트림 생성
			dos = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setFilePath(String aFilepath) {
		filepath = aFilepath;
	}
	
	@Override
	public void run() {
		try {
			System.out.println("파일 전송 작업을 시작합니다.");

			dos.writeUTF(filepath);
			System.out.printf("파일 이름(%s)을 전송하였습니다.\n", filepath);

			// 파일 내용을 읽으면서 전송
			File f = new File(filepath);
			fis = new FileInputStream(f);
			bis = new BufferedInputStream(fis);

			int len;
			int size = 4096;
			byte[] data = new byte[size];
			while ((len = bis.read(data)) != -1) {
				dos.write(data, 0, len);
			}

			dos.flush();
			dos.close();
			bis.close();
			fis.close();
			System.out.println("파일 전송 작업을 완료하였습니다.");
			System.out.println("보낸 파일의 사이즈 : " + f.length());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
