package menus;

import java.awt.Color;
import java.awt.image.BufferedImage;

import fileHandling.Reader;
import utility.ImageManip;

public enum Command
{
	NO_COMMAND			(CommCatagory.MISC),
	DEBUG_RESET			(CommCatagory.DEBUG, "debug_reset"),
	DEBUG_TOGGLE_DRAW	(CommCatagory.DEBUG, "debug_drawFill"),
	DEBUG_TOGGLE_DBDRAW	(CommCatagory.DEBUG, "debug_drawBox"),
	
	CAPS				(CommCatagory.UI),
	CTRL				(CommCatagory.UI),
	HOT_SCROLL			(CommCatagory.UI, "hotScroll"),
	SHOW				(CommCatagory.UI, "show"),
	HIDE				(CommCatagory.UI, "hide"),
	
	PAUSE				(CommCatagory.GAME, "play"),	//<-- yeah this is dumb
	PLAY				(CommCatagory.GAME, "pause"),	// it's because it's a toggle
	ZOOM				(CommCatagory.GAME, "camera"),
	
	NOTHING_MODE		(CommCatagory.MODE, "neutral",	0),
	BUILD_MODE			(CommCatagory.MODE, "build",	1),
	DIG_MODE			(CommCatagory.MODE, "dig",		2),
	PAINT_MODE			(CommCatagory.MODE, "brush",	3),
	
	WALK_LEFT			(CommCatagory.PLAYER, "walkLeft"),
	WALK_RIGHT			(CommCatagory.PLAYER, "walkRight"),
	ROTATE_LEFT_A		(CommCatagory.PLAYER, "rotateLeft_A"),
	ROTATE_RIGHT_A		(CommCatagory.PLAYER, "rotateRight_A"),
	JUMP				(CommCatagory.PLAYER, "jump"),
	CROUCH				(CommCatagory.PLAYER, "crouch"),
	BOARD				(CommCatagory.PLAYER, "board"),
	STOP				(CommCatagory.PLAYER, "slow"),
	
	ROTATE_LEFT_B		(CommCatagory.BUILDER, "rotateLeft_B"),
	ROTATE_RIGHT_B		(CommCatagory.BUILDER, "rotateRight_B"),
	
	WIDTH_UP			(CommCatagory.BUILDER, "widthUp"),
	WIDTH_DOWN			(CommCatagory.BUILDER, "widthDown"),
	HEIGHT_UP			(CommCatagory.BUILDER, "heightUp"),
	HEIGHT_DOWN			(CommCatagory.BUILDER, "heightDown"),
	
	ADD_BLOCK			(CommCatagory.BUILDER, "addBlock"),
	REMOVE_BLOCK		(CommCatagory.BUILDER, "removeBlock"),
	ADD_BODY			(CommCatagory.BUILDER, "addBody"),
	REMOVE_BODY			(CommCatagory.BUILDER, "removeBody"),
	
	//Blocks
	EMPTY_HAND			(CommCatagory.BLOCK),
	HULL_REC			(CommCatagory.BLOCK, 1, 1, "hullRec"),
	HULL_TRI			(CommCatagory.BLOCK, 1, 1, "hullTri"),
	
	THRUSTER			(CommCatagory.BLOCK, 2, 1, "fixedChemThruster"),
	THRUSTER_ROTOR		(CommCatagory.BLOCK, 1, 1, "rotorChemThruster"),
	GYRO				(CommCatagory.BLOCK, 1, 1, "gyro"),
	
	TANK				(CommCatagory.BLOCK, 2, 1, "tank"),
	CONTAINER			(CommCatagory.BLOCK, 2, 1, "container"),
	
	BATTERY				(CommCatagory.BLOCK, 2, 1, "battery"),
	GENERATOR			(CommCatagory.BLOCK, 2, 1, "generator"),
	SOLAR_BLOCK			(CommCatagory.BLOCK, 2, 1, "solarBlock"),
	
	CHAIR				(CommCatagory.BLOCK, 3, 3, "chair"),
	
	// Generic Commands
	KEY_G				(CommCatagory.KEY)
	;
	private CommCatagory cat;
	private String fname = "";
	public BufferedImage image;
	public boolean active = false;
	public int index = -1;
	
	public int w;
	public int h;
	
 	Command(CommCatagory c)
	{
		this.cat = c;
	}
	Command(CommCatagory c, String name)
	{
		this.cat = c;
		this.fname = name;
		
		if (c != CommCatagory.BLOCK)
		{
			this.image = Reader.loadImage("buttons/" + getName() + ".png");
			this.switchColor();	
		}
		else
		{
			this.image = Reader.loadImage("buttons/blocks/" + getName() + ".png");
		}
	}
 	Command(CommCatagory c, int w, int h, String name)
 	{
		this.cat = c;
		this.w = w;
		this.h = h;
		this.fname = name;
		
		if (c == CommCatagory.BLOCK)
			this.image = Reader.loadImage("buttons/blocks/" + getName() + ".png");
 	}
	Command(CommCatagory c, String name, int index)
	{
		this.cat = c;
		this.index = index;
		this.fname = name;
		
		this.image = Reader.loadImage("buttons/" + getName() + ".png");
		this.switchColor();
	}
	
	private void switchColor()
	{
		switch(this.catagory())
		{
		case MISC:		this.setColor(new Color(0xffffff));	break;
		case DEBUG:		this.setColor(new Color(0xffff00));	break;
		case GAME:		this.setColor(new Color(0xffffff));	break;
		case BUILDER:	this.setColor(new Color(0xff8300));	break;
		case PLAYER:	this.setColor(new Color(0x00ff00));	break;
		case SPACESHIP:	this.setColor(new Color(0x40d0f0));	break;
		default:	break;
		}
	}
	private void setColor(Color c)
	{
		if (this.image != null)
			ImageManip.tintWhite(this.image, c.getRGB());
	}
	
	public CommCatagory catagory()	{ return this.cat; }
	public String getName()			{ return this.fname; }
	
	public void setActive(boolean a)	{ this.active = a; }
	public boolean isActive()			{ return this.active; }
}
