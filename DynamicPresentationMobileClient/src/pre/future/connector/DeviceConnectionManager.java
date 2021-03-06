package pre.future.connector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import pre.future.mobile.MobileClientActivity;
import pre.future.mobile.sound.TransmitSoundController;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

/* 액션 받을 (태선이형이 넘길) 함수 or 클래스 생성 
 * 일단 이 클래스에서 하는 일 
 * 1. 액티비티가 가동되고 처음 이 객체가 생성되면 소켓연결을 통해 서버(PC클라이언트)에 접속
 * 2. 전송해야 할 액션을 받는다면 전송 
 * 
 * 1) 초기 소켓 연결을 위해 객체 생성자 or 초기화 하는 함수 필요
 * 2) 어떤 액션이 입력되면 바로 전송할 함수 
 * 3) 그러므로 소켓 버퍼는 전역(객체 변수)로 잡아서 객체 내부 메소드에서 접근하는게 필요
 * 4) 접근의 용이성을 위해 액션의 종류는 메소드로 나누어 놓고 이 클래스 내부에서 통합하여 전송 
 *     - 역시 마찬가지로 PC 클라이언트에서는 분류해야겠지
 * 5) 한 세션을 이용하기 때문에 데이터 구분을 위해 플래그 or 규격화한 패킷 종류 사용 
 * 
 */

public class DeviceConnectionManager extends Thread {
	public Socket clientSocket = null; // 연결에 사용할 소켓 객체
	PrintWriter writter = null;// 프린트 라이터 : 실제로 이 객체를 이용해서 패킷을 전송하고 플러시 해줌
	BufferedReader reader = null;// 버퍼드 리더 : 어떻게 사용할지 확신은 안서지만 소켓의 내용을 읽을 때 사용
	InetAddress pcAddress = null;
	Context context = null;

	public boolean connectionStatus = false;

	private static DeviceConnectionManager instance;

	public static DeviceConnectionManager getInstance() {
		if (instance == null) {
			instance = new DeviceConnectionManager();
		}
		return instance;
	}

	// 태선 생성
	public String getPCAddress() {
		return pcAddress.getHostAddress();
	}

	// 태선 생성
	public boolean getConnectionStatus() {
		return connectionStatus;
	}

	public String connect(Context rcvContext, String address, String credential) 
	{
		this.context = rcvContext;
		try {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);

			pcAddress = intToInetAddress(ipStringToInt(address));
			clientSocket = new Socket();
			clientSocket.connect(new InetSocketAddress(pcAddress.getHostAddress(), 10000),	5000);

			writter = new PrintWriter(clientSocket.getOutputStream());
			reader = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));

		} catch (UnknownHostException e) {
			System.out.println("ConnectorController : UnknownHostException in constructor");
			Log.i("DynamicProject","ConnectorController : UnknownHostException in constructor");
			e.printStackTrace();
			Intent intent = new Intent();
			intent.setAction("xep.ConnectionStatusReceiver");
			intent.putExtra("isUsable", false);
			connectionStatus = false;
			context.sendBroadcast(intent);
			return "Unknown Host";

		} catch (IOException e) {
			System.out	.println("ConnectorController : IOException in constructor");
			Log.i("DynamicProject","ConnectorController : IOException in constructor");
			e.printStackTrace();
			Intent intent = new Intent();
			intent.setAction("xep.ConnectionStatusReceiver");
			intent.putExtra("isUsable", false);
			connectionStatus = false;
			context.sendBroadcast(intent);
			return "Cannot Connection";
		}

		// 입력받은 Credential 전송
		writter.write('c' + credential + "\n");
		writter.flush();

		String str;
		try {
			str = reader.readLine();
			while (true) {
				if (str.contains("CredentialOK")) {
					// 인증에 성공했으므로 곧이어 여기서, VoIP세션 및 Hello Send Thread 시작
					TransmitSoundController deviceSoundActionManager = new TransmitSoundController();
					deviceSoundActionManager.transferVoiceInit();
					SendHelloThread sendHelloThread = new SendHelloThread(
							clientSocket);
					sendHelloThread.start();
					this.start();

					Intent intent = new Intent();
					intent.setAction("xep.ConnectionStatusReceiver");
					intent.putExtra("isUsable", true);
					connectionStatus = true;
					context.sendBroadcast(intent);
					return "CredentialOK";
				} else if (str.equalsIgnoreCase("CredentialNot")) {
					clientSocket.close();

					Intent intent = new Intent();
					intent.setAction("xep.ConnectionStatusReceiver");
					intent.putExtra("isUsable", false);
					connectionStatus = false;
					context.sendBroadcast(intent);
					return "CredentialNot";
				} else
					continue;
			}
		} catch (IOException e) {
			e.printStackTrace();

			Intent intent = new Intent();
			intent.setAction("xep.ConnectionStatusReceiver");
			intent.putExtra("isUsable", false);
			connectionStatus = false;
			context.sendBroadcast(intent);
			return "CredentialNot";
		}
	}// 함수 종료

	public void run() {
		try {
			clientSocket.setKeepAlive(true);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		try {
			clientSocket.setSoTimeout(5000);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		while (true) {
			String str;
			try {
				str = reader.readLine();
				Intent intent = new Intent();
				intent.setAction("xep.ConnectionStatusReceiver");
				intent.putExtra("isUsable", true);
				connectionStatus = true;
				context.sendBroadcast(intent);
			} catch (SocketException se) {
				// 서버 완전히 죽으면 여기서 걸림 이후 다시 접속 할 수 있도록 처리해 줘야할듯
				Intent intent = new Intent();
				intent.setAction("xep.ConnectionStatusReceiver");
				intent.putExtra("isUsable", false);
				connectionStatus = false;
				context.sendBroadcast(intent);
				clientSocket = null;
				
				System.exit(0);
				se.printStackTrace();
			} catch (NullPointerException ne) {
				ne.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.d("xephysis", "Timeout occured");
				Intent intent = new Intent();
				intent.setAction("xep.ConnectionStatusReceiver");
				intent.putExtra("isUsable", false);
				connectionStatus = false;
				context.sendBroadcast(intent);
				e.printStackTrace();
			}
		}
	}

	public void sendMouse(String input) {

		if (input.equalsIgnoreCase("left")) {
			writter.print("ml\n");
			writter.flush();
		}

		else if (input.equalsIgnoreCase("right")) {
			writter.print("mr\n");
			writter.flush();
		}
	}

	public void sendMouse(int x, int y) {
		writter.print("mx" + x + "y" + y + "\n");
		writter.flush();
	}

	// Added
	public void sendMouseByMotion(int x, int y) {
		writter.print("nx" + x + "y" + y + "\n");
		writter.flush();
	}

	public void sendKeyboard(String input) {
		Log.i("pre.future", "sendKeyboard is called");

		if (input.equalsIgnoreCase("left")) {
			writter.print("kl\n");
			writter.flush();
		}

		else if (input.equalsIgnoreCase("right")) {
			writter.print("kr\n");
			writter.flush();
		}
		
		else if ( input.equalsIgnoreCase("f5"))
		{
			writter.print("kf5\n");
			writter.flush();
		}
		else if ( input.equalsIgnoreCase("esc"))
		{
			writter.print("kesc\n");
			writter.flush();
		}
	}

	public static int ipStringToInt(String str) {
		int result = 0;
		String[] array = str.split("\\.");
		if (array.length != 4)
			return 0;
		try {
			result = Integer.parseInt(array[3]);
			result = (result << 8) + Integer.parseInt(array[2]);
			result = (result << 8) + Integer.parseInt(array[1]);
			result = (result << 8) + Integer.parseInt(array[0]);
		} catch (NumberFormatException e) {
			return 0;
		}
		return result;
	}

	public static InetAddress intToInetAddress(int hostAddress) {
		InetAddress inetAddress;
		byte[] addressBytes = { (byte) (0xff & hostAddress),
				(byte) (0xff & (hostAddress >> 8)),
				(byte) (0xff & (hostAddress >> 16)),
				(byte) (0xff & (hostAddress >> 24)) };

		try {
			inetAddress = InetAddress.getByAddress(addressBytes);
		} catch (UnknownHostException e) {
			return null;
		}
		return inetAddress;
	}

	public InetAddress getLocalIpAddress() {
		try {
			Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces();
			while (en.hasMoreElements()) {
				NetworkInterface interf = en.nextElement();
				Enumeration<InetAddress> ips = interf.getInetAddresses();
				while (ips.hasMoreElements()) {
					InetAddress inetAddress = ips.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						if (!inetAddress.getHostAddress().toString()
								.contains(":")) {
							return inetAddress;
						}
					}
				}
			}
		} catch (SocketException ex) {
			Log.e("Testing", ex.toString());
		}
		return null;
	}

	public class SendHelloThread extends Thread {
		Socket clientSocket;

		public SendHelloThread(Socket socket) {
			this.clientSocket = socket;
		}

		public void run() {
			while (true) {
				if (clientSocket.isConnected()) {
					writter.print("hello\n");
					writter.flush();
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
