package menus;

import framework.Artist;
import framework.InputEvent;
import framework.InputHandler;
import utility.Controllable;
import utility.IntRec;
import utility.MidRecI;

public abstract class Button implements Controllable
{
	protected Commandable ch;
	
	protected String label;
	protected int index = 0;
	protected Command com;
	
	protected MidRecI midBox = new MidRecI();
	protected IntRec box = new IntRec();
	protected boolean containsMouse = false;
	
	
	//Constructor
//	public Button(ClickHandler ch, String label, int x, int y, int w, int h, int index)
	public Button(Commandable ch, String label, int index, Command c, int x, int y, int w, int h)
	{
		this.ch = ch;
		this.label = label;
		this.index = index;
		this.com = c;
		
		this.box = new IntRec(x, y, w, h);
		
		this.midBox.x = x;
		this.midBox.y = y;
		this.midBox.w = w;
		this.midBox.h = h;
	}
	
	@Override
	public abstract void inputEvent(InputEvent item);

	@Override
	public abstract void update(double timePassed, InputHandler ih);
	
	public abstract void render(Artist a);
	
	void setIndex(int i)	{this.index = i; }
	void setLabel(String l)	{this.label = l; }
	void setCommand(Command c)	{this.com = c; }
	
	public String getLabel()	{ return this.label; }
	public int getIndex()		{ return this.index; }
	public Command getCommand()	{ return this.com; }
}