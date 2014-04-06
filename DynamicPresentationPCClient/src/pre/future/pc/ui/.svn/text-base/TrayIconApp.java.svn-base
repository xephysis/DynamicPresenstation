package pre.future.pc.ui;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;


public class TrayIconApp implements ActionListener
{
	static final String TRAYNAME="Dynamic Presentation";

	private SystemTray m_tray = SystemTray.getSystemTray();
	private TrayIcon m_ti;

	private JFrame m_frame;	
	
	private static TrayIconApp instance=null;

	/**
	 * 
	 * @return
	 */
	public static TrayIconApp getInstance()
	{
		if(instance==null)
		{
			instance=new TrayIconApp();
			System.out.println("getIntance()");
		}else
			System.out.println("Already existed");

		return instance;
	}
		
	public void initTray(JFrame frame)
	{
		this.m_frame=frame;
		Image image = Toolkit.getDefaultToolkit().getImage("images/duke.gif");

		m_ti = new TrayIcon(image, TRAYNAME, createPopupMenu());

		m_ti.setImageAutoSize(true);

		
		m_ti.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m_frame.setVisible(!m_frame.isVisible());
			}
		});


		try 
		{
			m_tray.add(m_ti);
		} 
		catch (AWTException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private PopupMenu createPopupMenu()
	{
		PopupMenu popupMenu = new PopupMenu("My Menu");

		MenuItem miShow = new MenuItem("Show TrayIconFrame");
		MenuItem miQuit = new MenuItem("Quit");
		miShow.addActionListener(this);
		miQuit.addActionListener(this);

		popupMenu.add(miShow);
		popupMenu.addSeparator();
		popupMenu.add(miQuit);

		return popupMenu;
	}

	public void actionPerformed(ActionEvent e)
	{
		if(e.getActionCommand() == "Show TrayIconFrame")
		{
			m_frame.setVisible(true);
		}
		else if(e.getActionCommand() == "Quit")
		{
			PcClientUI.getInstance().exitApp();
		}
	}
}
