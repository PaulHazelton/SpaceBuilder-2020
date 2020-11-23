//package building;
//
//import java.awt.Color;
//
//import org.jbox2d.callbacks.ContactImpulse;
//import org.jbox2d.callbacks.ContactListener;
//import org.jbox2d.collision.Manifold;
//import org.jbox2d.common.Vec2;
//import org.jbox2d.dynamics.Body;
//import org.jbox2d.dynamics.BodyDef;
//import org.jbox2d.dynamics.BodyType;
//import org.jbox2d.dynamics.contacts.Contact;
//
//import blocks.*;
//import framework.Artist;
//import framework.InputEvent;
//import framework.InputHandler;
//import games.FreePlay;
//import menus.CommCatagory;
//import menus.Command;
//import menus.Commandable;
//import physicsListeners.UserData;
//import utility.Controllable;
//import utility.Direction;
//import utility.PMath;
//
//public class Builder implements Commandable, Controllable, ContactListener
//{
//	//Game communication
//	private FreePlay game;
//	
//	//Control
//	private boolean active = false;
//	private boolean sugg1valid = false;
//	private boolean sugg2valid = false;
//	private int passCount = 0;
//	private int contactCount = 0;
//	
//	//Builder stuff
//	private static final String SuggestionID = "SuggestionSensor";
//	private Block blockType;
//	private Block ghostBlock;
//	private Block suggestionBlock;
//	private Body suggestionBody;
//	private Vec2 suggRenderPos;
//	private SpaceShip targetSpaceShip;
//	private ConnectionPoint nearestShipPoint;
//	private ConnectionPoint nearestGhostPoint;
//	private ConnectionPoint nearestShipPointOld;
//	private ConnectionPoint nearestGhostPointOld;
//	
//	//Positioning
//	private Vec2 position;
//	private float angle = 0;
//	public float angleOffset = 0;
//
//	//Mechanics
//	public static final float buildDistanceSq = 9;
//	public static final float buildDistance = 3;
//	public static final float rootBuildDistance = 1.7f;
//	
////Constructors
//	public Builder(FreePlay g, Vec2 pos)
//	{
//		this.game = g;
//		this.position = new Vec2(pos);
//		
//		this.pressCommand(Command.HULL_REC);
//		
//		this.suggRenderPos = this.suggestionBody.getPosition();
//	}
//	
////Game loop stuff
//	//Input
//	public void inputEvent(InputEvent item)
//	{}
//	//Update
//	@Override
//	public void update(double timePassed, InputHandler ih)
//	{
//		//Set position
//		this.position = ih.getMouseWorld();
//		
//		//If this is inactive, don't do anything
//		if (!this.active || this.blockType == null)
//			return;
//		
//		//If no spaceship, snap to mouse, don't do other builder stuff
//		if (this.targetSpaceShip == null)
//		{
//			this.suggestionBody.setTransform(this.position, this.ghostBlock.getAngle() + this.angleOffset);
//			this.suggRenderPos = this.position;
//		}
//		else
//		{
//			//Align the angle to the build target
//			this.angleOffset = this.targetSpaceShip.getBlockBody(0).getAngle();
//			
//			//Record old suggestion
//			this.nearestGhostPointOld = this.nearestGhostPoint;
//			this.nearestShipPointOld = this.nearestShipPoint;
//			
//			//Resolve suggested build spot
//			this.findNearestConnectionPoints();
//			
//			//Calc num of passes
//			//Did the suggestion change?
//			if (this.nearestGhostPoint == this.nearestGhostPointOld && this.nearestShipPoint == this.nearestShipPointOld)
//				this.passCount = 2;
//			else
//				this.passCount = 1;
//			
//			this.snapSuggestion();
//		}
//	}
//	//Render
//	public void render(Artist a)
//	{
//		//Skip if inactive
//		if (!this.active || this.blockType == null)
//			return;
//		
//		//Render target ss connection points
//		if (this.targetSpaceShip != null)
//			this.targetSpaceShip.renderConnectionPoints(a);
//		
//		//Render the suggestion block
//		a.setLocalSpace(this.suggRenderPos, this.suggestionBody.getAngle());
//		this.suggestionBlock.render(a);
//		this.suggestionBlock.renderConnectionPoints(a);
//		a.resetLocalSpace();
//		
//		if (this.nearestShipPoint == null || this.nearestGhostPoint == null)
//			return;
//		
//		//Building overlay
//		if (this.nearestShipPoint.squaredDistance < Builder.buildDistanceSq && this.contactCount == 0)
//		{	
//			a.setStroke(0.01f);
//			a.drawCircle(this.position, Builder.buildDistance, Color.white);
//			a.drawLine(this.suggRenderPos, this.position, Color.cyan);
//		}
//	}
//	
////Building helpers
//	private void reset()
//	{
//		if (this.blockType == null)
//		{
//			this.ghostBlock = null;
//			this.game.getWorld().destroyBody(this.suggestionBody);
//		}
//		else
//		{
//			this.ghostBlock = this.blockType.copy();
//			
//			this.ghostBlock.setGhost(this);
//			this.ghostBlock.setGhostAngle(this.angle);
//			this.recreateSuggestion();
//		}
//	}
//	private void recreateSuggestion()
//	{
//		float a = 0;
//		
//		//Delete old suggestion
//		if (this.suggestionBody != null)
//		{
//			a = this.suggestionBody.getAngle();
//			this.game.getWorld().destroyBody(this.suggestionBody);
//		}
//		
//		//Make new body
//		BodyDef bd = new BodyDef();
//		bd.setPosition(this.position);
//		bd.setAngle(a);
//		bd.setType(BodyType.KINEMATIC);
//		this.suggestionBody = this.game.getWorld().createBody(bd);
//		this.suggestionBody.setSleepingAllowed(false);
//		//Make new block
//		this.suggestionBlock = this.blockType.copy();
//		this.suggestionBlock.getFD().setSensor(true);
//		this.suggestionBlock.setGhost(this);
//		
//		UserData data = new UserData(this, SuggestionID);
//		this.suggestionBody.createFixture(this.suggestionBlock.getFD()).setUserData(data);
//	}	
//	private void findNearestConnectionPoints()
//	{
//		//Update connection points, get nearest ones
//		//Finding the two nearest connection points
//		//Get nearest shipPoint to world point
//		Vec2 w = this.position;
//		w = targetSpaceShip.getBlockBody(0).getLocalPoint(w);
//		
//		//Get 4 nearest point on ship to builder center
//		ConnectionPoint[] shipPoints = new ConnectionPoint[4];
//		shipPoints[0] = this.targetSpaceShip.getNearestConnectionPoint(w, Direction.RIGHT);
//		shipPoints[1] = this.targetSpaceShip.getNearestConnectionPoint(w, Direction.DOWN);
//		shipPoints[2] = this.targetSpaceShip.getNearestConnectionPoint(w, Direction.LEFT);
//		shipPoints[3] = this.targetSpaceShip.getNearestConnectionPoint(w, Direction.UP);
//		
//		this.nearestGhostPoint = null;
//		ConnectionPoint cp = null;
//		float bestDist = Builder.buildDistance*20;
//		
//		for (int i = 0; i < shipPoints.length; i++)
//		{
//			//if point DNE, skip
//			if (shipPoints[i] == null)
//				continue;
//			
//			//Get nearest ghostPoint to builder
//			w = shipPoints[i].getWorldPoint();
//			w = this.getLocalPoint(w);
//			cp = this.ghostBlock.getNearestConnectionPoint(w, Direction.get(i+2));
//			
//			if (cp == null)
//				continue;
//			
//			if (this.nearestGhostPoint == null)	//No valid suggestion
//			{
//				this.nearestGhostPoint = cp;
//				this.nearestShipPoint = shipPoints[i];
//				bestDist = (float) PMath.distance(shipPoints[i].getWorldPoint(), cp.getWorldPoint());
//			}
//			else if (PMath.distance(shipPoints[i].getWorldPoint(), cp.getWorldPoint()) < bestDist)	//Valid suggestion
//			{
//				this.nearestGhostPoint = cp;
//				this.nearestShipPoint = shipPoints[i];
//				bestDist = (float) PMath.distance(shipPoints[i].getWorldPoint(), cp.getWorldPoint());
//			}
//		}
//	}
//	private void snapSuggestion()
//	{
//		//Pass 1
//		if (this.passCount == 1)
//		{
//			this.sugg2valid = false;
//			
//			//If suggestion is valid, show ghost thing
//			if ((this.nearestGhostPoint != null && this.nearestShipPoint != null)&&
//				(this.nearestShipPoint.squaredDistance < Builder.buildDistanceSq)&&
//				(this.contactCount == 0))
//			{
//				this.sugg1valid = true;
//				
//				//Moving to correct spot
//				Vec2 newBlockSpot = nearestShipPoint.getWorldPoint();
//				newBlockSpot.subLocal(this.nearestGhostPoint);				
//				newBlockSpot = PMath.rotateVecAbout(newBlockSpot, nearestShipPoint.getWorldPoint(),
//						this.targetSpaceShip.getWorldAngle());
//				
//				this.suggestionBody.setTransform(newBlockSpot, this.ghostBlock.getAngle() + this.angleOffset);
//				this.suggRenderPos = this.position;
//			}
//			else
//			{
//				this.sugg1valid = false;
//				this.suggestionBody.setTransform(this.position, this.ghostBlock.getAngle() + this.angleOffset);
//				this.suggRenderPos = this.position;
//			}
//		}
//		else
//		{
//			if ((this.nearestGhostPoint != null && this.nearestShipPoint != null)&&
//					(this.nearestShipPoint.squaredDistance < Builder.buildDistanceSq)&&
//					(this.contactCount == 0) && this.sugg1valid)
//			{
//				this.sugg2valid = true;
//				
//				//Moving to correct spot
//				Vec2 newBlockSpot = nearestShipPoint.getWorldPoint();
//				newBlockSpot.subLocal(this.nearestGhostPoint);				
//				newBlockSpot = PMath.rotateVecAbout(newBlockSpot, nearestShipPoint.getWorldPoint(),
//						this.targetSpaceShip.getWorldAngle());
//				
//				this.suggestionBody.setTransform(newBlockSpot, this.ghostBlock.getAngle() + this.angleOffset);
//				
//				this.suggRenderPos = this.suggestionBody.getPosition();
//			}
//			else
//			{
//				this.sugg2valid = false;
//				this.sugg1valid = false;
//				this.suggestionBody.setTransform(this.position, this.ghostBlock.getAngle() + this.angleOffset);
//				this.suggRenderPos = this.position;
//			}
//		}
//	}
//	private Vec2 getLocalPoint(Vec2 point)
//	{
//		Vec2 w = new Vec2(point);
//		w = w.sub(this.position);
//		
//		return PMath.rotateVec(w, -this.angleOffset);
//	}
//
////Commands
//	private void rotateLeft()
//	{
//		if (this.ghostBlock == null)
//			return;
//		
//		this.angle = this.ghostBlock.getAngle() - Direction.DOWN.angle;
//		this.ghostBlock.setGhostAngle(this.angle);
//		this.suggestionBody.setTransform(this.suggestionBody.getPosition(), this.suggestionBody.getAngle() - Direction.DOWN.angle);
//	}
//	private void rotateRight()
//	{
//		if (this.ghostBlock == null)
//			return;
//
//		this.angle = this.ghostBlock.getAngle() + Direction.DOWN.angle;
//		this.ghostBlock.setGhostAngle(this.angle);
//		this.suggestionBody.setTransform(this.suggestionBody.getPosition(), this.suggestionBody.getAngle() - Direction.DOWN.angle);
//	}
//	private void changeSize(boolean width, boolean bigger)
//	{
//		
//	}
//	private void changeType(Command c)
//	{
//		switch(c)
//		{
//		case HULL_REC:			this.setBlockType(new Hull(ShapeType.REC, 0, 0, 0, 0, c.w, c.h));	break;
//		case HULL_TRI:			this.setBlockType(new Hull(ShapeType.TRI, 0, 0, 0, 0, c.w, c.h));	break;
//		
//		case THRUSTER:			this.setBlockType(new Thruster(0, 0, 0, c.w, c.h));					break;
//		case THRUSTER_ROTOR:	this.setBlockType(new RotorThruster(0, 0, 0, c.w, c.h));			break;
//		case GYRO:				this.setBlockType(new Gyro(0, 0, 0, c.w, c.h));						break;
//		
//		case TANK:				this.setBlockType(new Tank(0, 0, 0, c.w, c.h));						break;
//		case CONTAINER:			this.setBlockType(new Container(0, 0, Direction.RIGHT, c.w, c.h));	break;
//		
//		case BATTERY:			this.setBlockType(new Battery(0, 0, 0, c.w, c.h));					break;
//		case GENERATOR:			this.setBlockType(new Generator(0, 0, 0, c.w, c.h));				break;
//		case SOLARPANEL:		this.setBlockType(new SolarBlock(0, 0, Direction.RIGHT, c.w, c.h));	break;
//		
//		case CHAIR:				this.setBlockType(new Chair(0, 0, Direction.RIGHT, false));			break;
//		
//		case EMPTY_HAND:		this.setBlockType(null);	break;
//		default:	break;
//		}
//		this.reset();
//	}
//	private void build()
//	{
//		if (this.nearestGhostPoint == null || this.nearestShipPoint == null || !this.sugg2valid || this.contactCount != 0)
//			return;
//		
//		this.nearestShipPoint.parent.getSpaceShip().attachBlock(this.nearestGhostPoint, this.nearestShipPoint);
//		
//		this.reset();
//	}
//	private void newShip()
//	{
//		if (this.contactCount == 0)
//		{
//			game.buildNewShip(this.position, this.blockType.copy(), this.suggestionBody.getAngle());
//			this.reset();
//		}
//	}
//	
////Command handling
//	@Override
//	public void tapCommand(Command c)
//	{
//		
//	}
//	@Override
//	public void pressCommand(Command c)
//	{
//		if (c.catagory() == CommCatagory.BUILDER)
//		{
//			switch(c)
//			{
//			case ADD_BLOCK:			this.build();		break;
//			case ADD_BODY:			this.newShip();		break;
//			case ROTATE_LEFT_B:		this.rotateLeft();	break;
//			case ROTATE_RIGHT_B:	this.rotateRight();	break;
//			case WIDTH_UP:			this.changeSize(true, true);	break;
//			case WIDTH_DOWN:		this.changeSize(true, false);	break;
//			case HEIGHT_UP:			this.changeSize(false, true);	break;
//			case HEIGHT_DOWN:		this.changeSize(false, false);	break;
//			default:	break;
//			}
//		}
//		else if (c.catagory() == CommCatagory.BLOCK)
//		{
//			this.changeType(c);
//		}
//	}
//	@Override
//	public void releaseCommand(Command c)
//	{
//		
//	}
//
////Controlling stuff
//	public void setControlling(boolean b)
//	{
//		if (b && !this.active)
//		{
//			this.pressCommand(this.game.getControlPanel().pollHotBar());
//			this.reset();
//		}
//		else if (!b && this.active)
//			this.game.getWorld().destroyBody(this.suggestionBody);
//		
//		this.active = b;
//	}
//	public boolean getControlling()	{ return this.active; }
//	
////Contact Listining
//	@Override
//	public void beginContact(Contact contact)
//	{
//		this.contactCount++;
//	}
//	@Override
//	public void endContact(Contact contact)
//	{
//		this.contactCount--;
//	}
//
//	@Override
//	public void preSolve(Contact contact, Manifold oldManifold){}
//	@Override
//	public void postSolve(Contact contact, ContactImpulse impulse){}
//	
////Getters and setters
//	public void		setBlockType(Block b)
//	{
//		this.blockType = b;
//	}
//	public void		setTargetSpaceShip(SpaceShip s)	{ this.targetSpaceShip = s; }
//	public Vec2		getPosition()					{ return this.position; }
//	public void		setAngle(float a)
//	{
//		this.ghostBlock.setGhostAngle(a);
//	}
//}