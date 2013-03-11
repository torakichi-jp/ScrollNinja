package org.genshin.scrollninja.object.attack;

import org.genshin.scrollninja.GlobalDefine;
import org.genshin.scrollninja.collision.AbstractCollisionCallback;
import org.genshin.scrollninja.collision.CollisionObject;
import org.genshin.scrollninja.object.AbstractObject;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
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
		
		collisionObject.getBody().setActive(false);
	}

	@Override
	public void dispose()
	{
		collisionObject.dispose();
		
		super.dispose();
	}
	
	@Override
	public void update(float deltaTime)
	{
		final Body body = collisionObject.getBody();
		
		if(collisionEnabled != body.isActive())
		{
			body.setActive(collisionEnabled);
		}
	}

	/**
	 * 攻撃力を取得する。
	 * @return		攻撃力
	 */
	public float getPower()
	{
		return power;
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
		return !collisionEnabled;
	}

	/**
	 * 活動状態に移行する。
	 */
	protected void toActive()
	{
		collisionEnabled = true;
	}
	
	/**
	 * 待機状態に移行する。
	 */
	protected void toSleep()
	{
		collisionEnabled = false;
	}
	
	/**
	 * 衝突判定のコールバックオブジェクトを生成する。
	 * @return		衝突判定のコールバックオブジェクト
	 */
	protected abstract AbstractAttackCollisionCallback createCollisionCallback();
	
	/**
	 * Bodyオブジェクトを取得する。
	 * @return		Bodyオブジェクト
	 */
	protected Body getBody()
	{
		return collisionObject.getBody();
	}
	
	@Override
	protected int getUpdatePriority()
	{
		return GlobalDefine.UpdatePriority.ATTACK;
	}
	
	
	/** 衝突オブジェクト */
	private final CollisionObject collisionObject;
	
	/** 攻撃力 */
	private final float power;
	
	/** 衝突判定の有効フラグ */
	private boolean collisionEnabled = false;
	
	
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
