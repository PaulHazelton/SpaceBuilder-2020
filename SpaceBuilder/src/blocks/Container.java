package blocks;

import java.awt.image.BufferedImage;

import fileHandling.Reader;
import framework.Artist;
import framework.InputHandler;
import utility.Direction;

public class Container extends Block
{
	//Class ID
	private static final long serialVersionUID = -4666204652153451256L;

	//Image Stuff
	private static final String root = "blocks/containment/";
	private static BufferedImage[] images;
	private int imgNum;
	
//Image getting
	public static void fetchImages()
	{
		images = new BufferedImage[2];
		
		images[0] = Reader.loadImage(root + "container_w2_h1_a0.png");
		images[1] = Reader.loadImage(root + "container_w4_h2_a0.png");
	}
	
//Constructor
	public Container(Container c)
	{
		super(c.getShapeType(), c.x, c.y, c.getDirection(), c.w, c.h);
		this.create();
	}
	public Container(int x, int y, Direction d, int w, int h)
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
	}
	//Copy
	public Block copy()
	{
		return new Container(this);
	}
	
//Game Loop
	//Update
	@Override
 	public void update(double timePassed, InputHandler ih)
	{
		
	}
	//Render
	@Override
	public void render(Artist a)
	{
//		if (this.isGhost())
//			a.setOpacity(0.5f);
		a.drawImage(images[imgNum], this.getCen(), this.getDim(), this.getAngle(), this.color);
		
		a.resetOpacity();
	}

}
