package pre.future.controller.mouse;

import java.awt.AWTException;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;

public class MouseActionManager {
	
	private static MouseActionManager mcs=null;
	Toolkit tk;
	 Image img;
	 Cursor myCursor;
	 Cursor normalCursor;
	 //UAutoItX advanceUIMethods;
	 Robot rob;
	private MouseActionManager()
	{
		  tk = Toolkit.getDefaultToolkit();
		  img = tk.getImage( "asd.gif" );
		  myCursor = tk.createCustomCursor( img, new Point(10,10), "dynamite stick" );
		  normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
		  try {
			rob = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  //advanceUIMethods = new UAutoItX();
	}
	
	public static MouseActionManager getInstance()
	{
		if(mcs==null)
			mcs=new MouseActionManager();
		
		return mcs;
	}
	
	public void clickMouse(String receivedMsg)
	{
		Robot rob;
		try {
			rob = new Robot();
			
			if(receivedMsg.equalsIgnoreCase("ml"))
			{
				System.out.println("Mouse Left Button Clicked");
				rob.mousePress(InputEvent.BUTTON1_MASK);
				rob.mouseRelease(InputEvent.BUTTON1_MASK);
			}
			else if(receivedMsg.equalsIgnoreCase("mr"))
			{
				System.out.println("Mouse Right Button Clicked");
				rob.mousePress(InputEvent.BUTTON3_MASK);
				rob.mouseRelease(InputEvent.BUTTON3_MASK);
			}
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
	}
	
	//Heejoon Added
	public void moveMouse(int x,int y)
	{
		rob.mouseMove(
				 MouseInfo.getPointerInfo().getLocation().x+x, 
				 MouseInfo.getPointerInfo().getLocation().y+y);
	}
	
	//Heejoon Modified
	public void moveMouseByMotion(int x, int y)
	{
		Robot rob;
		try {
			rob = new Robot();
			
			int start_x=MouseInfo.getPointerInfo().getLocation().x;
			int start_y=MouseInfo.getPointerInfo().getLocation().y;
			int end_x=x+MouseInfo.getPointerInfo().getLocation().x;
			int end_y=y+MouseInfo.getPointerInfo().getLocation().y;
			
			for (int i=0; i<100; i++){  
			    int mov_x = ((end_x * i)/100) + (start_x*(100-i)/100);
			    int mov_y = ((end_y * i)/100) + (start_y*(100-i)/100);
			    rob.mouseMove(mov_x,mov_y);
			}
	   } catch (AWTException e) {
			e.printStackTrace();
		}   	

	}
	
}