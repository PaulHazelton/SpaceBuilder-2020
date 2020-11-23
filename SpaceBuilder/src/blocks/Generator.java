package blocks;

import java.awt.Color;
import java.awt.image.BufferedImage;

import fileHandling.Reader;
import framework.Artist;
import framework.InputHandler;
import utility.Direction;

public class Generator extends Block
{
	//Class ID
	private static final long serialVersionUID = 5294019238787485369L;
	
	
	//Image stuff
	private static final String root = "blocks/energy/";
	private static BufferedImage[] images;
	private int imgNum;
	
	//Generator properties
	private static final float maxFuelRateDensity = 2;	//kg/s / m^2
	private float maxFuelRate;	//kg/s
	private float fuelRate;		//kg/s 
	private float efficiency;	//0-1
	
	//Generator management
	private boolean auto =		false;
	private boolean on =		false;
	private boolean running =	false;
	
	//Cosmetics
	private Color darkColor =	Color.gray;
	
	
//Image getting
	public static void fetchImages()
	{
		images = new BufferedImage[2];
		
		images[0] = Reader.loadImage(root + "generator_w2_h1_a0.png");
		images[1] = Reader.loadImage(root + "generator_w4_h2_a0.png");
	}
	
//Constructor
	public Generator(Generator g)
	{
		super(g.getShapeType(), g.x, g.y, g.getDirection(), g.w, g.h);
		this.create();
	}
	public Generator(int x, int y, Direction d, int w, int h)
	{
		super(ShapeType.REC, x, y, d, w, h);
		this.create();
	}
	protected void create()
	{
		//Image
		if (w == 2 && h == 1)
			imgNum = 0;
		if (w == 4 && h == 1)
			imgNum = 1;
		
		this.maxFuelRate = this.getDim().x*this.getDim().y*Generator.maxFuelRateDensity;
		this.efficiency = 0.5f;
	}
	//Copy
	public Block copy()
	{
		return new Generator(this);
	}

//Game Loop stuff
	//Update
	@Override
	public void update(double timePassed, InputHandler ih)
	{
		if (this.auto || this.on)
			this.generate(timePassed);
		else
			this.running = false;
	}
	//Render
	@Override
	public void render(Artist a)
	{
//		if (this.isGhost())
//			a.setOpacity(0.5f);
		
		if (this.running)
			a.drawImage(images[imgNum], this.getCen(), this.getDim(), this.getAngle(), this.color);
		else
			a.drawImage(images[imgNum], this.getCen(), this.getDim(), this.getAngle(), this.darkColor);
		
		a.resetOpacity();
	}	

//Generator operations
	private void generate(double timePassed)
	{
		//If batteries are not at capacity, use fuel to fill them
		if (this.ship.getEnergy() < this.ship.getEnergyCapacity())
		{
			this.on = true;
			this.fuelRate = this.maxFuelRate;
			
			float fuel		= this.ship.takeFuel((float) (fuelRate*timePassed));
			float energy	= fuel*Tank.getFuelEnergyDensity()*this.efficiency;
			
			this.ship.giveEnergy(energy);
			
			this.running = true;
		}
		else
			this.running = false;
	}
	
	public void toggle()
	{
		this.on = !this.on;
	}

//Getters and setters
	@Override
	public void setColor(Color c)
	{
		super.setColor(c);
		this.darkColor = c.darker().darker().darker();
	}
}