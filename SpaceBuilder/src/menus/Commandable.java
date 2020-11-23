package menus;

public interface Commandable
{
	public void tapCommand(Command c);
	public void pressCommand(Command c);
	public void releaseCommand(Command c);
}
