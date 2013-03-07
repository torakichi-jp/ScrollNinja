package org.genshin.scrollninja.collision;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class CollisionListener implements ContactListener
{
	/**
	 * 衝突した瞬間に呼び出されるメソッド
	 */
	@Override
	public void beginContact(Contact contact)
	{
		final Fixture fixtureA = contact.getFixtureA();
		final Fixture fixtureB = contact.getFixtureB();
		
		if(fixtureA.isSensor() || fixtureB.isSensor())
		{
			final AbstractCollisionCallback callbackA = (AbstractCollisionCallback)fixtureA.getBody().getUserData();
			final AbstractCollisionCallback callbackB = (AbstractCollisionCallback)fixtureB.getBody().getUserData();

			callbackA.dispatch(callbackB, contact);
			callbackB.dispatch(callbackA, contact);
		}
	}
	
	/**
	 * 衝突していたオブジェクトが離れた瞬間に呼び出されるメソッド
	 */
	@Override
	public void endContact(Contact contact)
	{
		/* 何もしない */
	}

	/**
	 * 衝突している間、呼ばれ続けるメソッド
	 */
	@Override
	public void preSolve(Contact contact, Manifold oldManifold)
	{
		/* 何もしない */
	}
	
	/**
	 * 衝突している間、呼ばれ続けるメソッド
	 */
	@Override
	public void postSolve(Contact contact, ContactImpulse impulse)
	{
		final AbstractCollisionCallback callbackA = (AbstractCollisionCallback)contact.getFixtureA().getBody().getUserData();
		final AbstractCollisionCallback callbackB = (AbstractCollisionCallback)contact.getFixtureB().getBody().getUserData();
		
		callbackA.dispatch(callbackB, contact);
		callbackB.dispatch(callbackA, contact);
	}
}
