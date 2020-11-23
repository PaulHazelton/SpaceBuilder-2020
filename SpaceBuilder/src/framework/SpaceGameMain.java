package framework;

import java.awt.Color;

import javax.swing.JFrame;

import games.FreePlay;

/**
 * This is the portal from the main to the a "proper" gameloop.
 * Can NOT be instantiated
 * @author Paul Hazelton
 */
public class SpaceGameMain
{
	//Temporary window initializating stuff
	private static final boolean unDecorated = true;
	
	//Constructor: Forces this class to not be instantiated.
	private SpaceGameMain(){}
	
	
	//Initialize framework, jframe, and start the controller
	public static void main(String[] args)
	{	
		//Building the framework
		JFrame			frame		= new JFrame			();
		Model			model		= new Model				();
		Camera			camera		= new Camera			();
		GameCanvas		canvas		= new GameCanvas		();
		Controller		controller	= new Controller		(model);
		InputHandler	handler		= new InputHandler		(model);
		Artist			artist		= new Artist			(model);
		DebugDraw2D		debugDraw	= new DebugDraw2D		(model);
		
		//Set model communication
		InputEvent.initialize (model);
		model.frame			= (frame);
		model.gp			= (canvas);
		model.inputHandler	= (handler);
		model.ga			= (artist);
		model.cam			= (camera);
		model.debugDraw		= (debugDraw);
		Game game = new FreePlay(model);
//		Game game = new MainMenu(model);
		model.gm			= (game);
		
		//Input Listeners
		canvas.addMouseListener			(handler);
		canvas.addMouseMotionListener	(handler);
		canvas.addMouseWheelListener	(handler);
		canvas.addKeyListener			(handler);
		canvas.addComponentListener		(handler);
		frame.addWindowListener			(handler);
		//Allow tab and control like keys to be recognized
		canvas.setFocusTraversalKeysEnabled(false);
		
		//Initializing the frame
		frame.setUndecorated(unDecorated);
		frame.setTitle		("Java Game");
		frame.add			(canvas);
		frame.pack			();
		frame.setSize		(400, 400);
		frame.setLocation	(100, 100);
		frame.setVisible	(true);
		frame.setBackground(Color.black);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setIgnoreRepaint(true);
		
		//Begin the game loop
		controller.start();
	}
}