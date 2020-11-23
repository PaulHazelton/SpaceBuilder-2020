package framework;

import java.awt.AWTEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * An event that could be a variety of different different Input Events. This is what's sent to
 * {@link Game#inputEvent(InputEvent item)}
 * @see InputHandler
 * @author Paul Hazelton
 */
public class InputEvent
{
	//Framework communication
	private static Model model;
	
	/**
	 * Initialization for communication to the model.
	 * This should only be called once upon the program start by the Main Class. 
	 */
	protected static void initialize(Model model)
	{
		InputEvent.model = model;
	}
	
	/**
	 * The input event type. This is to help simplify input events by labeling the source as opposed to having a lot of seperate methods.
	 * @author Paul Hazelton
	 */
	public enum EventType
	{
		DUD,
		KEY_PRESSED, KEY_RELEASED, KEY_TYPED,
		MOUSE_PRESSED, MOUSE_RELEASED, MOUSE_CLICKED,
		MOUSE_MOVED, MOUSE_ENTERED, MOUSE_EXITED, MOUSE_DRAGGED,
		MOUSE_WHEEL_MOVED,
		COMPONENT_HIDDEN, COMPONENT_SHOWN, COMPONENT_RESIZED, COMPONENT_MOVED,
		WINDOW_OPENED, WINDOW_CLOSING, WINDOW_CLOSED, WINDOW_ICONIFIED, WINDOW_DEICONIFIED, WINDOW_ACTIVATED, WINDOW_DEACTIVATED
	}
	/**
	 * The input event catagory. Broad catagory of event. Keyboard, Mouse, MouseWheel, or Component.
	 * @author Paul Hazelton
	 */
	public enum EventCatagory
	{
		DUD, KEYBOARD, MOUSE, MOUSE_WHEEL, COMPONENT
	}
	
	/**
	 * @see EventType
	 */
	public EventType	type = EventType.DUD;
	/**
	 * @see EventCatagory
	 */	
	public EventCatagory catagory = EventCatagory.DUD;
	/**
	 * The event that was sent when the input event was triggered.
	 */
	public AWTEvent event = null;
	
	/**
	 * The character that was pressed/released/typed.
	 * <p>
	 * Warning: This will be null if the input event was not a keyboard event.
	 */
	public char		character;
	/**
	 * The key code that was pressed/released/typed.
	 * <p>
	 * Warning: This will be 0 if the input event was not a keyboard event.
	 */
	//TODO FINISH THIS
	public int		code;
	/**
	 * The x screen position of the mouse event.
	 * <p>
	 * Warning: This will be 0 if the input event was not a mouse event.
	 */
	public int		x;
	/**
	 * The y screen position of the mouse event.
	 * <p>
	 * Warning: This will be 0 if the input event was not a mouse event.
	 */
	public int		y;
	/**
	 * The button of the mouse event.
	 * <p>
	 * Warning: This will be 0 if the input event was not a mouse event.
	 */
	public int		button;
	/**
	 * The number of "ticks" that the mouse scroll wheel turned.
	 * <p>
	 * Warning: this will be 0 if the input event was not a mouse event.
	 */
	public int		wheelRotation;
	/**
	 * How much that the mouse scroll wheel turned.
	 * <p>
	 * Warning: this will be 0 if the input event was not a mouse event.
	 */
	public double	preciseWheelRotation;

	/**
	 * {@link InputEvent}s should only be created by the InputHandler class during an input event.
	 * @param type - the type of the event.
	 * @param e - the event object.
	 */
	InputEvent(EventType type, AWTEvent e)
	{
		if (model.allowEvents == false)
		{
			this.type = EventType.DUD;
			this.event = null;
		}
		
		else
		{
			this.type = type;
			this.event = e;
			
			if (e instanceof KeyEvent)
			{
				KeyEvent f = (KeyEvent)e;
				this.catagory = EventCatagory.KEYBOARD;
				this.character	= f.getKeyChar();
				this.code		= f.getKeyCode();
			}
			if (e instanceof MouseEvent)
			{
				MouseEvent f = (MouseEvent)e;
				this.catagory = EventCatagory.MOUSE;
				this.button		= f.getButton();
				this.x			= f.getX();
				this.y			= f.getY();
			}
			if (e instanceof MouseWheelEvent)
			{
				MouseWheelEvent f = (MouseWheelEvent)e;
				this.catagory = EventCatagory.MOUSE_WHEEL;
				this.button					= f.getButton();
				this.wheelRotation			= f.getWheelRotation();
				this.preciseWheelRotation	= f.getPreciseWheelRotation();
			}
			if (e instanceof ComponentEvent)
			{
				if (type == EventType.COMPONENT_MOVED)
				{
					this.catagory = EventCatagory.COMPONENT;
					this.x = model.frame.getX();
					this.y = model.frame.getY();
				}
				if (type == EventType.COMPONENT_RESIZED)
				{
					this.catagory = EventCatagory.COMPONENT;
					this.x = model.getWidth();
					this.y = model.getHeight();
				}
			}
		}
	}
}

