package org.genshin.scrollninja.object.attack;

import org.genshin.engine.system.PostureInterface;
import org.genshin.scrollninja.object.effect.SlashEffect;

public class SlashAttack extends AbstractAttack
{
	public SlashAttack(PostureInterface owner)
	{
		this.owner = owner;
	}
	
	@Override
	public void fire()
	{
		//---- エフェクトを発生させる。
		new SlashEffect(owner.getPositionX(), owner.getPositionY(), 0.0f);
	}

	@Override
	public boolean isSleep()
	{
		return false;
	}
	
	
	/** 所有者の位置情報 */
	private final PostureInterface owner;
}
