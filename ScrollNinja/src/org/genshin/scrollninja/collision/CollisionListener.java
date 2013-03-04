package org.genshin.scrollninja.collision;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class CollisionListener implements ContactListener
{
	/**
	 * 衝突した瞬間に呼び出されるメソッド
	 */
	@Override
	public void beginContact(Contact contact)
	{
		/* 何もしない */ 
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
