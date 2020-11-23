package games;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JFrame;

import framework.*;
import framework.Artist.DrawMode;
import framework.InputEvent.EventType;
import menus.CommCatagory;
import menus.Command;
import menus.Commandable;
import menus.ControlPanel;
import menus.MouseButton;
import menus.Vmouse;
import physicsListeners.PhysicsListener;
import planets.Chunk;
import planets.GroundSegment;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import blocks.*;
import building.Builder;
//import building.Builder;
import entities.Player;

import fileHandling.Reader;
import utility.Controllable;
import utility.Direction;
import utility.PMath;
import utility.Units;
import utility.Watchable;

public class FreePlay extends Game implements Commandable
{
	// Controlling
	private boolean paused;
	private boolean draw;
	
	private Controllable inputTarget;
	private Watchable cameraTarget;
	
	// Camera
	private Camera cam;
	
	// Objects
	// Environment
	private World world;
	private PhysicsListener physicsListener;
	BufferedImage background;
	
	// Stuff in the world
	private Player player;
	private SpaceShip debugShip;
	private Builder builder;
//	private BlockBody floor;
//	private Ground ground;
//	private ArrayList<SpaceShip> shipList;
	
	// UI
	private ControlPanel cp;
	
	// Debugging
	private boolean drawBG = true;
	private boolean doDebugDraw = true;
	
	
	// Constructor
	public FreePlay(Model model)
	{
		super(model);
		
		this.cam = model.cam;

		this.physicsListener = new PhysicsListener();
	}

	// Initialization
	@Override
	public	void initialize()
	{
		this.initModel();
		this.reset();
		this.update(0);
	}
	private void initModel()
	{
		this.model.frame.setTitle("Space Game - Free Play");
		
		this.model.autoUpdateCenter = false;
		this.model.frame.setExtendedState(JFrame.NORMAL);
		int s = 9;
//		this.model.frame.setLocation	(320, 100);
		this.model.frame.setLocation	(210, 10);
		this.model.setWindowSize		(128*s, 72*s);
		this.model.updateDimentions();
		
		this.model.displayFPS = false;
		this.doDebugDraw = false;
		
		this.model.ga.setRenderingHints(Artist.rhImage);

		//Util
		PMath			.initRandom();
		Reader			.initialize("resource/images/", "resource/text/");
		//Scene
		background = Reader.loadImage("scenes/stars.jpg");
		//Entities
		Player			.fetchImages();
		//blocks/basic
		Hull			.fetchImages();
		Thruster		.fetchImages();
		RotorThruster	.fetchImages();
		Gyro			.fetchImages();
		Container		.fetchImages();
		Tank			.fetchImages();
		Generator		.fetchImages();
		Battery			.fetchImages();
		SolarBlock		.fetchImages();
		//blocks/misc
		Chair			.fetchImages();
		//planets
		Chunk			.fetchImages();
		GroundSegment	.fetchImages();
		//Menus
		MouseButton		.fetchImages();
		Vmouse			.fetchImages();
		
		this.cp = new ControlPanel(this.model.getWidth(), this.model.getHeight(), this);
		this.updateCameraCenter(this.model.getWidth(), this.model.getHeight());
	}
	public void reset()
	{
		this.paused = true;
		this.model.allowEvents = false;
		
		Vec2 Gravity = new Vec2(0, 0);
		world = new World(Gravity);
		world.setParticleDensity(1.2f);
		world.setDebugDraw(model.debugDraw);
		
		world.setDestructionListener		(physicsListener);
		world.setParticleDestructionListener(physicsListener);
		world.setContactListener			(physicsListener);
		
		this.initGame();
		this.model.allowEvents = true;
	}	
	private void initGame()
	{
		//Control variables
		this.draw = true;
		this.paused = false;
		this.doDebugDraw = false;
	
		//Scene setup	_______________________________________
		this.model.cam.setZoom(100f);
//		this.world.setGravity(new Vec2(0, 9.8f));
//		this.model.setBackgroundColor(new Color(0x87ceeb));
		
		
		//Player
		this.player = new Player(this, this.getWorld(), new Vec2(0, 0), Direction.RIGHT.angle);
		
		
		//Create ships
//		this.shipList = new ArrayList<SpaceShip>();
		
		
		//Debug Ship	_______________________________________
		this.buildDebugShip();
//		this.builder.setTargetSpaceShip(this.debugShip);
		
		//Builder	___________________________________________
		this.builder = new Builder(this.debugShip.getBlockGrid(0));
//		this.builder = new Builder(this, new Vec2());
//		this.builder.setTargetSpaceShip(null);
		
		
		//Floor 	___________________________________________
//		this.floor = new BlockBody(this.getWorld(), BodyType.STATIC, 0, 0, 0);
//		//adding blocks
//		for (int i = 0; i < 40; i++)
//		{
//			Hull b = new Hull(ShapeType.REC, 0, -80 + i*4, 0, Direction.RIGHT, 4, 1);
//			b.setColor(Color.cyan);
//			this.floor.addBlock(b);
//		}
		
		
		//Ground	___________________________________________
//		this.ground = new Ground(this.world);
		
		
		//Controls	___________________________________________
		this.inputTarget = this.player;
		this.cameraTarget = this.player;
		this.player.setControlling(true);
//		this.debugShip.setControlling(false);
	}
	// Building debugShip
	private void buildDebugShip()
	{
		this.debugShip = new SpaceShip(this.world, new Vec2(2, -4), 0);
		
//		this.debugShip.addBlock(0, Command.THRUSTER,		1, 13,	Direction.UP,		2, 1, Color.red);
//		this.debugShip.addBlock(0, Command.THRUSTER_ROTOR,	2, 13,	Direction.UP,		1, 1, Color.red);
//		this.debugShip.addBlock(0, Command.GYRO,			3, 13,	Direction.UP,		1, 1, Color.magenta);
//		this.debugShip.addBlock(0, Command.GYRO,			4, 13,	Direction.UP,		1, 1, Color.magenta);
//		this.debugShip.addBlock(0, Command.THRUSTER_ROTOR,	5, 13,	Direction.UP,		1, 1, Color.red);
//		this.debugShip.addBlock(0, Command.THRUSTER,		6, 13,	Direction.LEFT,		2, 1, Color.red);
//		this.debugShip.addBlock(0, Command.TANK,			1, 12,	Direction.RIGHT,	2, 1, new Color(255, 80, 0));
//		this.debugShip.addBlock(0, Command.GENERATOR,		3, 12,	Direction.RIGHT,	2, 1, Color.green);
//		this.debugShip.addBlock(0, Command.TANK,			5, 12,	Direction.RIGHT,	2, 1, new Color(255, 80, 0));
//		this.debugShip.addBlock(0, Command.BATTERY,			1, 11,	Direction.RIGHT,	2, 1, Color.green);
//		this.debugShip.addBlock(0, Command.CONTAINER,		3, 11,	Direction.RIGHT,	2, 1, Color.yellow);
//		this.debugShip.addBlock(0, Command.BATTERY,			5, 11,	Direction.RIGHT,	2, 1, Color.green);
//		this.debugShip.addBlock(0, Command.SOLAR_BLOCK,		1, 10,	Direction.RIGHT,	2, 1, Color.cyan);
//		this.debugShip.addBlock(0, Command.SOLAR_BLOCK,		3, 10,	Direction.RIGHT,	2, 1, Color.cyan);
//		this.debugShip.addBlock(0, Command.SOLAR_BLOCK,		5, 10,	Direction.RIGHT,	2, 1, Color.cyan);
//		this.debugShip.addBlock(0, Command.CHAIR,			1, 7,	Direction.RIGHT,	3, 3, Color.magenta);
		
		this.debugShip.addBlock(0, new Thruster(		1, 13,	Direction.UP,		2, 1));//.red));
		this.debugShip.addBlock(0, new RotorThruster(	2, 13,	Direction.UP,		1, 1));//.red));
		this.debugShip.addBlock(0, new Gyro(			3, 13,	Direction.UP,		1, 1));//.magenta));
		this.debugShip.addBlock(0, new Gyro(			4, 13,	Direction.UP,		1, 1));//.magenta));
		this.debugShip.addBlock(0, new RotorThruster(	5, 13,	Direction.UP,		1, 1));//.red));
		this.debugShip.addBlock(0, new Thruster(		6, 13,	Direction.LEFT,		2, 1));//.red));
		this.debugShip.addBlock(0, new Tank(			1, 12,	Direction.RIGHT,	2, 1));//, new Color(255, 80, 0)));
		this.debugShip.addBlock(0, new Generator(		3, 12,	Direction.RIGHT,	2, 1));//.green));
		this.debugShip.addBlock(0, new Tank(			5, 12,	Direction.RIGHT,	2, 1));//, new Color(255, 80, 0)));
		this.debugShip.addBlock(0, new Battery(			1, 11,	Direction.RIGHT,	2, 1));//.green));
		this.debugShip.addBlock(0, new Container(		3, 11,	Direction.RIGHT,	2, 1));//.yellow));
		this.debugShip.addBlock(0, new Battery(			5, 11,	Direction.RIGHT,	2, 1));//.green));
		this.debugShip.addBlock(0, new SolarBlock(		1, 10,	Direction.RIGHT,	2, 1));//.cyan));
		this.debugShip.addBlock(0, new SolarBlock(		3, 10,	Direction.RIGHT,	2, 1));//.cyan));
		this.debugShip.addBlock(0, new SolarBlock(		5, 10,	Direction.RIGHT,	2, 1));//.cyan));
		this.debugShip.addBlock(0, new Chair(			1, 7,	Direction.RIGHT,	false));//.magenta));
	}
	
	// Loop ________
	//Input
	public void inputEvent(InputEvent item)
	{
		if (item.type == EventType.COMPONENT_RESIZED)
			this.updateCameraCenter(item.x, item.y);
		
		//Allowed to get event?
		if (!this.model.allowEvents)
			return;
		
		//Quit
		if (item.type == EventType.KEY_PRESSED && item.code == KeyEvent.VK_ESCAPE)
			this.model.setRunning(false);
		
		//UI stuff
		this.cp.inputEvent(item);
	}
	//Update
	@Override
	public void update(double timePassed)
	{
		if (!this.paused)
		{
			this.model.allowEvents = true;
			
			world.setAllowSleep(true);
			world.setWarmStarting(true);
			world.setSubStepping(true);
			world.setContinuousPhysics(true);
			
			world.step((float)timePassed, 3, 8);
		}
		
		//Move camera
		this.moveCamera(this.model.inputHandler);
		
		this.cp.update(timePassed);
		
		if (!this.paused)
			updateObjects(timePassed);
	}
	private void updateObjects(double timePassed)
	{
		InputHandler ih = this.model.inputHandler;
		
		this.builder	.update(timePassed, ih);
		this.player		.update(timePassed, ih);
		this.debugShip	.update(timePassed, ih);
//		for (SpaceShip s : this.shipList)
//			s.update(timePassed, ih);
	}
	//Render
	public void render(Artist a)
	{
		// Background
		if (this.draw && this.drawBG)
		{
//			a.setDrawMode(DrawMode.NORMAL);
//			a.drawImage(this.background, this.cam.getCenter(), new Vec2(2160, 1920), -this.cam.getRotate());
			a.setDrawMode(DrawMode.CENTERED_TRANSFORMED);
			a.setStroke(0.1f*1f/40f);
			a.drawRec(0, 0, 40, 40, 0, Color.green);
		}
		
		// Debug Drawing
		if (this.doDebugDraw)
		{
			DebugDraw2D draw = model.debugDraw;
			
			//6, 38
			draw.setFlags(6);
			this.getWorld().drawDebugData();
		}
		
		// Leave function if draw is false
		if (!this.draw)
			return;
		
		// FOREGROUND	________________
		a.setDrawMode(DrawMode.CENTERED_TRANSFORMED);
		{
			this.debugShip.render(a);
//			for(SpaceShip s : this.shipList)
//				s.render(a);
			this.builder.render(a);
			this.player.render(a);
		}
		
		// HUD
		a.setDrawMode(DrawMode.NORMAL);
		{
			this.cp.render(a);
			
			a.setFont(new Font("Consolas", Font.PLAIN, 24));
			
			int y = 10;
			Color color = Color.white;
			
			//FPS
			String text;
			text = Units.unitToString(this.model.getFPS_actual(), "fps");
			a.drawText("FPS:        " + text, new Vec2(10, y), 0, color);
			y += 30;
			
			//POSITION
			text = Units.unitToString(this.player.getWorldCenter().x, "m");
			text += " ,";
			text += Units.unitToString(this.player.getWorldCenter().y, "m");
			a.drawText("Position:   " + text, new Vec2(10, y), 0, color);
			y += 30;
			
			if (this.debugShip == null)
				return;
			//Mass
			text = Units.unitToString(this.debugShip.getBlockGrid(0).getBody().getMass(), "kg");
			a.drawText("Mass        " + text, new Vec2(10, y), 0, color);
			y += 30;
			
			//Fuel
			text = Units.unitToString(this.debugShip.getFuel(), "kg");
			text += " /" + Units.unitToString(this.debugShip.getFuelCapacity(), "kg");
			a.drawText("Fuel        " + text, new Vec2(10, y), 0, color);
			y += 30;
			
			//Energy
			text = Units.unitToString(this.debugShip.getEnergy(), "J");
			text += " /" + Units.unitToString(this.debugShip.getEnergyCapacity(), "J");
			a.drawText("Energy      " + text, new Vec2(10, y), 0, color);
			y += 30;
		}
	}
	//Terminate
	public int terminate()
	{
		return 0;
	}
	
	// Control
	@Override
	public void tapCommand(Command c)
	{
		switch(c.catagory())
		{
		case DEBUG:
			switch(c)
			{
			//Debug
			case DEBUG_RESET:			this.reset();			break;
			case DEBUG_TOGGLE_DRAW:		this.toggleDraw();		break;
			case DEBUG_TOGGLE_DBDRAW:	this.toggleDebugDraw();	break;
			default:	break;
			}
			break;
		case GAME:
			switch(c)
			{
			//Game
			case ZOOM:	cam.setZoom(100f);		break;
			case PAUSE:	this.paused = true;		break;
			case PLAY:	this.paused = false;	break;
			default:	break;
			}
			break;
//		case BUILDER:	this.builder.tapCommand(c);	break;
//		case BLOCK:		this.builder.tapCommand(c);	break;
		case PLAYER:	this.player.tapCommand(c);	break;
		case SPACESHIP:	break;
		case MISC:		break;
		default:		break;
		}
	}
	@Override
	public void pressCommand(Command c)
	{
		System.out.println("@FreePlay.pressCommand: " + c.toString() + ",\t" + c.catagory());
		
		switch(c.catagory())
		{
		case GAME:			break;
		case BUILDER:		this.builder.pressCommand(c);	break;
//		case BLOCK:			this.builder.pressCommand(c);	break;
		case PLAYER:		this.player.pressCommand(c);	break;
		case KEY:			this.debugShip.pressCommand(c);	break;
		case SPACESHIP:		this.debugShip.pressCommand(c);	break;
		case MISC:			break;
		default:			break;
		}
	}
	@Override
	public void releaseCommand(Command c)
	{
		switch(c.catagory())
		{
		case DEBUG:		break;
		case GAME:		break;
//		case BUILDER:	this.builder.releaseCommand(c);	break;
//		case BLOCK:		this.builder.releaseCommand(c);	break;
		case PLAYER:	this.player.releaseCommand(c);	break;
		case SPACESHIP:	break;
		case MISC:		break;
		default:		break;
		}
	}
	public void scrollCommand(int n, Command c)
	{
		if (c.catagory() == CommCatagory.GAME)
		{
			if (c == Command.ZOOM)
				cam.setZoom(cam.getZoom() * (1.0f - (n/10f)));
		}
	}
	
	public void setInputTarget(Controllable c)
	{
		this.inputTarget.setControlling(false);
		this.inputTarget = c;
		c.setControlling(true);
	}
	public Controllable getInputTarget()	{ return this.inputTarget; }
	
	public void setCameraTarget(Watchable w)
	{
		this.cameraTarget = w;
	}

	public void setMode(Command mode)
	{
		if (mode.catagory() != CommCatagory.MODE)
			return;

		this.inputTarget.setControlling(false);
		
		switch(mode)
		{
		case NOTHING_MODE:	this.inputTarget = this.player;		break;
//		case BUILD_MODE:	this.inputTarget = this.builder;	break;
		default:	break;
		}
		
		this.inputTarget.setControlling(true);
	}
	
	// Camera alignment
	public void updateCameraCenter(int screenWidth, int screenHeight)
	{
		this.cam.setCenter(new Vec2(screenWidth/2, screenHeight/2));
		moveCamera(this.model.inputHandler);
	}
	private void moveCamera(InputHandler ih)
	{
		if (this.cameraTarget == null)
			return;
		
		this.cam = this.model.cam;
		
		//Rotation
		cam.setRotate(this.cameraTarget.getWorldAngle());
		
		ih.updateMouseWorld();
		
		//Halfsies translation
		cam.setTranslation(PMath.midpoint(
				this.cameraTarget.getWorldCenter(),
				PMath.midpoint(this.cameraTarget.getWorldCenter(), this.model.inputHandler.getMouseWorld())));
		
		ih.updateMouseWorld();
	}
	
	// Building stuff
	public void buildNewShip(Vec2 pos, Block b, float a)
	{
//		SpaceShip s = new SpaceShip(this.getWorld(), pos, a);
//		s.addBlock(s.getBlockBody(0), b);
//		this.shipList.add(s);
//		this.builder.setTargetSpaceShip(s);
	}
	
	// Save and load spaceship (for testing)
	@SuppressWarnings("unused")
	private void saveShip(SpaceShip ss, String name)
	{
		String fileName = "saves/spaceships/" + name;
		
		try
		{
			FileOutputStream fs = new FileOutputStream(fileName);
			ObjectOutputStream os = new ObjectOutputStream(fs);
			os.writeObject(ss);
			os.close();
			fs.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	@SuppressWarnings("unused")
	private SpaceShip loadShip()
	{
		String fileName = "saves/spaceships/debugShip_01";
		
		try
		{
			FileInputStream fs = new FileInputStream(fileName);
			ObjectInputStream os = new ObjectInputStream(fs);
			Object o = os.readObject();
			os.close();
			fs.close();
			
			return (SpaceShip)o;
		}
		catch (IOException e)				{ e.printStackTrace(); }
		catch (ClassNotFoundException e)	{ e.printStackTrace(); }
		
		return null;
	}
	
	// Getters and Setters
	public World getWorld()
	{ return this.world; }
	public ControlPanel getControlPanel()
	{
		return this.cp;
	}
	
	// Buttons
	public void togglePause()		{ this.paused = !this.paused; }
	public void toggleDraw()		{ this.draw = !this.draw; }
	public void toggleDebugDraw()	{ this.doDebugDraw = !this.doDebugDraw; }
}