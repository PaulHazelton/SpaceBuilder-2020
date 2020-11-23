//package building;
//
//import java.awt.Color;
//
//import org.jbox2d.common.Vec2;
//
//import blocks.Block;
//import framework.Artist;
//import utility.Direction;
//import utility.PMath;
//
//public class ConnectionPoint extends Vec2
//{
//	//Class ID
//	private static final long serialVersionUID = 8489018318757891783L;
//	
//	//Visuals
//	public boolean visible = true;
//	public Color color = Color.white;
//	
//	//Mechanics
//	public boolean active = true;
//	public Block parent;
//	public float squaredDistance = 0;
//	private float angle; 
//	
////Constructors
//	public ConnectionPoint(float x, float y, float angle)
//	{
//		super(x, y);
//		this.angle = angle;
//	}
//	public ConnectionPoint(Vec2 v, float angle)
//	{
//		super(v.x, v.y);
//		this.angle = angle;
//	}
//	public ConnectionPoint(float x, float y, Direction d)
//	{
//		super(x, y);
//		this.angle = d.angle;
//	}
//	public ConnectionPoint(Vec2 v, Direction d)
//	{
//		super(v.x, v.y);
//		this.angle = d.angle;
//	}
//	
////Game Loop Stuff
//	public void render(Artist a)
//	{
//		if (!this.visible || !this.active)
//			return;
//		
//		a.setStroke(0.02f);
//	
//		//Draw direction line
//		a.drawLine(this, this.add(PMath.polarToCartesian(0.1f, this.angle)), Color.red);
//		
//		a.fillCircle(this, 0.02f, this.color);
////		a.fillRec(this, new Vec2(0.1f, 0.1f), 0, Color.white);
//	}
//	
////Connection Point Opperations
//	@Deprecated
//	public static ConnectionPoint getNearestOld(ConnectionPoint[] cpts, Vec2 point)
//	{
//		//VALIDATION
//		if (cpts.length == 0)
//		{
//			System.err.println("No Connection Points Exist!");
//			return new ConnectionPoint(0, 0, 0);
//		}
//		
//		//Local variables
//		float nearestDist = 0;
//		int nearestIndex = 0;
//		ConnectionPoint p;
//		
//		
//		p = cpts[0];
//		p.squaredDistance = (float)PMath.squaredDistance(p, point);
//		
//		nearestDist = (float)PMath.squaredDistance(p, point);
//		nearestIndex = 0;
//
//		//TODO
////		if (p.squaredDistance <= Builder.buildDistance)
////			p.visible = true;
////		else
////			p.visible = false;
//		
//		for (int i = 1; i < cpts.length; i++)
//		{
//			p = cpts[i]; 
//			
//			if (!p.active)
//				continue;
//			
//			p.squaredDistance = (float)PMath.squaredDistance(p, point);
//			
//			if (p.squaredDistance < nearestDist)
//			{
//				nearestDist = p.squaredDistance;
//				nearestIndex = i;
//			}
//			
////			if (p.squaredDistance <= Builder.buildDistance)
////				p.visible = true;
////			else
////				p.visible = false;
//		}
//		
//		return cpts[nearestIndex];
//	}
//	public static ConnectionPoint getNearest(ConnectionPoint[] cpts, Vec2 localPoint)
//	{
//		if (cpts.length == 0)
//			return null;
//		
//		float nearestDist = -1;
//		int nearestIndex = -1;
//		ConnectionPoint p = null;
//		
//		for (int i = 0; i < cpts.length; i++)
//		{
//			p = cpts[i];
//			
//			//Skip if inactive
//			if (!p.active)
//				continue;
//			
//			//Calculation
//			p.squaredDistance = (float) PMath.squaredDistance(p, localPoint);
//			
//			//Find nearest
//			if (p.squaredDistance < nearestDist || nearestIndex == -1)
//			{
//				nearestDist = p.squaredDistance;
//				nearestIndex = i;
//			}
//
//			//TODO
////			//Visuals
////			if (p.squaredDistance <= Builder.buildDistance)
////				p.visible = true;
////			else
////				p.visible = false;
//		}
//		
//		if (nearestIndex != -1)
//			return cpts[nearestIndex];
//		else
//			return null;
//	}	
//	public static ConnectionPoint getNearest(ConnectionPoint[] cpts, Vec2 localPoint, Direction d)
//	{
//		if (cpts.length == 0)
//			return null;
//		
//		float nearestDist = -1;
//		int nearestIndex = -1;
//		ConnectionPoint p = null;
//		
//		for (int i = 0; i < cpts.length; i++)
//		{
//			p = cpts[i];
//			p.color = Color.red;
//			
//			//Skip if inactive or wrong direction
//			if (!p.active || !PMath.zequals(PMath.normalizeAngle1(p.angle), d.angle, 0.7f))
//				continue;
//			//Skip if cp is facing away from local point
//			float a1 = PMath.cartesianToPolar(p, localPoint).y;
//			float a2 = p.angle;
//			float a3 = a1 - a2;
//			a3 = PMath.normalizeAngle1(a3);
//			if (!(a3 < 1 || a3 > 5.28))
//				continue;
//			
//			//Calculation
//			p.squaredDistance = (float) PMath.squaredDistance(p, localPoint);
//			
//			//Find nearest
//			if (p.squaredDistance < nearestDist || nearestIndex == -1)
//			{
//				nearestDist = p.squaredDistance;
//				nearestIndex = i;
//			}
//			
////			//Visuals	TODO
////			if (p.squaredDistance <= Builder.buildDistance)
////				p.visible = true;
////			else
////				p.visible = false;
//		}
//		
//		if (nearestIndex != -1)
//			return cpts[nearestIndex];
//		else
//			return null;
//	}
//	
//	
//	public Vec2 getWorldPoint()
//	{
//		if (this.parent.getBody() != null)
//		{
//			return this.parent.getBody().getWorldVector(this)
//				.add(this.parent.getBody().getPosition());
//		}
//		else if (this.parent.getGhost() != null)
//		{
//			Vec2 v = new Vec2(this);
//			v = v.add(this.parent.getGhost().getPosition());
//			v = PMath.rotateVecAbout(v, this.parent.getGhost().getPosition(), this.parent.getGhost().angleOffset);
//			
//			return v;
//		}
//		else
//		{
//			System.err.println("No body or ghost!");
//			return new Vec2 (0, 0);
//		}
//	}
//	
//	public float getAngle()	{ return this.angle; }
//}
