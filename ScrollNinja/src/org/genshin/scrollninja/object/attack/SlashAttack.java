package org.genshin.scrollninja.object.attack;

import org.genshin.engine.system.PostureInterface;
import org.genshin.scrollninja.collision.AbstractCollisionCallback;
import org.genshin.scrollninja.object.effect.FileEffect;
import org.genshin.scrollninja.object.ninja.AbstractNinja;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.World;

public class SlashAttack extends AbstractAttack
{
	/**
	 * コンストラクタ
	 * @param world		所属する世界オブジェクト
	 * @param owner		所有者の位置情報
	 */
	public SlashAttack(World world, PostureInterface owner)
	{
		super("data/jsons/collision/slash.json", world, 10.0f);
		this.owner = owner;
	}
	
	@Override
	public void update(float deltaTime)
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void fire()
	{
		//---- エフェクトを発生させる。
//		new SlashEffect(owner);
		new FileEffect("data/jsons/effect/slash.json", this);
	}

	@Override
	protected AbstractCollisionCallback createCollisionCallback()
	{
		return new SlashAttackCollisionCallback();
	}


	/** 所有者の位置情報 */
	private final PostureInterface owner;
	
	
	private class SlashAttackCollisionCallback extends AbstractAttackCollisionCallback
	{
		@Override
		public void collision(AbstractNinja obj, Contact contact)
		{
//			SlashAttack.this.toSleep();
			System.out.println("collison!!");
		}
	}
}
