package blocks;

import java.awt.Color;
import java.awt.image.BufferedImage;

import fileHandling.Reader;
import framework.Artist;
import framework.InputHandler;
import utility.Direction;
import utility.ImageManip;

public class Chair extends Block
{
	//Class ID
	private static final long serialVersionUID = -5124008243968540691L;
	
	//Image stuff
	private static BufferedImage[] images;
	private static final String root = "blocks/misc/";
	
	//Other
	private boolean mirrored = false;

//Initialization
	public static void fetchImages()
	{
		//Load color 0, color 1, and img
		images = new BufferedImage[3];
		
		images[0] = Reader.loadImage(root + "CaptainChair_col-0.png");
		images[1] = Reader.loadImage(root + "CaptainChair_col-1.png");
		images[2] = Reader.loadImage(root + "CaptainChair_img.png");
	}
	
	public Chair(Chair c)
	{
		super(c.getShapeType(), c.x, c.y, c.getDirection(), c.w, c.h);
		
		this.mirrored = c.mirrored;
		
		this.create();
	}
	public Chair(int x, int y, Direction d, boolean mirrored)
	{
		super(ShapeType.REC, x, y, d, 3, 3);
		
		this.mirrored = mirrored;
		
		this.create();
	}

	@Override
	protected void create()
	{
		// Set custom buildable edges
		this.cells[0][0].setAllEdges(false);
		this.cells[1][0].setAllEdges(false);
		this.cells[2][0].setAllEdges(false);
		this.cells[0][1].setAllEdges(false);
		this.cells[0][2].setEdge(Direction.LEFT, false);
		
		if (this.mirrored)
			this.mirrorCells();
		
		switch(this.getDirection())
		{
		case DOWN:
			this.rotateCellsCW();
			break;
		case LEFT:
			this.rotateCellsCW();
			this.rotateCellsCW();
			break;
		case UP:
			this.rotateCellsCW();
			this.rotateCellsCW();
			this.rotateCellsCW();
			break;
		case RIGHT: break;
		}
		
		//Color images
		this.setColors(new Color(0xff0000), new Color(0x00ffff));
		
		//Don't collide with players
		this.fixtureDef.filter.maskBits = 0b1111111111111101;
		this.fixtureDef.userData = this;
	}

	@Override
	public Block copy()
	{
		return new Chair(this);
	}

	@Override
	public void update(double timePassed, InputHandler ih)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(Artist a)
	{
//		if (this.isGhost())
//			a.setOpacity(0.5f);
		//Draw all three images	//TODO implement 2 color system
		a.drawImage(images[0], this.getCen(), this.getDim(), this.getAngle(), this.mirrored);
		a.drawImage(images[1], this.getCen(), this.getDim(), this.getAngle(), this.mirrored);
		a.drawImage(images[2], this.getCen(), this.getDim(), this.getAngle(), this.mirrored);
		
		a.resetOpacity();
	}
	
	public void setColors(Color chairC, Color details)
	{
		ImageManip.tint(images[0], chairC.getRGB());
		ImageManip.tint(images[1], details.getRGB());
	}
	
	public void setColor(Color c)
	{
		setColors(Color.red, c);
	}

	public boolean isMirrored()	{ return this.mirrored; }
}
