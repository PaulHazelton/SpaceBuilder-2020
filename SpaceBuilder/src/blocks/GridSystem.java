package blocks;

import java.io.Serializable;
import java.util.ArrayList;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.joints.Joint;

import framework.Artist;
import framework.InputHandler;

public class GridSystem implements Serializable
{
//Static fields
	//Class ID
	private static final long serialVersionUID = 1751228745252961497L;
	
//Collection of bodies and joints
	protected ArrayList<BlockGrid>	grids;
	protected ArrayList<Joint>		joints;

//Constructor
	public GridSystem()
	{
		grids = new ArrayList<BlockGrid>();
	}
	public GridSystem(BlockGrid bb)
	{
		grids = new ArrayList<BlockGrid>();
		this.grids.add(bb);
	}
	//For loading
//	public void addToWorld(World w)
//	{
//		for (BlockGrid bg : this.grids)
//		{
//			bg.addToWorld(w);
//		}
//	}
	
//Loop stuff
	//Update
	public void update(double timePassed, InputHandler ih)
	{
		for (BlockGrid bg : this.grids)
			bg.update(timePassed, ih);
	}
	//Render
	public void render(Artist a)
	{
		for (BlockGrid bg : this.grids)
			bg.render(a);
	}
	
//Getters and Setters
	//Position
	public Vec2 getCenter()						{ return this.grids.get(0).getCenter(); }
	//Body
	public BlockGrid getBlockGrid(int index)	{ return this.grids.get(index); }
}
