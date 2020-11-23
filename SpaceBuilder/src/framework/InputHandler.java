package framework;

import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.jbox2d.common.Vec2;

import framework.InputEvent.EventType;

/**
 * This should not be instantiated more than once in the main method.
 * This handles keyboard input, mouse input, and window input.
 * Every input event adds an InputItem to a queue. On each game tick model.getGame().inputEvent() is called and sent and item.
 * @author Paul Hazelton
 */
public class InputHandler implements
	KeyListener,
	MouseListener,
	MouseMotionListener,
	MouseWheelListener,
	ComponentListener,
	WindowListener
{
	private Model model;
	
	private ConcurrentLinkedQueue<InputEvent> inputQueue = new ConcurrentLinkedQueue<InputEvent>();
	
	//Quick access keys
	private final boolean[] keyCodes = new boolean[512];
	
	//Special Keys
	public boolean UP		= false;
	public boolean DOWN		= false;
	public boolean LEFT		= false;
	public boolean RIGHT	= false;
	
	public boolean SPACE	= false;
	public boolean ENTER	= false;
	
	public boolean L_SHIFT	= false;
	public boolean R_SHIFT	= false;
	public boolean L_CTRL	= false;
	public boolean R_CTRL	= false;
	public boolean L_ALT	= false;
	public boolean R_ALT	= false;
	
	public boolean CAPS_LOCK	= false;
	
	//Quick access mouse buttons
	private final boolean[] mouseButtons = new boolean[3];
	
	/** Used for quick access. true if left click is currently pressed */
	public boolean LEFT_CLICK	= false;
	/** Used for quick access. true if middle click is currently pressed */
	public boolean MIDDLE_CLICK	= false;
	/** Used for quick access. true if right click is currently pressed */
	public boolean RIGHT_CLICK	= false;
	
	public static final int LEFT_CLICK_CODE		= 1;
	public static final int MIDDLE_CLICK_CODE	= 2;
	public static final int RIGHT_CLICK_CODE	= 3;
	
	private Vec2 mouseScreen;
	private Vec2 mouseWorld;
	
	//Constructor
	public InputHandler(Model model)
	{	
		this.model = model;
		
		this.CAPS_LOCK = Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK);
		
		mouseScreen	= new Vec2(0, 0);
		mouseWorld	= new Vec2(0, 0);
	}
	
	//Handling the queue
	/**
	 * @Warning This should only be called by the Controller class
	 * @param gameMode The game that will respond to the InputEvents
	 */
	public void handleInput(Game gameMode)
	{
		while (!inputQueue.isEmpty())
			gameMode.superInputEvent(inputQueue.poll());
	}
	
	//Getting keys
	/**
	 * This tells if a particular key is currentlly pressed down.
	 * for example: {@link getKey(KeyEvent.VK_A)} will be true if the 'A' key is down.
	 * The KeyCode array is meant to provide quick access to see if a key is down.
	 * @param keyCode - Sending a {@link KeyEvent.VK_X} is convinient and will usually work, however some vk keys are out of bounds.
	 * @throws IllegalArgumentException if the argument is >= 512
	 */
	public boolean getKey(int keyCode) throws IllegalArgumentException
	{
		if (keyCode < 0 || keyCode >= this.keyCodes.length)
			throw new IllegalArgumentException();
		
		return this.keyCodes[keyCode];
	}
	/**
	 * This tells if a key (represented by a character) is pressed down.
	 * @param key Can be any ascii letter or number literal in single quotes (ex. {@literal 'A'}).
	 * Lowercase letters will be automatically capitalized.
	 * Tab (\t) and space will also work. 
	 * @return true if the character is currently down
	 * @throws IllegalArgumentException if the argument is out of bounds of the keyCode array [0,512)
	 */
	public boolean getKey(char key) throws IllegalArgumentException
	{
		//Convert char to ascii code
		int keyInt = (int)key;
		
		//Reject argument if it is out of bounds
		if (keyInt < 0 || keyInt >= keyCodes.length)
			throw new IllegalArgumentException();
		
		//Capitalize letters
		if (keyInt >= 97 && keyInt <= 122)
			return getKey(keyInt - 32);

		//Return char converted to code
		return getKey(keyInt);
	}
	//Getting mouse stuff
	/**
	 * This tells if a mouse button is currentlly pressed down.
	 * @param button - the mouse button.
	 * <p> 0 - Left button usually <p> 1 - Middle button usually <p> 2 - Right button usually
	 * @return true if the button is currently down.
	 * @throws IllegalArgumentException
	 * @see
	 * {@link framework.InputHandler#LEFT_CLICK},
	 * {@link framework.InputHandler#MIDDLE_CLICK},
	 * {@link framework.InputHandler#RIGHT_CLICK}
	 */
	public boolean getMouseButtons(int button) throws IllegalArgumentException
	{
		if (button >= this.mouseButtons.length || button < 0)
			throw new IllegalArgumentException();
		
		return this.mouseButtons[button];
	}
	
	/**
	 * The mouse position on the screen. This is updated automatically everytime the mouse is moved or dragged.
	 * @Warning bad idea to modify.
	 * @return the integer screen coordinates of the mouse location
	 */
	public Vec2 getMouseScreen()	{ return mouseScreen; }
	/**
	 * The mouse position in the world. This is based on the mouse screen position and the camera.
	 * Call {@link updateMouseWorld()} to correct the mouse world position after the camera is moved
	 * to get an accurate resault.
	 * @return The mouse position in the world.
	 */
	public Vec2 getMouseWorld()		{ return mouseWorld; }
	/**
	 * Use this to update the mouse world position (should probably be done after the camera is moved.)
	 * @Warning bad idea to modify.
	 */
	public void updateMouseWorld()
	{
		mouseWorld = this.model.cam.getScreenToWorld(mouseWorld, mouseScreen);
	}
	
	public void keyPressed		(KeyEvent e)
	{
		//If this key is already down, don't add a new event
		//(work around for keyPress acting like keyTyped java bug
		if (e.getKeyCode() < keyCodes.length && this.keyCodes[e.getKeyCode()] == true)
			return;
		
		//Add to the input queue
		this.inputQueue.add(new InputEvent(EventType.KEY_PRESSED, e));
		
		//Update the keyCode array
		if (e.getKeyCode() < keyCodes.length)
			this.keyCodes[e.getKeyCode()] = true;
		
		//Deal with Caps Lock
		this.CAPS_LOCK = Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK);
		
		//Special Keys
		switch (e.getKeyCode())
		{
		case KeyEvent.VK_UP:		this.UP		= true;	break;
		case KeyEvent.VK_DOWN:		this.DOWN	= true;	break;
		case KeyEvent.VK_LEFT:		this.LEFT	= true;	break;
		case KeyEvent.VK_RIGHT:		this.RIGHT	= true;	break;
			
		case KeyEvent.VK_SPACE:		this.SPACE	= true;	break;
		case KeyEvent.VK_ENTER:		this.ENTER	= true;	break;
					
		default:
			if		(e.getKeyLocation() == KeyEvent.KEY_LOCATION_LEFT)
			{
				switch (e.getKeyCode())
				{
				case KeyEvent.VK_SHIFT:		this.L_SHIFT	= true;	break;
				case KeyEvent.VK_CONTROL:	this.L_CTRL		= true;	break;
				case KeyEvent.VK_ALT:		this.L_ALT		= true;	break;
				}
			}
			else if	(e.getKeyLocation() == KeyEvent.KEY_LOCATION_RIGHT)
			{
				switch (e.getKeyCode())
				{
				case KeyEvent.VK_SHIFT:		this.R_SHIFT	= true;	break;
				case KeyEvent.VK_CONTROL:	this.R_CTRL		= true;	break;
				case KeyEvent.VK_ALT:		this.R_ALT		= true;	break;
				}
			}
			break;
		}
	}
	@Override
	public void keyReleased		(KeyEvent e)
	{
		//Add event to the inputQueue
		this.inputQueue.add(new InputEvent(EventType.KEY_RELEASED, e));
		
		//Update the keyCode array
		if (e.getKeyCode() < keyCodes.length)
			this.keyCodes[e.getKeyCode()] = false;
		
		//Special keys
		switch (e.getKeyCode())
		{
		case KeyEvent.VK_UP:		this.UP		= false;	break;
		case KeyEvent.VK_DOWN:		this.DOWN	= false;	break;
		case KeyEvent.VK_LEFT:		this.LEFT	= false;	break;
		case KeyEvent.VK_RIGHT:		this.RIGHT	= false;	break;
			
		case KeyEvent.VK_SPACE:		this.SPACE	= false;	break;
		case KeyEvent.VK_ENTER:		this.ENTER	= false;	break;
					
		default:
			if		(e.getKeyLocation() == KeyEvent.KEY_LOCATION_LEFT)
			{
				switch (e.getKeyCode())
				{
				case KeyEvent.VK_SHIFT:		this.L_SHIFT	= false;	break;
				case KeyEvent.VK_CONTROL:	this.L_CTRL		= false;	break;
				case KeyEvent.VK_ALT:		this.L_ALT		= false;	break;
				}
			}
			else if	(e.getKeyLocation() == KeyEvent.KEY_LOCATION_RIGHT)
			{
				switch (e.getKeyCode())
				{
				case KeyEvent.VK_SHIFT:		this.R_SHIFT	= false;	break;
				case KeyEvent.VK_CONTROL:	this.R_CTRL		= false;	break;
				case KeyEvent.VK_ALT:		this.R_ALT		= false;	break;
				}
			}
			break;
		}
	}
	@Override
	public void keyTyped		(KeyEvent e)
	{
		this.inputQueue.add(new InputEvent(EventType.KEY_TYPED, e));
	}
	
	@Override
	public void mousePressed	(MouseEvent e)
	{
		this.inputQueue.add(new InputEvent(EventType.MOUSE_PRESSED, e));
		
		mouseButtons[e.getButton() - 1] = true;
		
		if (e.getButton() == MouseEvent.BUTTON1)
			LEFT_CLICK = true;
		if (e.getButton() == MouseEvent.BUTTON2)
			MIDDLE_CLICK = true;
		if (e.getButton() == MouseEvent.BUTTON3)
			RIGHT_CLICK = true;
	}
	@Override
	public void mouseReleased	(MouseEvent e)
	{
		this.inputQueue.add(new InputEvent(EventType.MOUSE_RELEASED, e));
		
		mouseButtons[e.getButton() - 1] = false;
		
		if (e.getButton() == MouseEvent.BUTTON1)
			LEFT_CLICK = false;
		if (e.getButton() == MouseEvent.BUTTON2)
			MIDDLE_CLICK = false;
		if (e.getButton() == MouseEvent.BUTTON3)
			RIGHT_CLICK = false;
	}
	@Override
	public void mouseClicked	(MouseEvent e)
	{
		this.inputQueue.add(new InputEvent(EventType.MOUSE_CLICKED, e));
	}
	@Override
	public void mouseEntered	(MouseEvent e)
	{
		this.inputQueue.add(new InputEvent(EventType.MOUSE_ENTERED, e));
	}
	@Override
	public void mouseExited		(MouseEvent e)
	{
		this.inputQueue.add(new InputEvent(EventType.MOUSE_EXITED, e));
	}
	
	@Override
	public void mouseMoved		(MouseEvent e)
	{
		mouseScreen.x = e.getX();
		mouseScreen.y = e.getY();
		
		this.inputQueue.add(new InputEvent(EventType.MOUSE_MOVED, e));
	}
	@Override
	public void mouseDragged	(MouseEvent e)
	{
		mouseScreen.x = e.getX();
		mouseScreen.y = e.getY();
		
		this.inputQueue.add(new InputEvent(EventType.MOUSE_DRAGGED, e));
	}	
	
	@Override
	public void mouseWheelMoved	(MouseWheelEvent e)
	{
		this.inputQueue.add(new InputEvent(EventType.MOUSE_WHEEL_MOVED, e));
	}

	@Override
	public void componentHidden	(ComponentEvent e)
	{
		inputQueue.add(new InputEvent(EventType.COMPONENT_HIDDEN, e));
	}
	@Override
	public void componentShown	(ComponentEvent e)
	{
		inputQueue.add(new InputEvent(EventType.COMPONENT_SHOWN, e));
	}
	@Override
	public void componentMoved	(ComponentEvent e)
	{
		inputQueue.add(new InputEvent(EventType.COMPONENT_MOVED, e));
	}
	@Override
	public void componentResized(ComponentEvent e)
	{
		inputQueue.add(new InputEvent(EventType.COMPONENT_RESIZED, e));
		
		this.model.updateDimentions();
	}

	@Override
	public void windowOpened		(WindowEvent e)
	{
		inputQueue.add(new InputEvent(EventType.WINDOW_OPENED, e));
	}
	@Override
	public void windowClosing		(WindowEvent e)
	{
		inputQueue.add(new InputEvent(EventType.WINDOW_CLOSING, e));
		this.model.setRunning(false);
	}
	@Override
	public void windowClosed		(WindowEvent e)
	{
		inputQueue.add(new InputEvent(EventType.WINDOW_CLOSED, e));
	}
	@Override
	public void windowActivated		(WindowEvent e)
	{
		inputQueue.add(new InputEvent(EventType.WINDOW_ACTIVATED, e));
	}
	@Override
	public void windowDeactivated	(WindowEvent e)
	{
		inputQueue.add(new InputEvent(EventType.WINDOW_DEACTIVATED, e));
	}
	@Override
	public void windowIconified		(WindowEvent e)
	{
		inputQueue.add(new InputEvent(EventType.WINDOW_ICONIFIED, e));
	}
	@Override
	public void windowDeiconified	(WindowEvent e)
	{
		inputQueue.add(new InputEvent(EventType.WINDOW_DEICONIFIED, e));
	}
}