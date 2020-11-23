package utility;

public class IntPair
{
	public int x;
	public int y;
	
	public IntPair()
	{
		this.x = 0;
		this.y = 0;
	}
	public IntPair(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public boolean update(int x, int y)
	{
		if (this.x == x && this.y == y)
			return false;
		else
		{
			this.x = x;
			this.y = y;
			return true;
		}
	}
}