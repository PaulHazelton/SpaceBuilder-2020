package planets;

import java.awt.Color;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import framework.Artist;


public class Ground
{
	//There should be a finite number of chunks
	//New chunk will load in while old junks will be dropped
	//Order of chunks in array is irrelevent, chunks have their location stored
	public ChunkLoop cloop;
	private Vec2 camCenter;
	private final int chunkLoadLength = 21;
	
	//Constructor that makes chunks
	public Ground(World world)
	{
		cloop = new ChunkLoop(world, chunkLoadLength);
		this.camCenter = new Vec2(0, 0);
//		this.maxScreenWidth = 
	}
	
	public void generate()
	{
		
	}
	
	public void update(double timePassed, Vec2 camCenter)
	{
		this.camCenter = camCenter;
		this.cloop.update(camCenter);
	}
	
	public void render(Artist a)
	{
		//Render black void below ground
		Vec2 pos = new Vec2(camCenter.x, (chunkLoadLength*Chunk.sizeInMeters)/4);
		Vec2 dim = new Vec2(chunkLoadLength*Chunk.sizeInMeters, (chunkLoadLength*Chunk.sizeInMeters)/2);
		a.fillRec(pos, dim, 0, Color.black);
		
		//Render all chunks
		this.cloop.renderChunks(a);
		
//		for (int i = 0; i < this.chunks.length; i++)
//			this.chunks[i].render(a);
	}
}
