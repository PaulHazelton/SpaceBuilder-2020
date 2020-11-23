package testObjects;

import java.awt.event.KeyEvent;
//import java.awt.geom.Path2D;

import org.jbox2d.collision.shapes.*;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

//import org.jbox2d.dynamics.Body;

import framework.Artist;
import framework.InputHandler;
import utility.Direction;

public class TestObject
{
	public Body body;
	public FixtureDef fd;
	
	public float x, y, w, h;
	public float angle;
	
//	private Path2D.Float path;
	

	public TestObject(float x, float y, World world)
	{
		BodyDef bd = new BodyDef();
		
		bd.setPosition(new Vec2(x, y));
		bd.setType(BodyType.DYNAMIC);
		bd.setFixedRotation(false);
		
		this.body = world.createBody(bd);
		
		
		Shape shape = new PolygonShape();
		Vec2[] points = new Vec2[] { new Vec2(-1, -1), new Vec2(1, 0), new Vec2(-1, 1)};
		((PolygonShape) shape).set(points, points.length);
		
		this.fd = new FixtureDef();
		this.fd.setShape(shape);
		this.fd.setDensity(1f);
		this.body.createFixture(this.fd);
	}
//	private void createPath()
//	{
//		float[] xPoints = {-1, 0, 1, 0};
//		float[] yPoints = {1, -1, 1, 0};
//		
//		this.path = new Path2D.Float();
//		path.moveTo(xPoints[0], yPoints[0]);
//		for (int i = 1; i < xPoints.length; i++)
//		{
//			path.lineTo(xPoints[i], yPoints[i]);
//		}
//		path.closePath();
//	}
	
	public void update(InputHandler ih)
	{	
		if (ih.getKey(KeyEvent.VK_W))
			this.move(Direction.UP);
		if (ih.getKey(KeyEvent.VK_S))
			this.move(Direction.DOWN);
		if (ih.getKey(KeyEvent.VK_A))
			this.move(Direction.LEFT);
		if (ih.getKey(KeyEvent.VK_D))
			this.move(Direction.RIGHT);
		if (ih.getKey(KeyEvent.VK_Q))
			this.rotate((float)-1);
		if (ih.getKey(KeyEvent.VK_E))
			this.rotate((float)1);
		
		if (ih.getKey(KeyEvent.VK_F))
			this.scale(-0.5f, 0f);
		if (ih.getKey(KeyEvent.VK_G))
			this.scale(0.5f, 0f);
		if (ih.getKey(KeyEvent.VK_R))
			this.scale(0f, -0.5f);
		if (ih.getKey(KeyEvent.VK_T))
			this.scale(0f, 0.5f);
	}
	
	public void move(Direction d)
	{
		float speedMult = 10;
		
		float x = speedMult * (float)Math.cos(this.body.getAngle());
		float y = speedMult * (float)Math.sin(this.body.getAngle());
		
		switch(d)
		{
		case UP:
			this.body.applyForceToCenter(new Vec2(x, y));
			break;
		case RIGHT:
			this.body.applyTorque(2f);
			break;
		case LEFT:
			this.body.applyTorque(-2f);
			break;
		case DOWN:
			this.body.applyForceToCenter(new Vec2(-x, -y));
			break;
		default:
			break;
		}
	}
	public void rotate(float spin)
	{
		if (spin == 0)
			return;
//		
//		spin = (float) (((spin * 5) / 360f) * Math.PI);
		
		this.body.applyAngularImpulse(1 * spin);
		System.out.println("spin");
		
//		this.angle += spin;
//		
//		this.angle %= tau;
//		if (this.angle < 0)
//			this.angle += tau;
	}
	public void scale(float w, float h)
	{
		this.w += w;
		this.h += h;
	}
		
	public void render(Artist ga)
	{
//		ga.fillNormalPolygon(this.path, this.body.getPosition().x, this.body.getPosition().y, w, h, this.body.getAngle(), Color.WHITE);
	}
}