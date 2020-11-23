package blocks;

import java.awt.Color;
import java.awt.geom.Path2D;
import java.io.Serializable;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

//import building.ConnectionPoint;
//import building.Builder;
import framework.Artist;
import framework.InputHandler;
import utility.Direction;
import utility.PMath;


public abstract class Block implements Serializable, Cloneable
{
	//Class ID
	private static final long	serialVersionUID = -6662033880703916284L;
	
	//Scale of every block (0.5 means that a 1x1 block is 0.5m by 0.5m
	public static final float	scale = 0.5f;
	
//Block Details
	//Physics Info
	private Vec2			cen;
//	private float			angle;
	private	Direction		direction;
	private Vec2			dim;
	//Who it belongs to
	protected			SpaceShip		ship;
	protected			BlockGrid		blockGrid;
	protected transient	Body			body;
	//What this is
	protected transient	Fixture			fixture;
	protected transient	FixtureDef		fixtureDef;
	private				Path2D.Float	path;
	private				ShapeType		shapeType;
	//Controlling
	public boolean controlling = false;
	
	// For building
	protected Cell[][] cells;
	// Real block or not
	// private boolean isGhost = false;
	// Dimentions
	protected int x;
	protected int y;
	protected int w;
	protected int h;
	
	//Asthetics
	protected Color color = Color.white;
	
	// Constructor
	public Block(ShapeType  st, int x, int y, Direction d, int w, int h)
	{
		this.createBlock(st, x, y, d, w, h);
	}
	private void	createBlock(ShapeType  st, int x, int y, Direction d, int w, int h)
	{
		if (h > w)
			System.err.println("@Block.createBlock(): Warning! h > w !");
		
		this.shapeType = st;
		
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		
		
		float ww = w*scale;
		float hh = h*scale;
		float xx, yy;
		if (d == Direction.RIGHT || d == Direction.LEFT)
		{
			xx = scale*(x + (w - 1)/2.0f);
			yy = scale*(y + (h - 1)/2.0f);
			this.cells = new Cell[w][h];
		}
		else
		{
			xx = scale*(x + (h - 1)/2.0f);
			yy = scale*(y + (w - 1)/2.0f);
			this.cells = new Cell[h][w];
		}
		
		// Initialize all cells
		for (int i = 0; i < this.cells.length; i++) {
		for (int j = 0; j < this.cells[i].length; j++)
		{
			this.cells[i][j] = new Cell(i, j);
			this.cells[i][j].block = this;
			this.cells[i][j].setOccupied(true);
		}}
		
		// Marking buildable edges
		for (int i = 0; i < this.cells.length; i++)
		{
			this.cells[i][0].setEdge(Direction.UP, true);
			this.cells[i][cells[i].length-1].setEdge(Direction.DOWN, true);
		}
		for (int i = 0; i < this.cells[0].length; i++)
		{
			this.cells[0][i].setEdge(Direction.LEFT, true);
			this.cells[cells.length-1][i].setEdge(Direction.RIGHT, true);
		}
		
		this.cen = new Vec2(xx, yy);
		this.dim = new Vec2(ww, hh);
		this.direction = d;
		
		this.createFixtureDef();
	}
	private void	createFixtureDef()
	{
		Shape s = this.createPolygons(this.cen.x, this.cen.y, this.dim.x, this.dim.y);
		
		fixtureDef = new FixtureDef();
		fixtureDef.setShape(s);
		fixtureDef.setDensity(100);	//kg/m^2
		
		fixtureDef.setRestitution(0.1f);
		fixtureDef.setFriction(0.9f);
	}
	private Shape	createPolygons(float x, float y, float w, float h)
	{
		Vec2[][] points = new Vec2[2][];
		
		float hw = w/2f;
		float hh = h/2f;
		
		for (int i = 0; i < 2; i++)
		{
			switch(this.shapeType)
			{
			case REC:
				points[i] = new Vec2[] {
						new Vec2((x - hw), (y - hh)),
						new Vec2((x - hw), (y + hh)),
						new Vec2((x + hw), (y + hh)),
						new Vec2((x + hw), (y - hh)),	};
				break;
			case TRI:
				points[i] = new Vec2[] {
						new Vec2((x - hw), (y - hh)),
						new Vec2((x - hw), (y + hh)),
						new Vec2((x + hw), (y + hh)),	};
//						new Vec2((x + hw), (y - hh)),	};
				break;
			case TRI_M:
				points[i] = new Vec2[] {
//						new Vec2((x - hw), (y - hh)),
						new Vec2((x - hw), (y + hh)),
						new Vec2((x + hw), (y + hh)),
						new Vec2((x + hw), (y - hh)),	};
				break;
			}
			
			//Rotate points
			for (int j = 0; j < points[i].length; j++)
			{
				points[i][j] = PMath.rotateVecAbout(points[i][j], this.cen, this.direction.angle);
			}
			
			//Polygon in the back for color
			y += 0.01f;
			
			hw = hw * 0.95f;
			hh = hh - 0.02f;
		}
		
		PolygonShape s = new PolygonShape();
		s.set(points[0], points[0].length);
			
		//Creating the polygon using points2		
		this.path = new Path2D.Float();
		
		path.moveTo(points[1][0].x, points[1][0].y);
		for (int i = 1; i < points[1].length; i++)
		{
			path.lineTo(points[1][i].x, points[1][i].y);
		}
		path.closePath();
		
		return s;
	}

	protected abstract void create();
	
	protected void mirrorCells()
	{
		boolean temp;
		// Swap left and right
		for (int i = 0; i < h; i++)
		{
			temp = cells[0][i].getEdge(Direction.LEFT); 
			cells[0][i].setEdge(Direction.LEFT, cells[w-1][i].getEdge(Direction.RIGHT));
			cells[w-1][i].setEdge(Direction.RIGHT, temp);
		}
		// Flip top and bottom
		for (int i = 0; i < w/2; i++)
		{
			temp = cells[i][0].getEdge(Direction.UP);
			cells[i][0].setEdge(Direction.UP, cells[w-1 - i][0].getEdge(Direction.UP));
			cells[w-1 - i][0].setEdge(Direction.UP, temp);
			
			temp = cells[i][h-1].getEdge(Direction.UP);
			cells[i][0].setEdge(Direction.UP, cells[w-1 - i][h-1].getEdge(Direction.UP));
			cells[w-1 - i][h-1].setEdge(Direction.UP, temp);
		}
		
	}
	protected void rotateCellsCW()
	{
		// ASSUMPTIONS: We only care about the edges
		
		int w = this.cells.length;
		int h = this.cells[0].length;
		
		// Create new cell array with transposed width and height
		Cell[][] newCells = new Cell[h][w];
		boolean[] edgeList = new boolean[w*2 + h*2];
		
		// Initialize cells
		for (int i = 0; i < h; i++) {
		for (int j = 0; j < w; j++)
		{
			newCells[i][j] = new Cell(i, j);
			newCells[i][j].setOccupied(true);
		}}
		
		// Populate edgeList with edges from this.cells
		// Top and bottom edges
		for (int i = 0; i < w; i++)
		{
			edgeList[i] = this.cells[i][0].getEdge(Direction.UP);
			edgeList[w + h + i] = this.cells[w-1 - i][h-1].getEdge(Direction.DOWN);
		}
		// Left and right edges
		for (int i = 0; i < h; i++)
		{
			edgeList[w + i] = this.cells[w-1][i].getEdge(Direction.RIGHT);
			edgeList[2*w + h + i] = this.cells[0][h-1 - i].getEdge(Direction.LEFT);
		}
		
		// Apply edges, rotated cw, to newCells
		// Right and Left
		for (int i = 0; i < w; i++)
		{
			newCells[h-1][i].setEdge(Direction.RIGHT, edgeList[i]);
			newCells[0][w-1 - i].setEdge(Direction.LEFT, edgeList[w+h + i]);
		}
		// Top and Bottom
		for (int i = 0; i < h; i++)
		{
			newCells[i][0].setEdge(Direction.UP, edgeList[2*w + h + i]);
			newCells[h-1 - i][w-1].setEdge(Direction.DOWN, edgeList[w + i]);
		}
		
		this.cells = newCells;
	}
	
	// For loading
	public void addToWorld(Body b)
	{
		this.createFixtureDef();
		this.fixture = b.createFixture(this.fixtureDef);
		this.body = b;
	}
	// For building
	public abstract Block copy();
	
	// Loop
	public abstract void update(double timePassed, InputHandler ih);
	public abstract void render(Artist a);
	
	//Ghost
//	public Builder		getGhost()			{ return this.ghost; }
//	public boolean		isGhost()			{ return this.isGhost; }
//	public void			setGhost(Builder gb)
//	{
//		this.isGhost = true;
//		this.ghost = gb;
//		
//		Shape s = this.createPolygons(this.cen.x, this.cen.y, this.dim.x*0.8f, this.dim.y*0.8f);
//		this.getFD().setShape(s);
//	}
//	public void			setGhostAngle(float a)
//	{
//		if (!this.isGhost)
//		{
//			System.err.println("@Block.setGhostAngle: This is not a ghost!");
//			return;
//		}
//		
//		this.angle = a;
//		this.direction = PMath.nearestOrthogonal(a);
//		this.createConnectionPoints();
//	}
	
//Getters and Setters
	//Space Ship
	public void			setSpaceShip(SpaceShip ship)	{ this.ship = ship; }
	public SpaceShip	getSpaceShip()					{ return this.ship; }
	//Position and size
	public Vec2			getCen()			{ return this.cen; }
	public float		getAngle()			{ return this.direction.angle; }
	public Direction	getDirection()		{ return this.direction; }
	public Vec2			getDim()			{ return this.dim; }
	//World position
	public Vec2			getWorldCenter()
	{
		return this.getBody().getWorldVector(this.getCen())
				.add(this.getBody().getPosition());
	}
	public float		getWorldAngle()
	{
		if (this.getBody() == null)
			return 0;
		return this.getBody().getAngle() + this.direction.angle;
	}
	//Fixture
	public FixtureDef	getFD()				{ return this.fixtureDef; }
	public Fixture		getFixture()		{ return this.fixture; }
	public void			setFixture(Fixture f)	{ this.fixture = f; }
	//Shape
	public ShapeType	getShapeType()		{ return this.shapeType; }
	public Path2D.Float getPath()			{ return this.path; }
	//Area
	public float		getArea()			{ return this.getDim().x*this.getDim().y; }
	//Body
	public BlockGrid	getBlockGrid()				{ return this.blockGrid; }
	public void			setBlockGrid(BlockGrid bg)	{ this.blockGrid = bg; }
	public void			setBody(Body body)	{ this.body = body; }
	public Body			getBody()			{ return this.body; }
	// Color
	public void			setColor(Color c)
	{
		this.color = c;
	}
	// Cells
	public Cell[][]		getCells() { return this.cells; }
}