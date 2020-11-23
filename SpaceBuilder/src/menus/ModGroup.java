package menus;

public class ModGroup extends ButtonGroup
{

	public ModGroup(ControlPanel p, int x, int y)
	{
		super(p, x, y);
		
		// TODO remove commented code
		this.buttons.add(new ModButton(p, "CapsLock",	20, 0,  0, 6, 4, Command.CAPS,	true));
		ModButton shft = new ModButton(p, "Shift",		16, 0,  5, 6, 4, Command.STOP,	false);
		shft.setIbox(1, 5, 4, 4);
		this.buttons.add(shft);
		this.buttons.add(new ModButton(p, "Ctrl",		17, 0, 10, 6, 4, Command.CTRL,	false));
	}
	
//	@Override
//	public void pressCommand(Command c)
//	{
//		if (c == Command.CAPS)
//		{
//			((ModButton)this.buttons.get(1)).setInverting(Command.CAPS.active);
//		}
//		
//		super.pressCommand(c);
//	}
	
//	@Override
//	public void releaseCommand(Command c)
//	{
//		super.releaseCommand(c);
//	}
}
