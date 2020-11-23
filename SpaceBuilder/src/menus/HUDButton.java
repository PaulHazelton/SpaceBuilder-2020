package menus;

import java.awt.Color;
import java.awt.Graphics2D;

import framework.Artist;
import framework.InputEvent;
import framework.InputHandler;
import utility.IntRec;

public class HUDButton extends Button
{
	//Scale for the whole thing
	protected static int scale = 15;
	
	//Draw location
	public IntRec unitBox;
	public int xMargin;
	public int yMargin;
	
	//Color of button part
	private Color color;
	public static Color baseColor		= new Color(0x00, 0x00, 0x00, 128);	//333333
	public static Color hoveredColor	= new Color(0x55, 0x55, 0x55, 128);	//666666
	public static Color clickedColor	= new Color(0x66, 0x66, 0x66, 128);	//999999
	
	//Button image
	protected IntRec iBox;
	private IntRec iUnitBox;
	private boolean wierdShape = false;
	
	//Button status
	private boolean active = true;
	private boolean hovered = false;
	protected boolean pressed = false;
	
	//Alternate Commands
	private static Command mode = Command.NOTHING_MODE;
	protected Command[][] commands;

	
	//Static
	public static void setScale(int scale)
	{
		HUDButton.scale = scale;
	}

	public static void setMode(Command mode)
	{
		if (mode.catagory() != CommCatagory.MODE)
			return;
		
		HUDButton.mode = mode;
	}
	
	//Constructor
	public HUDButton(Commandable ch, String lable, int index, int x, int y, int w, int h, Command com)
	{
		super(ch, lable, index, com, x*scale, y*scale, w*scale, h*scale);
		this.unitBox = new IntRec(x, y, w, h);
		
		finishConstruction(com);
	}
	public HUDButton(Commandable ch, String lable, int index, int x, int y, int w, int h, int xMarg, int yMarg, Command com)
	{
		super(ch, lable, index, com, x*scale, y*scale, w*scale, h*scale);
		this.unitBox = new IntRec(x, y, w, h);
		this.resize(xMarg, yMarg);
		
		finishConstruction(com);
	}
	private void finishConstruction(Command com)
	{
		this.commands = new Command[4][2];
		for (int i = 0; i < this.commands.length; i++) {
		for (int j = 0; j < this.commands[i].length; j++)
		{
			this.commands[i][j] = Command.NO_COMMAND;
		}}
		
		this.setCommand(com);
		this.updateColor();
	}

	//Set commands
	@Override
	public void setCommand(Command c)
	{
		this.commands[Command.NOTHING_MODE.index][0] = c;
		shift();
	}	
	public void setCommands(Command mode, Command base)
	{
		this.setCommands(mode, base, Command.NO_COMMAND);
	}
	public void setCommands(Command mode, Command base, Command ctrl)
	{
		if (mode.catagory() != CommCatagory.MODE)
			return;
		
		this.commands[mode.index][0] = base;
		this.commands[mode.index][1] = ctrl;
		
		shift();
	}
	public void setAllCommands(Command base, Command ctrl)
	{
		for (int i = 0; i < this.commands.length; i++)
		{
			this.commands[i][0] = base;
			this.commands[i][1] = ctrl;
		}
		
		shift();
	}
	public void setAllCommands(Command c)
	{
		for (int i = 0; i < this.commands.length; i++)
		{
			this.commands[i][0] = c;
			this.commands[i][1] = c;
		}
		
		shift();
	}
	
	public void shift()
	{
		if (Command.CTRL.active)
			this.com = this.commands[mode.index][1];
		else
			this.com = this.commands[mode.index][0];
	}
	
	//Positioning
	public void setIbox(int x, int y, int w, int h)
	{
		this.wierdShape = true;
		this.iUnitBox = new IntRec(x, y, w, h);
		this.iBox = new IntRec();
		this.resize(xMargin, yMargin);
	}
	public void resize(int xMarg, int yMarg)
	{
		this.xMargin = xMarg;
		this.yMargin = yMarg;
		this.box.x = (unitBox.x*scale) + xMargin;
		this.box.y = (unitBox.y*scale) + yMargin;
		this.box.w = (unitBox.w*scale);
		this.box.h = (unitBox.h*scale);

		if (this.wierdShape)
		{
			this.iBox.x = (iUnitBox.x*scale) + xMargin;
			this.iBox.y = (iUnitBox.y*scale) + yMargin;
			this.iBox.w = (iUnitBox.w*scale);
			this.iBox.h = (iUnitBox.h*scale);
		}
		else
			this.iBox = this.box;
		
		this.release();
		this.setHovered(false);
	}
	//Controlling interface
	@Override
	public void setControlling(boolean b)
	{
		this.active = b;
	}
	public boolean getControlling()	{ return this.active; }
	@Override
	public void inputEvent(InputEvent item)
	{
		if (!this.active)
			return;
		
		switch(item.type)
		{
		case MOUSE_MOVED:
			if (!this.pressed)
			{
				if (this.box.containsPoint(item.x, item.y))
					this.setHovered(true);
				else
					this.setHovered(false);
			}
			break;
		case MOUSE_DRAGGED:
			if (!(this.box.containsPoint(item.x, item.y) && this.pressed))
				this.release();
			break;
		case MOUSE_PRESSED:
			if (this.box.containsPoint(item.x, item.y))
				press();
			break;
		case MOUSE_RELEASED:
			if (this.box.containsPoint(item.x, item.y))
				release();
			break;
			
		case KEY_PRESSED:
			if (item.code == this.index)
				this.press();
			break;
		case KEY_RELEASED:
			if (item.code == this.index)
				this.release();
			break;
		default:	break;
		}
	}

	//Modify status and preform actions
	private void setHovered(boolean h)
	{
		this.hovered = h;
		this.updateColor();
	}
	protected void click()
	{
		this.ch.tapCommand(this.com);
		this.ch.releaseCommand(this.com);
	}
	protected void press()
	{
		this.pressed = true;
		this.updateColor();		
		this.ch.pressCommand(this.com);
	}
	protected void release()
	{
		if (this.pressed)
			this.click();
		this.pressed = false;
		this.setHovered(this.hovered);
	}

	private void updateColor()
	{
		if (!hovered && !pressed)
		{
			this.color = baseColor;
		}
		else if (pressed)
		{
			this.color = clickedColor;
		}
		else if (hovered)
		{
			this.color = hoveredColor;
		}
	}
	
	//Game loop stuff
	@Override
	public void update(double timePassed, InputHandler ih)
	{
		
	}

	@Override
	public void render(Artist a)
	{
		Graphics2D g = a.getGraphics();
		
		g.setColor(color);
		g.fillRect(box.x, box.y, box.w, box.h);
		
		if (com.image != null)
			g.drawImage(com.image, iBox.x, iBox.y, iBox.w, iBox.h, null);
	}
}
