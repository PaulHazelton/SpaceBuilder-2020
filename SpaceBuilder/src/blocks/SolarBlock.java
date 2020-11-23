package blocks;

import java.awt.image.BufferedImage;

import fileHandling.Reader;
import framework.Artist;
import framework.InputHandler;
import utility.Direction;

public class SolarBlock extends Block
{
	//Class ID
	private static final long serialVersionUID = -4161565617569338087L;
	
	//Image stuff
	private static final String root = "blocks/energy/";
	private static BufferedImage[] images;
	private int imgNum;
	
	//Solar Panel stuff
	private static final float powerProductionDensity = 500000; // 500kw / m^2
	private float powerProduction = 0;	// 250 kW
	
//Initialization
	public static void fetchImages()
	{
		images = new BufferedImage[2];
		
		images[0] = Reader.loadImage(root + "solarBlock_w2_h1_a0.png");
		images[1] = Reader.loadImage(root + "solarBlock_w4_h2_a0.png");
	}
	
//Constructor
	public SolarBlock(SolarBlock s)
	{
		super(s.getShapeType(), s.x, s.y, s.getDirection(), s.w, s.h);
		this.create();
	}
	public SolarBlock(int x, int y, Direction d, int w, int h)
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
		
		this.powerProduction = this.getArea()*SolarBlock.powerProductionDensity;
	}
	//Copy
	public Block copy()
	{
		return new SolarBlock(this);
	}
	
//Game Loop
	//update
	@Override
	public void update(double timePassed, InputHandler ih)
	{
		this.ship.giveEnergy((float) (powerProduction*timePassed));
	}
	//render
	@Override
	public void render(Artist a)
	{
//		if (this.isGhost())
//			a.setOpacity(0.5f);
		
		a.drawImage(images[imgNum], this.getCen(), this.getDim(), this.getAngle(), this.color);
		a.resetOpacity();
	}
}
