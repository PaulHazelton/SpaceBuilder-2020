package framework;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

import org.jbox2d.common.Vec2;

/**
 * The camera contains all the transformations
 * that are applied to the graphics device before drawing.
 * the center should be the center of the content pane in pixels, but feel free to change it.
 * @author Paul Hazelton
 */
public class Camera
{	
	private Vec2 center;
	private Vec2 translation;
	private Vec2 scale;
	private Vec2 shear;
	private float zoom = 1;
	private float angle;
	
	//Util
	private static final float tau = (float)(Math.PI * 2d);
	private static final float EPSILON = 1f/256f;
	private static final float minZoom = 16f;
	private static final float maxZoom = 500f;
	
	/**
	 * Creates a default camera with translate at (0, 0), scale at (1, 1), and a center at 200, 200. 
	 */
	public Camera()
	{
		this.translation	= new Vec2(0, 0);
		this.center			= new Vec2(0, 0);
		this.scale			= new Vec2(1, 1);
		this.shear			= new Vec2(0, 0);
		this.angle			= 0;
	}
	public Camera(Vec2 translation)
	{
		this.translation = translation;
		this.center = new Vec2(0, 0);
		this.scale = new Vec2(1, 1);
		this.angle = 0;
	}

	public Vec2 getCenter()
	{ return this.center; }
	public void setCenter(Vec2 center)
	{ this.center = center; }

	public Vec2 getTranslation()
	{ return this.translation; }
	public void setTranslation(Vec2 translation)
	{ this.translation = translation; }

	public void translate(Vec2 translation)
	{
		this.translation = this.translation.add(translation);
	}
	
	public float getZoom()
	{ return this.zoom; }
	/**
	 * Just a bit quicker than {@link #setScale(float, float)
	 * @param zoom - the x and y scale.
	 */
	public void	setZoom(float zoom)
	{
		if (zoom < minZoom || zoom > maxZoom)
			return;
		
		this.zoom = zoom;
		this.scale.x = zoom;
		this.scale.y = zoom;
	}
	public Vec2 getScale()
	{ return this.scale; }
	public void setScale(Vec2 scale)
	{
		if (scale.x < EPSILON || scale.y < EPSILON)
		{
			System.out.println("Error @ Camera.setScale: invalid arguments");
			return;
		}
		
		this.scale = scale;
		
		if (scale.x == scale.y)
			this.zoom = scale.x;
		else
			this.zoom = -1;
	}
	
	public void zoom(float zoom)
	{
		this.setZoom(this.getZoom() + zoom);
	}
	
	public void setShear(Vec2 shear)	{ this.shear = shear; }
	public Vec2 getShear()				{ return this.shear; }
	
	public float	getRotate()
	{ return this.angle; }
	public void		setRotate(float angle)
	{
		angle %= (float)(2d * Math.PI);
		if (angle < 0f)
			angle += tau;
		this.angle = angle;
	}
	
	public void rotate(float angle)
	{
		this.setRotate(this.getRotate() + angle);
	}
	
	public Point2D.Float	getScreenToWorld(Point2D.Float point)
	{
		AffineTransform tr = new AffineTransform();
		
		tr.translate(this.center.x, this.center.y);
		tr.rotate	(angle);
		tr.scale	(this.scale.x, this.scale.y);
		tr.shear	(this.shear.x, this.shear.y);
		tr.translate(-this.translation.x, -this.translation.y);
		
		try
		{
			tr.invert();
		} catch (NoninvertibleTransformException e)
		{
			System.out.println("invert failed");
			return new Point2D.Float(0, 0);
		}
		
		tr.transform(point, point);
		
		return point;
	}
	public Vec2				getScreenToWorld(Vec2 newPoint, Vec2 point)
	{
		AffineTransform tr = new AffineTransform();
		
		tr.translate( this.center.x,		 this.center.y);
		tr.rotate	(-this.angle);
		tr.shear	( this.shear.x,			 this.shear.y);
		tr.scale	( this.scale.x,			 this.scale.y);
		tr.translate(-this.translation.x,	-this.translation.y);
		
		try
		{
			tr.invert();
		} catch (NoninvertibleTransformException e)
		{
			System.out.println("invert failed");
			return new Vec2(0, 0);
		}
		
		Point2D.Float point2D		= new Point2D.Float(point.x, point.y);
		Point2D.Float newPoint2D	= new Point2D.Float(newPoint.x, newPoint.y);
		tr.transform(point2D, newPoint2D);
		
		newPoint.x = newPoint2D.x;
		newPoint.y = newPoint2D.y;
		
		return newPoint;
	}
	
	public Vec2	getWorldToScreen(Vec2 point)
	{
		AffineTransform tr = new AffineTransform();
		
		tr.translate(this.center.x, this.center.y);
		tr.rotate	(-angle);
		tr.scale	(this.scale.x, this.scale.y);
		tr.translate(-this.translation.x, -this.translation.y);
		
		Point2D.Float point2D	= new Point2D.Float(point.x, point.y);
		
		tr.transform(point2D, point2D);
		
		return new Vec2(point2D.x, point2D.y);
	}
}