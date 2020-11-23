package utility;

public class IntRec
{
	public int x = 0;
	public int y = 0;
	public int w = 0;
	public int h = 0;
	
	public IntRec()	{}
	public IntRec(int x, int y, int w, int h)
	{
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	
	public int getRight()	{ return this.x + this.w; }
	public int getBottom()	{ return this.y + this.h; }
	
	public boolean containsPoint(int x0, int y0)
	{
		return ((x0 >= this.x)	&& x0 < this.getRight() &&
				y0 >= this.y	&& y0 < this.getBottom());
	}
}
