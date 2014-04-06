package pre.future.connectionManager;

import gov.nist.jrtp.RtpException;
import gov.nist.jrtp.RtpManager;
import gov.nist.jrtp.RtpSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import pre.future.controller.keyboard.KeyboardActionManager;
import pre.future.controller.mouse.MouseActionManager;
import pre.future.pc.ui.PcClientUI;

public class PCConnectionManager extends Thread {
	int credential;
	ServerSocket serverSocket = null; // 서버 소켓
	Socket socket = null; // 바인드 된 실제 사용할 소켓

	PrintWriter writter; // 프린트 라이터 : 어떻게 사용할지는 모르겠지만 전송 할때 이용하는 객체
	BufferedReader reader; // 버퍼드 리더 : 소켓 버퍼의 내용을 읽을때, 즉 전송받은 내용을 읽을 때 사용

	// Shin
	PcClientUI ui = PcClientUI.getInstance();

	// Shin
	public Socket getSocket() {
		return socket;
	}

	public void run() {
		// Shin
		try {
			credential = ui.getCredential();
			System.out.println("credential: " + credential);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			RtpManager rtpManager = new RtpManager();
			RtpSession rtpSession = rtpManager.createRtpSession(10001);
			System.out.println("Create Rtp Session Success");
			rtpSession.receiveRTPPackets();// Start Receiving thread for RTP Packets.
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (RtpException e) {
			e.printStackTrace();
		}

		try {
			serverSocket = new ServerSocket(10000);
			System.out.println("Listening Start");

			if ((socket = serverSocket.accept()) != null) {
				System.out.println("Accept OK");
				ui.setConnectionState(true);
				// ui.frameDispose();
				ActionThread actionThread = new ActionThread(socket);
				actionThread.start();//여기에 새로운 스레드 생성
			}
		} catch (IOException e) {
			System.out.println("Connector : IOException in Constructor");
			e.printStackTrace();
		}		
	}
	
	public class ActionThread extends Thread{
		Socket socket;
		public ActionThread(Socket socket)
		{
			this.socket = socket;
		}
		@SuppressWarnings("deprecation")
		public void run()
		{
			try {
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				writter = new PrintWriter(socket.getOutputStream());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			System.out.println("Socket Thread Started");
			KeyboardActionManager keyboardActionManager = KeyboardActionManager	.getInstance();
			MouseActionManager mouseActionManager = MouseActionManager.getInstance();
			
			while (true) {
				String str;
				try {
					str = reader.readLine();// 소켓 버퍼를 통해 스트링을 전송 받아 읽음
					
					if (str == null)
						break;
					
					if (str.charAt(0) == 'c')// 인증키 확인
					{
						if (Integer.parseInt(str.substring(str.indexOf('c') + 1)) == credential) {
							writter.print("CredentialOK\n");
							writter.flush();
							SendHelloThread sendHelloThread = new SendHelloThread(
									socket);
							sendHelloThread.start();
							ui.setConnectionState(true);
							socket.setKeepAlive(true);
							socket.setSoTimeout(5000);
						} else {
							writter.print("CredentialNot\n");
							writter.flush();
							ui.setConnectionState(false);
						}
					} else if (str.charAt(0) == 'm')// 터치패드 바이 마우스, 마우스 포인터 원복
					{
						if (str.indexOf('x') != -1) {
							// 파싱 기능 x위치와 y위치를 정한 다음
							// x-y 사이 서브 스트링해서 x 위치 이동값으로, y-끝 을 서브스트링해서 y이동값으로

							int posX = str.indexOf('x');
							int posY = str.indexOf('y');

							mouseActionManager.moveMouse(Integer.parseInt(str.substring(posX + 1, posY)),
									Integer.parseInt(str.substring(posY + 1)));
							//System.out.println("diff x is "+ Integer.parseInt(str.substring(posX + 1, posY))
							//		+ "diff y is "+ Integer.parseInt(str.substring(posY + 1)));
						} else {
							mouseActionManager.clickMouse(str);
						}
					} else if (str.charAt(0) == 'n')// 모션 바이 마우스, 마우스 포인터 레이저로
					{					
						if (str.indexOf('x') != -1) {
							// 파싱 기능 x위치와 y위치를 정한 다음
							// x-y 사이 서브 스트링해서 x 위치 이동값으로, y-끝 을 서브스트링해서 y이동값으로

							int posX = str.indexOf('x');
							int posY = str.indexOf('y');
							
							mouseActionManager.moveMouseByMotion(Integer.parseInt(str.substring(posX + 1, posY)),
									Integer.parseInt(str.substring(posY + 1)));
							//System.out.println("diff x is "+ Integer.parseInt(str.substring(posX + 1, posY))
							//		+ "diff y is "+ Integer.parseInt(str.substring(posY + 1)));
						} else {
							mouseActionManager.clickMouse(str);
						}
					} else if (str.charAt(0) == 'k')// 키보드 관련 패킷
					{
						keyboardActionManager.keyPressed(str);
					} else {
						ui.setConnectionState(true);
						// System.out.print("Received Hello Packet!");
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					ui.setConnectionState(false);
					System.out.println("Connector : IOException");
					e.printStackTrace();
					stop();
				}
			}
		}
	}

	public class SendHelloThread extends Thread {
		Socket socket;
		public SendHelloThread(Socket socket) {
			this.socket = socket;
		}
		public void run() {
			while (true) {
				if (socket.isConnected()) {
					writter.print("hello\n");
					writter.flush();
					//
				}
				try {
					sleep(4 * 1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
