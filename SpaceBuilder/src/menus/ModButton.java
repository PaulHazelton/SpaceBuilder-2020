package menus;

import java.awt.Color;
import java.awt.Graphics2D;

import framework.Artist;

public class ModButton extends HUDButton
{
	boolean inverting = false;
	boolean toggle = false;
	
	public ModButton(Commandable ch, String lable, int index, int x, int y, int w, int h, Command com, boolean toggle)
	{
		super(ch, lable, index, x, y, w, h, com);
		this.toggle = toggle;
		
		this.setAllCommands(com);
	}
	
	public void setInverting(boolean i)
	{
		this.inverting = i;
		com.active = !com.active;
	}
	
	@Override
	public void press()
	{
		if (!inverting && !toggle)
			com.active = true;
		
		super.press();
		
		if (this.toggle)
			com.active = !com.active;
		else
		{
			if (!inverting)
				com.active = true;
			else
				com.active = false;
		}
	}
	@Override
	public void release()
	{
		if (!inverting && !toggle)
			com.active = false;
		
		super.release();
		
		if (!toggle)
		{
			if (!inverting)
				com.active = false;
			else
				com.active = true;
		}
	}
	
	@Override
	public void render(Artist a)
	{
		super.render(a);
		
		// Draw the indicator
		if (com.active)
		{
			int s = scale/3;
			
			Graphics2D g = a.getGraphics();
			g.setColor(Color.cyan);
			g.fillRect(box.x+box.w-scale+s, box.y+s, s, box.h-(2*s));
		}
	}
}
