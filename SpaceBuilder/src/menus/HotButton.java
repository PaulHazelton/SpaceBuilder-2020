package menus;

import java.awt.Color;
import java.awt.Graphics2D;

import framework.Artist;

public class HotButton extends HUDButton
{
	boolean active = false;
	
	HotBar bar;
	
	int hotSpot = 0;
	
	public HotButton(HotBar bar, String lable, int index, int x, int y, int w, int h, int xMarg, int yMarg, Command com, int spot)
	{
		super(bar, lable, index, x, y, w, h, xMarg, yMarg, com);
		this.bar = bar;
		
		this.hotSpot = spot;
	}
	
	@Override
	public void press()
	{
		super.press();
		this.active = true;
		this.bar.press(this);
	}
	
	@Override
	public void render(Artist a)
	{
		super.render(a);
		
		if (this.active)
		{
			int s = scale/3;
			
			Graphics2D g = a.getGraphics();
			g.setColor(Color.cyan);
			g.fillRect(box.x+s, box.y - scale+s, box.w-(2*s), s);
			g.fillRect(box.x+s, box.y + box.h+s, box.w-(2*s), s);	
		}
	}
}
