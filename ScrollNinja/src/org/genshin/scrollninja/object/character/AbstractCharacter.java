package org.genshin.scrollninja.object.character;

import org.genshin.scrollninja.collision.AbstractCollisionCallback;
import org.genshin.scrollninja.collision.CollisionObject;
import org.genshin.scrollninja.object.AbstractObject;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.World;


public abstract class AbstractCharacter extends AbstractObject
{
	public AbstractCharacter(String collisionFilePath, World world)
	{
		super();
		
		collisionObject = new CollisionObject(collisionFilePath, world, createCollisionCallback());
	}

	@Override
	public void dispose()
	{
		if(collisionObject != null)
		{
			collisionObject.dispose();
			collisionObject = null;
		}
		
		super.dispose();
	}

	@Override
	public float getPositionX()
	{
		return collisionObject.getBody().getPosition().x;
	}

	@Override
	public float getPositionY()
	{
		return collisionObject.getBody().getPosition().y;
	}

	@Override
	public float getRotation()
	{
		return collisionObject.getBody().getAngle() * MathUtils.radiansToDegrees;
	}
	
	/**
	 * X方向の反転フラグを取得する。
	 * @return		X方向の反転フラグ
	 */
	public abstract boolean isFlipX();
	
	/**
	 * Y方向の反転フラグを取得する。
	 * @return		Y方向の反転フラグ
	 */
	public abstract boolean isFlipY();
	
	/**
	 * 衝突オブジェクトを取得する。
	 * @return		衝突オブジェクト
	 */
	protected CollisionObject getCollisionObject()
	{
		return collisionObject;
	}
	
	/**
	 * 衝突判定のコールバックオブジェクトを生成する。
	 * @return		衝突判定のコールバックオブジェクト
	 */
	protected abstract AbstractCharacterCollisionCallback createCollisionCallback();
	
	
	/** 衝突オブジェクト */
	private CollisionObject	collisionObject;
	
	
	/**
	 * 衝突判定のコールバックの基本クラス
	 */
	protected class AbstractCharacterCollisionCallback extends AbstractCollisionCallback
	{
		@Override
		public void dispatch(AbstractCollisionCallback collisionCallback, Contact contact)
		{
			collisionCallback.collision(AbstractCharacter.this, contact);
		}
	}
	
}