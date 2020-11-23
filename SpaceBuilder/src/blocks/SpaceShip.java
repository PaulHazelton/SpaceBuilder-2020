package blocks;

import java.io.Serializable;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

import menus.Command;
import menus.Commandable;
import utility.Watchable;

public class SpaceShip extends GridSystem implements Serializable, Watchable, Commandable
{
//Static fields
	//Class ID
	private static final long serialVersionUID = -2878897458702296734L;
	
	//Control
	private boolean active = false;
	
//Space Ship contents
	//Fuel
	private float	fuel;
	private float	fuelCapacity;
	private int		numOfTanks;
	//Energy
	private float	energy;
	private float	energyCapacity;
	private int		numOfBatteries;
	
//Constructors
	public SpaceShip()
	{
		
	}
	public SpaceShip(World w, Vec2 position, float angle)
	{
		this.grids.add(new BlockGrid(w, this, BodyType.DYNAMIC, position, angle));
	}

//Adding blocks and keeping track of what's here
//	public void addBlock(int bodyIndex, Command blockCom, int x, int y, Direction d, int w, int h)
//	{
//		this.addBlock(this.grids.get(bodyIndex), blockCom, x, y, d, w, h, false, Color.white);
//	}
//	public void addBlock(int bodyIndex, Command blockCom, int x, int y, Direction d, int w, int h, Color color)
//	{
//		this.addBlock(this.grids.get(bodyIndex), blockCom, x, y, d, w, h, false, color);
//	}
//	public void addBlock(int bodyIndex, Command blockCom, int x, int y, Direction d, int w, int h, boolean m, Color color)
//	{
//		this.addBlock(this.grids.get(bodyIndex), blockCom, x, y, d, w, h, m, color);
//	}
//	public void addBlock(BlockGrid bg, Command blockCom, int x, int y, Direction d, int w, int h, boolean m, Color color)
	public void addBlock(int index, Block b)
	{ this.addBlock(this.getBlockGrid(index), b); }
	public void addBlock(BlockGrid bg, Block b)
	{
//		Block b = bg.addBlock(this, blockCom, x, y, d, w, h, m, color);
		bg.addBlock(this, b);
		
		if(b.getClass() == Tank.class)
		{
			Tank t = (Tank)b;
			
			this.numOfTanks++;
			
			this.fuel			+= t.getFuel();
			this.fuelCapacity	+= t.getFuelCapacity();
		}
		else if(b.getClass() == Battery.class)
		{
			Battery t = (Battery)b;
			
			this.numOfBatteries++;
			
			this.energy			+= t.getEnergy();
			this.energyCapacity	+= t.getEnergyCapacity();
		}
	}
//	public void attachBlock(ConnectionPoint ghostPoint, ConnectionPoint shipPoint)
//	{
//		//Ensure that ghost and ship are correct
//		if (!ghostPoint.parent.isGhost())	{ System.err.println("ghostPoint does not belong to a ghost!"); return; }
//		if (shipPoint.parent.ship != this)	{ System.err.println("shipPoint does not belong to this ship!"); return ; }
//		
//		//Shorthand
//		Block b = ghostPoint.parent;
//		
//		//Moving to correct spot
//		Vec2 newBlockSpot = shipPoint;
//		Vec2 relativeCP = ghostPoint.sub(b.getCen());
//		newBlockSpot = newBlockSpot.sub(relativeCP);
//		
//		b.recreateBlock(newBlockSpot);
//		
//		
//		//Eliminating connection points
//		ConnectionPoint[] cps = b.getConnectionPoints();	//ghost block's connection points
//		
//		for (int i = 0; i < cps.length; i++)
//		{
//			ConnectionPoint gp = cps[i];	//Ghost point
//			ConnectionPoint sp = shipPoint.parent.getSpaceShip().getNearestConnectionPoint(gp);	//ship point
//			
//			if (gp == null || sp == null)
//				continue;
//			
//			//If ghost point and ship point are basically the same point, deactivate
//			if (PMath.zequals(gp, sp))
//			{
//				gp.active = false;
//				sp.active = false;
//			}
//		}
//		
//		
//		//Adding
//		this.addBlock(shipPoint.parent.getBlockGrid(), b);
//	}
	
	// Game Loop stuff
	// InputEvent
//	@Override
//	public void inputEvent(InputEvent item)
//	{
//		if (item.type == EventType.KEY_PRESSED)
//		{
//			if (item.code == KeyEvent.VK_G)
//				this.toggleAllGenerators();
//		}
//	}
	// Update
//	@Override
//	public void update(double timePassed, InputHandler ih)
//	{
//		super.update(timePassed, ih);
//	}
//	// Render
//	public void render(Artist a)
//	{
//		for (BlockGrid bg : this.grids)
//			bg.render(a);
//	}
	
//	public void	renderConnectionPoints(Artist a)
//	{
//		for (BlockBody bb : this.grids)
//		{
//			bb.renderConnectionPoints(a);
//		}
//	}

	// Space ship operations
	@Override
	public void tapCommand(Command c)
	{
		
	}
	@Override
	public void pressCommand(Command c)
	{
		System.out.println("@SpaceShip.pressCommand c = " + c.toString());
		
		if (c == Command.KEY_G)
			this.toggleAllGenerators();
	}
	@Override
	public void releaseCommand(Command c)
	{
		
	}
	// Generator
	public void toggleAllGenerators()
	{
		for (BlockGrid bg : this.grids)
		{
			for (int i = 0; i < bg.getNumOfBlocks(); i++)
			{
				Block b = bg.getBlock(i);
				
				if (b.getClass() == Generator.class)
				{
					Generator g = (Generator)b;
					g.toggle();
				}
			}
		}
	}
	// Fuel
	public float	takeFuel(float request)
	{
		float fuelTook;
		if (this.fuel >= request)
		{
			fuelTook = request;
			this.fuel -= request;
		}
		else
		{
			fuelTook = this.fuel;
			this.fuel = 0;
		}
		
		for (BlockGrid bg : this.grids)
		{
			for (Block b : bg.getBlocks())
			{
				if (b.getClass() != Tank.class)
					continue;
				
				Tank t = (Tank)b;
				
				t.takeFuel(request/(float)this.numOfTanks);
			}	
		}
		
		return fuelTook;
	}
	// Energy
	public float	takeEnergy(float request)
	{
		float energyTook;
		if (this.energy >= request)
		{
			energyTook = request;
			this.energy -= request;
		}
		else
		{
			energyTook = this.energy;
			this.energy = 0;
		}
		
		for (BlockGrid bg : this.grids)
		{
			for (Block b : bg.getBlocks())
			{
				if (b.getClass() != Battery.class)
					continue;
				
				Battery t = (Battery)b;
				
				t.takeEnergy(request/(float)this.numOfBatteries);
			}	
		}
		
		return energyTook;
	}
	public void		giveEnergy(float energy)
	{
		for (BlockGrid bg : this.grids)
		{
			for (Block b : bg.getBlocks())
			{
				if (b.getClass() != Battery.class)
					continue;
				
				Battery t = (Battery)b;
				
				float e = energy/(float)this.numOfBatteries;
				this.energy += t.giveEnergy(e);
			}	
		}
	}
	
//Building
//	public ConnectionPoint getNearestConnectionPoint(Vec2 localPoint)
//	{
//		List<Block> blocks = this.getNearestBlocks(this.getBlockBody(0), 4, localPoint);
//		ArrayList<ConnectionPoint> cpts = new ArrayList<ConnectionPoint>();
//		
//		for (Block b : blocks)
//		{
//			b.renderConnectionPoints = true;
//			
//			ConnectionPoint p = b.getNearestConnectionPoint(localPoint);
//			
//			if (p != null)
//				cpts.add(p);
//		}
//
//		ConnectionPoint[] cp_array = new ConnectionPoint[0];
//		cp_array = cpts.toArray(cp_array);
//
//		ConnectionPoint cpt = ConnectionPoint.getNearest(cp_array, localPoint);
//		
//		return cpt;
//	}
//	public ConnectionPoint getNearestConnectionPoint(Vec2 localPoint, Direction d)
//	{
//		List<Block> blocks = this.getNearestBlocks(this.getBlockBody(0), 4, localPoint);
//		ArrayList<ConnectionPoint> cpts = new ArrayList<ConnectionPoint>();
//		
//		for (Block b : blocks)
//		{
//			ConnectionPoint p = b.getNearestConnectionPoint(localPoint, d);
//			
//			if (p != null)
//				cpts.add(p);
//		}
//
//		ConnectionPoint[] cp_array = new ConnectionPoint[0];
//		cp_array = cpts.toArray(cp_array);
//
//		ConnectionPoint cpt = ConnectionPoint.getNearest(cp_array, localPoint);
//		
//		return cpt;
//	}
//	private List<Block> getNearestBlocks(BlockGrid bg, int count, Vec2 localPoint)
//	{
//		List<Block> blocks = new ArrayList<Block>();
//		
//		//Getting distances
//		for (Block b : bg.getBlocks())
//		{
//			b.squaredDistance = (float) PMath.squaredDistance(b.getCen(), localPoint);
//			
////			if (b.squaredDistance < Math.pow((Builder.rootBuildDistance + b.getDim().x), 2))
//			if (b.squaredDistance < Builder.buildDistanceSq)
//			{
//				blocks.add(b);
//				b.renderConnectionPoints = true;
//			}
//			else
//				b.renderConnectionPoints = false;
//		}
//		
//		//Sorting list
//		Collections.sort(blocks);
//
//		//Returning sublist of sorted list
//		count = PMath.limit(count, 0, blocks.size());
//		
//		blocks = blocks.subList(0, count);
//		
//		return blocks;
//	}
	
//Getters and setters
	//Controlling
	public void setControlling(boolean c)
	{
		for (BlockGrid bg : this.grids)
		{
			for (Block b : bg.getBlocks())
			{
				b.controlling = c;
			}
		}
	}
	public boolean getControlling()	{ return this.active; }
	
	//Watching
	public Vec2 getWorldCenter()	{ return this.getBlockGrid(0).getBody().getWorldCenter(); }
	public float getWorldAngle()	{ return this.getBlockGrid(0).getBody().getAngle(); }
	//Fuel
	public float	getFuel()			{ return this.fuel; }
	public float	getFuelCapacity()	{ return this.fuelCapacity; }
	public int		getNumOfTanks()		{ return this.numOfTanks; }
	//Energy
	public float	getEnergy()			{ return this.energy; }
	public float	getEnergyCapacity()	{ return this.energyCapacity; }
	public int		getNumOfBatteries()	{ return this.numOfBatteries; }
	//Physics
	public float	getMass(int bodyIndex)
	{
		return this.getBlockGrid(bodyIndex).getBody().getMass();
	}
}
