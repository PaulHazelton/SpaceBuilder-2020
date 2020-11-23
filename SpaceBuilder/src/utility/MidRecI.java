package utility;

public class MidRecI
{
	public int x = 0;
	public int y = 0;
	public int w = 0;
	public int h = 0;
	
	//Constructors
	public MidRecI()
	{ }
	public MidRecI(int x, int y, int w, int h)
	{
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	
	//MidRec functions
	public int getLeft()	{ return this.x - this.w/2; }
	public int getTop()		{ return this.y - this.h/2; }
	public int getRight()	{ return this.x + this.w/2; }
	public int getBottom()	{ return this.y + this.h/2; }
	
	public boolean containsPoint(int x0, int y0)
	{
		return (x0 >= this.getLeft()	&& x0 < this.getRight() &&
				y0 >= this.getTop()		&& y0 < this.getBottom());
	}
}
