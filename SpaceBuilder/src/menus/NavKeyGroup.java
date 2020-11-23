package menus;

public class NavKeyGroup extends ButtonGroup
{
	public NavKeyGroup(ControlPanel p, int x, int y)
	{
		super(p, x, y);
		
		this.buttons.add(new HUDButton(this, "Insert",	155,  0, 0, 4, 4, Command.NO_COMMAND));
		this.buttons.add(new HUDButton(this, "Delete",	127,  0, 5, 4, 4, Command.NO_COMMAND));
		this.buttons.add(new HUDButton(this, "Home",	 36,  5, 0, 4, 4, Command.NO_COMMAND));
		this.buttons.add(new HUDButton(this, "End",		 35,  5, 5, 4, 4, Command.NO_COMMAND));
		this.buttons.add(new HUDButton(this, "PgUp",	 33, 10, 0, 4, 4, Command.NO_COMMAND));
		this.buttons.add(new HUDButton(this, "PgDn",	 34, 10, 5, 4, 4, Command.NO_COMMAND));
		
		//Space
		this.buttons.add(new HUDButton(this, "Space",	32, 0, 10, 14, 4, Command.NO_COMMAND));
		
		//Enter
		this.buttons.add(new HUDButton(this, "Enter",	10, 15, 0, 14, 4, Command.BOARD));
		((HUDButton)this.buttons.get(this.buttons.size()-1)).setIbox(20, 0, 4, 4);
		
		//Arrow Keyss
		this.buttons.add(new HUDButton(this, "Left",	37, 15, 10, 4, 4, Command.NO_COMMAND));
		this.buttons.add(new HUDButton(this, "Up",		38, 20,  5, 4, 4, Command.SHOW));
		this.buttons.add(new HUDButton(this, "Right",	39, 25, 10, 4, 4, Command.NO_COMMAND));
		this.buttons.add(new HUDButton(this, "Down",	40, 20, 10, 4, 4, Command.HIDE));
	}
}
