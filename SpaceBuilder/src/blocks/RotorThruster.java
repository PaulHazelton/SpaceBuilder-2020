package blocks;

import java.awt.Color;
import java.awt.image.BufferedImage;

import org.jbox2d.common.Vec2;

import fileHandling.Reader;
import framework.Artist;
import framework.InputHandler;
import utility.Direction;
import utility.PMath;

public class RotorThruster extends Block
{
	//Class ID
	private static final long serialVersionUID = -2645782435009038654L;
	
	//Image stuff
	private static final String root = "blocks/locomotion_space/";
	private static BufferedImage[] images;
	private int imgNum;
	
	//Thrust stuff
	private static final float maxFuelRateDensity = 2.5f;	//kg/s / m^2
	private float maxFuelRate;	//kg/s
	private float fuelRate;		//kg/s
	
	private float efficiency;	//0-1
	private	float thrust;		//N
	private float thrustAngle;	//rad
	
	
//Initialization	
	public static void fetchImages()
	{
		images = new BufferedImage[4];
		
		images[0] = Reader.loadImage(root + "rotorChemThrust_i0_w1_h1_a0.png");
		images[1] = Reader.loadImage(root + "rotorChemThrust_i0_w2_h2_a0.png");
		images[2] = Reader.loadImage(root + "rotorChemThrust_i1_w1_h1_a0.png");
		images[3] = Reader.loadImage(root + "rotorChemThrust_i1_w2_h2_a0.png");
	}

//Constructor
	public RotorThruster(RotorThruster r)
	{
		super(r.getShapeType(), r.x, r.y, r.getDirection(), r.w, r.h);
		this.create();
	}
	public RotorThruster(int x, int y, Direction d, int w, int h)
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
		
		this.maxFuelRate	= this.getDim().x*this.getDim().y*RotorThruster.maxFuelRateDensity;
		this.fuelRate		= 0;
		this.efficiency		= 0.0025f;
		this.thrust			= 0f;
		this.thrustAngle	= 0;
	}
	//Copy
	public Block copy()
	{
		return new RotorThruster(this);
	}
	
//Game Loop
	//update
	@Override
	public void update(double timePassed, InputHandler ih)
	{
		if (!this.controlling)
		{
			this.thrustAngle = this.getWorldAngle();
			return;
		}
		
		//Angle to mouse
		this.thrustAngle = PMath.cartesianToPolar(this.getWorldCenter(), ih.getMouseWorld()).y;
		
		if (ih.LEFT_CLICK)
			this.fuelRate = this.maxFuelRate;
		else
			this.fuelRate = 0;
		
		if (this.fuelRate == 0)
			return;

		//Change fuel rate to fuel used, energy in fuel, and thrust
		float fuel		= this.ship.takeFuel((float) (fuelRate*timePassed));
		float energy	= fuel*Tank.getFuelEnergyDensity();
		float power		= (float)(energy/timePassed);
		this.thrust		= power*this.efficiency;

		Vec2 force = PMath.polarToCartesian(thrust, this.thrustAngle);
		this.body.applyForce(force, this.getWorldCenter());
	}
	//render
	@Override
	public void render(Artist a)
	{
//		if (this.isGhost())
//			a.setOpacity(0.5f);
		
		a.drawImage(images[imgNum], this.getCen(), this.getDim(), this.getAngle(), this.color);
		a.drawImage(images[imgNum + 2], this.getCen(), this.getDim(), thrustAngle - this.getWorldAngle() + this.getAngle());
		
		a.fillRec(
				this.getCen().add(	PMath.rotateVec(new Vec2(-0.5f, 0), -this.getWorldAngle() + thrustAngle + this.getAngle())),
				new Vec2(this.fuelRate/this.maxFuelRate, 0.05f),
				thrustAngle - this.getWorldAngle() + this.getAngle(),
				Color.red);
		
		a.resetOpacity();
	}
}
