package pre.future.controller.keyboard;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

public class KeyboardActionManager {

	private static KeyboardActionManager kcs=null;
	
	private KeyboardActionManager()
	{
		
	}
	
	public static KeyboardActionManager getInstance()
	{
		if(kcs==null)
			kcs=new KeyboardActionManager();
		
		return kcs;
	}
	
	public void keyPressed(String receivedMsg) 
	{	
		Robot rob;
		try {
			rob = new Robot();
			if(receivedMsg.equalsIgnoreCase("kl"))
			{
				System.out.println("Keyboard Left Key Pressed");
				rob.keyPress(KeyEvent.VK_LEFT);
			}
			else if(receivedMsg.equalsIgnoreCase("kr"))
			{
				System.out.println("Keyboard Right Key Pressed");
				rob.keyPress(KeyEvent.VK_RIGHT);
			}
			else if(receivedMsg.equalsIgnoreCase("kf5"))
			{
				rob.keyPress(KeyEvent.VK_F5);
			}
			else if(receivedMsg.equalsIgnoreCase("kesc"))
			{
				rob.keyPress(KeyEvent.VK_ESCAPE);
			}
			
			else
				return;
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
}
