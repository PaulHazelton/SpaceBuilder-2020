package blocks;

import java.awt.Color;
import java.awt.image.BufferedImage;

import org.jbox2d.common.Vec2;

import fileHandling.Reader;
import framework.Artist;
import framework.InputHandler;
import utility.Direction;

public class Tank extends Block
{
	//Class ID
	private static final long serialVersionUID = -1135989612505829056L;

	//Image Stuff
	private static final String root = "blocks/containment/";
	private static BufferedImage[] images;
	private int imgNum;
	
//Tank stuff
	//Fuel stuff
	private static float		fuelEnergyDensity	= 2000000f;	// 2MJ / kg
	private static final float	fuelCapacityDensity	= 500;		// kg / m^2
	private float fuel;			//kg
	private float fuelCapacity;	//kg
	
	//Base density (with no fuel)
	private float baseDensity;
	
	//Indicator	TODO Indicator class
	private Vec2	backRecPos;
	private Vec2	backRecDim;
	private Color	backRecColor;
	
	
//Static methods
	public static void fetchImages()
	{
		images = new BufferedImage[2];
		
		images[0] = Reader.loadImage(root + "fuelTank_w2_h1_a0.png");
		images[1] = Reader.loadImage(root + "fuelTank_w4_h2_a0.png");
	}
	
//Constructor
	public Tank(Tank t)
	{
		super(t.getShapeType(), t.x, t.y, t.getDirection(), t.w, t.h);
		this.create();
	}
	public Tank(int x, int y, Direction d, int w, int h)
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
		
		this.baseDensity = 50;	//kg/m^2
		
		//Fuel
		this.fuelCapacity = this.getDim().x*this.getDim().y*fuelCapacityDensity;
		this.fuel = this.fuelCapacity;
		
		//Mass with fuel
		this.getFD().setDensity(this.baseDensity + (this.fuel/this.getArea()));
//		fuelEnergyDensity = 4000000f;	//4MJ
		
		//Okay level meter
		this.backRecPos = new Vec2(this.getCen());
		this.backRecDim = new Vec2(this.getDim().x, this.getDim().y);
		this.backRecColor = Color.gray;
	}
	//Copy
	public Block copy()
	{
		return new Tank(this);
	}
	
//Game Loop
	//update
	@Override
	public void update(double timePassed, InputHandler ih)
	{
		//Okay level meter
		this.backRecDim.y = (this.fuel/this.fuelCapacity)*this.getDim().y;
		this.backRecPos.y = this.getCen().y + this.getDim().y/2f - this.backRecDim.y/2f;
		
		//Change mass
		this.getFixture().setDensity(this.baseDensity + (this.fuel/this.getArea()));		
		this.body.resetMassData();
	}
	//render
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
	
//Fuel Tank operations
	public float takeFuel(float request)
	{
		float fuelTook;
		if (this.fuel >= request)
		{
			fuelTook = request;
			this.fuel -= request;
		}
		else
		{
			fuelTook = this.fuel;
			this.fuel = 0;
		}
		
		return fuelTook;
	}
	
//Getters and Setters
	//fuel
	public static float getFuelEnergyDensity()	{ return fuelEnergyDensity; }
	
	public float getFuelCapacity()	{ return this.fuelCapacity; }
	public float getFuel()			{ return this.fuel; }
	//Color
	@Override
	public void setColor(Color color)
	{
		super.setColor(color);
		
		this.backRecColor = color.darker().darker();
	}
}
