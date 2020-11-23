package games;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;

import org.jbox2d.common.Vec2;

import fileHandling.Reader;
import framework.Artist;
import framework.Artist.DrawMode;
import framework.Game;
import framework.InputEvent;
import framework.InputEvent.EventType;
import framework.Model;
import menus.Button;
import menus.ClickHandler;
import menus.Command;
import menus.Commandable;
import menus.MMButtonSystem;

public class MainMenu extends Game implements ClickHandler, Commandable
{
	//Game loop null protection
	private boolean allowDraw = false;
	
	//Button stuff
	private MMButtonSystem menu;
	
	private String camp = "CAMPAIGN";
	private String free = "FREE PLAY";
	private String opts = "OPTIONS";
	private String abot = "ABOUT";
	
	//Constructor
	public MainMenu(Model model)
	{
		super(model);
	}
	
	//Game loop methods
	@Override
	public void initialize()
	{
		this.model.frame.setTitle("Space Game - Main Menu");
//		this.model.frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.model.frame.setLocation	(320, 100);
		this.model.setWindowSize			(1565, 660);
		this.model.frame.setVisible(true);
		this.model.frame.requestFocus();
		
		//Initialize stuff
		Reader.initialize();

		//Make buttons
//		menu = new ButtonSystem(60, 300, 720, 120, 60);
		menu = new MMButtonSystem(60, 300, 720, 120 + 60, 0);
		menu.addButton(this, "CAMPAIGN");
		menu.addButton(this, "FREE PLAY");
		menu.addButton(this, "OPTIONS");
		menu.addButton(this, "ABOUT");
		
		this.model.allowEvents = true;
		this.allowDraw = true;
	}

	@Override
	public void inputEvent(InputEvent item)
	{
		this.menu.inputEvent(item);
		
		switch (item.type)
		{
		case KEY_PRESSED:
			break;
		case KEY_RELEASED:
			break;
		case KEY_TYPED:
			break;
			
		case MOUSE_MOVED:
			break;
		case MOUSE_CLICKED:
			break;
		case MOUSE_DRAGGED:
			break;
		case MOUSE_ENTERED:
			break;
		case MOUSE_EXITED:
			break;
		case MOUSE_PRESSED:
			break;
		case MOUSE_RELEASED:
			break;
			
		case MOUSE_WHEEL_MOVED:		break;
		case WINDOW_ACTIVATED:		break;
		case WINDOW_CLOSED:			break;
		case WINDOW_CLOSING:		break;
		case WINDOW_DEACTIVATED:	break;
		case WINDOW_DEICONIFIED:	break;
		case WINDOW_ICONIFIED:		break;
		case WINDOW_OPENED:			break;
		default:	break;
		}
		
		
		if (item.type == EventType.KEY_PRESSED)
		{
			switch (item.code)
			{
			case KeyEvent.VK_ESCAPE:
				this.model.setRunning(false);
				break;
				
			case 65: // A
				this.model.changeGameMode(new FreePlay(this.model));
				break;
			default: break;
			}
		}
	}
	
	@Override
	public void update(double timePassed)
	{
		
	}
	
	@Override
	public void render(Artist a)
	{
		if (!this.allowDraw)
			return;
		
		a.setDrawMode(DrawMode.NORMAL);
		
		a.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 140));
		
		//Draw Banner
		a.fillRec(1920/2, 60 + 180/2, 1800, 180, 0, new Color(51, 51, 51));
		a.drawTextCentered("SPACE BUILDER", new Vec2(1920/2, 60 + 180/2), 0, Color.cyan);

		this.menu.render(a);
	}
	
	@Override
	public int terminate()
	{
		
		return 0;
	}

	//Menu methods
	public void click(Button src)
	{
		if		(src.getLabel() == this.camp)	//Campaign
		{
			//TODO
		}
		else if	(src.getLabel() == this.free)
		{
			this.model.changeGameMode(new FreePlay(this.model));
		}
		else if	(src.getLabel() == this.opts)
		{
			
		}
		else if	(src.getLabel() == this.abot)
		{
			
		}
	}
	public void press(Button src)
	{
		
	}
	public void release(Button src)
	{
		
	}

	@Override
	public void tapCommand(Command c)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pressCommand(Command c)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void releaseCommand(Command c)
	{
		// TODO Auto-generated method stub
		
	}
}
