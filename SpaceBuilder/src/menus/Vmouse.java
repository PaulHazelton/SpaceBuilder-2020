package menus;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import fileHandling.Reader;
import framework.Artist;
import framework.InputEvent;
import framework.InputHandler;
import utility.ImageManip;
import utility.IntRec;

public class Vmouse extends ButtonGroup
{
	private IntRec box;
	
	private static String root = "buttons/";
	private static BufferedImage mImg;
	private IntRec mBox;

	//Buttons
	private MouseButton wheel;
	
	
	//Delay for scroll light up
	private double time = 0;
	
	public static void fetchImages()
	{
		mImg = Reader.loadImage(root + "mouse"  + ".png");
		ImageManip.tintTransparency(mImg, 0x80000000);
	}
	
	public Vmouse(ControlPanel p, int x, int y)
	{
		super(p, x, y);

		MouseButton l;
		l = new MouseButton(this, "left", InputHandler.LEFT_CLICK_CODE, 0, 0, 4, 10, Command.NO_COMMAND);
		l.setIbox(0, 1, 4, 4);
		l.setCommands(Command.NOTHING_MODE, Command.NO_COMMAND);
		l.setCommands(Command.BUILD_MODE, Command.ADD_BLOCK, Command.ADD_BODY);
		this.buttons.add(l);
		
		l = new MouseButton(this, "right", InputHandler.RIGHT_CLICK_CODE, 10, 0, 4, 10, Command.NO_COMMAND);
		l.setIbox(10, 1, 4, 4);
		l.setCommands(Command.NOTHING_MODE, Command.NO_COMMAND);
		l.setCommands(Command.BUILD_MODE, Command.REMOVE_BLOCK, Command.REMOVE_BODY);
		this.buttons.add(l);
		
		this.wheel = new MouseButton(this, "wheel", InputHandler.MIDDLE_CLICK_CODE, 5, 0, 4, 6, Command.ZOOM);
		this.wheel.setIbox(5, 1, 4, 4);
		this.wheel.setAllCommands(Command.HOT_SCROLL, Command.ZOOM);
		this.buttons.add(this.wheel);
		
		this.resize();
	}
	
	@Override
	public void resize()
	{
		int s = ControlPanel.getScale();
		this.box = new IntRec(controlPanel.xMargin + x*s, controlPanel.yMargin + y*s, 14*s, 20*s);
		mBox = new IntRec(box.x, box.y + 7*s, 14*s, 13*s);

		super.resize();
	}
	
	public void inputEvent(InputEvent item)
	{
		for (HUDButton b : this.buttons)
		{
			b.inputEvent(item);
		}
	}

	void scroll(int n)
	{
		if (n != 0)
		{
			this.controlPanel.scrollCommand(n, this.wheel.com);
			this.time = 0;
			this.wheel.highlight();
		}
	}
	
	public void shift()
	{
		for (HUDButton b : this.buttons)
		{
			b.shift();
		}
	}
	
	public void update(double timePassed)
	{
		this.time += timePassed;
		if (this.time >= 0.1)
			this.wheel.unHighlight();
	}
	
	@Override
	public void render(Artist a)
	{
		Graphics2D g = a.getGraphics();
		g.drawImage(mImg, mBox.x, mBox.y, mBox.w, mBox.h, null);
		super.render(a);
	}
}
