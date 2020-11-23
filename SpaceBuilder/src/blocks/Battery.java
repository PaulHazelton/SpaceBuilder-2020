package blocks;

import java.awt.Color;
import java.awt.image.BufferedImage;

import org.jbox2d.common.Vec2;

import fileHandling.Reader;
import framework.Artist;
import framework.InputHandler;
import utility.Direction;

public class Battery extends Block
{
	//Class ID
	private static final long serialVersionUID = 6788543835614176505L;
	
	//Image stuff
	private static final String root = "blocks/energy/";
	private static BufferedImage[] images;
	private int imgNum;
	
	//Battery stuff
	private static final float energyCapacityDensity = 50000000;	// 50MJ
	private float energy;
	private float energyCapacity;
	
	//Indicator rendering	TODO make this a class (called Indicator?)
	private Vec2	backRecPos;
	private Vec2	backRecDim;
	private Color	backRecColor;
	
	//Image stuff
	public static void fetchImages()
	{
		images = new BufferedImage[2];
		
		images[0] = Reader.loadImage(root + "battery_w2_h1_a0.png");
		images[1] = Reader.loadImage(root + "battery_w4_h2_a0.png");
	}
	
//Constructor
	public Battery(Battery b)
	{
		super(b.getShapeType(), b.x, b.y, b.getDirection(), b.w, b.h);
		this.create();
	}
	public Battery(int x, int y, Direction d, int w, int h)
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
		
		//Battery stuff
		this.energyCapacity = w*h*Battery.energyCapacityDensity;
		this.energy = 0;
		
		//Okay level meter
		this.backRecPos = new Vec2(this.getCen());
		this.backRecDim = new Vec2(this.getDim().x, this.getDim().y*0.95f);
		this.backRecColor = Color.gray;
	}
	//Copy
	public Block copy()
	{
		return new Battery(this);
	}
	
//Game Loop stuff
	//Update
	@Override
 	public void update(double timePassed, InputHandler ih)
	{
		//Okay level meter
//		this.backRecDim.y = (this.energy/this.energyCapacity)*this.getDim().y;
//		this.backRecPos.y = this.getCen().y + this.getDim().y/2f - this.backRecDim.y/2f;
		
		this.backRecDim.x = (this.energy/this.energyCapacity)*this.getDim().x;
	}
	//Render
	@Override
	public void render(Artist a)
	{
//		if (this.isGhost())
//			a.setOpacity(0.5f);
		//Background color
		a.fillRec(this.getCen(), this.getDim(), this.getAngle(), this.backRecColor);
		//Fuel Meter
		a.fillRec(this.backRecPos, this.backRecDim, this.getAngle(), this.color);
		//Image
		a.drawImage(images[imgNum], this.getCen(), this.getDim(), this.getAngle(), null);
		
		a.resetOpacity();
	}

//Energy management
	public float takeEnergy(float request)
	{
		float energyTook;
		if (this.energy >= request)
		{
			energyTook = request;
			this.energy -= request;
		}
		else
		{
			energyTook = this.energy;
			this.energy = 0;
		}
		
		return energyTook;
	}
	public float giveEnergy(float e)
	{
		if (this.getEnergyVoid() >= e)
			this.energy += e;
		else
		{
			e = this.getEnergyVoid();
			this.energy = this.energyCapacity;
		}
		return e;
	}
	
//Getters and setters
	public float	getEnergy()			{ return this.energy; }
	public float	getEnergyCapacity()	{ return this.energyCapacity; }
	public float	getEnergyVoid()		{ return this.energyCapacity - this.energy; }
}
