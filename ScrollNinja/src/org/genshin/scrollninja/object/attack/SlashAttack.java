package org.genshin.scrollninja.object.attack;

import org.genshin.engine.system.PostureInterface;

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
//		new SlashEffect(owner);
	}

	@Override
	public boolean isSleep()
	{
		return true;
	}
	
	
	/** 所有者の位置情報 */
	private final PostureInterface owner;
}
