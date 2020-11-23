package menus;

import java.awt.Color;
import java.awt.Graphics2D;

//import building.Builder;
import framework.Artist;
import framework.InputEvent;
import framework.InputEvent.EventType;
import games.FreePlay;

public class ControlPanel extends ButtonSystem implements Commandable
{
	private static int scale = 10;
	FreePlay game;
	
	int screenWidth = 0;
	int screenHeight = 0;
	
	public boolean active = true;
	public Command mode = Command.NOTHING_MODE;

	private ButtonGroup tabGroup;
	private HotBar hotGroup;
	private ModGroup modGroup;
	private NavKeyGroup navGroup;
	private ButtonGroup letterGroup;
	private ButtonGroup numPad;
	
	private Vmouse vmouse;
	
	private boolean hidden = false;
	
	
	public static int getScale()	{ return scale; }
	
	
	public ControlPanel(int screenWidth, int screenHeight, FreePlay game)
	{
		super(0, 0, 0, 0);
		
		this.game = game;
		
		this.screenWidth  = screenWidth;
		this.screenHeight = screenHeight;
		
		//Tab
		this.tabGroup = new ButtonGroup(this, 25, 8);
		tabGroup.addButton(new CycleButton(this, "Tab", 9, 25,  2, 6, 4,
				this.xMargin, this.yMargin, new Command[]{
				Command.NOTHING_MODE, Command.BUILD_MODE}));//, Command.DIG_MODE, Command.PAINT_MODE});
		this.buttons.addAll(this.tabGroup.getButtons());
		
		//Modifier keys
		this.modGroup = new ModGroup(this, 25, 8);
		this.buttons.addAll(modGroup.getButtons());
		
		//Tool bar
		this.hotGroup = new HotBar(this, 33, 2);
		this.buttons.addAll(this.hotGroup.getButtons());
		
		//PgUp and arrow keys
		this.navGroup = new NavKeyGroup(this, 63, 8);
		this.buttons.addAll(this.navGroup.getButtons());
		
		//NumPad
		this.numPad = new ButtonGroup(this, 95, 2);
		
		
		//NumPad 1-9
		for (int c = 0; c < 3; c++) {
		for (int r = 1; r < 4; r++)
			numPad.addButton(new HUDButton(this, "Pad_" + Integer.toString(c*3+r), 96 + (c*3+r), -5 + r*5, 10 - c*5, 4, 4, Command.NO_COMMAND));
		}
		
		//NumPad 0
		numPad.addButton(new HUDButton(this, "Pad_0", 96, 0, 16, 9, 4, Command.NO_COMMAND));
		//NumPad .
		numPad.addButton(new HUDButton(this, "Pad_.", 110, 10, 16, 4, 4, Command.NO_COMMAND));
		
		this.buttons.addAll(this.numPad.getButtons());
		
		
		//Letters
		constructLetters();
		
		//Mouse
		this.vmouse = new Vmouse(this, 112, 2);
		
		//Resize after done
		this.resize(screenWidth, screenHeight);
	}

	private void constructLetters()
	{
		//Letter group
		this.letterGroup = new ButtonGroup(this, 33, 8);
		
		for (int j = 0; j < 3; j++) {
		for (int i = 0; i < 6; i++)
			this.letterGroup.addButton(new HUDButton(this, Integer.toString(i)+","+Integer.toString(j),
					0, i*5, j*5, 4, 4, Command.NO_COMMAND));
		}
		this.buttons.addAll(this.letterGroup.getButtons());

		//Set 18 letter details
		int i = this.buttons.size() - 18;
		
		this.buttons.get(i).setLabel("Q");	this.buttons.get(i).setIndex(81);
		this.buttons.get(i).setCommands(Command.NOTHING_MODE, Command.ROTATE_LEFT_A);
		this.buttons.get(i).setCommands(Command.BUILD_MODE, Command.ROTATE_LEFT_B);
		i++;
		this.buttons.get(i).setLabel("W");	this.buttons.get(i).setIndex(87);
		this.buttons.get(i).setCommand(Command.JUMP);
		this.buttons.get(i).setCommands(Command.BUILD_MODE, Command.HEIGHT_UP);
		i++;
		this.buttons.get(i).setLabel("E");	this.buttons.get(i).setIndex(69);
		this.buttons.get(i).setCommands(Command.NOTHING_MODE, Command.ROTATE_RIGHT_A);
		this.buttons.get(i).setCommands(Command.BUILD_MODE, Command.ROTATE_RIGHT_B);
		i++;
		
		this.buttons.get(i).setLabel("R");	this.buttons.get(i).setIndex(82);	i++;
		this.buttons.get(i).setLabel("T");	this.buttons.get(i).setIndex(84);	i++;
		
		this.buttons.set(i, new CycleButton(this, "Y", 89,
				(33+ 5*5), 8, 4, 4, this.xMargin, this.yMargin, new Command[] {Command.PLAY, Command.PAUSE}));
		i++;
		
		this.buttons.get(i).setLabel("A");	this.buttons.get(i).setIndex(65);
		this.buttons.get(i).setCommand(Command.WALK_LEFT);
		this.buttons.get(i).setCommands(Command.BUILD_MODE, Command.WIDTH_DOWN);i++;
		this.buttons.get(i).setLabel("S");	this.buttons.get(i).setIndex(83);
		this.buttons.get(i).setCommand(Command.CROUCH);
		this.buttons.get(i).setCommands(Command.BUILD_MODE, Command.HEIGHT_DOWN);i++;
		this.buttons.get(i).setLabel("D");	this.buttons.get(i).setIndex(68);
		this.buttons.get(i).setCommand(Command.WALK_RIGHT);
		this.buttons.get(i).setCommands(Command.BUILD_MODE, Command.WIDTH_UP);	i++;
		
		this.buttons.get(i).setLabel("F");	this.buttons.get(i).setIndex(70);	i++;
		this.buttons.get(i).setLabel("G");	this.buttons.get(i).setIndex(71);
		this.buttons.get(i).setCommand(Command.KEY_G);	i++;
		this.buttons.get(i).setLabel("H");	this.buttons.get(i).setIndex(72);	i++;
		
		this.buttons.get(i).setLabel("Z");	this.buttons.get(i).setIndex(90);
		this.buttons.get(i).setCommand(Command.DEBUG_RESET);					i++;
		this.buttons.get(i).setLabel("X");	this.buttons.get(i).setIndex(88);
		this.buttons.get(i).setCommand(Command.DEBUG_TOGGLE_DBDRAW);			i++;
		this.buttons.get(i).setLabel("C");	this.buttons.get(i).setIndex(67);
		this.buttons.get(i).setCommand(Command.DEBUG_TOGGLE_DRAW);				i++;
		
		this.buttons.get(i).setLabel("V");	this.buttons.get(i).setIndex(86);	i++;
		this.buttons.get(i).setLabel("B");	this.buttons.get(i).setIndex(66);	i++;
		this.buttons.get(i).setLabel("N");	this.buttons.get(i).setIndex(78);	i++;
	}
	
	private void resize(int sw, int sh)
	{
		this.screenWidth = sw;
		this.screenHeight = sh;
		
		ControlPanel.scale = screenWidth/128;
		ControlPanel.scale = scale / 3;	//if i wanna limit scale to a multiple of 3
		ControlPanel.scale = scale * 3;
		HUDButton.setScale(scale);
		this.w = scale*128;
		this.h = scale*24;
		
		if (hidden)
			this.h = scale*7;
		
		this.xMargin = (screenWidth - this.w)/2;
		this.yMargin = screenHeight - this.h;
		
		for (Button b : this.buttons)
		{
			((HUDButton)b).resize(this.xMargin, this.yMargin);
		}
		
		this.hotGroup.resize();
		this.modGroup.resize();
		this.navGroup.resize();
		this.letterGroup.resize();
		this.numPad.resize();
		this.vmouse.resize();
	}
	
	@Override
	public void inputEvent(InputEvent item)
	{
		//If window resized, resize Control Panel
		if (item.type == EventType.COMPONENT_RESIZED)
			this.resize(item.x, item.y);

		//Don't allow use of mouse when mouse in over control panel. TODO do this better
//		if (item.catagory == EventCatagory.MOUSE)
//		{
//			if (this.game.getInputTarget().getClass() == Builder.class)
//			{
//				boolean b = this.box.containsPoint(item.x, item.y); 
//				if (b && this.game.getInputTarget().getControlling())
//					this.game.getInputTarget().setControlling(false);
//				else if (!b && !this.game.getInputTarget().getControlling())
//					this.game.getInputTarget().setControlling(true);
//			}
//		}
		
		this.vmouse.inputEvent(item);
		
		//TODO Only send input events if button is near mouse event
		super.inputEvent(item);
	}

	@Override
	public void setControlling(boolean b)
	{
		this.active = b;
	}
	public boolean getControlling()	{ return this.active; }
	
	public void update(double timePassed)
	{
		this.vmouse.update(timePassed);
	}
	public void render(Artist a)
	{
		Graphics2D g = a.getGraphics();
		
		//Gray backPanel
//		g.setColor(new Color(0x666666));
		g.setColor(new Color(0xff, 0xff, 0xff, 128));
		g.fillRect(xMargin, yMargin, w, h);
		
		//Black interior panels
		g.setColor(new Color(0, 0, 0, 0xC0));
		g.fillRect(xMargin + (  1*scale), yMargin + (1*scale), 22*scale, 22*scale);
		g.fillRect(xMargin + ( 24*scale), yMargin + (1*scale), 69*scale, 22*scale);
		g.fillRect(xMargin + ( 94*scale), yMargin + (1*scale), 16*scale, 22*scale);
		g.fillRect(xMargin + (111*scale), yMargin + (1*scale), 16*scale, 22*scale);
		
		//Mouse stuff
		this.vmouse.render(a);
		
		super.render(a);
	}

	public void tapCommand(Command c)
	{
		this.game.tapCommand(c);
	}
	public void pressCommand(Command c)
	{
		if (c.catagory() == CommCatagory.UI)
		{
			if (c == Command.CAPS)
			{	
				((ModButton)this.buttons.get(2)).setInverting(!Command.CAPS.active);
			}
			
			if (c == Command.CTRL)
			{
				for (HUDButton b : this.buttons)
					b.shift();
				this.vmouse.shift();
			}
			
			if (c == Command.HIDE && !this.hidden)
			{
				this.hidden = true;
				this.resize(screenWidth, screenHeight);
			}
			else if (c == Command.SHOW && this.hidden)
			{
				this.hidden = false;
				this.resize(screenWidth, screenHeight);
			}
		}
		else
		{
			this.game.pressCommand(c);
			c.setActive(true);
		}
	}
	public void releaseCommand(Command c)
	{
		if (c.catagory() == CommCatagory.UI)
		{
			if (c == Command.CTRL)
			{
				for (HUDButton b : this.buttons)
					b.shift();
				this.vmouse.shift();
			}
		}
		else if (c.catagory() == CommCatagory.MODE)
		{
			this.setMode(c);
		}
		else
		{
			this.game.releaseCommand(c);
			c.setActive(false);
		}
	}
	public void scrollCommand(int n, Command c)
	{
		if (c == Command.HOT_SCROLL)
			this.hotGroup.scroll(-n);
		else
			this.game.scrollCommand(n, c);
	}

	public void setMode(Command mode)
	{
		if (mode.catagory() != CommCatagory.MODE)
			return;
		
		this.mode = mode;
		HUDButton.setMode(mode);
		
		for (HUDButton b : this.buttons)
			b.shift();
		this.vmouse.shift();
		

		game.setMode(mode);
	}
	public Command getMode()	{ return this.mode; }
	public boolean getHidden()	{ return this.hidden; }
	
	public Command pollHotBar() { return this.hotGroup.poll(); }
}
