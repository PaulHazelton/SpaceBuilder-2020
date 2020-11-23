package blocks;

import java.awt.image.BufferedImage;

import fileHandling.Reader;
import framework.Artist;
import framework.InputHandler;
import utility.Direction;

public class Hull extends Block
{
	// Class ID
	private static final long serialVersionUID = 7835656154101162863L;

	// Image stuff
	private static BufferedImage[][][] images;
	private static final String root = "blocks/blocks_basic/hull/backPanel_128/";
	// Image number
	private int shapeNum	= 0;
	private int sizeNum		= 0;
	private int armorRating	= 0;
	
	// Initialization
	public static void fetchImages()
	{
		//Shape type, dimentions, armor
		images = new BufferedImage[2][10][3];
		
		for (int s = 0; s < images		.length; s++) {
		for (int d = 0; d < images[s]	.length; d++) {
		for (int a = 0; a < images[s][d].length; a++) {
			
			String shp = "rec";
			if (s == 1)
				shp = "tri";
			
			
			//Getting width and height from dimention number
			int w = 0;
			int h = 0;
			
				 if (d < 4)
			{
				h = 1;
				w = h + d;
			}
			else if (d < 7)
			{
				h = 2;
				w = h + (d%4);
			}
			else if (d < 9)
			{
				h = 3;
				w = h + (d%7);
			}
			else if (d < 10)
			{
				h = 4;
				w = h + (d%9);
			}
			
			images[s][d][a] = Reader.loadImage(
					root + shp + "_w" + w + "_h" + h + "_a" + a + ".png");
		}}}
	}
	
	// Constructor
	public Hull(Hull h)
	{
		super(h.getShapeType(), h.x, h.y, h.getDirection(), h.w, h.h);
		this.armorRating = h.armorRating;
		this.create();
		
		this.color = h.color;
	}
	public Hull(ShapeType st, int ar, int x, int y, Direction d, int w, int h)
	{
		super(st, x, y, d, w, h);
		this.armorRating = ar;
		this.create();
	}
	protected void create()
	{
		switch (this.getShapeType())
		{
			case REC:	shapeNum = 0;	break;
			case TRI:	shapeNum = 1;	break;
			case TRI_M:	shapeNum = 1;	break;
		}
		
		this.sizeNum		= resolveDimentionNumber(w, h);
	}
	private int resolveDimentionNumber(int w, int h)
	{
		int d = 0;
		if (h == 1)
			d = w - 1;
		if (h == 2)
			d = w + 2;
		if (h == 3)
			d = w + 4;
		if (h == 4)
			d = w + 5;
		
		return d;
	}
	//copy
	public Block copy()
	{
		return new Hull(this);
	}
	
	// Game Loop
	// Update
	public void update(double timePassed, InputHandler ih)
	{
		
	}
	// Render
	public void render(Artist a)
	{
//		if (this.isGhost())
//			a.setOpacity(0.5f);
		
		//Drawing image with backColor (if rectangle)
		if (this.getShapeType() == ShapeType.REC)
		{
			a.drawImage(Hull.images[this.shapeNum][this.sizeNum][this.armorRating],
					this.getCen(), this.getDim(), this.getAngle(), this.color);
		}
		//Drawing the polygon then the image (if triangle)
		else if (this.getShapeType() == ShapeType.TRI)
		{
//			if (!this.isGhost())
//				a.fillPolygon(this.getPath(), color);
			a.fillPolygon(this.getPath(), color);
			a.drawImage(Hull.images[this.shapeNum][this.sizeNum][this.armorRating],
					this.getCen(), this.getDim(), this.getAngle());
		}
		//Drawing the polygon then the image (if triangle)
		else if (this.getShapeType() == ShapeType.TRI_M)
		{
//			if (!this.isGhost())
//				a.fillPolygon(this.getPath(), color);
			a.fillPolygon(this.getPath(), color);
			a.drawImage(Hull.images[this.shapeNum][this.sizeNum][this.armorRating],
					this.getCen(), this.getDim(), this.getAngle(), true);
		}
		
		a.resetOpacity();
	}
	
	// Getters and setters
	public float getArea()
	{
		if (this.shapeNum == 0)
			return this.getDim().x*this.getDim().y;
		else
			return this.getDim().x*this.getDim().y*0.5f;
	}
}