package org.genshin.scrollninja.object.attack;

import org.genshin.scrollninja.GlobalDefine;
import org.genshin.scrollninja.collision.AbstractCollisionCallback;
import org.genshin.scrollninja.collision.CollisionObject;
import org.genshin.scrollninja.object.AbstractObject;
import org.genshin.scrollninja.object.character.AbstractCharacter;
import org.genshin.scrollninja.object.character.ninja.AbstractNinja;
import org.genshin.scrollninja.object.terrain.Terrain;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.World;

/**
 * 攻撃の基本クラス
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public abstract class AbstractAttack extends AbstractObject
{
	/**
	 * コンストラクタ
	 * @param collisionFilePath		衝突判定の定義ファイルのパス
	 * @param world					所属する世界オブジェクト
	 * @param power					攻撃力
	 * @param owner					攻撃者の種類
	 */
	public AbstractAttack(String collisionFilePath, World world, float power, AbstractCharacter owner)
	{
		collisionObject = new CollisionObject(collisionFilePath, world, createCollisionCallback());
		this.power = power;
		this.owner = owner;
		
		//---- 最初は待機状態にしておく。
		collisionObject.getBody().setActive(false);
		
		//---- 攻撃者の種類に合わせて、衝突する対象を設定する。
		collisionObject.addCollisionCategory("Attack", (this.owner instanceof AbstractNinja) ? "Enemy" : "Player");
	}

	@Override
	public void dispose()
	{
		collisionObject.dispose();
		
		super.dispose();
	}
	
	/**
	 * 攻撃を実行する。
	 */
	public abstract void fire();

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
	
	/**
	 * 待機状態か調べる。
	 * @return		待機状態ならtrue
	 */
	public boolean isSleep()
	{
		return !collisionObject.getBody().isActive();
	}

	/**
	 * 活動状態に移行する。
	 */
	protected void toActive()
	{
		collisionObject.getBody().setActive(true);
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
	protected AttackCollisionCallback createCollisionCallback()
	{
		return new AttackCollisionCallback();
	}
	
	/**
	 * 衝突オブジェクトを取得する。
	 * @return		衝突オブジェクト
	 */
	protected CollisionObject getCollisionObject()
	{
		return collisionObject;
	}
	
	/**
	 * 攻撃の所有者を取得する。
	 * @return		攻撃の所有者
	 */
	protected AbstractCharacter getOwner()
	{
		return owner;
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
	
	/** 攻撃の所有者 */
	private final AbstractCharacter owner;
	
	
	/**
	 * 衝突判定のコールバック
	 */
	protected class AttackCollisionCallback extends AbstractCollisionCallback
	{
		@Override
		public void dispatch(AbstractCollisionCallback collisionCallback, Contact contact)
		{
			collisionCallback.collision(AbstractAttack.this, contact);
		}
		
		@Override
		public void collision(Terrain obj, Contact contact)
		{
			toSleep();
		}

		@Override
		public void collision(AbstractCharacter obj, Contact contact)
		{
			toSleep();
		}
	}
}
