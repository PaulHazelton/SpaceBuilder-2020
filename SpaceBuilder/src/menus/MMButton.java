package menus;

import java.awt.Color;
import java.awt.Font;

import org.jbox2d.common.Vec2;

import framework.Artist;
import framework.InputEvent;
import framework.InputHandler;
import utility.MidRecI;

public class MMButton extends Button
{
	//Colors
	private static Color B_boxColor = new Color(51, 51, 51);
	private static Color B_textColor = Color.cyan;
	
	private Color boxColor = MMButton.B_boxColor;
	private Color textColor = MMButton.B_textColor;
	
	//Animation stuff
	private MidRecI frontPanel;
	
	//Constructor
	public MMButton(Commandable ch, String lable, int x, int y, int w, int h, int index)
	{
		super(ch, lable, index, Command.NO_COMMAND, x, y, w, h);
		
		this.frontPanel = new MidRecI(x, y + 30, w, h - 60);
	}

	//Game loop stuff
	@Override
	public void inputEvent(InputEvent item)
	{
		switch(item.type)
		{
		case MOUSE_MOVED:
			
			this.containsMouse = this.midBox.containsPoint(item.x, item.y);
			
			if (this.containsMouse)
			{
//				this.frontPanel = new MidRecI(x, y + 30, w, h - 60);
				this.frontPanel.y = midBox.y - 30;
				this.textColor = MMButton.B_textColor;
			}
			else
			{
				this.frontPanel.y = midBox.y + 30;
				this.textColor = Color.BLACK;
			}
			
			break;
		case MOUSE_PRESSED:
			
			if (this.containsMouse)
				this.ch.tapCommand(this.com);
			
			break;
		default:	break;
		}
	}
	
	@Override
	public void update(double timePassed, InputHandler ih)
	{
		// TODO Auto-generated method stub
		
	}
	
	public void render(Artist a)
	{
		//Setup
		a.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 100));
		a.setStroke(0.1f);
		
		//Draw stuff
//		a.fillRec(box.x, box.y, box.w, box.h, 0, new Color(0xFF00FF));
		
		a.fillRec(frontPanel.x, frontPanel.y, frontPanel.w, frontPanel.h, 0, boxColor);
		a.drawTextCentered(label, new Vec2(frontPanel.x, frontPanel.y), 0, this.textColor);
	}

	//TODO
	public void setControlling(boolean b) {}
	public boolean getControlling()	{ return true; }
}
