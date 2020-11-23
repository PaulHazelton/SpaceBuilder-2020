package physicsListeners;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.callbacks.DestructionListener;
import org.jbox2d.callbacks.ParticleDestructionListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.Shape;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.particle.ParticleGroup;
import org.jbox2d.serialization.JbDeserializer.ObjectListener;
import org.jbox2d.serialization.JbSerializer.ObjectSigner;
import org.jbox2d.serialization.UnsupportedListener;
import org.jbox2d.serialization.UnsupportedObjectException;

import entities.Player;


public class PhysicsListener
	implements 
		ContactListener,
		ObjectListener,
		ObjectSigner,
		UnsupportedListener,
		DestructionListener,
		ParticleDestructionListener
{
	
	int count = 0;
	
	@Override
	public boolean isUnsupported(UnsupportedObjectException argException)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Long getTag(World world)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getTag(Body body)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getTag(Shape shape)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getTag(Fixture fixture)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getTag(Joint joint)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void processWorld(World world, Long tag)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void processBody(Body body, Long tag)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processFixture(Fixture fixture, Long tag)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void processShape(Shape shape, Long tag)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processJoint(Joint joint, Long tag)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beginContact(Contact contact)
	{
		if (contact.m_fixtureA.m_userData == null &&
			contact.m_fixtureB.m_userData == null)
			return;
		
		//TODO Temperary fix until all userData is of the UserData type
		if (contact.getFixtureA().getUserData() instanceof UserData)
			((UserData)contact.getFixtureA().getUserData()).cListener.beginContact(contact);
		else if (contact.getFixtureB().getUserData() instanceof UserData)
			((UserData)contact.getFixtureB().getUserData()).cListener.beginContact(contact);
		
		
		//TODO old player running crap. Do it better with the userData class
		if (contact.m_fixtureA.m_userData != null)
		{
			if (contact.m_fixtureA.m_userData instanceof Player)
				((Player) contact.m_fixtureA.m_userData).beginContact(contact);
		}
		else if (contact.m_fixtureB.m_userData != null)
		{
			if (contact.m_fixtureB.m_userData instanceof Player)
				((Player) contact.m_fixtureB.m_userData).beginContact(contact);
		}
	}

	@Override
	public void endContact(Contact contact)
	{
		//TODO old player running crap. Do it better with the userData class
		if (contact.m_fixtureA.m_userData == null &&
			contact.m_fixtureB.m_userData == null)
			return;
		
		if (contact.getFixtureA().getUserData() instanceof UserData)
			((UserData)contact.getFixtureA().getUserData()).cListener.endContact(contact);
		else if (contact.getFixtureB().getUserData() instanceof UserData)
			((UserData)contact.getFixtureB().getUserData()).cListener.endContact(contact);
			
		if (contact.m_fixtureA.m_userData instanceof Player)
			((Player) contact.m_fixtureA.m_userData).endContact(contact);
		else if (contact.m_fixtureB.m_userData instanceof Player)
			((Player) contact.m_fixtureB.m_userData).endContact(contact);
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

	@Override
	public void sayGoodbye(Joint joint)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sayGoodbye(Fixture fixture)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sayGoodbye(ParticleGroup group)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sayGoodbye(int index)
	{
		// TODO Auto-generated method stub
		
	}
}
