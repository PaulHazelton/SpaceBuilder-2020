package menus;

import java.util.ArrayList;

import framework.Artist;
import framework.InputEvent;
import framework.InputHandler;
import utility.Controllable;

public abstract class ButtonSystem implements Commandable, Controllable
{
	//Collection of buttons
	protected ArrayList<HUDButton> buttons = new ArrayList<HUDButton>();
	protected int buttonCount = 0;
	
	//Position Data
	protected int xMargin;
	protected int yMargin;
	protected int w;
	protected int h;
	
	//Constructor
	public ButtonSystem(int xMargin, int yMargin, int w, int h)
	{
		this.xMargin = xMargin;
		this.yMargin = yMargin;
		this.w = w;
		this.h = h;
	}
	
	//Game Loop Methods
	@Override
	public void inputEvent(InputEvent item)
	{
		for (Button b : this.buttons)
		{
			b.inputEvent(item);
		}
	}

	@Override
	public void update(double timePassed, InputHandler ih)
	{
		for (Button b : this.buttons)
		{
			b.update(timePassed, ih);
		}
	}
	
	public void render(Artist a)
	{
		for (Button b : this.buttons)
		{
			b.render(a);
		}
	}
}