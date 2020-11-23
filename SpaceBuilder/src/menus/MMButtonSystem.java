package menus;

import java.awt.Color;
import java.awt.image.BufferedImage;

import fileHandling.Reader;
import framework.Artist;

public class MMButtonSystem extends ButtonSystem
{
	//Decoratives
	private BufferedImage backPanel;

	//MainMenu Stuff
//	private int gap;
	
	//Constructor
	public MMButtonSystem(int xMargin, int yMargin, int w, int h, int gap)
	{
		super(xMargin, yMargin, w, h);
//		this.gap = gap;
		this.backPanel = Reader.loadImage("blocks/blocks_station/rec_w4_h4_a0_d0.png");
	}
	
	//Game Loop stuff
	@Override
	public void render(Artist a)
	{
		a.drawImage(backPanel, this.xMargin + this.w/2, this.yMargin + this.w/2, this.w, this.w, 0, Color.cyan);
		super.render(a);
	}

	//ButtonSystem Methods
	public void addButton(Commandable ch, String label)
	{
//		this.buttons.add(new MMButton(ch, label,
//				xMargin + w/2,							//xPos
//				yMargin + h/2 + buttonCount*(h + gap),	//yPos
//				w, h, this.buttonCount));
//		
//		this.buttonCount++;
	}

	//TODO come on
	public void setControlling(boolean b)	{}
	public boolean getControlling()	{ return true; }

	@Override
	public void tapCommand(Command c)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pressCommand(Command c)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void releaseCommand(Command c)
	{
		// TODO Auto-generated method stub
		
	}
}
