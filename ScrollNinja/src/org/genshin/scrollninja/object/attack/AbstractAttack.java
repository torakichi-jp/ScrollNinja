package org.genshin.scrollninja.object.attack;

import org.genshin.scrollninja.collision.AbstractCollisionCallback;
import org.genshin.scrollninja.collision.CollisionObject;
import org.genshin.scrollninja.object.AbstractObject;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.World;

/**
 * 攻撃の基本クラス
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public abstract class AbstractAttack extends AbstractObject implements AttackInterface
{
	/**
	 * コンストラクタ
	 * @param collisionFilePath		衝突判定の定義ファイルのパス
	 * @param world					所属する世界オブジェクト
	 * @param power					攻撃力
	 */
	public AbstractAttack(String collisionFilePath, World world, float power)
	{
		collisionObject = new CollisionObject(collisionFilePath, world, createCollisionCallback());
		this.power = power;
		
		//toSleep();
	}

	@Override
	public void dispose()
	{
		collisionObject.dispose();
		
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

	@Override
	public boolean isSleep()
	{
		return !collisionObject.getBody().isActive();
	}
	
	/**
	 * 待機状態に移行する。
	 */
	protected void toSleep()
	{
		collisionObject.getBody().setActive(false);
	}
	
	/**
	 * 衝突判定のコールバックオブジェクトを生成する。
	 * @return		衝突判定のコールバックオブジェクト
	 */
	protected abstract AbstractCollisionCallback createCollisionCallback();
	
	
	/** 衝突オブジェクト */
	private final CollisionObject collisionObject;
	
	/** 攻撃力 */
	private final float power;
	
	
	/**
	 * 衝突判定のコールバック
	 */
	protected abstract class AbstractAttackCollisionCallback extends AbstractCollisionCallback
	{
		@Override
		public void dispatch(AbstractCollisionCallback collisionCallback, Contact contact)
		{
			collisionCallback.collision(AbstractAttack.this, contact);
		}
	}
}
