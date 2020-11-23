package framework;

import java.awt.Color;

import javax.swing.JFrame;

import org.jbox2d.common.Vec2;

/**
 * This is the basic communication portal between the framework
 * classes and protects the privacy of sensitive fields
 * <p>
 * Every class in the framework package should have a pointer to the Model.
 * The model should only be instantiated once in the main method.
 * @author Paul Hazelton
 */
public class Model 
{	
	//Framwork communication
	public JFrame		frame;
	public GameCanvas	gp;
	public Artist		ga;
	public Camera		cam;
	public InputHandler	inputHandler;
	public Game			gm;
	public DebugDraw2D	debugDraw;
	
	//Prevent render before initialization
	public boolean allowRender = true;
	//Prevent unnecessary ComponentResize events
	public boolean allowEvents = false;
	
	//Frame size data
	private int width;
	private int height;
	//Update camera on resize
	public boolean autoUpdateCenter = true;
	
	//Game loop control
	private boolean running = true;
	//Debugging
	public boolean displayFPS = false;
	//Game loop data
	private int FPS = 60;
	private double FPS_actual = 0;
	
	
	//Constructor
	/**
	 * The model should only by constructed in the main method 
	 */
	public Model()	{}
	
	//Changing the frame
	public void makeUndecorated()
	{
		GameCanvas p = this.gp;
		String title = this.frame.getTitle();
		int x = this.frame.getX();
		int y = this.frame.getY();
		int w = this.frame.getWidth();
		int h = this.frame.getHeight();
		
		this.frame = new JFrame();
		this.frame.setUndecorated(true);
		this.frame.setTitle(title);
		this.frame.add(p);
		this.frame.pack();
		this.frame.setLocation(x, y);
		this.frame.setSize(w, h);
		this.frame.setVisible(true);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	//Framwork communication
	public void changeGameMode(Game gm)
	{
		this.gm = gm;
		this.gm.initialize();
	}
	
	/**
	 * Sets the width and height of the content pane. 0, 0 is exactly at the top left corner of the window (below the header)
	 * @param w - the width of the content pane
	 * @param h - the height of the content pane
	 */
	public void setWindowSize(int w, int h)
	{
		//The weird insets thing. Adding the insets makes it so you can set the content pane.
		frame.setSize
		(
			frame.getInsets().left	+ frame.getInsets().right	+ w,
			frame.getInsets().top	+ frame.getInsets().bottom	+ h
		);
		
		this.updateDimentions();
	}

	
	//Game loop control
	/**
	 * Set control over running the game loop
	 * @param run true if running
	 */
	public void		setRunning(boolean run)
	{ this.running = run; }
	/**
	 * Get control over running the game loop
	 * @return true if running
	 */
	public boolean	getRunning()
	{ return this.running; }	

	//Game loop data
	/**
	 * @return The target number of frames per second
	 */
	public int	getFPS()
	{ return this.FPS; }
	/**
	 * Changes the target FPS is Controller.start() has not been called yet.
	 * If Controller.start() has already been called, this has no effect.
	 * TODO Change this and the Controller so target FPS can be changed at runtime
	 * @param FPS Target number of frames per second
	 */
	public void	setFPS(int FPS)
	{
		this.FPS = FPS;
	}

	/**
	 * @return The number of frames drawn in the last second
	 */
	public double	getFPS_actual()				{ return this.FPS_actual; }
	/**
	 * This should only be called by the controller so report actual FPS.
	 * @param frames The number of frames drawn in the last second
	 */
	protected void	setFPS_actual(double frames)	{ this.FPS_actual = frames; }

	//Dimentions of the content pane
	/**
	 * Updates the width and height of the content pane, and camera center accordingly
	 */
	public void		updateDimentions()
	{
		this.width  = this.frame.getWidth () - this.frame.getInsets().left - this.frame.getInsets().right;
		this.height = this.frame.getHeight() - this.frame.getInsets().top  - this.frame.getInsets().bottom;
		
		if (autoUpdateCenter)
		{
			int x = this.frame.getContentPane().getWidth() / 2;
			int y = this.frame.getContentPane().getHeight() / 2;
			this.cam.setCenter(new Vec2(x, y));
		}
	}
	/**
	 * @return The width of the content pane
	 */
	public int		getWidth()
	{
		this.updateDimentions();
		return this.width;
	}
	/**
	 * @return The height of the content pane
	 */
	public int		getHeight()
	{
		this.updateDimentions();
		return this.height;
	}
	
	/**
	 * @param backColor Sets the background color of the canvas
	 */
	public void		setBackgroundColor(Color backColor)
	{ this.gp.setBackground(backColor); }
	/**
	 * @return The background color of the canvas
	 */
	public Color	getBackgroundColor()
	{ return this.gp.getBackground(); }
}