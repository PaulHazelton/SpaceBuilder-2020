package utility;

import org.jbox2d.common.Vec2;

/*	AS OF 2018-0702-1920:
 * 	
 * 	Box2D density of 1 = 1000 kg/m^3 = 1 g/cm^3 = 1 g/mL
 * 	Box2D density:	g/cm^3
 */

public class Units
{
	public static final float massScale		= 1000;
	public static final float densityScale	= 1000;
	public static final float forceScale	= 1000;
	
	public static final String[][] prefixes		= new String[][] {
		{"a" , "f" , "p" , "n" , "μ" , "m" , " " , "k" , "M", "G" , "T" , "P" , "E" },	//regular
		{"ag", "fg", "pg", "ng", "μg", "mg", " g", "kg", " t", "kt", "Mt", "Pg", "Eg"},	//mass
		{"a" , "f" , "p" , "n" , "μ" , "m" , " " , "K", "M", "G", "T", "P", "E"},		//no unit
	};
	public static final int prefixOffset = 6;
	
	public static int sigFigs = 4;
	
	
	@Deprecated
	public static float convertUpMass(float mass)
	{ return mass*Units.massScale; }
	@Deprecated
	public static float convertUpDensity(float density)
	{ return density*Units.densityScale; }
	@Deprecated
	public static float convertUpForce(float force)
	{ return force*Units.forceScale; }
	
	@Deprecated
	public static float convertDownMass(float mass)
	{ return mass/Units.massScale; }
	@Deprecated
	public static float convertDownDensity(float density)
	{ return density/Units.densityScale; }
	@Deprecated
	public static float convertDownForce(float force)
	{ return force/Units.forceScale; }
	
	public static String massToString(float mass)
	{
		String text;
		
		text = String.format("%3.2f", mass);
		text += "kg";
		
		return text;
	}
	public static String densityToString(float density)
	{	
		String text;
		
		text = String.format("%3.2f", density);
		text += "kg/m^2";
		
		return text;
	}
	public static String forceToString(float force)
	{
		String text;
		
		text = String.format("%3.2f", force);
		text += "N";
		
		return text;
	}
	
	public static String unitToString(double value, String unit)
	{
		//Variables
		String	text		= "";	//text to return
		double	newValue	= 0;	//used for doing math while not changing value
		int		mod3		= 0;	//num of digits mod 3 (sorta). (25 km -> mod3 = 2)
		int		index1		= 0;	//index for prefix. (25 km -> index1 = 1)
		int		index2		= 0;	//used for second normalization
		int		intVal		= 0;	//integer value of adjusted number (25.35 km -> intVal = 25)
		int		sigDigs		= 0;	//the significant digits of the value. (25.35 km -> sigDigs = 2535)
		
		newValue = Math.abs(value);
		
		//zero skip
		if (PMath.zequals(newValue, 0))
		{
			if (unit.length() == 2)
				return " 0.000 " + unit;
			else
				return " 0.000  " + unit;
		}
		
		//Get the Units.sigFigs most significant digits of the new Value. This rounds it
		sigDigs = PMath.getSigFigs(newValue, Units.sigFigs);
		
		//Normalize
		index1 = (int) Math.floor(Math.log10(newValue)/3);
		newValue = newValue/Math.pow(10, index1*3);
		//Round number
		intVal = PMath.roundDoubleToInt(newValue * Math.pow(10, Units.sigFigs - 1));
		intVal /= (int)Math.pow(10, Units.sigFigs - 1);
		//number of digits mod 3 (sorta)
		mod3 = PMath.numOfDigits(intVal);
		//normalize again (may have rolled over from 999.9 to 1.000 k)
		index2 = (int) Math.floor(Math.log10(intVal)/3);
		intVal = (int) (intVal/Math.pow(10, index2*3));
		//did it roll over?
		if (mod3 > PMath.numOfDigits(intVal))
			index1 += 1;
		//calc mod3 again
		mod3 = PMath.numOfDigits(intVal);
		
		
		//Building string
		String sigDigs_s = Integer.toString(sigDigs);

		//positive or negative
		if (value > 0)
			text = " ";
		else
			text = "-";
		
		//Piece together 2 halves of string 
		text += sigDigs_s.substring(0, mod3);
		
		if (Units.sigFigs - mod3 > 0)
			text += "." + sigDigs_s.substring(mod3, sigDigs_s.length());
		else
			text += ' ';
		
		text += ' ';
		
		//Units at end
		index1 += prefixOffset;
		int unitType = 0;
		
		//kg exception
		if (unit == "kg")
		{
			index1 += 1;
			unitType = 1;
			unit = "";
		}
		else if (unit == "" || unit == null)
		{
			unitType = 2;
			unit = "";
		}
		
		//prefix of scientific notation if out of bounds
		if (index1 >= 0 && index1 < Units.prefixes[unitType].length)
			unit = Units.prefixes[unitType][index1] + unit;
		else
			unit = "*10^" + Integer.toString(index1*3) + unit;
		
		//return number with unit
		text += unit;
		return text;
	}
	public static String unitToStringSN(double value, String unit)
	{
		String text		= "";
		String format	= "";
		
		double newValue	= Math.abs(value);
		int numMod3		= 0;
		int index		= 0;
		
		if (PMath.zequals(value, 0))
			return "0.000 " + unit;
		
		index = (int) Math.floor(Math.log10(newValue)/3);
		
		newValue = value/Math.pow(10, index*3);
		numMod3 = PMath.numOfDigits(newValue);
		
		
		format = "%";
		format += Integer.toString(numMod3 + 1);
		format += ".";
		format += Integer.toString(4 - numMod3);
		format += "f";
		
		text = String.format(format, newValue);
		text += unit;
		
		if (index != 0)
		{
			text += " *10^";
			text += Integer.toString(index*3);
		}
		
		return text;
	} 

	public static String vectorUnitToString(Vec2 v, String unit)
	{
		String text = Units.unitToString(v.x, unit);
		text += ", ";
		text += Units.unitToString(v.y, unit);
		
		return text;
	}
}
