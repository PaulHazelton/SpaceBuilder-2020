package framework;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.RenderingHints;


/**
 * The GameCanvas is the base component for the graphics, this is what the graphics are drawn to using the buffer strategy.
 * @author Paul Hazelton
 */
class GameCanvas extends Canvas
{
	//Class ID
	private static final long serialVersionUID = -3058259319572356602L;
	
	//Aliasing
	private RenderingHints rhPolygon;
	private RenderingHints rhImage;
	
	
	//Constructor
	GameCanvas()
	{
		//Default background color (This color is briefly shown during initialization)
		this.setBackground(Color.BLACK);
		
		//Create rendering hints (Aliasing)
		rhPolygon = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		rhImage = new RenderingHints(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
	}

	/**
	 * Gives a predefined rendering hint good for polygons
	 */
	public RenderingHints getRHpolygon()
	{ return this.rhPolygon; }
	/**
	 * Gives a predefined rendering hint good-ish for images
	 */
	public RenderingHints getRHimage()
	{ return this.rhImage; }
}
