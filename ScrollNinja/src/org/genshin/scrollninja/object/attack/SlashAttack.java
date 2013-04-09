package org.genshin.scrollninja.object.attack;

import org.genshin.scrollninja.object.character.AbstractCharacter;
import org.genshin.scrollninja.object.character.ninja.AbstractNinja;
import org.genshin.scrollninja.object.effect.AbstractEffect;
import org.genshin.scrollninja.object.effect.FileEffect;
import org.genshin.scrollninja.render.RenderObject;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.World;

public class SlashAttack extends AbstractAttack
{
	/**
	 * コンストラクタ
	 * @param world		所属する世界オブジェクト
	 * @param owner		所有者の位置情報
	 */
	public SlashAttack(World world, AbstractCharacter owner)
	{
		super("data/jsons/collision/slash.json", world, 50.0f, owner instanceof AbstractNinja ? AttackOwner.PLAYER : AttackOwner.ENEMY);
		this.owner = owner;
	}
	
	@Override
	public void dispose()
	{
		//---- このクラスを破棄する。
		if(effect != null)
		{
			effect.dispose();
			effect = null;
		}
		
		//---- 基本クラスを破棄する。
		super.dispose();
	}

	@Override
	public void update(float deltaTime)
	{
		//---- 待機状態なら何もしない。
		if(isSleep())
			return;
		
		//---- 衝突判定、描画オブジェクトを更新する。
		angle += angularVelocity * deltaTime;
		updateCollisionObject();
		
		for(RenderObject ro : effect.getRenderObjects())
		{
			ro.flip(owner.isFlipX(), owner.isFlipY());
		}
		
		//---- エフェクトが消えたら斬撃終了。
		if(effect.isFinished())
		{
			effect = null;
			toSleep();
		}
	}
	
	@Override
	public void fire()
	{
		//---- エフェクトを発生させる。
		effect = new FileEffect("data/jsons/effect/slash.json", this);
		
		//---- 回転の初期設定
		if(angularVelocity == null)
		{
			angularVelocity = new Float((125.0f * MathUtils.degreesToRadians) / effect.getLife());
		}
		angle = 0.0f;
		
		//---- 活動状態へ移行する。
		toActive();
	}

	@Override
	public float getPositionX()
	{
		return owner.getPositionX();
	}

	@Override
	public float getPositionY()
	{
		return owner.getPositionY();
	}

	@Override
	public float getRotation()
	{
		return owner.getRotation();
	}

	@Override
	public boolean isSleep()
	{
		return effect == null;
	}

	@Override
	protected AbstractAttackCollisionCallback createCollisionCallback()
	{
		return new SlashAttackCollisionCallback();
	}
	
	/**
	 * 衝突判定を更新する。
	 */
	private void updateCollisionObject()
	{
		final Body body = getBody();
		final float flipRatio = owner.isFlipX() ? -1.0f : 1.0f;
		body.setAngularVelocity(angularVelocity * flipRatio);
		body.setTransform(getPositionX(), getPositionY(), (getRotation() - 90.0f) * MathUtils.degreesToRadians + angle * flipRatio);
	}
	
	
	/** 所有者の位置情報 */
	private final AbstractCharacter owner;
	
	/** エフェクトオブジェクト */
	private AbstractEffect effect;
	
	/** 斬撃の角度 */
	private float angle;
	
	/** 角速度（radian/秒） */
	private static Float angularVelocity;
	
	
	/**
	 * 衝突判定のコールバック
	 */
	private class SlashAttackCollisionCallback extends AbstractAttackCollisionCallback
	{
		@Override
		public void collision(AbstractCharacter obj, Contact contact)
		{
			SlashAttack.this.toSleep();
		}
	}
}
