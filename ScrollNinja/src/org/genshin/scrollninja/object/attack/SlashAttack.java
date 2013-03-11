package org.genshin.scrollninja.object.attack;

import org.genshin.scrollninja.object.character.AbstractCharacter;
import org.genshin.scrollninja.object.effect.AbstractEffect;
import org.genshin.scrollninja.object.effect.FileEffect;
import org.genshin.scrollninja.render.RenderObject;

import com.badlogic.gdx.math.MathUtils;
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
		super("data/jsons/collision/slash.json", world, 10.0f);
		this.owner = owner;
	}
	
	@Override
	public void update(float deltaTime)
	{
		super.update(deltaTime);
		
		//---- 待機状態なら何もしない。
		if(isSleep())
			return;
		
		//---- 位置情報を設定する。
		getBody().setTransform(owner.getPositionX(), owner.getPositionY(), owner.getRotation() * MathUtils.degreesToRadians);
		
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
		
		//---- 活動状態へ移行する。
		toActive();
	}

	@Override
	protected AbstractAttackCollisionCallback createCollisionCallback()
	{
		return new SlashAttackCollisionCallback();
	}
	
	
	/** 所有者の位置情報 */
	private final AbstractCharacter owner;
	
	/** エフェクトオブジェクト */
	private AbstractEffect effect;
	
	
	/**
	 * 衝突判定のコールバック
	 */
	private class SlashAttackCollisionCallback extends AbstractAttackCollisionCallback
	{
		@Override
		public void collision(AbstractCharacter obj, Contact contact)
		{
			//SlashAttack.this.toSleep();
		}
	}
}
