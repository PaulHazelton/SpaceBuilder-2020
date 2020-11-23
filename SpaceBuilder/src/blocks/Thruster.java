package blocks;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import org.jbox2d.common.Vec2;

import fileHandling.Reader;
import framework.Artist;
import framework.InputHandler;
import utility.Direction;
import utility.PMath;

public class Thruster extends Block
{
	//Class ID
	private static final long serialVersionUID = -8336262967299835598L;
	
	//Image stuff
	private static final String root = "blocks/locomotion_space/";
	private static BufferedImage[] images;
	private int imgNum;
	
	//Thruster stuff
	private static final float maxFuelRateDensity = 2.5f;	// kg/s / m^2
	private float maxFuelRate;	// kg/s
	private float fuelRate;		// kg/s
	
	private float efficiency;	// N / W (0-1)
	private	float thrust;		// N
	
	
//Initialization	
	public static void fetchImages()
	{
		images = new BufferedImage[4];
		
		images[0] = Reader.loadImage(root + "fixedChemThrust_w2_h1_a0.png");
		images[1] = Reader.loadImage(root + "fixedChemThrust_w4_h1_a0.png");
		images[2] = Reader.loadImage(root + "fixedChemThrust_w4_h2_a0.png");
		images[3] = Reader.loadImage(root + "fixedChemThrust_w8_h2_a0.png");
	}

//Constructor
	public Thruster(Thruster t)
	{
		super(t.getShapeType(), t.x, t.y, t.getDirection(), t.w, t.h);
		this.create();
	}
	public Thruster(int x, int y, Direction d, int w, int h)
	{
		super(ShapeType.REC, x, y, d, w, h);
		this.create();
	}

	protected void create()
	{
//		if (h > w)
//		{
//			int temp = h;
//			h = w;
//			w = temp;
//		}
		
		//Image
		if (w == 2 && h == 1)
			imgNum = 0;
		if (w == 4 && h == 1)
			imgNum = 1;
		if (w == 4 && h == 2)
			imgNum = 2;
		if (w == 8 && h == 2)
			imgNum = 3;
		
		this.maxFuelRate = this.getDim().x*this.getDim().y*Thruster.maxFuelRateDensity;
		
		thrust = 0f;
		efficiency = 0.0025f;
	}
	//Copy
	public Block copy()
	{
		return new Thruster(this);
	}
	
//Game Loop stuff
	//update
	@Override
	public void update(double timePassed, InputHandler ih)
	{
		if (!this.controlling)
			return;
		
		//Picking target fuel rate
		float inc = (float) (this.maxFuelRate*timePassed*2);

		switch (this.getDirection())
		{
		case UP:
			if (ih.getKey(KeyEvent.VK_W))	this.fuelRate += inc;
			else 							this.fuelRate -= inc;
			break;
		case LEFT:
			if (ih.getKey(KeyEvent.VK_A))	this.fuelRate += inc;
			else							this.fuelRate -= inc;
			break;
		case DOWN:
			if (ih.getKey(KeyEvent.VK_S))	this.fuelRate += inc;
			else							this.fuelRate -= inc;
			break;
		case RIGHT:
			if (ih.getKey(KeyEvent.VK_D))	this.fuelRate += inc;
			else							this.fuelRate -= inc;
			break;
		}
		
		
		this.fuelRate = PMath.limit(this.fuelRate, 0, this.maxFuelRate);
		
		//If engine is off, don't bother
		if (this.fuelRate == 0)
			return;
		
		//Change fuel rate to fuel used, energy in fuel, and thrust
		float fuel		= this.ship.takeFuel((float) (fuelRate*timePassed));
		float energy	= fuel*Tank.getFuelEnergyDensity();
		float power		= (float)(energy/timePassed);
		this.thrust		= power*this.efficiency;
		
		Vec2 force = PMath.polarToCartesian(thrust, this.getWorldAngle());
		this.body.applyForce(force, this.getWorldCenter());
	}
	//render
	@Override
	public void render(Artist a)
	{
//		if (this.isGhost())
//			a.setOpacity(0.5f);
		
		a.drawImage(images[imgNum], this.getCen(), this.getDim(), this.getAngle(), this.color);
		
		a.fillRec(
				this.getCen(),
				new Vec2((this.fuelRate)/this.maxFuelRate, 0.05f),
				this.getAngle(),
				Color.red);
		
		a.resetOpacity();
	}
}
