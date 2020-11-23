package utility;

public enum Direction
{
	RIGHT	(0),
	DOWN	((float)(Math.PI * 0.5d)),
	LEFT	((float)(Math.PI * 1.0d)),
	UP		((float)(Math.PI * 1.5d));
	
	public final float angle;
	Direction(float angle)
	{
		this.angle = angle;
	}
	
	public static Direction rotateCW(Direction d)
	{
		switch(d)
		{
		case RIGHT:	return Direction.DOWN;
		case DOWN:	return Direction.LEFT;
		case LEFT:	return Direction.UP;
		case UP:	return Direction.RIGHT;
		default:	return Direction.RIGHT;
		}
	}
	public static Direction rotateCCW(Direction d)
	{
		switch(d)
		{
		case RIGHT:	return Direction.UP;
		case DOWN:	return Direction.RIGHT;
		case LEFT:	return Direction.DOWN;
		case UP:	return Direction.LEFT;
		default:	return Direction.RIGHT;
		}
	}
	
	/**
	 * 0=RIGHT,<br>1=DOWN,<br>2=LEFT,<br>3=UP
	 */
	public static Direction get(int i)
	{
		i = i%4;
		switch(i)
		{
		case 0:	return RIGHT;
		case 1:	return DOWN;
		case 2:	return LEFT;
		case 3:	return UP;
		default:	return get(-i);	//Pretty slick huh?
		}
	}
}
