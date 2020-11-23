package planets;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.jbox2d.collision.shapes.ChainShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import fileHandling.Reader;
import framework.Artist;
import utility.IntPair;
import utility.PMath;

public class Chunk
{
	//Chunk is its own class, Ground has chunks, chunks have chainshapes and groundsegments
	//Size of chunk: 16x16 units, 8x8 meters
	//Width and height of 8 meters
	//Chunks will be defined (location wise) by their center
	//(0, 0) will be the origin of the center chunk
	
	private static BufferedImage dirt = null;
	
	public static final int sizeInUnits		= 16;
	public static final int sizeInMeters	= 8;
	
	//Chunk location in chunk coordinates
	int x;
	int y;
	
	//Chunk stuff
	World world;
	
	Body body = null;
	Vec2[] points_old = null;
	Color color = null;
	GroundSegment[] gss = null;
	
	//World data (in meters)
	Vec2 pos;
	Vec2 dim;
	
	
	public static void fetchImages()
	{
		dirt = Reader.loadImage("blocks/land/dirt_rec_w8_h8_t0.png");
	}
	
	//This chunk constuctor will make the origin chunk
	//This chunk should be created first
	public Chunk(World world)
	{
		this.world = world;
		
		this.x = 0;
		this.y = 0;
		
		this.pos = new Vec2(0, 0);
		this.dim = new Vec2(8, 8);
		
		this.createChunk(world);
	}
	//Eventually the chunk constructor should take adjacent chunks as arguments
	public Chunk(World world, int x, int y)
	{
		this.world = world;
		
		this.x = x;
		this.y = y;
		
		this.pos = new Vec2(x*sizeInMeters, 0);
		this.dim = new Vec2(8, 8);
		
		this.createFlatChunk(world);
	}
	
	
	private void createChunk(World world)
	{
		/* Notes:
		 * w in units == 16
		 * 1x1, 1x2, 1x3, 1x4 triangles are allowed.
		 * 1x1, 1x2, 1x3, 1x4 boxes are allowed.
		 * a unit of 1 is half a meter
		 */
		
		//Make start aligned to int system
		
		//Stuff I need to make the vertices
		ArrayList<IntPair> points = new ArrayList<IntPair>();
		ArrayList<Vec2> vecs = new ArrayList<Vec2>();
		ArrayList<GroundSegment> gssList = new ArrayList<GroundSegment>();
		//Xs & Yx in units not meters
		int px = -sizeInUnits/2;	//Chunk will run from -8 units to +8 units
		int py = 0;
		int xJump = 0;
		int yJump = 0;
		//Prime loop
		points.add(new IntPair(px, py));
		vecs.add(new Vec2(px/2f, py/2f));
		
		//Generate vertices
		while(px < (sizeInUnits/2) - 3)
		{
			//Pick x and y jumps
			xJump = this.pickXjump();
			yJump = this.pickYjump(xJump);
			
			//Create groundSegments
			gssList.add(new GroundSegment(px, py, xJump, yJump));
			
			//Increment x and y coordinates of last point
			px += xJump;
			py += yJump;
			
			//Add jumps to old points and join with lists
			points.add(new IntPair(px, py));
			vecs.add(new Vec2(px/2f, py/2f));
		}
		
		//Add that last shape (if needed)
		if (px != sizeInUnits/2)
		{
			//How far do we need to go?
			xJump = (sizeInUnits/2) - px;
			yJump = this.pickYjump(xJump);
			
			//Create groundSegments
			gssList.add(new GroundSegment(px, py, xJump, yJump));
			
			//Increment x and y coordinates of last point
			px += xJump;
			py += yJump;
			
			//Add jumps to old points and join with lists
			points.add(new IntPair(px, py));
			vecs.add(new Vec2(px/2f, py/2f));
		}
		
		//Convert ints to vecs
		points_old = vecs.toArray(new Vec2[vecs.size()]);
		
		//Create body
		BodyDef bd = new BodyDef();
		bd.setType(BodyType.STATIC);
		this.body = world.createBody(bd);
		//Create fixture
		ChainShape cs = new ChainShape();
		cs.createChain(points_old, points_old.length);
		FixtureDef fd = new FixtureDef();
		fd.setShape(cs);
		fd.setFriction(0.4f);
		this.body.createFixture(fd);
		
		//Set color
		this.color = new Color(0x09660d);
		
		//Create GroundSegments
		this.gss = gssList.toArray(new GroundSegment[gssList.size()]);
	}
	private void createFlatChunk(World world)
	{
		//Create 5 points
		points_old = new Vec2[5];
		for (int i = 0; i < 5; i++)
		{
			points_old[i] = new Vec2(this.pos.x - sizeInMeters/2 + i*2, 0);
		}
		
		//Create 4 1x4 flat blocks
		this.gss = new GroundSegment[4];
		for (int i = 0; i < 4; i++)
		{
			this.gss[i] = new GroundSegment(this.x*sizeInUnits - sizeInUnits/2 + i*4, 0, 4, 0);
		}
		
		
		//Create body
		BodyDef bd = new BodyDef();
		bd.setType(BodyType.STATIC);
		this.body = world.createBody(bd);
		//Create fixture
		ChainShape cs = new ChainShape();
		cs.createChain(points_old, points_old.length);
		FixtureDef fd = new FixtureDef();
		fd.setShape(cs);
		fd.setFriction(0.4f);
		this.body.createFixture(fd);
		
		//Set color
		this.color = new Color(0x09660d);
	}
	
	//Pick x jump between 1, 2, 3, 4
	private int pickXjump()	{ return PMath.random.nextInt(4) + 1; }
	private int pickYjump(int xJump)
	{
		if (xJump == 1)	//yJump can be 1, 2, 3, 4 or negative those
			return PMath.random.nextInt(9) - 4;
		else			//yJump can be 0, 1 or negative
			return PMath.random.nextInt(3) - 1;
	}
	
	public void render(Artist a)
	{
		//Render big dirt (temperary)
		Vec2 dirt1 = new Vec2(this.pos.x - Chunk.sizeInMeters/4, this.pos.y + Chunk.sizeInMeters/4);
		Vec2 dirt2 = new Vec2(this.pos.x + Chunk.sizeInMeters/4, this.pos.y + Chunk.sizeInMeters/4);
		
		a.drawImage(dirt, dirt1, this.dim.mul(0.5f), 0);
		a.drawImage(dirt, dirt2, this.dim.mul(0.5f), 0);
		
		//Draw ground segments
		for (int i = 0; i < gss.length; i++)
		{
			gss[i].render(a);
		}
		
//		this.debugRender(a);
	}
	
	@SuppressWarnings("unused")
	private void debugRender(Artist a)
	{
		//Draw Chunk border
		a.setStroke(0.01f);
		a.drawRec(this.pos, this.dim, 0, new Color(0x80000000, true));
		
		//Draw edge
//		a.setStroke(0.1f);
//		for (int i = points_old.length - 1; i > 0; i--)
//		{
//			a.drawLine(points_old[i], points_old[i-1], this.color);
//			a.fillCircle(points_old[i], 0.1f, Color.white);
//		}
	}
	
	public void delete()
	{
		this.world.destroyBody(this.body);
	}
}
