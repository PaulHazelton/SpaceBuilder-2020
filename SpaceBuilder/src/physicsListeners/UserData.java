package physicsListeners;

import org.jbox2d.callbacks.ContactListener;

public class UserData
{
	ContactListener cListener;
	String name;
	int id;
	
	public UserData (ContactListener cl, String name)
	{
		this.cListener = cl;
		this.name = name;
	}
	public UserData (ContactListener cl, int id)
	{
		this.cListener = cl;
		this.id = id;
	}
}
