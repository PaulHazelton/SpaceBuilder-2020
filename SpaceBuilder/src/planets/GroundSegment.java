package planets;

import java.awt.image.BufferedImage;

import org.jbox2d.common.Vec2;

import fileHandling.Reader;
import framework.Artist;
import utility.PMath;

public class GroundSegment
{
	//Scale of every block (0.5 means that a 1x1 block is 0.5m by 0.5m
	
	//Static Images
	private static BufferedImage[][] images;
	private static final String root = "blocks/land/";	//place holder graphics
	
	//Image
	private int shapeType = 0;	//Rectangle = 0, Triangle = 1;
	private int dNumber = 0;	//Dimention number for the image array
	private boolean mirrored = false;
	
	//Rendering info
	private Vec2 pos;
	private Vec2 dim;
	
	
	public static void fetchImages()
	{
		//ShapeType, Dimentions.
		images = new BufferedImage[2][4];
		
		//Load rectangles
		for (int i = 1; i <= 4; i++)
			images[0][i-1] = Reader.loadImage(root + "dirt_rec_w" + i + "_h1_t0.png");
		//Load triangles
		for (int i = 1; i <= 4; i++)
			images[1][i-1] = Reader.loadImage(root + "dirt_tri_w" + i + "_h1_t0.png");
	}
	
	
	//Temporary constructor for the simple Ground constructor
	public GroundSegment(int x, int y, int w, int yChange)
	{
		//Calc rendering info
		if (yChange == 0)
		{
			dim = new Vec2(w/2f, 1f/2f);
			pos = new Vec2(x/2f + dim.x/2f, y/2f + dim.y/2f);
			
			shapeType = 0;
			
			this.mirrored = PMath.random.nextBoolean();
		}
		else	// (yChange != 0)
		{
			shapeType = 1;
			
			if (yChange > 0)
			{
				dim = new Vec2(w/2f, yChange/2f);
				pos = new Vec2(x/2f + dim.x/2f, y/2f + dim.y/2f);
			}
			else
			{
				dim = new Vec2(w/2f, -yChange/2f);
				pos = new Vec2(x/2f + dim.x/2f, y/2f - dim.y/2f);
				this.mirrored = true;
			}
		}
		
		//Calc dNumber
		if		(yChange == 0)
			this.dNumber = w - 1;
		else if (yChange == 1 || yChange == -1)
			this.dNumber = w - 1;
		else if (yChange > 1)
			this.dNumber = yChange - 1;
		else if (yChange < -1)
			this.dNumber = -yChange - 1;
		
		//Rotate stuff
		//TODO LEAVE OFF POINT 2019-0607-0023
	}
	
	public void render(Artist a)
	{
		a.drawImage(images[this.shapeType][this.dNumber], this.pos, this.dim, 0, this.mirrored);
	}
}
