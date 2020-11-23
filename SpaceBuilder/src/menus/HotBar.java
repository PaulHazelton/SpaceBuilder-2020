package menus;

import utility.PMath;

public class HotBar extends ButtonGroup
{
	private int activeIndex = 0;
	private HotButton activeButton = null;
	
	public HotBar(ControlPanel cp, int x, int y)
	{
		super(cp, x, y);
		
		this.addButton( 0, 49, Command.HULL_REC);
		this.addButton( 1, 50, Command.HULL_TRI);
		
		this.addButton( 2, 51, Command.THRUSTER);
		this.addButton( 3, 52, Command.THRUSTER_ROTOR);
		this.addButton( 4, 53, Command.GYRO);
		
		this.addButton( 5, 54, Command.TANK);
		this.addButton( 6, 55, Command.CONTAINER);
		
		this.addButton( 7, 56, Command.BATTERY);
		this.addButton( 8, 57, Command.GENERATOR);
		this.addButton( 9, 48, Command.SOLAR_BLOCK);
		
		this.addButton(10, 45, Command.CHAIR);
		this.addButton(11, 61, Command.EMPTY_HAND);
		
		((HotButton)this.buttons.get(0)).active = true;
		this.activeButton = (HotButton)this.buttons.get(0);
	}

	private void addButton(int i, int code, Command c)
	{
		HotButton b = new HotButton(this, "HotBar_" + Integer.toString(i), code, i*5, 0, 4, 4, x, y, Command.NO_COMMAND, i);
		b.setCommands(Command.BUILD_MODE, c);
		
		addButton(b);
	}
	
	public void press(Button src)
	{
		if (this.activeButton != src)
		{
			this.activeButton.active = false;
			this.activeButton.release();
			this.activeButton = (HotButton)src;
			this.activeIndex = this.activeButton.hotSpot;
		}
	}
	
	public void scroll(int n)
	{
		this.activeIndex -= n;
		this.activeIndex = PMath.arrayMod(this.activeIndex, this.buttons.size());
		
		this.buttons.get(this.activeIndex).press();
		this.buttons.get(this.activeIndex).release();
	}

	public Command poll()
	{
		return this.activeButton.com;
	}
}
