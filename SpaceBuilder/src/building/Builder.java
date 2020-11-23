package building;

import java.awt.Color;

import org.jbox2d.common.Vec2;

import blocks.Block;
import blocks.BlockGrid;
import blocks.Hull;
import blocks.ShapeType;
import framework.Artist;
import framework.InputHandler;
import menus.Command;
import menus.Commandable;
import utility.Direction;
import utility.IntPair;

public class Builder implements Commandable
{
	// World Position
	public Vec2 pos;
	private IntPair intPos;
	public Direction d = Direction.RIGHT;
	public int xx, yy;
	public int w, h;
	
	// TODO I'm gonna start with a simple case.
	// The builder is constructed with a blockGrid sent.
	// This is the only grid to build on.
	// Also, you can only build one type of block.
	// This needs to be fixed when the builder is working.
	// DEBUG CRAP
	private BlockGrid bg;
	private Block b;
	
	public Builder(BlockGrid bg)
	{
		this.bg = bg;
		this.bg.setBuilder(this);
		this.pos = new Vec2(0, 0);
		this.intPos = new IntPair();
		this.w = 3;
		this.h = 1;
		this.b = new Hull(ShapeType.REC, 0, 0, 0, Direction.RIGHT, w, h);
	}
	
	public void update(double timePassed, InputHandler ih)
	{
		this.pos = ih.getMouseWorld();
		if (this.intPos.update(bg.mouseToGridXY(pos).x, bg.mouseToGridXY(pos).y))
		{
			if (d == Direction.RIGHT || d == Direction.LEFT)
			{
				xx = intPos.x - (w-1)/2;
				yy = intPos.y - (h-1)/2;
			}
			else
			{
				xx = intPos.x - (h-1)/2;
				yy = intPos.y - (w-1)/2;
			}
			this.b = new Hull(ShapeType.REC, 2, xx, yy, d, w, h);
		}
	}
	
	public void render(Artist a)
	{
//		a.setLocalSpace(new Vec2(intPos.x*Block.scale, intPos.y*Block.scale).add(bg.getPosition()), bg.getAngle());
//		this.b.render(a);
//		a.fillCircle(pos, 0.1f, Color.magenta);
//		a.resetLocalSpace();
	}
	
	// TODO this is janky
	public Block getBlock() { return this.b; }

	@Override
	public void tapCommand(Command c)
	{
		
	}

	@Override
	public void pressCommand(Command c)
	{
		switch(c)
		{
		case ROTATE_LEFT_B:		this.d = Direction.rotateCCW(d);	break;
		case ROTATE_RIGHT_B:	this.d = Direction.rotateCW(d);		break;
		case WIDTH_UP:			this.w++;	break;
		case WIDTH_DOWN:		this.w--;	break;
		case HEIGHT_UP:			this.h++;	break;
		case HEIGHT_DOWN:		this.h--;	break;
//		case ROTATE_LEFT_B:		this.b = new Hull(ShapeType.REC, 0, intPos.x, intPos.y, Direction.RIGHT, 3, 1); break;
//		case ROTATE_RIGHT_B:	this.b = new Hull(ShapeType.REC, 0, intPos.x, intPos.y, Direction.UP, 3, 1); break;
		case ADD_BLOCK: this.bg.addBlock(bg.getShip(), b);
		}
		
		if (d == Direction.RIGHT || d == Direction.LEFT)
		{
			xx = intPos.x - (w-1)/2;
			yy = intPos.y - (h-1)/2;
		}
		else
		{
			xx = intPos.x - (h-1)/2;
			yy = intPos.y - (w-1)/2;
		}
		this.b = new Hull(ShapeType.REC, 2, xx, yy, d, w, h);
	}

	@Override
	public void releaseCommand(Command c)
	{
		
	}
}