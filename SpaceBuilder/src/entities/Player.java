package entities;

import java.awt.Font;
import java.awt.image.BufferedImage;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;
import org.jbox2d.dynamics.joints.WeldJointDef;

import blocks.Chair;
import fileHandling.Reader;
import framework.Artist;
import framework.InputEvent;
import framework.InputHandler;
import games.FreePlay;
import menus.Command;
import menus.Commandable;
import utility.Controllable;
import utility.Direction;
import utility.PMath;
import utility.Watchable;

public class Player implements Commandable, Controllable, Watchable, ContactListener
{
	//Game thing
	private FreePlay game;
	
	//Image stuff
	private static BufferedImage image;
	private boolean mirrored = false;
	
	//Box2d stuff
	protected Body		boxB;
	protected Fixture	boxF;
	protected Fixture	sensF;
	protected Fixture	ballF;
	
	//Walking info
	protected boolean	inSpace = true;
	protected int		contactCount = 0;
	protected boolean	contactGround = false;
	
	//Location
	private Vec2 dim;
	private float angle;
	
	//Controlling
	public boolean controlling = false;
	
	//Sit
	private boolean queueSit = false;
	private Chair chair = null;
	private boolean sitting = false;
	
	
//Initialization
	public static void fetchImages()
	{
		Player.image = Reader.loadImage("humanoids/player/placeholder/player_right.png");
	}
	
//Constructor
	public Player (FreePlay fp, World w, Vec2 pos, float a)
	{
		this.game = fp;
		this.dim = new Vec2(0.64f, 1.75f);
		this.angle = 0;

		//Make box2d stuff
		this.makeBox(w, pos, a);
		this.makeWheel(w, pos, a);
//		this.makeJoint(w);
	}
	//Constructor helper
	private void makeBox(World w, Vec2 pos, float a)
	{
		//Body
		BodyDef bd = new BodyDef();
		bd.setPosition(pos);
		bd.setAngle(a);
		bd.setType(BodyType.DYNAMIC);
		bd.angularDamping	= 0;
		bd.linearDamping	= 0;

		this.boxB = w.createBody(bd);
		
		//Basic Box fixture
		FixtureDef fd = new FixtureDef();
		Shape s = new PolygonShape();
//		((PolygonShape) s).setAsBox((this.dim.x)/2f, ((this.dim.y)/2f)*0.95f, new Vec2(0, -0.05f), 0);
		((PolygonShape) s).setAsBox((this.dim.x)/2.3f, ((this.dim.y)/2f)*0.95f, new Vec2(0, -0.05f), 0);
		fd.setShape(s);
		fd.setFriction(0.5f);
		fd.setDensity(94);		// 89 kg/m^2
		fd.filter.categoryBits = 0x0002;
		
		this.boxF = boxB.createFixture(fd);
		
		//Sensor
		FixtureDef sDef = new FixtureDef();
		s = new PolygonShape();
//		((PolygonShape) s).setAsBox((this.dim.x)/2.2f, 0.1f, new Vec2(0, 1.75f/2f), 0);
		((PolygonShape) s).setAsBox((this.dim.x)/2f, 0.2f, new Vec2(0, 1.75f/2f), 0);
		sDef.setShape(s);
		sDef.setDensity(0);
		sDef.isSensor = true;
		sDef.userData = this;
		fd.filter.categoryBits = 0x0001;
		fd.filter.maskBits = 0xffff;
		
		this.sensF = this.boxB.createFixture(sDef);
	}
	private void makeWheel(World w, Vec2 pos, float a)
	{	
		//Ball
		FixtureDef ball = new FixtureDef();
		Shape s = new CircleShape();
//		s.setRadius(this.dim.y/8f);
//		((CircleShape)s).m_p.set(0, this.dim.y/2f - this.dim.y/8f);
		s.setRadius(this.dim.y/8f);
		((CircleShape)s).m_p.set(0, this.dim.y/2f - this.dim.y/16f);
		ball.setShape(s);
		ball.setFriction(0.0f);
		ball.setDensity(1f);	//Approx 0.15kg (not much)
		ball.filter.categoryBits = 0x0002;
		
		this.ballF = boxB.createFixture(ball);
	}
	

//Command handling
	@Override
	public void tapCommand(Command c)
	{
		
	}
	@Override
	public void pressCommand(Command c)
	{
		if (c == Command.WALK_LEFT)
			this.mirrored = true;
		if (c == Command.WALK_RIGHT)
			this.mirrored = false;
		
		if (this.contactGround && !inSpace)
		{
			if (c == Command.JUMP)
				this.jump();
		}
	}
	@Override
	public void releaseCommand(Command c)
	{
		
	}
//Game Loop
	@Override
	public void inputEvent(InputEvent item)
	{
		
	}
	@Override
	public void update(double timePassed, InputHandler ih)
	{
		//Update usual stuff
		angle = PMath.normalizeAngle2(this.boxB.getAngle());

		
		//Input stuff
		if (!this.controlling)
			return;
		
		if (!sitting)
		{
			if (inSpace)
			{
				this.boxB.setFixedRotation(false);
				
				this.fly(ih);
				
				if (Command.STOP.active)
					this.spaceStop();
			}
			else // in gravity
			{
				if (this.contactGround)
				{
					this.correctAngle();
					this.walk(ih);
				}
			}
		}

		if (this.queueSit)
		{
			this.queueSit = false;
			this.sit(this.chair);
		}
	}

	
//Maneuverability functions
	@SuppressWarnings("unused")
	private void walk(InputHandler ih)
	{
		//If 'A' is down, move to the left
//		if (ih.getKey(KeyEvent.VK_A) && !ih.getKey(KeyEvent.VK_D))
		if (Command.WALK_LEFT.isActive() && !Command.WALK_RIGHT.isActive())
		{
			if (this.boxB.getLinearVelocity().x < 0.1) //Already moving to the left
			{
				//If speed is low, speed up fast
				if (this.boxB.getLinearVelocity().x > -2)
					this.boxB.setLinearVelocity(new Vec2(-2, this.boxB.getLinearVelocity().y));
				else if (this.boxB.getLinearVelocity().x > -10)
					this.boxB.applyForceToCenter(new Vec2(-4*this.getMass(), 0));
			}
			else	//Not moving to the left, do the stopping
			{
				if (this.contactGround)
					groundStop();
			}
		}
		//If 'D' is down, move to the right
//		else if (ih.getKey(KeyEvent.VK_D) && !ih.getKey(KeyEvent.VK_A))
		else if (Command.WALK_RIGHT.isActive() && !Command.WALK_LEFT.isActive())
		{
			if (this.boxB.getLinearVelocity().x > -0.1) //Already moving to the left
			{
				//If speed is low, speed up fast
				if (this.boxB.getLinearVelocity().x < 2)
					this.boxB.setLinearVelocity(new Vec2(2, this.boxB.getLinearVelocity().y));
				else if (this.boxB.getLinearVelocity().x < 10)
					this.boxB.applyForceToCenter(new Vec2(4*this.getMass(), 0));
			}
			else	//Not moving to the left, do the stopping
			{
				if (this.contactGround)
					groundStop();
			}
		}
		else	//No lateral movement keys, do the stopping
		{
			if (this.contactGround)
				groundStop();
		}
	}
	private void groundStop()
	{
		if (Math.abs(this.boxB.getLinearVelocity().x) > 0.1f)
		{
			Vec2 f = new Vec2(-this.boxB.getLinearVelocity().x*this.getMass()*10, 0);
			this.boxB.applyForceToCenter(f);
		}
		else
			this.boxB.setLinearVelocity(new Vec2(0, this.boxB.getLinearVelocity().y));
	}
	private void spaceStop()
	{
		if (Command.JUMP.active || Command.CROUCH.active
				|| Command.WALK_LEFT.active || Command.WALK_RIGHT.active
				|| Command.ROTATE_LEFT_A.active || Command.ROTATE_RIGHT_A.active)
			return;
		
		// Linear Slowing
		if (this.boxB.getLinearVelocity().length() > 0.1f)
		{
			Vec2 f2 = new Vec2 (this.boxB.getLinearVelocity());
			f2.normalize();
			f2.mulLocal(-this.getMass()*2);
			
			this.boxB.applyForceToCenter(f2);
		}
		else
			this.boxB.setLinearVelocity(new Vec2(0, 0));
		
		// Angular Slowing
		if (Math.abs(this.boxB.getAngularVelocity()) > 0.02f)
		{
			float t = Math.signum(this.boxB.getAngularVelocity());
			
			t *= -this.getMass();
			
			this.boxB.applyTorque(t);
		}
		else
			this.boxB.setAngularVelocity(0);
	}
	private void jump()
	{
		this.boxB.applyLinearImpulse(new Vec2(0, (-9.8f*this.getMass()*0.5f)), this.boxB.getWorldCenter(), true);
	}
	private void fly(InputHandler ih)
	{
		// Relative angle flying (flying normally)
		Vec2 f = new Vec2();
		if (Command.JUMP.isActive())
			f = PMath.polarToCartesian(2*boxB.getMass(), this.getWorldAngle() + Direction.UP.angle);
		else if (Command.CROUCH.isActive())
			f = PMath.polarToCartesian(2*boxB.getMass(), this.getWorldAngle() + Direction.DOWN.angle);
		else if (Command.WALK_LEFT.isActive())
			f = PMath.polarToCartesian(2*boxB.getMass(), this.getWorldAngle() + Direction.LEFT.angle);
		else if (Command.WALK_RIGHT.isActive())
			f = PMath.polarToCartesian(2*boxB.getMass(), this.getWorldAngle() + Direction.RIGHT.angle);
		this.boxB.applyForceToCenter(f);
		
		// Orthogonal flying
//		if (Command.JUMP.isActive())
//			this.boxB .applyForceToCenter(new Vec2(0, -2*boxB .getMass()));
//		if (Command.CROUCH.isActive())
//			this.boxB .applyForceToCenter(new Vec2(0, 2*boxB .getMass()));
//		if (Command.WALK_LEFT.isActive())	//Fly left
//			this.boxB .applyForceToCenter(new Vec2(-2*boxB .getMass(), 0));	
//		if (Command.WALK_RIGHT.isActive())	//Fly right
//			this.boxB .applyForceToCenter(new Vec2( 2*boxB .getMass(), 0));
		
		// Rotating
		if (Command.ROTATE_LEFT_A.isActive())
			this.boxB.applyTorque(-100);
		if (Command.ROTATE_RIGHT_A.isActive())
			this.boxB.applyTorque( 100);
	}
		
	private void correctAngle()
	{
		//If angle is to big don't correct
		if (Math.abs(angle) > 0.6f)
			return;
		
		if (!PMath.zequals(angle, 0, 0.01))	//If character is up, Correct standing up
			this.boxB.setAngularVelocity(-this.boxB.getInertia()*angle*0.5f);
		else
		{
			this.boxB.setTransform(this.boxB.getPosition(), 0);
			this.boxB.setFixedRotation(true);
			this.boxB.setAngularVelocity(0);
		}
	}
	
//Pilot spaceShip
	private void sit(Chair c)
	{
		this.sitting = true;
		
		c.getSpaceShip().setControlling(true);
//		this.game.setInputTarget(c.getSpaceShip());
		this.game.setCameraTarget(c.getSpaceShip());
		
		WeldJointDef jd = new WeldJointDef();
		jd.bodyA = this.boxB;
		jd.bodyB = c.getBody();
		jd.localAnchorA.set(new Vec2(0, 0));
		jd.localAnchorB.set(c.getCen());
		jd.referenceAngle = c.getAngle();
		
		this.game.getWorld().createJoint(jd);
		
		this.boxB.setFixedRotation(false);
		this.mirrored = c.isMirrored();
	}
	private void unSit(Chair c)
	{
		this.sitting = false;
	}
	
	//Render
	public void render(Artist a)
	{
		a.setLocalSpace(this.boxB.getPosition(), this.boxB.getAngle());
		a.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 1));
		
		a.drawImage(Player.image, new Vec2(0, 0), this.dim, 0, this.mirrored);
		
		a.resetLocalSpace();
	}
	
//Getters and setters
	public Body getBody()
	{ return this.boxB; }

	public Vec2 getWorldCenter()	{ return this.boxB.getWorldCenter(); }
	public float getWorldAngle()	{ return this.boxB.getAngle(); }
	public float getMass()	{ return this.boxB.getMass(); }// + this.ballB.getMass(); }
	
	public void setControlling(boolean c)	{ this.controlling = c; }
	public boolean getControlling()	{ return this.controlling; }

	@Override
	public void beginContact(Contact contact)
	{
		if (!contact.isTouching())
			return;
		
		//Called when sensor below feet contacts something
		contactCount++;
		
		if (this.contactCount > 0)
			this.contactGround = true;
		
		if (contact.m_fixtureA.m_userData == null || contact.m_fixtureB.m_userData == null)
			return;
		if (contact.m_fixtureA.m_userData instanceof Chair)
		{
			this.queueSit = true;
			this.chair = ((Chair)contact.m_fixtureA.m_userData);
		}
		if (contact.m_fixtureB.m_userData instanceof Chair)
		{
			this.queueSit = true;
			this.chair = ((Chair)contact.m_fixtureB.m_userData);
		}
	}
	@Override
	public void endContact(Contact contact)
	{
		//Called when sensor below feet contacts something
		contactCount--;
		
		if (this.contactCount == 0)
			this.contactGround = false;
		
		if (contact.m_fixtureA.m_userData == null || contact.m_fixtureB.m_userData == null)
			return;
		if (contact.m_fixtureA.m_userData instanceof Chair)
			this.unSit((Chair)contact.m_fixtureA.m_userData);
		if (contact.m_fixtureB.m_userData instanceof Chair)
			this.unSit((Chair)contact.m_fixtureB.m_userData);
	}
	@Override
	public void preSolve(Contact contact, Manifold oldManifold)
	{
		// TODO Auto-generated method stub
		
	}
	@Override
	public void postSolve(Contact contact, ContactImpulse impulse)
	{
		// TODO Auto-generated method stub
		
	}
}