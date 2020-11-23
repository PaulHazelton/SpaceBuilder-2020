package blocks;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import fileHandling.Reader;
import framework.Artist;
import framework.InputHandler;
import utility.Direction;

public class Gyro extends Block
{
	//Class ID
	private static final long serialVersionUID = 705998089919518506L;
	
	
//Image stuff
	private static final String root = "blocks/locomotion_space/";
	private static BufferedImage[] images;
	private int imgNum;

	
//Gyro Stuff
	private static final float maxPowerConsumptionDensity = 1000000;	// 1MW / m^2
	private float maxPowerConsumption;	// W
	private float powerConsumption;		// W
	
	private float efficiency;	// N*m / W
	private float torque;		// N*m
	
	private float auxAngle;
	
	
//Initialization	
	public static void fetchImages()
	{
		images = new BufferedImage[4];
		
		images[0] = Reader.loadImage(root + "gyroscope_i0_w1_h1_a0.png");
		images[1] = Reader.loadImage(root + "gyroscope_i0_w2_h2_a0.png");
		images[2] = Reader.loadImage(root + "gyroscope_i1_w1_h1_a0.png");
		images[3] = Reader.loadImage(root + "gyroscope_i1_w2_h2_a0.png");
	}

//Constructor
	public Gyro(Gyro g)
	{
		super(g.getShapeType(), g.x, g.y, g.getDirection(), g.w, g.h);
		this.create();
	}
	public Gyro(int x, int y, Direction d, int w, int h)
	{
		super(ShapeType.REC, x, y, d, w, h);
		this.create();
	}
	protected void create()
	{
		if (w == 1 && h == 1)
			imgNum = 0;
		if (w == 2 && h == 2)
			imgNum = 1;
		
		this.maxPowerConsumption = this.getArea()*Gyro.maxPowerConsumptionDensity;
		this.powerConsumption = 0;
		this.efficiency = 0.02f;	// 0.01
		this.torque = 0;
	}
	//Copy
	public Block copy()
	{
		return new Gyro(this);
	}
	
//Game Loop
	//update
	@Override
	public void update(double timePassed, InputHandler ih)
	{
		if (!this.controlling)
			return;
		
		if (ih.getKey(KeyEvent.VK_Q))
		{
			this.powerConsumption = this.maxPowerConsumption;
			this.torque = -1;
		}
		if (ih.getKey(KeyEvent.VK_E))
		{
			this.powerConsumption = this.maxPowerConsumption;
			this.torque = 1;
		}
		if (!ih.getKey(KeyEvent.VK_Q) && !ih.getKey(KeyEvent.VK_E))
		{
			this.powerConsumption = 0;
			this.torque = 0;
			return;
		}
		
		float energy	= this.ship.takeEnergy((float) (this.powerConsumption*timePassed));
		float power		= (float) (energy/timePassed);
		this.torque		*= power*this.efficiency;
		
		this.getBody().applyTorque(this.torque);
		this.auxAngle += torque * 0.01f;
		
//		//TODO angular velocity reduce mode and and target angle mode
	}
	//render
	@Override
	public void render(Artist a)
	{
//		if (this.isGhost())
//			a.setOpacity(0.5f);
		
		a.drawImage(images[imgNum], this.getCen(), this.getDim(), this.getAngle(), this.color);
		a.drawImage(images[imgNum + 2], this.getCen(), this.getDim(), auxAngle);
		
		a.resetOpacity();
	}
	
}