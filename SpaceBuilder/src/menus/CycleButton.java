package menus;

import utility.PMath;

public class CycleButton extends HUDButton
{
	private Command[] commands;
	private int i;
	
	public CycleButton(Commandable ch, String lable, int index, int x, int y, int w, int h, int xMarg, int yMarg,
			Command[] coms)
	{
		super(ch, lable, index, x, y, w, h, xMarg, yMarg, Command.NO_COMMAND);
		
//		this.setCommand(coms[0]);
		this.setAllCommands(coms[0]);
		
		this.commands = coms;
		this.i = 0;
	}
	
	@Override
	public void click()
	{
		//Cycle command
		if (Command.CTRL.active)
			i--;
		else
			i++;
		
		i = PMath.arrayMod(i, this.commands.length);
		this.setAllCommands(this.commands[i]);
		
		this.ch.tapCommand(this.com);
		this.ch.releaseCommand(this.com);
	}
}
