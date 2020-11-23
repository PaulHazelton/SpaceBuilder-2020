package planets;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import framework.Artist;
import utility.PMath;

public class ChunkLoop
{
	private int length;
	private int pivot;
	private int cycles;
	private int centerChunk;
	
	private Chunk[] chunks;
	
	private World world;
	
	public ChunkLoop(World world, int length)
	{
		this.world = world;
		
		this.length = length;
		this.pivot = 0;
		this.cycles = 0;
		this.centerChunk = this.length/2;	//9/2 = 4
		
		this.chunks = new Chunk[this.length];
		
		for (int i = 0; i < this.length; i++)
		{
			this.chunks[i] = new Chunk(world, i, 0);
		}
	}
	
	public void update(Vec2 camCenter)
	{
		int diff = calcCenterChunk(camCenter) - this.centerChunk;

		if (diff == 0)
			return;
		else if (diff > 0)
		{
			for (int i = 0; i < diff; i++)
				rollRight();
		}
		else if (diff < 0)
		{
			for (int i = 0; i > diff; i--)
				rollLeft();
		}
	}
	
	private int calcCenterChunk(Vec2 camCenter)
	{
		//Calc center chunk based on camCenter
		float x = camCenter.x;
		if (x >= 0)
			x += Chunk.sizeInMeters/2;	//0 is 4 meters into the chunk
		else
			x -= Chunk.sizeInMeters/2;
		return (int)x/Chunk.sizeInMeters;
	}
	
	public void rollRight()
	{
		//Change cell then advance pivot
		//x = i + (cylces+1)*9
		this.chunks[this.pivot].delete();
		this.chunks[this.pivot] = new Chunk(this.world, this.pivot + (cycles+1)*this.length, 0);
		
		//Move pivot
		this.pivot++;
		if (this.pivot == this.length)
		{
			this.cycles++;
			this.pivot = PMath.arrayMod(this.pivot, this.length);
		}
		
		this.centerChunk++;
	}
	public void rollLeft()
	{
		//Move pivot then change cell
		this.pivot--;
		if (this.pivot < 0)
		{
			this.cycles--;
			this.pivot = PMath.arrayMod(this.pivot, this.length);
		}
		
		//x = i + cyles*9
		this.chunks[this.pivot].delete();
		this.chunks[this.pivot] = new Chunk(world, this.pivot + this.cycles*this.length, 0);
		
		this.centerChunk--;
	}
	
	public void renderChunks(Artist a)
	{
		for (int i = 0; i < this.length; i++)
		{
			this.chunks[i].render(a);
		}
	}
}