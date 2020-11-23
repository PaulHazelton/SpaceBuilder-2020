package menus;

import java.util.ArrayList;

import framework.Artist;

public class ButtonGroup implements Commandable
{
	protected ControlPanel controlPanel;
	protected ArrayList<HUDButton> buttons;
	
	protected int x;
	protected int y;
	
	public ButtonGroup(ControlPanel p, int x, int y)
	{
		this.controlPanel = p;
		this.x = x;
		this.y = y;
		
		this.buttons = new ArrayList<HUDButton>();
	}
	
	public void addButton(HUDButton b)
	{
		this.buttons.add(b);
		b.resize(controlPanel.xMargin + x*ControlPanel.getScale(), controlPanel.yMargin + y*ControlPanel.getScale());
	}
	
	public void resize()
	{
		for (HUDButton b : this.buttons)
			b.resize(controlPanel.xMargin + x*ControlPanel.getScale(), controlPanel.yMargin + y*ControlPanel.getScale());
	}

	public ArrayList<HUDButton> getButtons()	{ return this.buttons; }
	
	@Override
	public void tapCommand(Command c)
	{
		this.controlPanel.tapCommand(c);
	}
	@Override
	public void pressCommand(Command c)
	{
		this.controlPanel.pressCommand(c);
	}
	@Override
	public void releaseCommand(Command c)
	{
		this.controlPanel.releaseCommand(c);
	}

	public void render(Artist a)
	{
		for (HUDButton b : this.buttons)
		{
			b.render(a);
		}
	}
}
