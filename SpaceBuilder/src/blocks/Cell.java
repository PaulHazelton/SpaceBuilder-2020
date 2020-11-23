package blocks;

import java.awt.Color;

import org.jbox2d.common.Vec2;

import framework.Artist;
import utility.Direction;
import utility.Edge;

// Each Block grid contains a 2D array of cells.
// Each Cell has information about a grid square of space.
public class Cell
{
	// Block pointer
	public Block block;
	
	private boolean occupied = false;
	private boolean[] buildEdges;
	
	// Debugging information (visuals)
	public static final boolean drawDebugInfo = false;
	private Color c;
	private Vec2 pos;
	private Vec2 dim;
	private Edge[] debugEdges; 
	
	public Cell(int x, int y)
	{
		this.pos = new Vec2(x*Block.scale, y*Block.scale);
		this.dim = new Vec2(Block.scale*0.2f, Block.scale*0.2f);
		this.c = new Color(255, 255, 255, 80);
		
		this.buildEdges = new boolean[4];
		
		for (int i = 0; i < 4; i++)
			buildEdges[i] = false;
		
		if (Cell.drawDebugInfo)
			createDebugEdges();
	}
	private void createDebugEdges()
	{
		// Creating edges for debugging
		this.debugEdges = new Edge[4];
		float d = Block.scale*0.4f;
		this.debugEdges[Direction.RIGHT.ordinal()]
				= new Edge(this.pos.add(new Vec2( d, -d)), this.pos.add(new Vec2( d,  d)));
		this.debugEdges[Direction.DOWN.ordinal()]
				= new Edge(this.pos.add(new Vec2(-d,  d)), this.pos.add(new Vec2( d,  d)));
		this.debugEdges[Direction.LEFT.ordinal()]
				= new Edge(this.pos.add(new Vec2(-d, -d)), this.pos.add(new Vec2(-d,  d)));
		this.debugEdges[Direction.UP.ordinal()]
				= new Edge(this.pos.add(new Vec2(-d, -d)), this.pos.add(new Vec2( d, -d)));
	}

	public void rotateCellCW()
	{
		boolean temp = this.buildEdges[3];
		this.buildEdges[3] = this.buildEdges[2];
		this.buildEdges[2] = this.buildEdges[1];
		this.buildEdges[1] = this.buildEdges[0];
		this.buildEdges[0] = temp;
	}
	
	public void applyCell(Cell c)
	{
		this.block = c.block;
		this.setOccupied(c.occupied);
		for (int i = 0; i < 4; i++)
			this.buildEdges[i] = c.buildEdges[i];
	}
	
	public void render(Artist a)
	{
		a.setStroke(0.04f);
		
		// Lil' indicator box
		a.fillRec(pos, dim, 0, c);
		
		// 4 buildEdges
		if (this.occupied)
		{
			for (int i = 0; i < 4; i++)
			{
				if (this.buildEdges[i])
					a.drawLine(this.debugEdges[i].a, this.debugEdges[i].b, Color.green);
			}
		}
	}
	
	public void setOccupied(boolean o)
	{
		this.occupied = o;
		if (o)
			this.c = new Color(0, 255, 0, 128);
		else
			this.c = new Color(255, 255, 255, 80);
	}
	public boolean isOccupied() { return this.occupied; }

	public void setEdge(Direction d, boolean buildable)
	{
		this.buildEdges[d.ordinal()] = buildable;
	}
	public void setEdges(boolean r, boolean d, boolean l, boolean u)
	{
		this.buildEdges[0] = r;
		this.buildEdges[1] = d;
		this.buildEdges[2] = l;
		this.buildEdges[3] = u;
	}
	public void setAllEdges(boolean buildable)
	{
		for (int i = 0; i < 4; i++)
			this.buildEdges[i] = buildable;
	}
	public boolean getEdge(Direction d)	{ return this.buildEdges[d.ordinal()]; }
}
