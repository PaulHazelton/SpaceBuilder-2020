package framework;

import org.jbox2d.common.Vec2;
import framework.InputEvent.EventType;

/**
 * This class should be extended to make your game.
 * Feel free make other game modes that extend the Game class.
 * @author Paul Hazelton
 */

public abstract class Game
{
	protected Model model;
	
	/**
	 * This class is where the good stuff begins. This class has all the necessary game loop methods. You can have multiple Game
	 * subcalsses but only one can run at a time.
	 * @param model - the model.
	 * @see framework.Model
	 */
	public Game(Model model)
	{
		this.model = model;
	}
	
	//All games must have these basic game loop methods
	/**
	 * This is called exactly once at the beginning of the game loop.
	 * The is method is where you should load your assets, construct the objects, and prepare for the game to start
	 */
	public abstract void initialize();

	/**
	 * update is called once every tick. Ticks per second is determined by model.setUPS();
	 * @param timePassed - the time in seconds that has passed since the begginning of the last tick.
	 */
	public abstract void update(double timePassed);
	/**
	 * This is called once for each input event on a tick just before {@link #update}. Input events are stored as {@link InputEvent} objects in a queue.
	 * the queue is then looped through on a tick just before {@link #update} which empties the queue.
	 * This is to avoid interuptions and possible (although unlikely) loss of input events.
	 * @param item - this holds all the data of the oldest unhandled input event. It holds some basic data like mouse position or keyChar.
	 * @see InputEvent
	 * @see #update
	 */
	public abstract void inputEvent(InputEvent item);
	/**
	 * redner is called once every frame (usually 60 times a second) and this is where you should do all of your rendering.
	 * @param artist - The game artist. 
	 * @see Artist
	 */
	public abstract void render(Artist artist);
	
	/**
	 * This will be called when the user clicks the 'X' button at the top of the frame.
	 * The program will terminate after this method is returned unless a -1 is returned.
	 * @return The status of the termination.
	 * <p>
	 * returning a 0 indicates a normal termination
	 * <p>
	 * returning a non zero indicates an abnormal termination
	 * <p>
	 * returning a -1 will indicate that the game should continue to loop after the the method returns.
	 */
	public abstract int  terminate();
	//This calls handleInput on the input handler on the tick which triggers inputEvents
	public void handleInput()
	{
		this.model.inputHandler.handleInput(this);
	}
	//This keeps the camera at the center of the frame
	public void superInputEvent(InputEvent item)
	{
		if (item.type == EventType.COMPONENT_RESIZED)
		{
			int x = this.model.frame.getContentPane().getWidth() / 2;
			int y = this.model.frame.getContentPane().getHeight() / 2;
			
			this.model.cam.setCenter(new Vec2(x, y));
		}
		
		this.inputEvent(item);
	}
}
