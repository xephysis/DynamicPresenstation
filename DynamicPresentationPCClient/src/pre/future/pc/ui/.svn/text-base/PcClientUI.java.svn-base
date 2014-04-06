package pre.future.pc.ui;


import java.awt.Color;
import java.awt.Cursor;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;




public class PcClientUI {
	private JFrame frame;
	private JTextField titleIp;
	private JTextField titleNo;
	private JTextField contentIp;
	private JTextField contentNo;
	
	private JLabel stateDisplay;
	private TrayIconApp m_trayIconApp=TrayIconApp.getInstance();
	
	private boolean state=false;
	private static int credential;
	
	private static PcClientUI instance=null;
	
	
	/**
	 * 
	 * @return
	 */
	public static PcClientUI getInstance()
	{
		if(instance==null)
		{
			instance=new PcClientUI();
			System.out.println("getIntance()");
		}else
			System.out.println("Already existed");

		return instance;
	}

	/**
	 * Create the application.
	 */	
	private PcClientUI() {
		
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setSize(310,140);
		frame.setTitle("Dynamic Presentation");
		frame.setVisible(true);

		stateDisplay = new JLabel();
		displayConnectionState(state);		
		
		systemTray(frame);

		titleIp = new JTextField();
		titleIp.setText("IP");
		titleIp.setBounds(35, 32, 32, 21);
		frame.getContentPane().add(titleIp);
		titleIp.setColumns(10);
		titleIp.setEditable(false);

		titleNo = new JTextField();
		titleNo.setText("No.");
		titleNo.setBounds(35, 63, 32, 21);
		frame.getContentPane().add(titleNo);
		titleNo.setColumns(10);
		titleNo.setEditable(false);

		contentIp = new JTextField();
		contentIp.setBounds(79, 32, 116, 21);
		frame.getContentPane().add(contentIp);
		contentIp.setColumns(10);
		contentIp.setEditable(false);

		getAddress();

		contentNo = new JTextField();
		contentNo.setBounds(79, 63, 116, 21);
		frame.getContentPane().add(contentNo);
		contentNo.setColumns(10);
		contentNo.setEditable(false);

		credential=createRandomNum();
		System.out.println("credential in PcClientUI: "+credential);

		contentNo.setText(Integer.toString(this.getCredential()));
	}

	/**
	 * 
	 * @param state
	 */
	public void displayConnectionState(boolean state){
		
		if(state==false){
			stateDisplay.setText("| Disconnected |");
			stateDisplay.setForeground(Color.RED);
		}
		else
		{
			stateDisplay.setText("| Connected |");
			stateDisplay.setForeground(Color.GREEN);
		}
		stateDisplay.setBounds(35,7,116,21);
		frame.getContentPane().add(stateDisplay);
	}
	
	/**
	 * 
	 * @return
	 */
	public int createRandomNum(){
		int ranNum=(int)(Math.random()*9999+1);
		if(ranNum<1000)
		{
			System.out.println(ranNum);
			int num=(int)(Math.random()*9+1)*1000;
			ranNum+=num;
		}
		return ranNum;
	}
	
	
	//Heejoon Modify
	
	/**
	 * 
	 */
	public void getAddress()
	{
		String ipAddr = "";		
		
		try {
			Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); 
			while(en.hasMoreElements()) {
				NetworkInterface interf = en.nextElement();
				Enumeration<InetAddress> ips = interf.getInetAddresses();
				while (ips.hasMoreElements()) {
					InetAddress inetAddress = ips.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						if(!inetAddress.getHostAddress().toString().contains(":")){
							System.out.println(inetAddress.getHostAddress());
							ipAddr+=inetAddress.getHostAddress()+" ";
						}
					}
				}
			}
		} catch (SocketException ex) {
		}
		  
		contentIp.setText(ipAddr);
		/*
		InetAddress addr;
		try {
			addr = InetAddress.getLocalHost();
			String ipAddr = addr.getHostAddress();
			contentIp.setText(ipAddr);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}
	
	/**
	 * 
	 * @return
	 */
	public int getCredential()
	{
		return credential;
	}
	
	/**
	 * 
	 * @param connectionState
	 */
	public void setConnectionState(boolean connectionState)
	{
		this.state=connectionState;
		displayConnectionState(state);
	}
	
	/**
	 * Frame Minimalization
	 */
	public void setFrameMinimalized()
	{
		frame.setState(Frame.ICONIFIED);
	}	
	
	/**
	 * Frame Close(not remining on the TASKBAR)
	 */
	public void frameDispose()
	{
		frame.dispose();
	}
	
	public void systemTray(JFrame frame)
	{
		m_trayIconApp.initTray(frame);
	}
	
	public void exitApp()
    {
		frame.dispose();
		System.exit(0);
	}
	
}
