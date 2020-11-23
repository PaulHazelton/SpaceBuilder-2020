package blocks;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

import building.Builder;
import framework.Artist;
import framework.InputHandler;
import menus.CommCatagory;
import menus.Command;
import utility.Direction;
import utility.IntPair;
import utility.PMath;

public class BlockGrid implements Serializable
{
	// Class ID. If this changes, saving and loading will be a pain
	private static final long serialVersionUID = 3687451002763050413L;
	
	// Box2D components
	private BodyDef		bodyDef;
	private Body		body;
	
	// List of blocks
	private ArrayList<Block> blocks;
	
	// 2D Array of cells
	private static final int chunkSize = 16;
	private Cell[][] cells; 
	
	// Building
	// TODO janky
	private SpaceShip ship;
	private Builder builder;
	private int mx, my;
	private boolean doHovering = true;
	
	// Constructors
	public BlockGrid(World w, SpaceShip ship, BodyType bt, Vec2 pos, float a)
	{ createBlockGrid(w, ship, bt, pos, a); }
	public BlockGrid(World w, SpaceShip ship, BodyType bt, float x, float y, float a)
	{ createBlockGrid(w, ship, bt, new Vec2(x, y), a); }
	private void createBlockGrid(World w, SpaceShip ship, BodyType bt, Vec2 pos, float a)
	{
		this.ship = ship;
		
		bodyDef = new BodyDef();
		bodyDef.setPosition(pos);
		bodyDef.setAngle(a);
		bodyDef.setType(bt);
		
		body = w.createBody(bodyDef);
		
		blocks = new ArrayList<Block>();
		cells = new Cell[chunkSize][chunkSize];
		for (int i = 0; i < chunkSize; i++) {
		for (int j = 0; j < chunkSize; j++)
		{
			cells[i][j] = new Cell(i, j);
		}}
	}
	
	// Loop stuff
	// Update
	public void update(double timePassed, InputHandler ih)
	{
		// Update blocks
		for (Block b : this.blocks)
			b.update(timePassed, ih);
		
		// TODO Debugging
		IntPair ip = this.mouseToGridXY(ih.getMouseWorld());
		mx = ip.x;
		my = ip.y;
	}
	// Render
	public void render(Artist a)
	{
		a.setLocalSpace(this.getPosition(), this.getAngle());
		
		// Render all blocks
		for (Block b : this.blocks)
			b.render(a);
		
		// Render grid (debugging)
		if (Cell.drawDebugInfo)
		{
			for (int i = 0; i < chunkSize; i++) {
			for (int j = 0; j < chunkSize; j++)
			{
				this.cells[i][j].render(a);
			}}
			
			// Render mouse position
			if (this.doHovering)
			{
				a.setStroke(0.2f);
				a.drawRec(new Vec2(mx, my).mulLocal(Block.scale), new Vec2(0.5f, 0.5f).mulLocal(Block.scale), 0, Color.yellow);
			}
		}
		
		a.setOpacity(0.5f);
		this.builder.getBlock().render(a);
		if (!placeable(builder.getBlock()))
			a.fillRec(builder.getBlock().getCen(), builder.getBlock().getDim(), builder.getBlock().getAngle(), new Color(255, 0, 0, 128));
		a.resetOpacity();
		
		a.resetLocalSpace();
	}

	// Block handling
//	public Block addBlock(SpaceShip ship, Command blockCom, int x, int y, Direction d, int w, int h, boolean m, Color c)
	public Block addBlock(SpaceShip ship, Block b)
	{
		// Check if placeable
		if (!placeable(b) && this.blocks.size() > 0)
		{
			System.err.println("@BlockGrid.addBlock(): cannot place block");
			return null;
		}
		
		// Create block
//		Block b = null;
//		switch(blockCom)
//		{
//		case HULL_REC:			b = new Hull(ShapeType.REC, 0, x, y, d, w, h);	break;
//		case THRUSTER:			b = new Thruster(x, y, d, w, h);				break;
//		case THRUSTER_ROTOR:	b = new RotorThruster(x, y, d, w, h);			break;
//		case GYRO:				b = new Gyro(x, y, d, w, h);					break;
//		case TANK:				b = new Tank(x, y, d, w, h);					break;
//		case GENERATOR:			b = new Generator(x, y, d, w, h);				break;
//		case BATTERY:			b = new Battery(x, y, d, w, h);					break;
//		case CONTAINER:			b = new Container(x, y, d, w, h);				break;
//		case SOLAR_BLOCK:		b = new SolarBlock(x, y, d, w, h);				break;
//		case CHAIR:				b = new Chair(x, y, d, m);						break;
//		default:
//			System.out.println("@BlockGrid.addBlock(): Block not supported!");
//			return null;
//		}
		
		b.setSpaceShip(ship);
//		b.setColor(c);
		b.setFixture(this.body.createFixture(b.getFD()));
		b.setBody(this.body);
		b.setBlockGrid(this);
		this.blocks.add(b);
		
//		if (d == Direction.UP || d == Direction.DOWN)
//		{
//			int temp = h;
//			h = w;
//			w = temp;
//		}
		
		// Apply cells
		this.applyCells(b.getCells(), b.x, b.y);
		
		return b;
	}
	
	private void applyCells(Cell[][] blockCells, int x, int y)
	{
		for (int i = 0; i < blockCells.length; i++) {
		for (int j = 0; j < blockCells[i].length; j++)
		{
			this.cells[x + i][y + j].applyCell(blockCells[i][j]);
		}}
	}
	
	@Deprecated
	private boolean placeable(Command blockCom, int x, int y, Direction d, int w, int h)
	{
		if (blockCom.catagory() != CommCatagory.BLOCK)
			return false;
		
		if (d == Direction.UP || d == Direction.DOWN)
		{
			int temp = h;
			h = w;
			w = temp;
		}
		
		// Check if space is occupied
		for (int i = 0; i < w; i++) {
		for (int j = 0; j < h; j++)
		{
			if (x + i < 0 || x + i >= chunkSize)
			{
				System.err.println("@BlockGrid.placeable(): placing out of bounds.");
				return false;
			}
			if(this.cells[x + i][y + j].isOccupied())
			{
				System.err.println("@BlockGrid.placeable(): block(s) are occupied.");
				return false;
			}
		}}
		
		return true;
	}
	public boolean placeable(Block b)
	{
		int x = b.x;
		int y = b.y;
		int w = b.w;
		int h = b.h;
		if (b.getDirection() == Direction.UP || b.getDirection() == Direction.DOWN)
		{
			int temp = h;
			h = w;
			w = temp;
		}
		
		// Check if space is occupied
		for (int i = 0; i < w; i++) {
		for (int j = 0; j < h; j++)
		{
			if (x + i < 0 || x + i >= chunkSize || y + j < 0 || y + j >= chunkSize)
			{
				System.err.println("@BlockGrid.placeable(): placing out of bounds.");
				return false;
			}
			if(this.cells[x + i][y + j].isOccupied())
			{
				System.err.println("@BlockGrid.placeable(): block(s) are occupied.");
				return false;
			}
		}}
		
		// Check if any valid connections line up
		// If there is a single match, return true
		// TODO out of bounds issues
		if (x < 1 || y < 1)
			return false;
		if (x + w >= chunkSize || y + h >= chunkSize)
			return false;
		
		for (int i = 0; i < w; i++)
		{
			// Top
			if (this.cells[x + i][y - 1].getEdge(Direction.DOWN) && b.cells[i][0].getEdge(Direction.UP))
				return true;
			// Bottom
			if (this.cells[x + i][y + h].getEdge(Direction.UP) && b.cells[i][h-1].getEdge(Direction.DOWN))
				return true;
		}
		for (int i = 0; i < h; i++)
		{
			// LEFT
			if (this.cells[x - 1][y + i].getEdge(Direction.RIGHT) && b.cells[0][i].getEdge(Direction.LEFT))
				return true;
			// RIGHT
			if (this.cells[x + w][y + i].getEdge(Direction.LEFT) && b.cells[w-1][i].getEdge(Direction.RIGHT))
				return true;
		}
		
		return false;
	}
	
	
	public IntPair mouseToGridXY(Vec2 mouseWorld)
	{
		IntPair pair = new IntPair();
		Vec2 mPos = mouseWorld.clone();
		mPos.subLocal(this.getPosition());
		mPos = PMath.rotateVec(mPos, -this.getAngle());
		mPos.mulLocal(1/Block.scale);
		mPos.addLocal(new Vec2 (Block.scale, Block.scale));
		pair.x = (int)mPos.x;
		pair.y = (int)mPos.y;
		if (mPos.x < 0)
			pair.x -= 1;
		if (mPos.y < 0)
			pair.y -= 1;
		
		return pair;
	}
	
	// Getters and Setters
	// TODO janky
	public SpaceShip getShip() { return this.ship; }
	public void setBuilder(Builder b) { this.builder = b; }
	
	public Body getBody()	{ return this.body; }
	// Blocks
	public int 				getNumOfBlocks()	{ return this.blocks.size(); }
	public Block			getBlock(int index)	{ return this.blocks.get(index); }
	public ArrayList<Block> getBlocks()			{ return this.blocks; }
	// Location
	public Vec2		getPosition()	{ return this.body.getPosition(); }
	public Vec2		getCenter()		{ return this.body.getWorldCenter(); }
	public float	getAngle()		{ return this.body.getAngle(); }
	
	public Vec2		getLocalPoint(Vec2 point)
	{
		return this.body.getLocalPoint(point);
	}
}