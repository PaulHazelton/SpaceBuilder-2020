package utility;

import framework.InputEvent;
import framework.InputHandler;

public interface Controllable
{
	public void inputEvent(InputEvent item);
	public void update(double timePassed, InputHandler ih);
	
	public void setControlling(boolean b);
	public boolean getControlling();
}