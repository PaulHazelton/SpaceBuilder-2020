package framework;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.common.Color3f;
import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;
import org.jbox2d.particle.ParticleColor;

import utility.PMath;

public class DebugDraw2D extends DebugDraw
{
	private Model model;
	
	private Graphics2D graphics;
	private AffineTransform normalTransform;
	
	
	public DebugDraw2D(Model model)
	{
		this.model = model;
	}
	
	public void beginDraw()
	{
		this.normalTransform = this.graphics.getTransform();
	}
	public void endDraw()
	{
		this.graphics.setTransform(this.normalTransform);
	}
	
	public void updateDraw()
	{
		
	}

	@Override
	public void drawPoint(Vec2 argPoint, float argRadiusOnScreen, Color3f argColor)
	{
		
	}

	@Override
	public void drawSolidPolygon(Vec2[] vertices, int vertexCount, Color3f color)
	{
		Path2D.Float path = createPath(vertices, vertexCount);
//		Color normalColor = new Color(color.x, color.y, color.z);
		
		this.prepareGraphics();
		
		this.graphics.setColor(new Color(255, 0, 0, 125));
		this.graphics.fill(path);
		
		this.graphics.setColor(new Color(255, 128, 128, 255));
		this.graphics.setStroke(new BasicStroke(0.03f));
		this.graphics.draw(path);
		
		
		this.returnGraphics();
	}

	@Override
	public void drawCircle(Vec2 center, float radius, Color3f color)
	{

	}

	@Override
	public void drawSolidCircle(Vec2 center, float radius, Vec2 axis, Color3f color)
	{
		this.prepareGraphics();
		
		float x = center.x;
		float y = center.y;
		float w = radius*2;
		float h = w;
		float angle = 0;
		
		this.graphics.translate(x, y);
		this.graphics.rotate(angle);
		this.graphics.scale(w/2f, h/2f);
		
		this.graphics.setColor(new Color(255, 0, 0, 125));
		this.graphics.fillOval(-1, -1, 2, 2);
		
		this.graphics.setColor(new Color(255, 128, 128, 255));
		this.graphics.setStroke(new BasicStroke(0.03f));
		
		this.graphics.drawOval(-1, -1, 2, 2);
		
//		this.graphics.setColor(new Color(255, 0, 0, 125));
////	this.graphics.fillOval((int)(center.x*this.model.getCamera().getScale().x*0.1f), 0, 2, 2);
//	this.graphics.fillOval((int)(center.x), (int)(center.y), (int)radius, (int)radius);
//	
	
//	this.graphics.draw(path);
		
		
		this.returnGraphics();
	}

	@Override
	public void drawSegment(Vec2 p1, Vec2 p2, Color3f color)
	{
		this.prepareGraphics();
		
		this.graphics.setColor(new Color(255, 128, 128, 255));
		this.graphics.setStroke(new BasicStroke(0.03f));
		
		
		Vec2 mid = PMath.midpoint(p1, p2);
		Vec2 polar = PMath.cartesianToPolar(p1, p2);
		
		this.graphics.translate(mid.x, mid.y);
		this.graphics.rotate(polar.y);
		this.graphics.scale(polar.x/2f, 1);
				
		this.graphics.drawLine(-1, 0, 1, 0);
		
//		this.graphics.drawLine((int)p1.x, (int)p1.y, (int)p2.x, (int)p2.y);
		
		
		this.returnGraphics();
	}

	@Override
	public void drawTransform(Transform xf)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawString(float x, float y, String s, Color3f color)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawParticles(Vec2[] centers, float radius, ParticleColor[] colors, int count)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawParticlesWireframe(Vec2[] centers, float radius, ParticleColor[] colors, int count)
	{
		// TODO Auto-generated method stub
		
	}
	
	private Path2D.Float createPath(Vec2[] vertices, int vertexCount)
	{
		float[] xPoints = new float[vertexCount];
		float[] yPoints = new float[vertexCount];
		
		for (int i = 0; i < vertexCount; i++)
		{
			xPoints[i] = vertices[i].x;
			yPoints[i] = vertices[i].y;
		}
		
		Path2D.Float path = new Path2D.Float();
		path.moveTo(xPoints[0], yPoints[0]);
		for (int i = 1; i < xPoints.length; i++)
		{
			path.lineTo(xPoints[i], yPoints[i]);
		}
		path.closePath();
		
		return path;
	}
	
	private void prepareGraphics()
	{	
		Camera cam = this.model.cam;
		
		this.graphics.translate	(cam.getCenter().x, cam.getCenter().y);
		this.graphics.rotate	(-cam.getRotate());
		this.graphics.scale		(cam.getScale().x, cam.getScale().y);
		this.graphics.translate	(-cam.getTranslation().x, -cam.getTranslation().y);
	}
	private void returnGraphics()
	{
		this.graphics.setTransform(this.normalTransform);
	}
	
	public void setGraphics(Graphics2D g)
	{
		this.graphics = g;
	}
}
