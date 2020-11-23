package utility;

import java.util.Random;

import org.jbox2d.common.Vec2;

public class PMath
{
	private static float	sinTable[];
	private static int		sinTableResolution;
	
	public static Random		random;
	public static final double	epsilon = (1.0d/100000.0d);
	
	private PMath() {}
	
	// ANGLES	______________________________________________
	/**
	 * Turns an angle (in rads) between 0 and 2pi
	 * @param angle the input angle
	 * @return the new angle
	 */
	public static float		normalizeAngle1(float angle)
	{
		if (angle < 0)
		{
			angle = (float) (Math.abs(angle) % (Math.PI*2));
			angle = (float) ((Math.PI*2) - angle);
		}
		else if(angle > 0)
		{
			angle = (float) (angle % (Math.PI*2));
		}
		
		return angle;
	}
	/**
	 * Turns an angle (in rads) between -pi and pi
	 * @param angle the input angle
	 * @return the new angle
	 */
	public static float		normalizeAngle2(float angle)
	{
		angle = normalizeAngle1(angle);
		if (angle > Math.PI)
			angle = (float) (-2*Math.PI + angle);
		
		return angle;
	}
	
	public static Direction	nearestOrthogonal(float angle)
	{
		Direction d = Direction.RIGHT;
		
		angle = normalizeAngle1(angle);
		
		angle -= (float)Math.PI/4d;
		
		if (angle > Direction.RIGHT.angle)
			d = Direction.DOWN;
		if (angle > Direction.DOWN.angle)
			d = Direction.LEFT;
		if (angle > Direction.LEFT.angle)
			d = Direction.UP;
		if (angle > Direction.UP.angle)
			d = Direction.RIGHT;
		
		return d;
	}
	
	// SIN TABLES	__________________________________________
	public static void	initializeSinTable(int resolution)
	{
		sinTable = new float[resolution];
		sinTableResolution = resolution;
		
		for (int i = 0; i < resolution; i++)
		{
			sinTable[i] = (float) Math.sin(map(i, 0, resolution, 0, (float)Math.PI*2));
		}
	}
	public static float	getSinTableResolution()
	{ return PMath.sinTableResolution; }
	
	public static float	sinTableOf(float angle)
	{
		if (sinTable == null)
		{
			//TODO Make this and the one below it a proper throws or something
			System.err.println("ERROR, SIN TABLE NOT INITIALIZED");
			return 0;
		}
		
		angle = normalizeAngle1(angle);
		
		float indexF = map(angle, 0, (float)Math.PI*2, 0, sinTableResolution);
		indexF = indexF % sinTableResolution;
		
		return sinTable[(int) Math.floor(indexF)];
	}
	public static float	sinTableAt(int index)
	{
		if (sinTable == null)
		{
			System.err.println("ERROR, SIN TABLE NOT INITIALIZED");
			return 0;
		}
		return sinTable[index];
	}

	// NUMBERS	______________________________________________	
	public static void		initRandom()
	{
		random = new Random();
	}
	public static float		randomFloat(float min, float max)
	{
		float r = max - min;
		float n = random.nextFloat();
		n *= r;
		n += min;
		
		return n;
	}
	
	public static boolean	zequals(double number, double near, double threshold)
	{
		if (number > near - threshold && number < near + threshold)
			return true;
		
		return false;
	}
	public static boolean	zequals(double number, double near)
	{
		return zequals(number, near, epsilon);
	}
	
	public static float		map(float num, float lowIn, float highIn, float lowOut, float highOut)
	{
		float range1 = highIn - lowIn;
		float x1 = num - lowIn;
		
		x1 = x1/range1;
		
		float range2 = highOut - lowOut;
		float x2 = x1*range2;
		
		return x2;
	}
	public static float		limit(float value, float min, float max)
	{	
			 if (value > max)
			value = max;
		else if (value < min)
			value = min;
			 
		return value;
	}
	public static int		limit(int value, int min, int max)
	{	
			 if (value > max)
			value = max;
		else if (value < min)
			value = min;
			 
		return value;
	}
	
	public static int		arrayMod(int i, int length)
	{
		if (i >= length)
			return i % length;
		if (i < 0)
			return i % length + length;
		else
			return i;
	}
	
	public static int		numOfDigits(double value)
	{
		value = Math.abs(value);
		
		if (value == 0)
			return 1;
		
		int num = (int) (Math.log10(value));
		num += (value > 1)? 1 : -1;
		
		return num;
	}
	public static int		numOfDigits(int value)
	{
		return (int)(Math.log10(value)+1);
	}

	public static int		getSigFigs(double value, int digits)
	{
		//digits > 0
		
		//value = 0.9999999
		
		//normalize (make the num satisfy (1 >= x < 10))
		double normal = normalizeDouble(value);
		//normal = 9.9999999
		
		normal *= Math.pow(10, digits - 1);
		//normal = 9999.999
		
		int intVal = roundDoubleToInt(normal);
		//intVal = 10000
		
		if (PMath.numOfDigits(intVal) > digits)
		{
			intVal /= 10;
//			System.out.println("tripped!");
		}
		//intVal = 1000
		return intVal;
	}
	
	public static int		roundDoubleToInt(double value)
	{
		//value = 9999.999
		value += 0.5d;
		//value = 10000.49999
		return (int)(value);
		//return  10000
	}
	
	private static double	normalizeDouble(double value)
	{
		double log10 = Math.log10(value);
		value /= Math.pow(10, Math.floor(log10));
		
		return value;
	}
	
	// VECTORS	______________________________________________
	public static boolean zequals(Vec2 a, Vec2 b)
	{
		return (PMath.zequals(a.x, b.x) && PMath.zequals(a.y, b.y));
	}
	
	public static Vec2 midpoint(Vec2 a, Vec2 b)
	{
		Vec2 mid = new Vec2((a.x + b.x)/2, (a.y + b.y)/2);
		return mid;
	}
	
	public static double distance(Vec2 a, Vec2 b)
	{
		float x = a.x - b.x;
		float y = a.y - b.y;
		
		return Math.sqrt(x*x + y*y);
	}
	public static double squaredDistance(Vec2 a, Vec2 b)
	{
		float x = a.x - b.x;
		float y = a.y - b.y;
		
		return (x*x + y*y);
	}

	public static Vec2 rotateVec(Vec2 v, double angle)
	{
		Vec2 vec = new Vec2(v);
		
		vec = cartesianToPolar(vec);
		vec.y += angle;
		vec = polarToCartesian(vec);
		return vec;
	}
	public static Vec2 rotateVecAbout(Vec2 v, Vec2 pointOfRotation, double angle)
	{
		Vec2 resultant = new Vec2(v);
		resultant = resultant.sub(pointOfRotation);
		resultant = rotateVec(resultant, angle);
		resultant = resultant.add(pointOfRotation);
		return resultant;
	}
	
	public static Vec2 cartesianToPolar(Vec2 vector)
	{
		float r		= (float)Math.hypot(vector.x, vector.y);
		float angle	= (float)Math.atan2(vector.y, vector.x);
		angle = PMath.normalizeAngle1(angle);
		
		return new Vec2(r, angle);
	}
	public static Vec2 cartesianToPolar(Vec2 vec1, Vec2 vec2)
	{
		return cartesianToPolar(vec2.sub(vec1));
	}
	
	public static Vec2 polarToCartesian(float r, float a)
	{
		float x = (float)(r*Math.cos(a));
		float y = (float)(r*Math.sin(a));
		
		return new Vec2(x, y);
	}
	public static Vec2 polarToCartesian(Vec2 v)
	{
		return polarToCartesian(v.x, v.y);
	}
}